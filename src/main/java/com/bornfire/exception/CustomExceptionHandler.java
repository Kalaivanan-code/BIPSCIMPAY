package com.bornfire.exception;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.ws.client.WebServiceIOException;
import static com.bornfire.exception.ErrorResponseCode.*;


import com.bornfire.controller.IPSConnection;
@ControllerAdvice
public class CustomExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

	private String CONNECT24 = "BOB011";
	private String IPSX = "BOB021";
	private String FIELD = "BOB001";
	private String BAD_REQUEST = "BOB012";

	@ExceptionHandler(Connect24Exception.class)
	public final ResponseEntity<ErrorResponse> handleConnect24Exception(Connect24Exception ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage().split(":")[1]);
		String errCode=ex.getLocalizedMessage().split(":")[0];
		ErrorResponse error = new ErrorResponse(errCode, details);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	

	@ExceptionHandler(IPSXRestException.class)
	public final ResponseEntity<ErrorRestResponse> handleIPSXRestException(IPSXRestException ex, WebRequest request) {
		String errMessage=ex.getLocalizedMessage();
		ErrorRestResponse error = new ErrorRestResponse(errMessage.split(":")[0], errMessage.split(":")[1]);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(FieldException.class)
	public final ResponseEntity<ErrorResponse> handleFieldException(FieldException ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		String errMessage=ex.getLocalizedMessage();
		details.add(errMessage);
		ErrorResponse error = new ErrorResponse(FIELD, details);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	
	
	@ExceptionHandler(IPSXException.class)
	public final ResponseEntity<ErrorResponse> handleIPSXException(IPSXException ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage().split(":")[1]);
		String errCode=ex.getLocalizedMessage().split(":")[0];
		ErrorResponse error = new ErrorResponse(errCode, details);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConnectException.class)
	public final ResponseEntity<Object> handleConstraintViolationException(ConnectException ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add("Something Went wrong at server end");
		logger.info("ConnectException");

		System.out.println("ok");
		ErrorResponse error = new ErrorResponse(BAD_REQUEST,details);
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@ExceptionHandler(ServerErrorException.class)
	public final ResponseEntity<Object> handleCustomConstraintViolationException(ServerErrorException ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage().split(":")[1]);
		String errCode=ex.getLocalizedMessage().split(":")[0];
		ErrorResponse error = new ErrorResponse(errCode,details);
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@ExceptionHandler(HttpServerErrorException.class)
	public final ResponseEntity<Object> handleHttpServerErrorException(ConnectException ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add("Something Went wrong at server end");
		logger.info("HttpServerErrorException");

		System.out.println("ok");
		ErrorResponse error = new ErrorResponse(BAD_REQUEST,details);
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(CustomWebServiceIOException.class)
	public final ResponseEntity<Object> handleWebServiceIOException(CustomWebServiceIOException ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add("Something Went wrong at server end");
		logger.info("CustomWebServiceIOException");
		System.out.println("ok1");
		ErrorResponse error = new ErrorResponse(BAD_REQUEST,details);
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(SocketTimeoutException.class)
	public final ResponseEntity<Object> handleSocketTimeoutException(SocketTimeoutException ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add("Something Went wrong at server end");
		logger.info("SocketTimeoutException");
		System.out.println("ok3");
		ErrorResponse error = new ErrorResponse(BAD_REQUEST,details);
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
