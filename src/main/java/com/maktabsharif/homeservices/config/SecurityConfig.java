package com.maktabsharif.homeservices.config;

import com.maktabsharif.homeservices.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public SecurityConfig(UserRepository userRepository,
                          BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/client/create").permitAll()
                .requestMatchers("/client/**").hasAnyRole("ADMIN", "CLIENT")
                .requestMatchers("/expert/create").permitAll()
                .requestMatchers("/expert/**").hasAnyRole("ADMIN", "EXPERT")
                .requestMatchers("/user/activateAccount/**").permitAll()
                .requestMatchers("transactions/**").permitAll()
                .requestMatchers("/subservices/**").hasRole("ADMIN")
                .requestMatchers("/services/**").hasRole("ADMIN")
                .requestMatchers("orders/create").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic();

        return httpSecurity.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService((username) -> userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException(String.format("This %s not found.", username))))
                .passwordEncoder(passwordEncoder);
    }

}
