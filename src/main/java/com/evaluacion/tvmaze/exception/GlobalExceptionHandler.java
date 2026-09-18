package com.evaluacion.tvmaze.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;


@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {

		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

		return ResponseEntity.badRequest()
				.body(Map.of("status", 400, "message", "Error de validación", "errors", errors));
	}

	@ExceptionHandler(HttpClientErrorException.NotFound.class)
	public ResponseEntity<Map<String, Object>> handleNotFound(HttpClientErrorException.NotFound ex) {

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", 404, "message", "Show no encontrado"));
	}

	@ExceptionHandler(ResourceAccessException.class)
	public ResponseEntity<Map<String, Object>> handleTvMazeUnavailable(ResourceAccessException ex) {

		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(Map.of("status", 503, "message", "TVNaze no se encuentra displonible"));
	}

	@ExceptionHandler(RestClientException.class)
	public ResponseEntity<Map<String, Object>> handleTvMazeError(RestClientException ex) {

		return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
				.body(Map.of("status", 502, "message", "Error al consultar TVMaze"));
	}

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<Map<String, Object>> handleMongoError(DataAccessException ex) {

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(Map.of("status", 500, "message", "Error al acceder a Mongo"));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleUnexpectedError(Exception ex) {

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(Map.of("status", 500, "message", "Error itnento del servidor"));
	}
}
