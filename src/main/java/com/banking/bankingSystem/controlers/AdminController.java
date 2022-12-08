package com.banking.bankingSystem.controlers;
import com.banking.bankingSystem.modules.DTO.AccountDTO;
import com.banking.bankingSystem.modules.accounts.Account;
import com.banking.bankingSystem.modules.users.ThirdParty;
import com.banking.bankingSystem.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
public class AdminController {
    @Autowired
    AdminService adminService;

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

    @DeleteMapping("account/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccount(@PathVariable Long userId, @RequestHeader Long accountId){
        adminService.deleteAccount(userId, accountId);
    }
}
