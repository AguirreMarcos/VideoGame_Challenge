package es.marcosaguirre.videogame.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.marcosaguirre.videogame.common.BaseException;
import es.marcosaguirre.videogame.common.Constants;
import es.marcosaguirre.videogame.common.Message;
import es.marcosaguirre.videogame.common.Response;
import es.marcosaguirre.videogame.dto.OperationDto;
import es.marcosaguirre.videogame.service.IOperationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/operations")
public class OperationController {
	
	@Autowired
	private IOperationService operationService;
	
	@GetMapping
	public ResponseEntity<Response<List<OperationDto>>> getOperations() throws BaseException {
		
		List<OperationDto> operations = new ArrayList<>();
		operations.addAll(operationService.getAll());
		Response<List<OperationDto>> response = new Response<>();
		if(operations.isEmpty()) {
			response.addMessage(new Message(Constants.MSJ_EMPTY_LIST));
			return new ResponseEntity<Response<List<OperationDto>>>(response, HttpStatus.OK);
		}
		response.setData(operations);
		return new ResponseEntity<Response<List<OperationDto>>>(response, HttpStatus.OK);

	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Response<OperationDto>> getOperationById(@PathVariable Long id){
		OperationDto operationDto = null;
		try {
			operationDto = operationService.getById(id);
		} catch (BaseException e) {
			log.warn("Operation with id " + id + " doesn't exists");
		}
		Response<OperationDto> response = new Response<>();
		if(operationDto == null) {
			response.addMessage(new Message(Constants.ERROR_CODE_ELEMENT_NOT_FOUND, Constants.MSJ_ENTRY_DOES_NOT_EXISTS, "Operation with id: " + Long.toString(id) ));
			return new ResponseEntity<Response<OperationDto>>(response, HttpStatus.NOT_FOUND);
		}
		response.setData(operationDto);
		return new ResponseEntity<Response<OperationDto>>(response, HttpStatus.OK);
	}
	
	
}
