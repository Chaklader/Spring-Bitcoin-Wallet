package mobi.puut.controllers;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Chaklader on 6/24/17.
 */
public class WalletManager {

    public static WalletAppKit bitcoin;

    private WalletModel model = new WalletModel();

    // public static NetworkParameters networkParameters = MainNetParams.get();
    public static NetworkParameters networkParameters = TestNet3Params.get();

    public static final String APP_NAME = "WalletTemplate";

    public static final String WALLET_FILE_NAME = APP_NAME.replaceAll("[^a-zA-Z0-9.-]", "_")
            + networkParameters.getPaymentProtocolId();

    private static final Logger logger = LoggerFactory.getLogger(WalletManager.class);

    private List<WalletSetupCompletedListener> setupCompletedListeners = Collections.synchronizedList(new LinkedList<>());

    public static WalletManager setupWallet(final String walletName) {

        logger.info("Setup Wallet");
        WalletManager walletManager = new WalletManager();
        walletManager.setupWalletKit(walletName);

        try {
            if (walletManager.bitcoin.isChainFileLocked()) {
                return walletManager;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return walletManager;
        }

        walletManager.bitcoin.startAsync();
        return walletManager;
    }

    private WalletManager() {
    }

    /**
     * @param walletId takes wallet ID and generate the directory for the wallet
     * @return reutrn the file direcotry
     */
    protected File getWalletDirectory(final String walletId) {

        File dir = new File(walletId);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir;
    }

    public WalletAppKit getBitcoin() {
        return bitcoin;
    }

    public WalletModel getModel() {
        return model;
    }

    public void addWalletSetupCompletedListener(final WalletSetupCompletedListener listener) {
        setupCompletedListeners.add(listener);
    }


    /**
     * @param walletId set up the wallet for the provided wallet ID
     */
    private void setupWalletKit(final String walletId) {

        File directory = getWalletDirectory(walletId);

        // if the seed is not null, that means we are restoring from the backup
        bitcoin = new WalletAppKit(networkParameters, directory, WALLET_FILE_NAME) {

            @Override
            protected void onSetupCompleted() {

                // Don't make the user wait for confirmations
                // they're sending their own money anyway!!
                bitcoin.wallet().allowSpendingUnconfirmedTransactions();
                Wallet wallet = bitcoin.wallet();
                model.setWallet(wallet);
                setupCompletedListeners.forEach(listener -> listener.onSetupCompleted(wallet));
            }
        };

        // Now configure and start the appkit. This will take a second or two - we could show a temporary splash screen
        // or progress widget to keep the user engaged whilst we initialise, but we don't.
        if (networkParameters == RegTestParams.get()) {
            bitcoin.connectToLocalHost();   // You should run a regtest mode bitcoind locally.
        } else if (networkParameters == TestNet3Params.get()) {
            bitcoin.useTor();
        }

        bitcoin.setDownloadListener(model.getSyncProgressUpdater())
                .setBlockingStartup(false)
                .setUserAgent(APP_NAME, "1.0");
    }
}
