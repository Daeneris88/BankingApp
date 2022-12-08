package com.banking.bankingSystem;
import com.banking.bankingSystem.modules.DTO.AccountDTO;
import com.banking.bankingSystem.modules.accounts.Account;
import com.banking.bankingSystem.modules.accounts.CreditCard;
import com.banking.bankingSystem.modules.accounts.Savings;
import com.banking.bankingSystem.modules.users.AccountHolder;
import com.banking.bankingSystem.modules.users.Address;
import com.banking.bankingSystem.modules.users.Admin;
import com.banking.bankingSystem.modules.users.Role;
import com.banking.bankingSystem.repositories.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AdminTest {
    @Autowired
    WebApplicationContext context;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    protected AdminRepository adminRepository;
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
    public void create_checking_account() throws Exception {
        Address address = new Address("C. Pelayo", "Barcelona", "08045");
        Admin admin = adminRepository.save(new Admin("Admin", "1234"));
        AccountHolder accountHolder = accountHolderRepository.save(new AccountHolder("Lala", "1234", address, "abc@abc.com", LocalDate.of(1988, 8, 12)));
        Role adminRole = roleRepository.save(new Role("ADMIN", admin)) ;
        Role role = roleRepository.save(new Role("ACCOUNT_HOLDER", accountHolder)) ;
        AccountDTO account = new AccountDTO(accountHolder.getId(), null, new BigDecimal(2000), "secretKey");
        String body = objectMapper.writeValueAsString(account);

        MvcResult result = (MvcResult) mockMvc.perform(post("/create-checking").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
        assertFalse(result.getResponse().getContentAsString().contains("creditLimit"));

    }

    @Test
    public void create_studentChecking_account() throws Exception {
        Address address = new Address("C. Pelayo", "Barcelona", "08045");
        AccountHolder accountHolder = accountHolderRepository.save(new AccountHolder("Lala", "1234", address, "abc@abc.com", LocalDate.of(2015, 8, 12)));
        Role role = roleRepository.save(new Role("ACCOUNT_HOLDER", accountHolder)) ;
        AccountDTO account = new AccountDTO(accountHolder.getId(), null, new BigDecimal(2000), "secretKey");
        String body = objectMapper.writeValueAsString(account);

        MvcResult result = (MvcResult) mockMvc.perform(post("/create-checking").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
        assertFalse(result.getResponse().getContentAsString().contains("minimumBalance"));
        assertFalse(result.getResponse().getContentAsString().contains("penaltyFee"));
        assertFalse(result.getResponse().getContentAsString().contains("monthlyMaintenanceFee"));
    }

    @Test
    public void create_savings_account() throws Exception {
        Address address = new Address("C. Pelayo", "Barcelona", "08045");
        AccountHolder accountHolder = accountHolderRepository.save(new AccountHolder("Lala", "1234", address, "abc@abc.com", LocalDate.of(2015, 8, 12)));
        AccountDTO account = new AccountDTO(accountHolder.getId(), null, new BigDecimal(2000), "secretKey");
        String body = objectMapper.writeValueAsString(account);

        MvcResult result = (MvcResult) mockMvc.perform(post("/create-savings").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
        assertFalse(result.getResponse().getContentAsString().contains("creditLimit"));
        assertTrue(result.getResponse().getContentAsString().contains("minimumBalance"));
    }

    @Test
    public void create_creditCard_account() throws Exception {
        Address address = new Address("C. Pelayo", "Barcelona", "08045");
        AccountHolder accountHolder = accountHolderRepository.save(new AccountHolder("Lala", "1234", address, "abc@abc.com", LocalDate.of(2015, 8, 12)));
        AccountDTO account = new AccountDTO(accountHolder.getId(), null, new BigDecimal(2000), "secretKey");
        String body = objectMapper.writeValueAsString(account);

        MvcResult result = (MvcResult) mockMvc.perform(post("/create-creditCard").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("creditLimit"));
        assertFalse(result.getResponse().getContentAsString().contains("minimumBalance"));
    }




}
