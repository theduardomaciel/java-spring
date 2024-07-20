package dev.theduardomaciel.javaspring.handler;

import dev.theduardomaciel.javaspring.handler.BusinessException;
import dev.theduardomaciel.javaspring.handler.ResponseError;
import jakarta.annotation.Resource;
import org.springframework.cglib.proxy.UndeclaredThrowableException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
	private HttpHeaders headers(){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
	
	// O método responseError() é responsável por retornar o corpo do erro com
	// as informações necessárias que serão retornadas ao cliente.
	private ResponseError responseError(String message,HttpStatus statusCode){
		ResponseError responseError = new ResponseError();
		responseError.setStatus("error");
		responseError.setError(message);
		responseError.setStatusCode(statusCode.value());
		return responseError;
	}
	
	// O método handleGeneral() intercepta as exceções do sistema e verifica
	// se se trata de uma exceção genérica ou de negócio
	@ExceptionHandler(Exception.class)
	private ResponseEntity<Object> handleGeneral(Exception e, WebRequest request) {
		if (e.getClass().isAssignableFrom(UndeclaredThrowableException.class)) {
			System.out.println("UndeclaredThrowableException");
			// Caso a exceção seja do tipo UndeclaredThrowableException, é feito um
			// cast para BusinessException e tratado como tal.
			UndeclaredThrowableException exception = (UndeclaredThrowableException) e;
			return handleBusinessException((BusinessException) exception.getUndeclaredThrowable(), request);
		} else {
			System.out.println("Exception");
			// Caso não seja uma BusinessException, é lançada uma exceção genérica.
			String message = messageSource.getMessage("error.server", new Object[]{e.getMessage()}, null);
			ResponseError error = responseError(message,HttpStatus.INTERNAL_SERVER_ERROR);
			return handleExceptionInternal(e, error, headers(), HttpStatus.INTERNAL_SERVER_ERROR, request);
		}
	}
	
	// O método handleBusinessException() é responsável por criar um ResponseEntity
	// contendo o nosso ResponseError devidamente estruturado.
	
	// Obs.: Toda exceção de negócio vai retornar uma resposta HTTP com StatusCode
	// 409 - CONFLICT, pois todas as exceções de negócio são conflitos de fluxo.
	
	@ExceptionHandler({BusinessException.class})
	private ResponseEntity<Object> handleBusinessException(BusinessException e, WebRequest request) {
		ResponseError error = responseError(e.getMessage(),HttpStatus.CONFLICT);
		return handleExceptionInternal(e, error, headers(), HttpStatus.CONFLICT, request);
	}
}
