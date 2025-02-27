package dev.theduardomaciel.javaspring.handler;

import jakarta.annotation.Resource;
import org.springframework.cglib.proxy.UndeclaredThrowableException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// A anotação @RestControllerAdvice é utilizada para definir que a classe é um
// controlador de exceções, ou seja, ela irá tratar exceções lançadas pelos
// controladores da aplicação.
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@Resource
	private MessageSource messageSource;
	
	// O método headers() é responsável por criar um objeto HttpHeaders com o
	// Content-Type definido como application/json.
	// Obs.: Outras configurações podem ser adicionadas conforme a necessidade.
	private HttpHeaders headers() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
	
	// O método responseError() é responsável por retornar o corpo do erro com
	// as informações necessárias que serão retornadas ao cliente.
	private ResponseError responseError(String message, HttpStatus statusCode) {
		return new ResponseError(message, message, statusCode.value());
	}
	
	// O método handleGeneral() intercepta as exceções do sistema e verifica
	// se se trata de uma exceção genérica ou de negócio
	@ExceptionHandler(Exception.class)
	private ResponseEntity<Object> handleGeneral(Exception e, WebRequest request) {
		if (e.getClass().isAssignableFrom(UndeclaredThrowableException.class)) {
			UndeclaredThrowableException exception = (UndeclaredThrowableException) e;
			return handleBusinessException((BusinessException) exception.getUndeclaredThrowable(), request);
		} else {
			ResponseError error = responseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			return handleExceptionInternal(e, error, headers(), HttpStatus.INTERNAL_SERVER_ERROR, request);
		}
	}
	
	// O método handleBusinessException() é responsável por criar um ResponseEntity
	// contendo o nosso ResponseError devidamente estruturado.
	@ExceptionHandler({BusinessException.class})
	private ResponseEntity<Object> handleBusinessException(BusinessException e, WebRequest request) {
		ResponseError error = responseError(e.getMessage(), e.getStatusCode());
		return handleExceptionInternal(e, error, headers(), e.getStatusCode(), request);
	}
	
	@ExceptionHandler({IllegalArgumentException.class})
	private ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e, WebRequest request) {
		ResponseError error = responseError(e.getMessage(), HttpStatus.BAD_REQUEST);
		return handleExceptionInternal(e, error, headers(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler({AuthorizationDeniedException.class})
	private ResponseEntity<Object> handleAuthorizationDeniedException(AuthorizationDeniedException e, WebRequest request) {
		ResponseError error = responseError(e.getMessage(), HttpStatus.FORBIDDEN);
		return handleExceptionInternal(e, error, headers(), HttpStatus.FORBIDDEN, request);
	}
}
