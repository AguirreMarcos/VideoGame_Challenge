package es.marcosaguirre.videogame.exception;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExceptionResponse {
	
	private String errorCode;
	
	private String errorMessage;
	
	private List<String> errors;
	
}
