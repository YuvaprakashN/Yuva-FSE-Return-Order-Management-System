package com.roms.microservice.authenticationservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.roms.microservice.authenticationservice.filter.AuthenticationInterceptorFilter;
import com.roms.microservice.authenticationservice.service.CustomUserDetailService;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailService userDetailService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).passwordEncoder(encoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().exceptionHandling().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers("/**").permitAll().anyRequest().authenticated();
//				.antMatchers("/auth/HealthCheck/**","/auth/swagger-ui/**","/auth/**","/h2-console/**","/actuator/**","/v2/api-docs/**","/swagger-ui/**","/swagger-ui.html","/api/swagger-ui/**", "/v2/api-docs",
//                        "/swagger-resources", 
//                       "/swagger-resources/configuration/ui", 
//                       "/swagger-resources/configuration/security").permitAll().anyRequest().authenticated();
		http.headers().frameOptions().disable();
		http.addFilterBefore(authenticationInterceptorFilterBean(), UsernamePasswordAuthenticationFilter.class);
	}

//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers("/auth/login","/h2-console/**","/v2/api-docs","/configuration/ui",
//					"/swagger-resources/**","/configuration/security","/swagger-ui.html","/webjars/**","/auth/swagger");
//	}
	@Bean
	public AuthenticationInterceptorFilter authenticationInterceptorFilterBean() {
		return new AuthenticationInterceptorFilter();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
 
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
