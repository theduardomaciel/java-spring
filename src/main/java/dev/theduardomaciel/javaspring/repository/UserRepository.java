package dev.theduardomaciel.javaspring.repository;

import dev.theduardomaciel.javaspring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// Aqui estão implementadas representações dos métodos que seriam utilizados para manipular os dados de usuários em um banco de dados
// Em um cenário real, utilizaríamos um JPA Repository para realizar as operações de CRUD

public interface UserRepository extends JpaRepository<User, Integer> {
	@Query("SELECT e FROM User e JOIN FETCH e.roles WHERE e.username= (:username)")
	public User findByUsername(@Param("username") String username);
	
	boolean existsByUsername(String username);
}