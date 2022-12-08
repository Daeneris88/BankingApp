package com.banking.bankingSystem.services;
import com.banking.bankingSystem.modules.DTO.AccountDTO;
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

    public Account createChecking(AccountDTO accountDTO) {
        if(accountHolderRepository.findById(accountDTO.getPrimaryOwnerId()).isPresent()) {
            AccountHolder user = accountHolderRepository.findById(accountDTO.getPrimaryOwnerId()).get();
            Period intervalPeriod = Period.between(user.getDateOfBirth(), LocalDate.now());
            if (intervalPeriod.getYears() < 24) {
                return accountRepository.save( new StudentChecking(accountDTO.getBalance(), accountDTO.getSecretKey(), user));
            } else {
                return accountRepository.save(new Checking(accountDTO.getBalance(), accountDTO.getSecretKey(), user));
            }
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

    }
}
