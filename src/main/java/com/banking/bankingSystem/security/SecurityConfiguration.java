package com.banking.bankingSystem.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalAuthentication
public class SecurityConfiguration {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConf) throws Exception {
        return authConf.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic();
        httpSecurity.authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/create-thirdParty").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/create-creditCard").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/create-savings").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/create-checking").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/create-admin").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/create-accountHolder").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/balance/{userId}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/account/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/account-balance").hasRole("ACCOUNT_HOLDER")
                .requestMatchers(HttpMethod.POST, "/transfer").hasRole("ACCOUNT_HOLDER")
                .anyRequest().permitAll();
        httpSecurity.csrf().disable();
        return httpSecurity.build();
    }
}
