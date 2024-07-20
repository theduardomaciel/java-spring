package dev.theduardomaciel.javaspring.handler;

import org.springframework.http.HttpStatus;

public class ObligatoryArgumentException extends BusinessException {
	public ObligatoryArgumentException(String message) {
		super("O campo %s é obrigatório", HttpStatus.BAD_REQUEST, message);
	}
}
