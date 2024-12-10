package com.booking_dent.dentApp.security;//package com.booking_dent.dentApp.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@AllArgsConstructor
@Configuration
public class SecurityConfiguration {

    private UserRepository userRepository;

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
                        .requestMatchers("/patientView/**").hasAnyAuthority("patient")
                        .requestMatchers("/staffView/**").hasAnyAuthority("staff", "doctor", "admin")
                        .anyRequest().permitAll() //wszystkie inne strony wymagają uwierzytelnienia
                )
                .formLogin(formLogin -> formLogin
                        .successHandler(customAuthenticationSuccessHandler())
                        .permitAll()//formularz logowania dostępny dla wszystkich
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login") //po wylogowaniu przekierowanie na /login
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll() //wylogowanie dostępne dla wszystkich
                );
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            //pobierz nazwę użytkownika z kontekstu bezpieczeństwa
            String username = authentication.getName();

            //znajdź użytkownika w bazie danych
            UserEntity user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            //sprawdź role użytkownika
            boolean isPatient = user.getRoles().stream()
                    .anyMatch(role -> role.getRoleName().equalsIgnoreCase("patient"));

            //w zależności od roli
            if (isPatient) {
                response.sendRedirect("/patientView/dashboard");
            } else {
                response.sendRedirect("/staffView/dashboard");
            }
        };
    }
}
