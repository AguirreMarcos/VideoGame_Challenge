package es.marcosaguirre.videogame.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.marcosaguirre.videogame.common.Response;


@RestController
public class CustomErrorController implements ErrorController{
	
	private static final String PATH = "/error";
	
	@RequestMapping(PATH)
	public ResponseEntity<Response<ExceptionResponse>> error(HttpServletRequest req, HttpServletResponse res) {
		ExceptionResponse er = new ExceptionResponse();
		Response<ExceptionResponse> response = new Response<>();
		er.setErrorCode(""+res.getStatus());
		if(res.getStatus() == 404) {
			er.setErrorMessage("Trying to access not valid URL");
		}else if(res.getStatus() == 400) {
			er.setErrorMessage("Request not valid");
		}else {
			er.setErrorMessage("An error has occurred");
		}
		response.setData(er);
		ResponseEntity<Response<ExceptionResponse>> responseEntity = new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		
		return responseEntity; 
	}

	@Override
	public String getErrorPath() {
		return PATH;
	}

}
