package com.banking.bankingSystem;
import com.banking.bankingSystem.modules.accounts.CreditCard;
import com.banking.bankingSystem.modules.accounts.Savings;
import com.banking.bankingSystem.modules.users.AccountHolder;
import com.banking.bankingSystem.modules.users.Address;
import com.banking.bankingSystem.modules.users.Admin;
import com.banking.bankingSystem.modules.users.Role;
import com.banking.bankingSystem.repositories.AccountHolderRepository;
import com.banking.bankingSystem.repositories.AccountRepository;
import com.banking.bankingSystem.repositories.AdminRepository;
import com.banking.bankingSystem.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class BankingSystemApplication implements CommandLineRunner {

	@Autowired
	AdminRepository adminRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	AccountHolderRepository accountHolderRepository;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(BankingSystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Admin admin = adminRepository.save(new Admin("admin", passwordEncoder.encode("admin")));
		Role role = roleRepository.save( new Role("ADMIN", admin));
		Address address = new Address("C. Pelayo", "Barcelona", "08045");
		AccountHolder accountHolder = accountHolderRepository.save(new AccountHolder("Lala", passwordEncoder.encode("1234"), address, "abc@abc.com", LocalDate.of(1988, 8, 12)));
		Role role1 = roleRepository.save(new Role("ACCOUNT_HOLDER", accountHolder));
		AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Pepa", passwordEncoder.encode("1234"), address, "abd@abc.com", LocalDate.of(1999, 8, 12)));
		Role role2 = roleRepository.save(new Role("ACCOUNT_HOLDER", accountHolder1));
		Savings savings = accountRepository.save( new Savings(BigDecimal.valueOf(2000.00), "secretKey", accountHolder));
		CreditCard creditCard = accountRepository.save( new CreditCard(BigDecimal.valueOf(2001.00), accountHolder1));
	}
}
