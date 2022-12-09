package com.banking.bankingSystem.services;
import com.banking.bankingSystem.modules.DTO.AccountDTO;
import com.banking.bankingSystem.modules.accounts.*;
import com.banking.bankingSystem.modules.users.*;
import com.banking.bankingSystem.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;

@Service
public class AdminService {
    @Autowired    ThirdPartyRepository thirdPartyRepository;
    @Autowired    RoleRepository roleRepository;
    @Autowired    AccountHolderRepository accountHolderRepository;
    @Autowired    AccountRepository accountRepository;
    @Autowired    AdminRepository adminRepository;
    @Autowired    PasswordEncoder passwordEncoder;

    public ThirdParty createThirdParty(ThirdParty user) {
        return thirdPartyRepository.save(user);
    }

    public Account accountBalanceUpdate(Long userId, String secretKey, BigDecimal bigDecimal) {
        if (accountHolderRepository.findById(userId).isPresent()) {
            AccountHolder accountHolder = accountHolderRepository.findById(userId).get();
            Account account = accountCheck(accountHolder.getPrimaryAccountList(), secretKey, bigDecimal, accountHolder);
            if (account == null) account = accountCheck(accountHolder.getSecondaryAccountList(), secretKey, bigDecimal, accountHolder);
            if (account == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Id not found");
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Id not found");
    }

    public Account createChecking(AccountDTO accountDTO) {
        if(accountHolderRepository.findById(accountDTO.getPrimaryOwnerId()).isPresent()) {
            AccountHolder user = accountHolderRepository.findById(accountDTO.getPrimaryOwnerId()).get();
            Period intervalPeriod = Period.between(user.getDateOfBirth(), LocalDate.now());
            if (intervalPeriod.getYears() < 24) return accountRepository.save( new StudentChecking(accountDTO.getBalance(), accountDTO.getSecretKey(), user));
            else return accountRepository.save(new Checking(accountDTO.getBalance(), accountDTO.getSecretKey(), user));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Id not found");
    }

    public Account createCreditCard(AccountDTO account) {
        if(accountHolderRepository.findById(account.getPrimaryOwnerId()).isPresent()) {
            AccountHolder user = accountHolderRepository.findById(account.getPrimaryOwnerId()).get();
            return accountRepository.save( new CreditCard(account.getBalance(), user));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Id not found");
    }

    public Account createSavings(AccountDTO account) {
        if(accountHolderRepository.findById(account.getPrimaryOwnerId()).isPresent()) {
            AccountHolder user = accountHolderRepository.findById(account.getPrimaryOwnerId()).get();
            return accountRepository.save( new Savings(account.getBalance(), account.getSecretKey(), user));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Id not found");
    }

    public void deleteAccount(Long userId, Long accountId) {
        if(accountHolderRepository.findById(userId).isPresent()){
            if(accountRepository.findById(accountId).isPresent()&& accountRepository.findById(accountId).get().getPrimaryOwner().getId() == userId){
                accountRepository.deleteById(accountId);
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This account is not from this user");
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Id not found");
    }

    public Admin createAdmin(Admin user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        adminRepository.save(user);
        roleRepository.save(new Role("ADMIN", user));
        return user;
    }

    public AccountHolder createAccountHolder(AccountHolder user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        accountHolderRepository.save(user);
        roleRepository.save(new Role("ACCOUNT_HOLDER", user));
        return user;
    }

    private Account accountCheck(List<Account> accountList, String secretKey, BigDecimal bigDecimal, AccountHolder accountHolder){
        for (int i = 0; i < accountList.size(); i++) {
            Account tempAccount = accountList.get(i);
            if (Objects.equals(tempAccount.getSecretKey(), secretKey)) {
                tempAccount.setBalance(tempAccount.getBalance().add(bigDecimal));
                accountHolderRepository.save(accountHolder);
                return tempAccount;
            }
        }
        return null;
    }
}
