package dev.theduardomaciel.javaspring.handler;

public class ObligatoryArgumentException extends BusinessException {
	public ObligatoryArgumentException(String message) {
		super("O argumento '%s' é obrigatório.", message);
	}
	
	public ObligatoryArgumentException(String message, Object ... params) {
		super(message, params);
	}
}
