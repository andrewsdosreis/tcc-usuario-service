package br.com.lutadeclasses.usuarioservice.exception.handler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.lutadeclasses.usuarioservice.exception.LoginException;
import br.com.lutadeclasses.usuarioservice.exception.UsuarioNaoEncontradoException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String MGS_ERRO = "Erro => {}";

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ErrorHandler> objectNotFound(UsuarioNaoEncontradoException e, HttpServletRequest request) {
        var erro = ErrorHandler.builder()
                               .timestamp(System.currentTimeMillis())
                               .status(HttpStatus.NOT_FOUND.value())
                               .error("NotFound Exception")
                               .message(e.getMessage())
                               .path(request.getRequestURI())
                               .method(request.getMethod())
                               .build();
        logger.error(MGS_ERRO, erro);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorHandler> jsonProcessingException(JsonProcessingException e, HttpServletRequest request) {
        var erro = ErrorHandler.builder()
                               .timestamp(System.currentTimeMillis())
                               .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                               .error("JsonProcessing Exception")
                               .message(e.getMessage())
                               .path(request.getRequestURI())
                               .method(request.getMethod())
                               .build();
        logger.error(MGS_ERRO, erro);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

    @ExceptionHandler(JsonPatchException.class)
    public ResponseEntity<ErrorHandler> jsonPatchException(JsonPatchException e, HttpServletRequest request) {
        var erro = ErrorHandler.builder()
                               .timestamp(System.currentTimeMillis())
                               .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                               .error("JsonPatchException Exception")
                               .message(e.getMessage())
                               .path(request.getRequestURI())
                               .method(request.getMethod())
                               .build();
        logger.error(MGS_ERRO, erro);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorHandler> handleValidationExceptions(MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult()
         .getAllErrors()
         .forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        var erro = ErrorHandler.builder()
                               .timestamp(System.currentTimeMillis())
                               .status(HttpStatus.BAD_REQUEST.value())
                               .error("Bad Request")
                               .message(errors.toString())
                               .path(request.getRequestURI())
                               .method(request.getMethod())
                               .build();        
        logger.error(MGS_ERRO, erro);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorHandler> dataIntegrityException(DataIntegrityViolationException e, HttpServletRequest request) {
        var erro = ErrorHandler.builder()
                               .timestamp(System.currentTimeMillis())
                               .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                               .error("DataIntegrityViolation")
                               .message(e.getMessage())
                               .path(request.getRequestURI())
                               .method(request.getMethod())
                               .build();
        logger.error(MGS_ERRO, erro);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
	}

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ErrorHandler> loginException(LoginException e, HttpServletRequest request) {
        var erro = ErrorHandler.builder()
                               .timestamp(System.currentTimeMillis())
                               .status(HttpStatus.UNAUTHORIZED.value())
                               .error("Unauthorized")
                               .message(e.getMessage())
                               .path(request.getRequestURI())
                               .method(request.getMethod())
                               .build();
        logger.error(MGS_ERRO, erro);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorHandler> exception(Exception e, HttpServletRequest request) {
        var erro = ErrorHandler.builder()
                               .timestamp(System.currentTimeMillis())
                               .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                               .error("Internal Server Error")
                               .message(e.getMessage())
                               .path(request.getRequestURI())
                               .method(request.getMethod())
                               .build();
        logger.error(MGS_ERRO, erro);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

}
