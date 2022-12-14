package com.banking.bankingSystem.controlers;
import com.banking.bankingSystem.modules.DTO.AccountDTO;
import com.banking.bankingSystem.modules.accounts.Account;
import com.banking.bankingSystem.modules.users.AccountHolder;
import com.banking.bankingSystem.modules.users.Admin;
import com.banking.bankingSystem.modules.users.ThirdParty;
import com.banking.bankingSystem.modules.users.User;
import com.banking.bankingSystem.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
public class AdminController {
    @Autowired
    AdminService adminService;

    @PostMapping("/create-admin")
    @ResponseStatus(HttpStatus.CREATED)
    public Admin createAdmin(@RequestBody Admin user){
        return adminService.createAdmin(user);
    }
    @PostMapping("/create-accountHolder")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder createAccountHolder(@RequestBody AccountHolder user){
        return adminService.createAccountHolder(user);
    }

    @PostMapping("/create-checking")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createChecking(@RequestBody AccountDTO account){
        return adminService.createChecking(account);
    }

    @PostMapping("/create-creditCard")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createCreditCard(@RequestBody AccountDTO account){
        return adminService.createCreditCard(account);
    }

    @PostMapping("/create-savings")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createSavings(@RequestBody AccountDTO account){
        return adminService.createSavings(account);
    }

    @PostMapping("/create-thirdParty")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty createThirdParty(@RequestBody ThirdParty user){
        return adminService.createThirdParty(user);
    }

    @PatchMapping("/balance/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Account accountBalance(@PathVariable Long userId, @RequestHeader String secretKey, @RequestParam BigDecimal bigDecimal){
       return adminService.accountBalanceUpdate(userId, secretKey, bigDecimal);
    }

    @DeleteMapping("/account/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccount(@PathVariable Long userId, @RequestParam Long accountId){
        adminService.deleteAccount(userId, accountId);
    }
}
