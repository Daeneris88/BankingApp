package com.banking.bankingSystem;
import com.banking.bankingSystem.modules.accounts.CreditCard;
import com.banking.bankingSystem.modules.accounts.Savings;
import com.banking.bankingSystem.modules.users.AccountHolder;
import com.banking.bankingSystem.modules.users.Address;
import com.banking.bankingSystem.modules.users.Role;
import com.banking.bankingSystem.repositories.AccountHolderRepository;
import com.banking.bankingSystem.repositories.AccountRepository;
import com.banking.bankingSystem.repositories.RoleRepository;
import com.banking.bankingSystem.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AccountHolderTest {
    @Autowired
    WebApplicationContext context;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    protected UserDetailsService userDetailsService;
    MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected UsernamePasswordAuthenticationToken getPrincipal(String userName) {
        UserDetails user = this.userDetailsService.loadUserByUsername(userName);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        user.getPassword(),
                        user.getAuthorities());
        return authentication;
    }

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void get_account_balance() throws Exception {
        Address address = new Address("C. Pelayo", "Barcelona", "08045");
        AccountHolder accountHolder = accountHolderRepository.save(new AccountHolder("Super", "1234", address, "abc@abc.com", LocalDate.of(1988, 8, 12)));
        Savings savings = accountRepository.save( new Savings(BigDecimal.valueOf(2000.00), "secretKey", accountHolder));
        roleRepository.save(new Role("ACCOUNT_HOLDER", accountHolder)) ;
        UsernamePasswordAuthenticationToken principal = this.getPrincipal("Super");
        SecurityContextHolder.getContext().setAuthentication(principal);

        MvcResult result = mockMvc.perform(get("/account-balance").param("accountId", savings.getId().toString()).principal(principal))
                .andExpect(status().isOk()).andReturn();
        assertEquals("2000.00", result.getResponse().getContentAsString());
    }

    @Test
    void get_account_balance_account_not_related_to_accountHolder() throws Exception {
        Address address = new Address("C. Pelayo", "Barcelona", "08045");
        AccountHolder accountHolder = accountHolderRepository.save(new AccountHolder("Super", "1234", address, "abc@abc.com", LocalDate.of(1988, 8, 12)));
        AccountHolder accountHolder2 = accountHolderRepository.save(new AccountHolder("jaja", "1234", address, "abc@abc.com", LocalDate.of(1988, 8, 12)));
        CreditCard creditCard = accountRepository.save( new CreditCard(BigDecimal.valueOf(2001.00), accountHolder2));
        roleRepository.save(new Role("ACCOUNT_HOLDER", accountHolder)) ;
        UsernamePasswordAuthenticationToken principal = this.getPrincipal("Super");
        SecurityContextHolder.getContext().setAuthentication(principal);

        MvcResult result = mockMvc.perform(get("/account-balance").param("accountId", creditCard.getId().toString()).principal(principal))
                .andExpect(status().isNotFound()).andReturn();
        assertEquals("404 NOT_FOUND \"This account is not related to this Account Holder\"", result.getResolvedException().getMessage());
    }
    @Test
    void get_account_balance_account_id_not_found() throws Exception {
        Address address = new Address("C. Pelayo", "Barcelona", "08045");
        AccountHolder accountHolder = accountHolderRepository.save(new AccountHolder("Super", "1234", address, "abc@abc.com", LocalDate.of(1988, 8, 12)));
        accountRepository.save( new Savings(BigDecimal.valueOf(2000.00), "secretKey", accountHolder));
        accountRepository.save( new CreditCard(BigDecimal.valueOf(2001.00), accountHolder));
        roleRepository.save(new Role("ACCOUNT_HOLDER", accountHolder)) ;
        UsernamePasswordAuthenticationToken principal = this.getPrincipal("Super");
        SecurityContextHolder.getContext().setAuthentication(principal);

        MvcResult result = mockMvc.perform(get("/account-balance").param("accountId", "123456789").principal(principal))
                .andExpect(status().isNotFound()).andReturn();
        assertEquals("404 NOT_FOUND \"Account Id not found\"", result.getResolvedException().getMessage());
    }

    /* Este test con el security implementado no tiene sentido
    @Test
    void get_account_balance_user_name_not_found() throws Exception {
        Address address = new Address("C. Pelayo", "Barcelona", "08045");
        AccountHolder accountHolder = accountHolderRepository.save(new AccountHolder("Super", "1234", address, "abc@abc.com", LocalDate.of(1988, 8, 12)));
        CreditCard creditCard = accountRepository.save( new CreditCard(BigDecimal.valueOf(2001.00), accountHolder));
        roleRepository.save(new Role("ACCOUNT_HOLDER", accountHolder)) ;
        UsernamePasswordAuthenticationToken principal = this.getPrincipal("Super");
        SecurityContextHolder.getContext().setAuthentication(principal);

        MvcResult result = mockMvc.perform(get("/account-balance").param("accountId", creditCard.getId().toString()).principal(principal))
                .andExpect(status().isNotFound()).andReturn();
        assertEquals("404 NOT_FOUND \"User name not found\"", result.getResolvedException().getMessage());
    }*/


}
