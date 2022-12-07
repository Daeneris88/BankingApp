package com.banking.bankingSystem.services;
import com.banking.bankingSystem.modules.Transfer;
import com.banking.bankingSystem.modules.accounts.Account;
import com.banking.bankingSystem.modules.users.AccountHolder;
import com.banking.bankingSystem.repositories.AccountHolderRepository;
import com.banking.bankingSystem.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class AccountHolderService {
    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    AccountRepository accountRepository;

    public BigDecimal getBalance2(Long id, String userName){
        AccountHolder accountHolder = accountHolderRepository.findByName(userName).get();

        return accountRepository.findById(id).get().getBalance();
    }

    public BigDecimal getBalance(String secretKey, Long id) {
        AccountHolder accountHolder = accountHolderRepository.findById(id).get();
        boolean isPresent = false;
        List<Account> primaryAccountList = accountHolder.getPrimaryAccountList();
        List<Account> secondaryAccountList = accountHolder.getSecondaryAccountList();
        for (int i = 0; i < primaryAccountList.size(); i++) {
            Account account = primaryAccountList.get(i);
            if (Objects.equals(account.getSecretKey(), secretKey)) {
                isPresent = true;
                return account.getBalance();
            }
        }
        if (!isPresent) {
            for (int i = 0; i < secondaryAccountList.size(); i++) {
                Account account = secondaryAccountList.get(i);
                if (Objects.equals(account.getSecretKey(), secretKey)) {
                    isPresent = true;
                    return account.getBalance();
                }
            }
        }
        if (!isPresent) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Id not found");
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Id not found");
    }

    public BigDecimal transfer2(Transfer transfer, String username) {
        AccountHolder sender = accountHolderRepository.findByName(username).get();
        Account sendingAccount = null;
        Account receivingAccount = null;
        for(Account a : sender.getPrimaryAccountList()){
            if(a.getId() == transfer.getSendingId()) sendingAccount = a;
        }

        for(Account a : accountRepository.findAll()){
            if(a.getId() == transfer.getRecevingId() && a.getPrimaryOwner().equals(transfer.getRecipientName())) receivingAccount = a;
        }
        sendingAccount.setBalance(sendingAccount.getBalance().subtract(transfer.getAmount()));
        accountRepository.save(sendingAccount);
        receivingAccount.setBalance(receivingAccount.getBalance().add(transfer.getAmount()));
        accountRepository.save(receivingAccount);
        return sendingAccount.getBalance();

    }
}
