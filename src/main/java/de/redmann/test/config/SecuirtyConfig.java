package de.redmann.test.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by redmann on 13.10.16.
 */
@Configuration
@EnableWebSecurity
public class SecuirtyConfig extends WebSecurityConfigurerAdapter
{
	//@formatter:off
    public static final String[] PUBLIC_MATCHERS = {
            "/webjars/**",
            "/css/**",
            "/js/**",
            "/images/**",
            "/",
            "/about/**",
            "/contact/**",
            "/error/**/*",
    };
    //@formatter:on
	
	
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception
	{
		//@formatter:off
        httpSecurity
                .authorizeRequests()
                .antMatchers(PUBLIC_MATCHERS).permitAll()
                .anyRequest().authenticated()
                .and()
                    .formLogin().loginPage("/login").defaultSuccessUrl("/payload")
                    .failureUrl("/login?error").permitAll()
                .and()
                    .logout().permitAll();
        //@formatter:on
	}
	
	
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
	{
		//@formatter:off
		auth
                .inMemoryAuthentication()
                    .withUser("user").password("password").roles("USER");
        //@formatter:on
	}
}
