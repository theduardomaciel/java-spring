package dev.theduardomaciel.javaspring.handler;

import org.springframework.http.HttpStatus;

// A classe possui dois construtores, um para definição de uma mensagem simples
// e o outro para uma mensagem mais customizada utilizando o recurso de varargs.
public class BusinessException extends RuntimeException {
	private final HttpStatus statusCode;
	
	// Aqui, a mensagem e o status code são passados diretamente.
	public BusinessException(String message, HttpStatus statusCode) {
		super(message);
		this.statusCode = statusCode;
	}
	
	// Aqui, a mensagem é formatada para incluir os parâmetros passados e o status code é definido.
	public BusinessException(String message, HttpStatus statusCode, Object... params) {
		super(String.format(message, params));
		this.statusCode = statusCode;
	}
	
	public HttpStatus getStatusCode() {
		return statusCode;
	}
}
