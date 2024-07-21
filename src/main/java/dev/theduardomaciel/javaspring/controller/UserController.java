package dev.theduardomaciel.javaspring.controller;

import dev.theduardomaciel.javaspring.model.User;
import dev.theduardomaciel.javaspring.repository.UserRepository;
import dev.theduardomaciel.javaspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// O Controller é como um "proxy", realizando um direcionamento entre o cliente e o repositório,
// que possui a funcionalidade existente para manipular os dados

// Regras de negócio não devem ser implementadas aqui, mas sim em camadas de serviço (service) ou de domínio (domain).

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserRepository userRepository;
	private final UserService userService;
	
	public UserController(UserRepository userRepository, UserService userService) {
		this.userRepository = userRepository;
		this.userService = userService;
	}
	
	@GetMapping()
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<User> getUsers() {
		return userRepository.findAll();
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public Optional<User> getUser(@PathVariable("id") int id) {
		return userRepository.findById(id);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteUser(@PathVariable("id") int id) {
		userRepository.deleteById(id);
	}
	
	@PostMapping
	public void postUser(@RequestBody User user) {
		userService.createUser(user);
	}
}
