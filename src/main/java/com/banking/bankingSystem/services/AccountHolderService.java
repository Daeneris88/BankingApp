package com.banking.bankingSystem.services;
import com.banking.bankingSystem.modules.Transfer;
import com.banking.bankingSystem.modules.accounts.Account;
import com.banking.bankingSystem.modules.accounts.Checking;
import com.banking.bankingSystem.modules.accounts.CreditCard;
import com.banking.bankingSystem.modules.accounts.Savings;
import com.banking.bankingSystem.modules.users.AccountHolder;
import com.banking.bankingSystem.repositories.AccountHolderRepository;
import com.banking.bankingSystem.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
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
                if (isPresent){
                    Account account = accountRepository.findById(id).get();
                    if (account instanceof Savings){
                        Period intervalPeriod = Period.between(((Savings) account).getInterestDate(), LocalDate.now());
                        if(intervalPeriod.getYears() >= 1 ){
                            account.setBalance(account.getBalance().multiply(((Savings) account).getInterestRate()
                                    .multiply(BigDecimal.valueOf(intervalPeriod.getYears()))));
                            ((Savings) account).setInterestDate(LocalDate.now());
                        }
                    }
                    if (account instanceof CreditCard){
                        Period intervalPeriod = Period.between(((CreditCard) account).getInterestDate(), LocalDate.now());
                        if(intervalPeriod.getMonths() >= 1 ){
                            account.setBalance(account.getBalance().multiply(((CreditCard) account).getInterestRate()
                                    .multiply(BigDecimal.valueOf(intervalPeriod.getMonths()))));
                            ((CreditCard) account).setInterestDate(LocalDate.now());
                        }
                    }
                    if (account instanceof Checking){
                        Period intervalPeriod = Period.between(((Checking) account).getMonthlyMaintenanceDate(), LocalDate.now());
                        if(intervalPeriod.getMonths() >= 1 ){
                            account.setBalance(account.getBalance().multiply(((Checking) account).getMonthlyMaintenanceFee()
                                    .multiply(BigDecimal.valueOf(intervalPeriod.getMonths()))));
                            ((Checking) account).setMonthlyMaintenanceDate(LocalDate.now());
                        }
                    }
                    return accountRepository.findById(id).get().getBalance();
                }
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
            Account receivingAccount = null;
            for(Account account : sender.getPrimaryAccountList()){
                if(Objects.equals(account.getId(), transfer.getSendingId())) sendingAccount = account;
            }
            if(sendingAccount == null) {
                for (Account account : sender.getSecondaryAccountList()) {
                    if (Objects.equals(account.getId(), transfer.getSendingId())) sendingAccount = account;
                }
            }
            if(sendingAccount == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Id not found");

            for(Account account : accountRepository.findAll()){
                if(Objects.equals(account.getId(), transfer.getReceivingId())) {
                    if(account.getPrimaryOwner().getName().equals(transfer.getRecipientName())) receivingAccount = account;
                    if(account.getSecondaryOwner() != null){
                        if(account.getSecondaryOwner().getName().equals(transfer.getRecipientName())) receivingAccount = account;
                    }
                }
            }
            if(receivingAccount == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Id is not from this owner");

            if(sendingAccount.getBalance().subtract(transfer.getAmount()).compareTo(BigDecimal.valueOf(0)) < 0)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You don't have enough money to do the transfer");
            else {
                if(sendingAccount instanceof Checking){
                    BigDecimal minBalance = ((Checking) sendingAccount).getMinimumBalance();
                    if (minBalance.compareTo(sendingAccount.getBalance().subtract(transfer.getAmount())) > 0){
                        sendingAccount.setBalance(sendingAccount.getBalance().subtract(transfer.getAmount()).subtract(((Checking) sendingAccount).getPenaltyFee()));
                        accountRepository.save(sendingAccount);
                    }
                }
                if(sendingAccount instanceof Savings){
                    BigDecimal minBalance = ((Savings) sendingAccount).getMinimumBalance();
                    if (minBalance.compareTo(sendingAccount.getBalance().subtract(transfer.getAmount())) > 0){
                        sendingAccount.setBalance(sendingAccount.getBalance().subtract(transfer.getAmount()).subtract(((Savings) sendingAccount).getPenaltyFee()));
                        accountRepository.save(sendingAccount);
                    }
                }
                else{
                    sendingAccount.setBalance(sendingAccount.getBalance().subtract(transfer.getAmount()));
                    accountRepository.save(sendingAccount);
                }
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
