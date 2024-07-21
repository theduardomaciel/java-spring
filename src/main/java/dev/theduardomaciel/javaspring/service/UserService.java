package dev.theduardomaciel.javaspring.service;

import dev.theduardomaciel.javaspring.model.User;
import dev.theduardomaciel.javaspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	private final UserRepository repository;
	private final PasswordEncoder encoder;
	
	public UserService(UserRepository repository, PasswordEncoder encoder){
		this.repository = repository;
		this.encoder = encoder;
	}
	
	public void createUser(User user){
		String pass = user.getPassword();
		
		// Criptografando antes de salvar no Banco de Dados
		user.setPassword(encoder.encode(pass));
		
		repository.save(user);
	}
}