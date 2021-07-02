package br.com.uabrestingaseca.biblioteca.config;

import br.com.uabrestingaseca.biblioteca.exceptions.handler.RestAuthenticationEntryPoint;
import br.com.uabrestingaseca.biblioteca.security.jwt.JwtConfigurer;
import br.com.uabrestingaseca.biblioteca.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsUtils;

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
				.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
				.antMatchers("/auth/signin", "/api-docs/**", "/swagger-ui.html**").permitAll()
				.antMatchers("/auth/signup").hasAnyAuthority("ADMIN", "GERENTE")
				.antMatchers("/auth/users").hasAnyAuthority("ADMIN", "GERENTE")
				.antMatchers("/").permitAll()
				.antMatchers("/info").hasAnyAuthority("ADMIN", "GERENTE")
				.antMatchers(HttpMethod.GET, "/livros/**").permitAll()
				.antMatchers(HttpMethod.POST, "/livros/**").hasAnyAuthority("ADMIN", "GERENTE")
				.antMatchers(HttpMethod.PUT, "/livros/**").hasAnyAuthority("ADMIN", "GERENTE")
				.antMatchers(HttpMethod.DELETE, "/livros/**").hasAnyAuthority("ADMIN", "GERENTE")
				.antMatchers(HttpMethod.GET, "/exemplares/**").permitAll()
				.antMatchers(HttpMethod.POST, "/exemplares/**").hasAnyAuthority("ADMIN", "GERENTE")
				.antMatchers(HttpMethod.PUT, "/exemplares/**").hasAnyAuthority("ADMIN", "GERENTE")
				.antMatchers(HttpMethod.DELETE, "/exemplares/**").hasAnyAuthority("ADMIN", "GERENTE")
				.antMatchers("/autores/**").hasAnyAuthority("ADMIN", "GERENTE")
				.antMatchers("/baixas/**").hasAnyAuthority("ADMIN", "GERENTE")
				.antMatchers(HttpMethod.GET, "/reservas/**").hasAnyAuthority("ADMIN", "GERENTE", "ALUNO")
				.antMatchers(HttpMethod.POST, "/reservas/**").hasAnyAuthority("ADMIN", "GERENTE", "ALUNO")
				.antMatchers(HttpMethod.PUT, "/reservas/**").hasAnyAuthority("ADMIN", "GERENTE")
				.antMatchers(HttpMethod.DELETE, "/reservas/**").hasAnyAuthority("ADMIN", "GERENTE")
				.antMatchers(HttpMethod.GET,"/emprestimos/**").hasAnyAuthority("ADMIN", "GERENTE", "ALUNO")
				.antMatchers(HttpMethod.POST,"/emprestimos/**").hasAnyAuthority("ADMIN", "GERENTE")
				.antMatchers(HttpMethod.PUT,"/emprestimos/**").hasAnyAuthority("ADMIN", "GERENTE")
				.antMatchers(HttpMethod.DELETE,"/emprestimos/**").hasAnyAuthority("ADMIN", "GERENTE")
				.antMatchers("/pendencias/usuario/**").hasAnyAuthority("ADMIN", "GERENTE", "ALUNO")
				.antMatchers("/pendencias/adm/**").hasAnyAuthority("ADMIN", "GERENTE")
				.antMatchers("/users").denyAll()
			.and()
				.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
			.and()
				.apply(new JwtConfigurer(tokenProvider));
	}
}
