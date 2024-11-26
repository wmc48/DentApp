package com.booking_dent.dentApp.security;//package com.booking_dent.dentApp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService
    ) throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                        //.requestMatchers("/", "/index", "/navbar", "/register", "/static").permitAll() //główna strona i /index dostępne dla wszystkich
//                        .requestMatchers("/patient/**", "/patientDetails/**", "/reservation/**").hasAnyAuthority("patient", "admin", "staff", "doctor")
//                        .requestMatchers("/employee/**", "/employeeDetails/**", "/scheduleReservation").hasAnyAuthority("admin", "staff", "doctor")
                        .anyRequest().permitAll() //wszystkie inne strony wymagają uwierzytelnienia
                )
                .formLogin(formLogin -> formLogin
                        .permitAll() //formularz logowania dostępny dla wszystkich
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login") //po wylogowaniu przekierowanie na /login
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll() //wylogowanie dostępne dla wszystkich
                );

        return httpSecurity.build();
    }
}
