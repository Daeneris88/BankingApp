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

    public BigDecimal getBalance(Long id, String userName){
        if(accountHolderRepository.findByName(userName).isPresent()){
            AccountHolder accountHolder = accountHolderRepository.findByName(userName).get();
            if(accountHolderRepository.findById(id).isPresent()){
                boolean isPresent = false;
                List<Account> primaryAccountList = accountHolder.getPrimaryAccountList();
                List<Account> secondaryAccountList = accountHolder.getSecondaryAccountList();
                    isPresent = isPresent(primaryAccountList, id);
                if (!isPresent) isPresent = isPresent(secondaryAccountList, id);
                if (isPresent) return accountRepository.findById(id).get().getBalance();
                else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This account is not related to this Account Holder");
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Id not found");
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User name not found");
    }

    public BigDecimal transfer(Transfer transfer, String userName) {
        if(accountHolderRepository.findByName(userName).isPresent()){
            AccountHolder sender = accountHolderRepository.findByName(userName).get();
            Account sendingAccount = null;
            for(Account account : sender.getPrimaryAccountList()){
                if(account.getId() == transfer.getSendingId()) sendingAccount = account;
            }
            if(sendingAccount == null) {
                for (Account account : sender.getSecondaryAccountList()) {
                    if (account.getId() == transfer.getSendingId()) sendingAccount = account;
                }
            }
            if(sendingAccount == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Id not found");

            Account receivingAccount = null;
            for(Account account : accountRepository.findAll()){
                if(account.getId() == transfer.getReceivingId() &&
                        (account.getPrimaryOwner().equals(transfer.getRecipientName()) ||
                                account.getSecondaryOwner().equals(transfer.getRecipientName()))) receivingAccount = account;
            }
            if(receivingAccount == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Id is not from this owner");

            if(sendingAccount.getBalance().subtract(transfer.getAmount()).compareTo(BigDecimal.valueOf(0)) < 0)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You don't have enough money to do the transfer");
            else {
                sendingAccount.setBalance(sendingAccount.getBalance().subtract(transfer.getAmount()));
                accountRepository.save(sendingAccount);
                receivingAccount.setBalance(receivingAccount.getBalance().add(transfer.getAmount()));
                accountRepository.save(receivingAccount);
                return sendingAccount.getBalance();
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User name not found");
    }

    private boolean isPresent(List<Account> accountList, Long id){
        boolean isPresent = false;
        for (int i = 0; i < accountList.size(); i++) {
            Account account = accountList.get(i);
            if (Objects.equals(account.getId(), id)) {
                isPresent = true;
            }
        }
        return isPresent;
    }



    /* before security implementation
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
    }     */
}
