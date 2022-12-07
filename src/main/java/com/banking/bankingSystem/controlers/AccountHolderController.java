package com.banking.bankingSystem.controlers;
import com.banking.bankingSystem.modules.Transfer;
import com.banking.bankingSystem.security.CustomUserDetails;
import com.banking.bankingSystem.services.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@Controller
public class AccountHolderController {
    @Autowired
    AccountHolderService accountHolderService;

    /* Before Security
    @GetMapping("/account-balance")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal getBalance(@RequestHeader String secretKey, @RequestParam Long id){
        return accountHolderService.getBalance(secretKey, id);
    } */

    @GetMapping("/account-balance")
    public BigDecimal getBalance(@RequestParam Long accountId, @AuthenticationPrincipal CustomUserDetails user){
        return accountHolderService.getBalance(accountId, user.getUsername());
    }

    @PostMapping("/transfer")
    public BigDecimal transfer(@RequestBody Transfer transfer, @AuthenticationPrincipal CustomUserDetails user){
        return accountHolderService.transfer(transfer, user.getUsername());
    }

}
