package dev.theduardomaciel.javaspring.handler;

// A classe possui dois construtores, um para definição de uma mensagem simples
// e o outro para uma mensagem mais customizada utilizando o recurso de varargs.
public class BusinessException extends RuntimeException {
	// Aqui, a mensagem é passada diretamente.
	public BusinessException(String message) {
		super(message);
	}
	
	// Aqui, a mensagem é formatada para incluir os parâmetros passados.
	public BusinessException(String message, Object ... params) {
		super(String.format(message, params));
	}
}
