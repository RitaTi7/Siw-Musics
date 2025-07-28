package it.uniroma3.siw.authentication;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static it.uniroma3.siw.model.Credentials.ADMIN_ROLE;
import static it.uniroma3.siw.model.Credentials.USER_ROLE;

@Configuration 
@EnableWebSecurity
public class AuthConfiguration {
	
	@Autowired
	private DataSource dataSource;
	
	/*specifica come il sistema debba recuperare username, password e ruoli nel db*/
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		auth.jdbcAuthentication().dataSource(dataSource)
		.authoritiesByUsernameQuery("SELECT username, role FROM credentials WHERE username=?")
		.usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
	}
	
	/*definisce come salvare in maniera criptata la password nel db*/
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	/*definisce il filtro HTTP che gestisce le policies di autenticazione e autorizzazione
	 * restituiscono lo stesso oggetto http però aggiornato all'invocazione del metodo precedente*/
	@Bean
	protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			.csrf().and().cors().disable().authorizeHttpRequests()
			.requestMatchers(HttpMethod.GET, "/", "/login","/register","/common/**", "/brano/**", "/autore/**" , "/css/**", "/images/**", "/uploads/**").permitAll()
			.requestMatchers(HttpMethod.POST, "/register", "/login", "/common/**", "/uploads/**").permitAll()
			.requestMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(ADMIN_ROLE)
			.requestMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(ADMIN_ROLE)
			.requestMatchers(HttpMethod.GET, "/user/**").hasAnyAuthority(USER_ROLE)
			.requestMatchers(HttpMethod.POST, "/user/**").hasAnyAuthority(USER_ROLE)
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.loginPage("/login").permitAll()
			.defaultSuccessUrl("/success", true)
			.failureUrl("/login?error=true")
			.and()
			.logout()
			.logoutUrl("/logout")
			.logoutSuccessUrl("/")
			.invalidateHttpSession(true)
			.deleteCookies("JSESSIONID")
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.clearAuthentication(true).permitAll();
		
			return httpSecurity.build();
	}
}
