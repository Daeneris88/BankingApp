package com.banking.bankingSystem;

import com.banking.bankingSystem.modules.users.Admin;
import com.banking.bankingSystem.modules.users.Role;
import com.banking.bankingSystem.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankingSystemApplication implements CommandLineRunner {

	@Autowired
	AdminRepository adminRepository;



	public static void main(String[] args) {
		SpringApplication.run(BankingSystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Admin admin = adminRepository.save(new Admin("admin", "admin"));
		Role role = new Role("ADMIN", admin);

	}
}
