package dev.theduardomaciel.javaspring.security;

import dev.theduardomaciel.javaspring.security.jwt.JWTFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	private final JWTFilter jwtFilter;
	
	private final SecurityDatabaseService securityDatabaseService;
	
	public SecurityConfig(JWTFilter jwtFilter, SecurityDatabaseService securityDatabaseService) {
		this.jwtFilter = jwtFilter;
		this.securityDatabaseService = securityDatabaseService;
	}
	
	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.userDetailsService(securityDatabaseService)
				.passwordEncoder(NoOpPasswordEncoder.getInstance());
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.headers(headers -> headers
						.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
				)
				.cors(cors -> {
				})
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(authorizeRequests ->
						authorizeRequests
								.requestMatchers(HttpMethod.POST, "/users").permitAll()
								.requestMatchers(HttpMethod.POST, "/login").permitAll()
								.requestMatchers("/users-only").hasRole("USER")
								.requestMatchers("/admins-only").hasRole("ADMIN")
								.requestMatchers(new AntPathRequestMatcher("/v3/**")).permitAll()
								.requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
								.requestMatchers(
										"/",
										"/docs",
										"/public/**",
										"/h2-console/**").permitAll()
								// caso queira especificar um método: .requestMatchers(HttpMethod.GET, "/public/**").permitAll()
								.anyRequest().authenticated()
				)
				// O sessionManagement é recomendado, pois o Spring Security cria uma sessão por padrão
				.sessionManagement(sessionManagement -> sessionManagement
						.sessionFixation().migrateSession()
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.logout(withDefaults());
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}