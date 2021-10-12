package com.bornfire.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice

public class CustomExceptionHandler1 extends ResponseEntityExceptionHandler {


	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub

		System.out.println("Required Fields");
		List<String> details = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());
		ErrorResponse error = new ErrorResponse("BIPSR1", details);
		return new ResponseEntity<>(error, status);
	}
	
	@Override
	protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		System.out.println("Request binding");

		List<String> details = Arrays.asList(ex.getMessage());
		ErrorResponse error = new ErrorResponse("BIPSR2", details);
		return new ResponseEntity<>(error, status);
		
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolationException(Exception ex, WebRequest request) {
		System.out.println("handleConstraintViolationException");
		List<String> details = new ArrayList<String>();
		details.add(ex.getMessage());

		ErrorResponse error = new ErrorResponse("BIPSR3", details);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		System.out.println("Method Not Support");
		System.out.println(ex.getMessage());
		List<String> details = Arrays.asList(ex.getMessage());
		ErrorResponse error = new ErrorResponse("BIPSR4", details);
		return new ResponseEntity<>(error, status);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		System.out.println("Media Not Support");

		List<String> details = Arrays.asList(ex.getMessage());
		ErrorResponse error = new ErrorResponse("BIPSR5", details);
		return new ResponseEntity<>(error, status);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		System.out.println("handleHttpMessageNotReadable");
		List<String> details = new ArrayList<String>();
		details.add(ex.getMessage());

		ErrorResponse error = new ErrorResponse("BIPSR6", details);
		return new ResponseEntity<>(error, status);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		System.out.println("handleHttpMediaTypeNotSupported");
		List<String> details = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

		details.add(builder.toString());

		ErrorResponse error = new ErrorResponse("BIPSR7", details);
		return new ResponseEntity<>(error, status);
	}
	
	
	
	
//	

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		System.out.println("handleMissingServletRequestParameter");
		List<String> details = new ArrayList<String>();
		details.add(ex.getParameterName() + " parameter is missing");

		ErrorResponse error = new ErrorResponse("BIPSR8", details);
		return new ResponseEntity<>(error,status);
	}

	

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		System.out.println("handleNoHandlerFoundException");
		List<String> details = new ArrayList<String>();
		details.add(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));

		ErrorResponse error = new ErrorResponse("BIPSR9", details);
		return new ResponseEntity<>(error, status);

	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		System.out.println("handleMethodArgumentTypeMismatch");
		List<String> details = new ArrayList<String>();
		details.add(ex.getMessage());

		ErrorResponse error = new ErrorResponse("BIPSR10", details);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	
	
	@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException ex) {
		System.out.println("handleResourceNotFoundException");
        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());
        
        ErrorResponse error = new ErrorResponse("BIPSR11", details);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        
    }
	
	@Override
	protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		System.out.println("Missing Path");

		List<String> details = Arrays.asList(ex.getMessage());
		ErrorResponse error = new ErrorResponse("BIPSR11", details);
		return new ResponseEntity<>(error, status);
	}

	

	@Override
	protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		System.out.println("handle");

		List<String> details = Arrays.asList(ex.getMessage());
		ErrorResponse error = new ErrorResponse("BIPSR12", details);
		return new ResponseEntity<>(error, status);	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		System.out.println("typeMisMatch");
		List<String> details = Arrays.asList(ex.getMessage());
		ErrorResponse error = new ErrorResponse("BIPSR13", details);
		return new ResponseEntity<>(error, status);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		System.out.println("handleHttpMessageNotWritable");

		List<String> details = Arrays.asList(ex.getMessage());
		ErrorResponse error = new ErrorResponse("BIPSR14", details);
		return new ResponseEntity<>(error, status);	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		System.out.println("handleMissingServletRequestPart");
		List<String> details = Arrays.asList(ex.getMessage());
		ErrorResponse error = new ErrorResponse("BIPSR15", details);
		return new ResponseEntity<>(error, status);
	}

	@Override
	
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		System.out.println("handleBindException");

		List<String> details = Arrays.asList(ex.getMessage());
		ErrorResponse error = new ErrorResponse("BIPSR16", details);
		return new ResponseEntity<>(error, status);
	}

	@Override
	protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex,
			HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
		// TODO Auto-generated method stub
		System.out.println("handleAsyncRequestTimeoutException");
		List<String> details = Arrays.asList(ex.getMessage());
		ErrorResponse error = new ErrorResponse("BIPSR17", details);
		return new ResponseEntity<>(error, status);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}


	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}


}