package es.marcosaguirre.videogame.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

public class ValidationUtil {
	public static List<String> fromBindingErrors(BindingResult errors) {
		List<String> validErrors = new ArrayList<String>();
		for (ObjectError objectError: errors.getAllErrors()) {
			validErrors.add(objectError.getDefaultMessage());
		}
		return validErrors;
	}
}
