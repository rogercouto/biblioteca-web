package br.com.uabrestingaseca.biblioteca.exceptions.handler;

import java.util.List;
import java.util.stream.Collectors;

import br.com.uabrestingaseca.biblioteca.exceptions.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.uabrestingaseca.biblioteca.exceptions.InvalidJwtAuthenticationException;
import br.com.uabrestingaseca.biblioteca.exceptions.ResourceNotFoundException;

@ControllerAdvice
@RestController
public class CustomizeResponseEntityHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionResponse> handleAllException(Exception exception,WebRequest request){
		ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> handleResourceNotExistsException(Exception exception,WebRequest request){
		ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidJwtAuthenticationException.class)
	public final ResponseEntity<ExceptionResponse> handleInvalidJwtAuthenticationException(Exception exception,WebRequest request){
		ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ValidationException.class)
	protected final ResponseEntity<ExceptionResponse> handleValidationException(ValidationException exception, WebRequest request){
		ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), exception.getErrors());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errors = exception.getBindingResult()
				.getAllErrors()
				.stream()
				.map(error -> error.getDefaultMessage())
				.collect(Collectors.toList());
		ExceptionResponse exceptionResponse = new ExceptionResponse("Erro de validação nos dados", errors);
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}


}
