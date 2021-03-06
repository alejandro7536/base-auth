package com.bolsadeideas.springboot.app;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolsadeideas.springboot.app.auth.handler.filter.JWTAuthenticationFilter;
import com.bolsadeideas.springboot.app.auth.handler.filter.JWTAuthorizationFilter;
import com.bolsadeideas.springboot.app.auth.service.JWTService;
import com.bolsadeideas.springboot.app.models.service.JpaUserDetailService;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)//activa las anotaciones de seguridad
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private JpaUserDetailService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/font/**", "/images/**", "/listar**", "/locale").permitAll()

				.anyRequest().authenticated()
				.and()
				.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtService))//filtros creados
				.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtService))
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	}
 
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder build) throws Exception {
		
		build.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder);
	}
}
 