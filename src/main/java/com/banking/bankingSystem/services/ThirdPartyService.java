package com.banking.bankingSystem.services;
import com.banking.bankingSystem.enums.AccountStatus;
import com.banking.bankingSystem.modules.Transfer;
import com.banking.bankingSystem.modules.accounts.Account;
import com.banking.bankingSystem.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;

@Service
public class ThirdPartyService {
    @Autowired
    AccountRepository accountRepository;
    public BigDecimal transfer(Transfer transfer, String hashedKey) {
        if(accountRepository.findById(transfer.getSendingId()).isPresent()){
            Account sendingAccount = accountRepository.findById(transfer.getSendingId()).get();
            if(sendingAccount.getBalance().subtract(transfer.getAmount()).compareTo(BigDecimal.valueOf(0)) < 0)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not enough funds");
            if(sendingAccount.getStatus() == AccountStatus.FROZEN)
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Account frozen");
            else{
                Account receivingAccount = accountRepository.findById(accountRepository.findBySecretKey(transfer.getSecretKey()).getId()).get();
                sendingAccount.setBalance(sendingAccount.getBalance().subtract(transfer.getAmount()));
                accountRepository.save(sendingAccount);
                receivingAccount.setBalance(receivingAccount.getBalance().add(transfer.getAmount()));
                accountRepository.save(receivingAccount);
                return transfer.getAmount();
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
    }
}
