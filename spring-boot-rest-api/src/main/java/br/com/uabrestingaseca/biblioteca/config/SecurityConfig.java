package br.com.uabrestingaseca.biblioteca.config;

import br.com.uabrestingaseca.biblioteca.exceptions.handler.RestAuthenticationEntryPoint;
import br.com.uabrestingaseca.biblioteca.security.jwt.JwtConfigurer;
import br.com.uabrestingaseca.biblioteca.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private RestAuthenticationEntryPoint authenticationEntryPoint;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder;
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic().disable()
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.authorizeRequests()
				.antMatchers("/auth/signin", "/api-docs/**", "/swagger-ui.html**").permitAll()
				.antMatchers("/auth/signup").hasAuthority("ADMIN")
				.antMatchers("/").permitAll()
				.antMatchers("/info").hasAuthority("ADMIN")
				.antMatchers("/livros/**").permitAll()
				.antMatchers("/exemplares/**").permitAll()
				.antMatchers("/autores/**").permitAll()
				.antMatchers("/baixas/**").permitAll()
				.antMatchers("/reservas/**").permitAll()
				.antMatchers("/pendencias/usuario/**").permitAll()
				.antMatchers("/pendencias/adm/**").hasAnyAuthority("ADMIN")
				.antMatchers("/users").denyAll()
			.and()
				.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
			.and()
				.apply(new JwtConfigurer(tokenProvider));
	}
}
