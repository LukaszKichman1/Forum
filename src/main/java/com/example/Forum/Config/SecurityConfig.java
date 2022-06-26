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
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    UserPrincipalDetailsService userPrincipalDetailsService;


    @Autowired
    public SecurityConfig(UserPrincipalDetailsService userPrincipalDetailsService) {
        this.userPrincipalDetailsService = userPrincipalDetailsService;

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/registration").permitAll()
                .antMatchers("/user/activation").permitAll()
                .antMatchers("/post/showall").permitAll()

                .antMatchers("/post/add").authenticated()
                .antMatchers("/post/showonybyid").authenticated()
                .antMatchers("/post/deleteownpostbyid").authenticated()
                .antMatchers("/post/deleteallownpost").authenticated()
                .antMatchers("/post/updatepost").authenticated()
                .antMatchers("/comment/deleteowncommentbyid").authenticated()
                .antMatchers("/comment/updatecomment").authenticated()

                .antMatchers("/user/showoneuserbyid").hasRole("ADMIN")
                .antMatchers("/user/showoneuserbynickname").hasRole("ADMIN")
                .antMatchers("/user/deletebyid").hasRole("ADMIN")
                .antMatchers("/post/deletepostbyid").hasRole("ADMIN")
                .antMatchers("/user/all").hasRole("ADMIN")
                .antMatchers("/comment/deletecommentbyid").hasRole("ADMIN")
                .and()
                .formLogin().permitAll();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userPrincipalDetailsService);
        return daoAuthenticationProvider;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}