package mobi.puut.controllers;

import mobi.puut.entities.WalletInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.List;

/**
 * Created by Chaklader on 6/24/17.
 */
@Controller
public class BitcoinWalletsController {


    @Autowired
    private WalletService walletService;

    @RequestMapping(value = "/")
    public String showBitcoinWallet(final Model model) {
        List<WalletInfo> wallets = walletService.getAllWallets();
        model.addAttribute("wallets", wallets);
        return "main";
    }

    /**
     * @param walletName accept walletName and generate the requested wallet
     * @return redirects to the landing page
     */
    @RequestMapping(value = "/generateAddress", method = RequestMethod.POST)
    public String generateAddress(final @RequestParam String walletName) {
        walletService.generateAddress(walletName);
        return "redirect:/";
    }

    @RequestMapping(value = "/balance")
    public String showWalletBalance(final Model model, @RequestParam final Long id) {
        WalletModel walletModel = walletService.getWalletModel(id);
        model.addAttribute("walletModel", walletModel);
        model.addAttribute("wallet_id", id);
        return "balance";
    }

    @RequestMapping(value = "/transactions")
    public String showWalletTransactions(final Model model, @RequestParam final Long id) {
        WalletModel walletModel = walletService.getWalletModel(id);
        model.addAttribute("walletModel", walletModel);
        model.addAttribute("wallet_id", id);
        return "transactions";
    }

    @RequestMapping(value = "/sendMoney", method = RequestMethod.GET)
    public String showSendMoney(final Model model, @RequestParam final Long id) {
        WalletModel walletModel = walletService.getWalletModel(id);
        model.addAttribute("walletModel", walletModel);
        model.addAttribute("wallet_id", id);
        return "sendMoney";
    }

    @RequestMapping(value = "/sendMoney", method = RequestMethod.POST)
    public String sendMoney(final Model model,
                            @RequestParam final Long id, @RequestParam String amount, @RequestParam String address) {
        WalletModel walletModel = walletService.sendMoney(id, amount, address);
        model.addAttribute("walletModel", walletModel);
        model.addAttribute("wallet_id", id);
        return "sendMoney";
    }
}
