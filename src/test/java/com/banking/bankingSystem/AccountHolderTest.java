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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
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
        CreditCard creditCard = accountRepository.save( new CreditCard(BigDecimal.valueOf(2001.00), accountHolder));
        accountHolder.getPrimaryAccountList().add(savings);
        accountHolder.getPrimaryAccountList().add(creditCard);
        Role role = roleRepository.save(new Role("ACCOUNT_HOLDER", accountHolder)) ;

        UsernamePasswordAuthenticationToken principal = this.getPrincipal("Super");
        SecurityContextHolder.getContext().setAuthentication(principal);

        MvcResult result = (MvcResult) mockMvc.perform(get("/account-balance").param("accountId", "2").principal(principal)).andExpect(status().isOk()).andExpect((ResultMatcher) jsonPath("$.balance", is(2001.00)));

        new BigDecimal(result.getResponse().getContentAsString());
    }


}
