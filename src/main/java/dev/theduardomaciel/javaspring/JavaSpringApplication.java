package dev.theduardomaciel.javaspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// ATENÇÃO! Não esquecer de remover o DataSourceAutoConfiguration quando estiver usando o H2 Database

@SpringBootApplication(/*exclude = {DataSourceAutoConfiguration.class }*/)
public class JavaSpringApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(JavaSpringApplication.class, args);
	}
	
}
