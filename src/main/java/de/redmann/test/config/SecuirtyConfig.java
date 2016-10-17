package de.redmann.test.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import de.redmann.test.backend.service.UserSecurityService;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by redmann on 13.10.16.
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class SecuirtyConfig extends WebSecurityConfigurerAdapter
{
	@Autowired
	private UserSecurityService		userSecurityService;
	@Autowired
	private Environment				env;
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
            "/console/**",
            "/actuator/**"
    };
    //@formatter:on
	
	
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception
	{
		List<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
		
		if (activeProfiles.contains("dev"))
		{
			log.info("dev mode");
			httpSecurity.csrf().disable();
			httpSecurity.headers().frameOptions().disable();
		}
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
                .userDetailsService(userSecurityService);
        //@formatter:on
	}
}
