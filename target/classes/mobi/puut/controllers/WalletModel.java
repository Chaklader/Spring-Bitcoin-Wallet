package mobi.puut.controllers;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.listeners.DownloadProgressTracker;
import org.bitcoinj.utils.MonetaryFormat;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.WalletChangeEventListener;

import java.util.*;

/**
 * Created by Chaklader on 6/12/17.
 */
public class WalletModel {

    private int UserId;

    private Address address;

    private String transaction;

    private Coin balance = Coin.ZERO;

    private double syncProgress = -1.0;

    private static double SYNCHRONISATION_FINISHED = 1.0;

    private ProgressBarUpdater syncProgressUpdater = new ProgressBarUpdater();

    private List<Transaction> transactions = Collections.synchronizedList(new ArrayList<>());

    public WalletModel() {}

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        this.UserId = userId;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public WalletModel(Wallet wallet) {
        setWallet(wallet);
    }


    public boolean isSyncFinished() {
        return syncProgress == SYNCHRONISATION_FINISHED;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * @param
     * @return the Satoshi coin based on the wallet balance
     * 1 BTC =  10^8 Satoshi coin
     */
    public Coin getBalance() {
        return balance;
    }

    /**
     * @return get the BTC amount as float from the wallet balance
     */
    public float getBalanceFloatFormat() {

        float bal = (float) balance.getValue();
        float fac = (float) Math.pow(10, 8);

        float result = bal / fac;
        return result;
    }

    public double getSyncProgress() {
        return syncProgress;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public ProgressBarUpdater getSyncProgressUpdater() {
        return syncProgressUpdater;
    }


    /**
     * @param wallet takes the wallet as argument and update all the properties
     */
    private void update(Wallet wallet) {

        this.balance = wallet.getBalance();

        this.address = wallet.currentReceiveAddress();

        transactions.addAll(wallet.getRecentTransactions(100, false));

        // find to most recent transaction
        this.transaction = Objects.isNull(transactions) ||
                transactions.isEmpty() ? "No transaction" : String.valueOf(transactions.get(0));
    }


    /**
     * @param wallet set up an updated wallet balance, address and transactions
     * @return return true after successful update of the wallet
     */
    public boolean setWallet(Wallet wallet) {

        try {
            wallet.addChangeEventListener(new WalletChangeEventListener() {
                @Override
                public void onWalletChanged(Wallet wallet) {
                    update(wallet);
                }
            });
            update(wallet);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * inner class to tracks download progress of the wallet
     */
    private class ProgressBarUpdater extends DownloadProgressTracker {

        @Override
        protected void progress(double percentage, int blocksSoFar, Date date) {
            super.progress(percentage, blocksSoFar, date);
            syncProgress = percentage / 100.0;
        }

        @Override
        protected void doneDownload() {
            super.doneDownload();
            syncProgress = SYNCHRONISATION_FINISHED;
        }
    }

    /**
     * @param transaction take the wallet transaction as an input
     * @return the human readable transaction info as String
     */
    public String addTransactionHistory(Transaction transaction) {

        if (Objects.isNull(transaction)) {
            return "No transaction";
        }

        Coin value = transaction.getValue(WalletManager.bitcoin.wallet());

        if (value.isPositive()) {
            String message = "Incoming payment of " + MonetaryFormat.BTC.format(value);
            return message;
        } else if (value.isNegative()) {
            Address address = transaction.getOutput(0).getAddressFromP2PKHScript(WalletManager.networkParameters);
            String message = "Outbound payment to " + address + " worth of " +
                    (MonetaryFormat.BTC.format(value)).toString().replaceAll("-", "");
            return message;
        }

        String message = "Payment with id " + transaction.getHash();
        return message;
    }
}

