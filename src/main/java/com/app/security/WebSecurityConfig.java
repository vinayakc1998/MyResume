package com.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
	@Autowired
    @Qualifier("userDetailsService")
    protected UserDetailsService userDetailsService;

	//Available Spring Security Password Encryption Bean Creation which uses hash algorithm (Blowfish Block Cipher)
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.cors().and().csrf().disable()
            .authorizeRequests()
            .antMatchers("/css/**","/images/**","/", "/user/login", "/user/register","/user/mocktest").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().loginPage("/user/login").usernameParameter("email").passwordParameter("password")
            .loginProcessingUrl("/user/login")
            .defaultSuccessUrl("/user/process-login", true)
            .failureUrl("/user/login")
            .permitAll();
        http.logout().logoutUrl("/user/logout")
        	.logoutSuccessUrl("/")
        	.invalidateHttpSession(true)
        	.deleteCookies("JSESSIONID");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean(name = BeanIds.USER_DETAILS_SERVICE)
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }
  
}