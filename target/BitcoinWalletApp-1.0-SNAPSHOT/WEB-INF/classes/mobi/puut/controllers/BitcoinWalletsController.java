package mobi.puut.controllers;

import mobi.puut.entities.WalletInfo;
import mobi.puut.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

/**
 * Created by Chaklader on 6/24/17.
 */
@Controller
public class BitcoinWalletsController {

    @Autowired
    private WalletService walletService;

    @GetMapping(value = "/")
    public String showBitcoinWallet(final Model model) {
        List<WalletInfo> wallets = walletService.getAllWallets();
        model.addAttribute("wallets", wallets);
        return "main";
    }

    /**
     * @return redirects to the landing page
     */
    @PostMapping(value = "/generateAddress")
    public String generateAddress() {
        walletService.generateAddress();
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

    @GetMapping(value = "/sendMoney")
    public String showSendMoney(final Model model, @RequestParam final Long id) {
        WalletModel walletModel = walletService.getWalletModel(id);
        model.addAttribute("walletModel", walletModel);
        model.addAttribute("wallet_id", id);
        return "sendMoney";
    }

    @PostMapping(value = "/sendMoney")
    public String sendMoney(final Model model,
                            @RequestParam final Long id, @RequestParam String amount, @RequestParam String address) {
        WalletModel walletModel = walletService.sendMoney(id, amount, address);
        model.addAttribute("walletModel", walletModel);
        model.addAttribute("wallet_id", id);
        return "sendMoney";
    }
}
