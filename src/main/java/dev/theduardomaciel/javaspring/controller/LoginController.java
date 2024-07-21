package dev.theduardomaciel.javaspring.controller;

import dev.theduardomaciel.javaspring.dto.Login;
import dev.theduardomaciel.javaspring.dto.Session;
import dev.theduardomaciel.javaspring.model.User;
import dev.theduardomaciel.javaspring.repository.UserRepository;
import dev.theduardomaciel.javaspring.security.jwt.JWTConfig;

import dev.theduardomaciel.javaspring.security.jwt.JWTCreator;
import dev.theduardomaciel.javaspring.security.jwt.JWTObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class LoginController {
	private final PasswordEncoder encoder;
	private final UserRepository repository;
	
	public LoginController(PasswordEncoder encoder, UserRepository repository) {
		this.encoder = encoder;
		this.repository = repository;
	}
	
	@PostMapping("/login")
	public Session logar(@RequestBody Login login) {
		User user = repository.findByUsername(login.getUsername());
		if (user != null) {
			boolean passwordOk = encoder.matches(login.getPassword(), user.getPassword());
			if (!passwordOk) {
				throw new RuntimeException("Senha inválida para o login: " + login.getUsername());
			}
			
			// Enviamos um objeto "Sessão" para retornar as informações do usuário
			Session session = new Session();
			session.setLogin(user.getUsername());
			
			JWTObject jwtObject = new JWTObject();
			jwtObject.setIssuedAt(new Date(System.currentTimeMillis()));
			jwtObject.setExpiration((new Date(System.currentTimeMillis() + JWTConfig.EXPIRATION)));
			jwtObject.setRoles(user.getRoles().toString());
			session.setToken(JWTCreator.create(JWTConfig.PREFIX, JWTConfig.KEY, jwtObject));
			
			return session;
		} else {
			throw new RuntimeException("Erro ao tentar fazer login");
		}
	}
}
