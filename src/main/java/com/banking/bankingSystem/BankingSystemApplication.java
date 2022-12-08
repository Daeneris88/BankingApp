package com.banking.bankingSystem;

import com.banking.bankingSystem.modules.accounts.Account;
import com.banking.bankingSystem.modules.users.AccountHolder;
import com.banking.bankingSystem.modules.users.Address;
import com.banking.bankingSystem.modules.users.Admin;
import com.banking.bankingSystem.modules.users.Role;
import com.banking.bankingSystem.repositories.AccountHolderRepository;
import com.banking.bankingSystem.repositories.AdminRepository;
import com.banking.bankingSystem.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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



	public static void main(String[] args) {
		SpringApplication.run(BankingSystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Admin admin = adminRepository.save(new Admin("admin", "admin"));
		Role role = new Role("ADMIN", admin);
		Address address = new Address("C. Pelayo", "Barcelona", "08045");
		AccountHolder accountHolder = accountHolderRepository.save(new AccountHolder("Lala", "1234", address, "abc@abc.com", LocalDate.of(1988, 8, 12)));
		Role role1 = roleRepository.save(new Role("ACCOUNT_HOLDER", accountHolder)) ;

	}
}
