package dev.theduardomaciel.javaspring.security;

import dev.theduardomaciel.javaspring.model.User;
import dev.theduardomaciel.javaspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SecurityDatabaseService  implements UserDetailsService {
	private final UserRepository userRepository;
	
	public SecurityDatabaseService(@Autowired UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) {
		User userEntity = userRepository.findByUsername(username);
		
		if (userEntity == null) {
			throw new UsernameNotFoundException(username);
		}
		
		Set<GrantedAuthority> authorities = new HashSet<>();
		userEntity.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
		});
		
		// Retornamos o UserDetails com as informações do usuário
		return new org.springframework.security.core.userdetails.User(userEntity.getUsername(),
				userEntity.getPassword(),
				authorities);
	}
}