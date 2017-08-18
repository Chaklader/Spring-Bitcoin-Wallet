package mobi.puut.controllers;

import mobi.puut.entities.User;
import mobi.puut.entities.WalletInfo;
import mobi.puut.services.UserService;
import mobi.puut.services.WalletService;
import org.bitcoinj.core.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Chaklader on 6/24/17.
 */
@RestController
@RequestMapping("/rest")
public class WalletRestController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    //    To revert to a previous commit, ignoring any changes:
    //    git reset --hard HEAD

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;


    /**
     * @return return the number of wallet created as String
     */
    @ResponseBody
    @RequestMapping("/walletsNumber")
    public String getWalletsCount() {

        List<WalletInfo> wallets = walletService.getAllWallets();
        return String.valueOf(wallets.size());
    }

    /**
     * @param id takes wallet index as the Long ID argument
     * @return return the balance of the request wallet
     */
    @ResponseBody
    @RequestMapping("/walletBalance")
    public String getWalletBalance(@RequestParam final Long id) {

        WalletModel walletModel = getWalletModel(id);
        return String.valueOf(walletModel.getBalanceFloatFormat());
    }


    /**
     * @param id takes wallet index as the Long ID argument
     * @return return the number of transaction executed on
     * the requested wallet
     */
    @ResponseBody
    @RequestMapping("/walletTransactionsNumber")
    public String getWalletTransactionsNumber(@RequestParam final Long id) {

        WalletModel walletModel = getWalletModel(id);
        List<Transaction> history = walletModel.getTransactions();
        return String.valueOf(history.size());
    }

    private WalletModel getWalletModel(Long id) {
        return walletService.getWalletModel(id);
    }

    private WalletInfo getWalletInfo(Long id) {
        return walletService.getWalletInfo(id);
    }
}
