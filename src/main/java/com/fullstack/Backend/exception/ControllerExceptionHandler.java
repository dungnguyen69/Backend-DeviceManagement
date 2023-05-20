package com.fullstack.Backend.exception;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import org.springframework.web.bind.MethodArgumentNotValidException;

@Order(1)
@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
//	@ExceptionHandler(DataIntegrityViolationException.class)
//	@ResponseStatus(value = INTERNAL_SERVER_ERROR)
//	@ResponseBody
//	public ErrorMessage resourceNotFoundException(DataIntegrityViolationException ex, WebRequest request) {
//		final List<String> errors = new ArrayList<String>();
//		errors.add(ex.getMostSpecificCause().getLocalizedMessage());
//		ErrorMessage message = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(),
//				errors, request.getDescription(false));
//		return message;
//	}

	// If mandatory fields like name, inventory number, serial number are empty
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		final List<String> errors = new ArrayList<String>();
		for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(), errors,
				request.getDescription(false));
		return new ResponseEntity<Object>(message, HttpStatus.BAD_REQUEST);
	}
	
	protected ResponseEntity<Object> handleMethodArgument(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		final List<String> errors = new ArrayList<String>();
		for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(), errors,
				request.getDescription(false));
		return new ResponseEntity<Object>(message, HttpStatus.BAD_REQUEST);
	}
}
