package com.abdisalam.efleague;

import com.abdisalam.efleague.UserAuthService;
import com.abdisalam.efleague.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
                        .requestMatchers("/landingPage", "/landingPage/**","/test-email","/images/**","/webjars/**", "/users/login", "/users/signup", "/css/**", "/js/**").permitAll()

                        //Only captains and admins can create and manage teams
                        .requestMatchers("/teams/create").hasAnyRole("ADMIN")

                        .requestMatchers("/teams/{teamId}/edit").hasAnyRole("ADMIN")

                        //All authenticated users can view teams and matchups
                        .requestMatchers("/teams", "/matchups").authenticated()

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form

                        .loginPage("/users/login")//Custom login Page
                        .defaultSuccessUrl("/landingPage", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/users/login?logout")//Redirect to login page after logout
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );
                //Disable CSRF for now (Enable later for added security)
        return httpSecurity.build();
    }





}
