package com.banking.bankingSystem.controlers;
import com.banking.bankingSystem.modules.accounts.Account;
import com.banking.bankingSystem.modules.users.ThirdParty;
import com.banking.bankingSystem.modules.users.User;
import com.banking.bankingSystem.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Optional;

@RestController
public class AdminController {
    @Autowired
    AdminService adminService;

    @PostMapping("/create-checking/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createChecking(@RequestBody Account account, @PathVariable Long userId){
        return adminService.createChecking(account, userId);
    }

    @PostMapping("/create-creditCard/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createCreditCard(@RequestBody Account account, Optional<BigDecimal> interestRate, Optional<BigDecimal> creditLimit, @PathVariable Long userId){
        return adminService.createCreditCard(account, userId, interestRate, creditLimit);
    }
    @PostMapping("/create-savings/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createSavings(@RequestBody Account account, @PathVariable Long userId){
        return adminService.createSavings(account, userId);
    }

    @PostMapping("/create-thirdParty")
    @ResponseStatus(HttpStatus.CREATED)
    public User createThirdParty(@RequestBody ThirdParty user){
        return adminService.createThirdParty(user);
    }

    @PatchMapping("/balance/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Account accountBalance(@PathVariable Long userId, @RequestHeader String secretKey, @RequestParam BigDecimal bigDecimal){
       return adminService.accountBalanceUpdate(userId, secretKey, bigDecimal);
    }
}
