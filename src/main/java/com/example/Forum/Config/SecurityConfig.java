package com.example.Forum.Config;

import com.example.Forum.Service.UserPrincipalDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{

    UserPrincipalDetailsService userPrincipalDetailsService;


    @Autowired
    public SecurityConfig( UserPrincipalDetailsService userPrincipalDetailsService)
    {
        this.userPrincipalDetailsService=userPrincipalDetailsService;

    }



    //pobieranie uzytkownikow z bazy
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
    {
        auth.authenticationProvider(authenticationProvider());
    }


    //dostep do endpointow
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/registration").permitAll()
                .antMatchers("/user/activation").permitAll()
                .antMatchers("/user/showoneuserbyid").hasRole("ADMIN")
                .antMatchers("/user/showoneuserbynickname").hasRole("ADMIN")
                .antMatchers("/user/deletebyid").hasRole("ADMIN")
                .antMatchers("/deletemoldy").authenticated()
                .antMatchers("/deleteused").authenticated()
                .antMatchers("/logs").hasRole("ADMIN")
                .and()
                .formLogin().permitAll();
    }

    //sprawdzanie princepalow usera
    @Bean
    DaoAuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userPrincipalDetailsService);
        return daoAuthenticationProvider;
    }


    //hasowanie hasel
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

}