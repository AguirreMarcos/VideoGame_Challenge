package es.marcosaguirre.videogame.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import es.marcosaguirre.videogame.common.Response;
import es.marcosaguirre.videogame.common.ValidationUtil;


@ControllerAdvice
public class ExceptionHandlingController {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Response<ExceptionResponse>> invalidInput(MethodArgumentNotValidException ex) {
		
		BindingResult result = ex.getBindingResult();
		Response<ExceptionResponse> res = new Response<>();
		ExceptionResponse response = new ExceptionResponse();
		response.setErrorCode("Validation Error");
		response.setErrorMessage("Invalid inputs.");
		response.setErrors(ValidationUtil.fromBindingErrors(result));
		res.setData(response);
		return new ResponseEntity<Response<ExceptionResponse>>(res, HttpStatus.BAD_REQUEST);
		
	}
	
}
