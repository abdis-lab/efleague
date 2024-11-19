package com.abdisalam.efleague;

import com.abdisalam.efleague.services.UserAuthService;
import com.abdisalam.efleague.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserAuthService userAuthService;


    public SecurityConfig(UserAuthService userAuthService){
        this.userAuthService =  userAuthService;
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    //Configuration the security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {


        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/landingPage", "/users/login", "/users/signup", "/css/**", "/js/**").permitAll()

                        //Only captains and admins can create and manage teams
                        .requestMatchers("/teams/create", "/teams/{teamId}/edit").hasAnyRole("CAPTAIN", "ADMIN")

                        //All authenticated users can view temas and matchups
                        .requestMatchers("/teams", "/matchups").authenticated()

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form

                        .loginPage("/login")//Custom login Page
                        .defaultSuccessUrl("/landingPage", true)
                        .permitAll()
                )
                .logout(logout -> logout

                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")//Redirect to login page after logout
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        .disable()
                );//Disable CSRF for now (Enable later for added security)
        return httpSecurity.build();
    }


    @Bean
    public UserDetailsService userDetailsService(){
        return userAuthService;
    }




}
