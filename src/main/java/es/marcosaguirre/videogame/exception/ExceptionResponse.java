package es.marcosaguirre.videogame.exception;

import java.util.List;

import lombok.Data;

@Data
public class ExceptionResponse {
	
	private String errorCode;
	
	private String errorMessage;
	
	private List<String> errors;
	
	public ExceptionResponse() {
		
	}
	
}
