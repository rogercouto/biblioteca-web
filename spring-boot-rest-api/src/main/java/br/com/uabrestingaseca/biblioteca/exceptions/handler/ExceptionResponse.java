package br.com.uabrestingaseca.biblioteca.exceptions.handler;

import br.com.uabrestingaseca.biblioteca.exceptions.ValidationException;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ExceptionResponse implements Serializable{

	private static final long serialVersionUID = 1L;

	public String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public List<String> errors;

	public ExceptionResponse(String message, List<String> errors) {
		this.message = message;
		this.errors = errors;
	}
	public ExceptionResponse(String message, String error) {
		this.message = message;
		setError(error);
	}
	public ExceptionResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public void setError(String error) {
		this.errors = new LinkedList<>();
		this.errors.add(error);
	}
}
