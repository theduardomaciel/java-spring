package dev.theduardomaciel.javaspring.init;

import dev.theduardomaciel.javaspring.model.User;
import dev.theduardomaciel.javaspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// Para não termos que inserir manualmente os usuários toda vez que
// a aplicação for iniciada, podemos criar um CommandLineRunner

// Numa aplicação real, isso seria substituído por um formulário
// de cadastro de usuários

@Component
public class StartApplication implements CommandLineRunner {
	public final UserRepository repository;
	
	public StartApplication(UserRepository repository) {
		this.repository = repository;
	}
	
	@Transactional
	@Override
	public void run(String... args) throws Exception {
		User user = repository.findByUsername("admin");
		
		if (user == null) {
			user = new User();
			user.setName("Administrador");
			user.setUsername("admin");
			user.setPassword("admin");
			user.getRoles().add("ADMIN");
			repository.save(user);
		}
		
		user = repository.findByUsername("user");
		
		if (user == null) {
			user = new User();
			user.setName("Usuário padrão");
			user.setUsername("user");
			user.setPassword("123");
			user.getRoles().add("USER");
			repository.save(user);
		}
		
		System.out.println("Usuários cadastrados:");
		repository.findAll().forEach(iUser -> System.out.println(iUser.toString()));
	}
}