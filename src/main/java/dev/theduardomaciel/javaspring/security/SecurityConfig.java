package dev.theduardomaciel.javaspring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	private final SecurityDatabaseService securityDatabaseService;
	
	public SecurityConfig(SecurityDatabaseService securityDatabaseService) {
		this.securityDatabaseService = securityDatabaseService;
	}
	
	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(securityDatabaseService).passwordEncoder(NoOpPasswordEncoder.getInstance());
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(authorizeRequests ->
						authorizeRequests
								.requestMatchers("/", "/public/**").permitAll()
								// caso queira especificar um método: .requestMatchers(HttpMethod.GET, "/public/**").permitAll()
								.anyRequest().authenticated()
				)
				.httpBasic(withDefaults())
				.logout(withDefaults());
		/*
		* Para mudar a tela de login:
		* padrão: .formLogin(withDefaults())
		*   .formLogin(formLogin -> formLogin
                .loginPage("/login")  // specify custom login page if needed
                .permitAll()
            )
        * Para mudar a URL de logout:
        * padrão: .logout(withDefaults())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .permitAll()
            );
		* */
		
		return http.build();
	}
	
	/*
	@Bean
	public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("user")
				.password(passwordEncoder.encode("password"))
				.roles("USER")
				.build());
		manager.createUser(User.withUsername("admin")
				.password(passwordEncoder.encode("admin"))
				.roles("USER", "ADMIN")
				.build());
		return manager;
	}
	*/
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}