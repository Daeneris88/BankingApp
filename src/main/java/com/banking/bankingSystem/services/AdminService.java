package com.banking.bankingSystem.services;
import com.banking.bankingSystem.modules.accounts.*;
import com.banking.bankingSystem.modules.users.*;
import com.banking.bankingSystem.repositories.AccountHolderRepository;
import com.banking.bankingSystem.repositories.AccountRepository;
import com.banking.bankingSystem.repositories.RoleRepository;
import com.banking.bankingSystem.repositories.ThirdPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    ThirdPartyRepository thirdPartyRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    AccountRepository accountRepository;

    public User createThirdParty(ThirdParty user) {
        Role role = roleRepository.save(new Role("THIRD_PARTY", user));
        return thirdPartyRepository.save(user);
    }

    public Account accountBalanceUpdate(Long userId, String secretKey, BigDecimal bigDecimal) {
        if (accountHolderRepository.findById(userId).isPresent()) {
            AccountHolder accountHolder = accountHolderRepository.findById(userId).get();
            boolean isPresent = false;
            List<Account> primaryAccountList = accountHolder.getPrimaryAccountList();
            List<Account> secondaryAccountList = accountHolder.getSecondaryAccountList();
            for (int i = 0; i < primaryAccountList.size(); i++) {
                Account account = primaryAccountList.get(i);
                if (Objects.equals(account.getSecretKey(), secretKey)) {
                    account.setBalance(account.getBalance().add(bigDecimal));
                    accountHolderRepository.save(accountHolder);
                    isPresent = true;
                    return account;
                }
            }
            if (!isPresent) {
                for (int i = 0; i < secondaryAccountList.size(); i++) {
                    Account account = secondaryAccountList.get(i);
                    if (Objects.equals(account.getSecretKey(), secretKey)) {
                        account.setBalance(account.getBalance().add(bigDecimal));
                        accountHolderRepository.save(accountHolder);
                        isPresent = true;
                        return account;
                    }
                }
            }
            if (!isPresent) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Id not found");
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Id not found");
    }

    public Account createChecking(Account account, Long id) {
        if(accountHolderRepository.findById(id).isPresent()) {
            AccountHolder user = accountHolderRepository.findById(id).get();
            Period intervalPeriod = Period.between(user.getDateOfBirth(), LocalDate.now());

            if (intervalPeriod.getYears() < 24) {
                StudentChecking studentChecking = new StudentChecking(account.getBalance(), account.getSecretKey(), user);
                user.getPrimaryAccountList().add(studentChecking);
                accountHolderRepository.save(user);
                accountRepository.save(studentChecking);
                return studentChecking;
            } else {
                Checking checking = new Checking(account.getBalance(), account.getSecretKey(), user);
                user.getPrimaryAccountList().add(checking);
                accountHolderRepository.save(user);
                accountRepository.save(checking);
                return checking;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Id not found");
    }

    public Account createCreditCard(Account account, Long id, Optional<BigDecimal> interestRate, Optional<BigDecimal> creditLimit) {
        if(accountHolderRepository.findById(id).isPresent()) {
            AccountHolder user = accountHolderRepository.findById(id).get();
            CreditCard creditCard = new CreditCard(account.getBalance(), user);
            user.getPrimaryAccountList().add(creditCard);
            accountHolderRepository.save(user);
            accountRepository.save(creditCard);
            return creditCard;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Id not found");
    }

    public Account createSavings(Account account, Long id) {
        if(accountHolderRepository.findById(id).isPresent()) {
            AccountHolder user = accountHolderRepository.findById(id).get();
            Savings savings = new Savings(account.getBalance(), account.getSecretKey(), user);
            user.getPrimaryAccountList().add(savings);
            accountHolderRepository.save(user);
            accountRepository.save(savings);
            return savings;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Id not found");
    }
}
