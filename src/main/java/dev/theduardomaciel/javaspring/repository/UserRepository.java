package dev.theduardomaciel.javaspring.repository;

import dev.theduardomaciel.javaspring.handler.ObligatoryArgumentException;
import dev.theduardomaciel.javaspring.model.User;
import dev.theduardomaciel.javaspring.handler.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

// Aqui estão implementadas representações dos métodos que seriam utilizados para manipular os dados de usuários em um banco de dados
// Em um cenário real, utilizaríamos um JPA Repository para realizar as operações de CRUD

@Repository
public class UserRepository {
	public void saveUser(User user) {
		if (user.getUsername() == null)
			throw new ObligatoryArgumentException("username");
		
		if(user.getId() == null)
			System.out.println("SAVE - Recebendo o usuário na camada de repositório");
		else
			System.out.println("UPDATE - Recebendo o usuário na camada de repositório");
		
		System.out.println(user);
	}
	
	public void deleteById(String id) {
		System.out.printf("DELETE/id - Recebendo o id: %s para excluir um usuário%n", id);
		System.out.println("User deleted with id: " + id);
	}
	
	public List<User> findAll() {
		System.out.println("LIST - Listando os usuários do sistema:");
		return List.of(
				new User("user1", "password1"),
				new User("user2", "password2"),
				new User("user3", "password3")
		);
	}
	
	public User findById(String id) {
		System.out.printf("FIND/id - Recebendo o id: %s para localizar um usuário%n", id);
		return new User("user_with_id_" + id, "password");
	}
}
