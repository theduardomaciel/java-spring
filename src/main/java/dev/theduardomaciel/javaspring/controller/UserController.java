package dev.theduardomaciel.javaspring.controller;

import dev.theduardomaciel.javaspring.model.User;
import dev.theduardomaciel.javaspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// O Controller é como um "proxy", realizando um direcionamento entre o cliente e o repositório,
// que possui a funcionalidade existente para manipular os dados

// Regras de negócio não devem ser implementadas aqui, mas sim em camadas de serviço (service) ou de domínio (domain).

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping()
	public List<User> getUsers() {
		return userRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public User getUser(@PathVariable("id") String id) {
		return userRepository.findById(id);
	}
	
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable("id") String id) {
		userRepository.deleteById(id);
	}
	
	@PutMapping()
	public void updateUser(@RequestBody User user) {
		userRepository.saveUser(user);
	}
}
