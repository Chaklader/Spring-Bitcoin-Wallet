package mobi.puut.services;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import mobi.puut.controllers.RandomString;
import mobi.puut.controllers.WalletManager;
import mobi.puut.controllers.WalletModel;
import mobi.puut.database.StatusData;
import mobi.puut.database.UserData;
import mobi.puut.database.WalletInfoData;
import mobi.puut.entities.Status;
import mobi.puut.entities.User;
import mobi.puut.entities.WalletInfo;
import org.bitcoinj.core.*;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.Wallet;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import static mobi.puut.controllers.WalletManager.networkParameters;

/**
 * Created by Chaklader on 6/24/17.
 */
@Service
public class WalletService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private RandomString randomString;

    @Autowired
    private UserData userData;

    @Autowired
    private StatusData statusData;

    @Autowired
    private WalletInfoData walletInfoData;

    private Map<String, WalletManager> genWalletMap = new ConcurrentHashMap<>();

    private Map<Long, WalletManager> walletMangersMap = new ConcurrentHashMap<>();

    public List<Status> getWalletStatuses(final Long id) {
        return statusData.getByWalletId(id);
    }

    public WalletInfo getWalletInfo(Long walletId) {
        return walletInfoData.getById(walletId);
    }

    /**
     * @return return all the walletInfo as list
     */
    public List<WalletInfo> getAllWallets() {

        try {
            return walletInfoData.getAllWallets();
        } catch (HibernateException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * takes walletName as argument and generate a wallet accordance to that
     *
     * @param
     */
    public synchronized WalletInfo generateAddress() {

        CountDownLatch finshedSetup = new CountDownLatch(1);

        randomString = new RandomString();
        final String fileName = randomString.nextString();

        final WalletInfo walletInfo = new WalletInfo();

        final WalletManager walletManager = WalletManager.setupWallet(fileName);

        try {
            walletManager.addWalletSetupCompletedListener((wallet) -> {

                Address address = wallet.currentReceiveAddress();
                WalletInfo newWallet = createWalletInfo(address.toString(), "Bitcoin", "BTC");

                walletInfo.setId(newWallet.getId());
                walletInfo.setAddress(newWallet.getAddress());

                walletInfo.setCode(newWallet.getCode());
                walletInfo.setCurrency(newWallet.getCurrency());

                walletMangersMap.put(newWallet.getId(), walletManager);
                finshedSetup.countDown();
            });

            finshedSetup.await();
            return walletInfo;
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return walletInfo;
    }

    /**
     * get the wallet model with the provided ID
     *
     * @param id
     * @return
     */
    public WalletModel getWalletModel(final Long id) {
        WalletManager walletManager = getWalletManager(id);
        WalletModel model = walletManager == null ? null : walletManager.getModel();
        return model;
    }

    /**
     * Send money from the wallet using the wallet name, address and amount
     *
     * @param walletId takes the wallet name
     * @param amount   takes the sending amount
     * @param address  takes address to send the money
     * @return return wallet model with subtracted transaction amount
     */
    public WalletModel sendMoney(final Long walletId, final String amount, final String address) {

        User user = getCurrentUser();
        WalletModel model = null;
        WalletManager walletManager = getWalletManager(walletId);

        if (walletManager != null) {

            Wallet wallet = walletManager.getBitcoin().wallet();

            if (Objects.isNull(wallet)) {
                return model;
            }

            send(user, walletId, wallet, address, amount);
            model = walletManager.getModel();
        }
        return model;
    }

    /**
     * take the amount as Stirng and parse it as Satoshi coin
     *
     * @param amountStr wallet money amount as String
     * @return
     */
    protected Coin parseCoin(final String amountStr) {

        try {
            Coin amount = Coin.parseCoin(amountStr);

            if (amount.getValue() <= 0) {
                throw new IllegalArgumentException("Invalid amount: " + amountStr);
            }
            return amount;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid amount: " + amountStr);
        }
    }

    /**
     * Execute the sending from the user to the external wallet
     *
     * @param user      wallet user
     * @param walletId  wallet ID for the user
     * @param wallet    wallet belongs to the user
     * @param address   external address to send to BTC
     * @param amountStr amount to send to the external user
     */
    protected void send(final User user, final Long walletId, final Wallet wallet,
                        final String address, final String amountStr) {
        // Address exception cannot happen as we validated it beforehand.
        Coin balance = wallet.getBalance();

        try {
            Coin amount = parseCoin(amountStr);
            Address destination = Address.fromBase58(networkParameters, address);

            SendRequest req;
            if (amount.equals(balance))
                req = SendRequest.emptyWallet(destination);
            else
                req = SendRequest.to(destination, amount);

            Wallet.SendResult sendResult = wallet.sendCoins(req);

            Futures.addCallback(sendResult.broadcastComplete, new FutureCallback<Transaction>() {
                @Override
                public void onSuccess(@Nullable Transaction result) {
                    String message = result.toString();
                    saveTransaction(user, walletId, address, message, balance);
                }

                @Override
                public void onFailure(Throwable t) {
                    String error = "Could send money. " + t.getMessage();
                    saveTransaction(user, walletId, address, error, balance);
                }
            });
            sendResult.tx.getConfidence().addEventListener((tx, reason) -> {
                if (reason == TransactionConfidence.Listener.ChangeReason.SEEN_PEERS) {
                    //todo
                }
            });
        } catch (InsufficientMoneyException e) {
            String error = "Could not empty the wallet. " +
                    "You may have too little money left in the wallet to make a transaction.";
            saveTransaction(user, walletId, address, error, balance);

        } catch (ECKey.KeyIsEncryptedException e) {
            String error = "Could send money. " + "Remove the wallet password protection.";
            saveTransaction(user, walletId, address, error, balance);
        } catch (AddressFormatException e) {
            String error = "Could send money. Invalid address: " + e.getMessage();
            saveTransaction(user, walletId, address, error, balance);
        } catch (Exception e) {
            String error = "Could send money. " + e.getMessage();
            saveTransaction(user, walletId, address, error, balance);
        }
    }

    /**
     * find the wallet manager with provided ID
     *
     * @param id
     * @return
     */
    protected synchronized WalletManager getWalletManager(final Long id) {

        WalletManager walletManager = walletMangersMap.get(id);

        if (walletManager == null) {
            WalletInfo walletInfo = walletInfoData.getById(id);
            if (walletInfo != null) {
                String fileName = randomString.nextString();
                walletManager = WalletManager.setupWallet(fileName);
                walletMangersMap.put(walletInfo.getId(), walletManager);
            }
        }

        return walletManager;
    }

    /**
     * @return return the user of concern
     */
    protected User getCurrentUser() {
        User user = userData.getById(1); //TODO
        return user;
    }

    /**
     * create instances in the wallet_info table with the wallet name and the address
     *
     * @param address
     * @return
     */
    protected WalletInfo createWalletInfo(final String address, final String currency, final String code) {
        return walletInfoData.create(address, currency, code);
    }

    /**
     * save the transaction statuses to the status database table
     *
     * @param user
     * @param walletId
     * @param address  external address to send the transactions
     * @param message
     * @param balance
     * @return the generated status instance
     */
    protected Status saveTransaction(final User user, final Long walletId, final String address,
                                     final String message, final Coin balance) {
        Status status = new Status();
        status.setAddress(address.length() > 90 ? address.substring(0, 89) : address);
        status.setUser_id(user.getId());
        status.setWallet_id(walletId);
        status.setTransaction(message.length() > 90 ? message.substring(0, 89) : message);
        status.setBalance(balance.getValue());
        return statusData.saveStatus(status);
    }

    public void deleteWalletInfoById(Long id) {
        walletInfoData.deleteWalletInfoById(id);
    }
}
