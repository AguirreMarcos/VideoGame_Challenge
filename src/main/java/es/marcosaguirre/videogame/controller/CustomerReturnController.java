package es.marcosaguirre.videogame.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.marcosaguirre.videogame.common.BaseException;
import es.marcosaguirre.videogame.common.Constants;
import es.marcosaguirre.videogame.common.Message;
import es.marcosaguirre.videogame.common.Response;
import es.marcosaguirre.videogame.dto.ConfirmedReturnResponseDto;
import es.marcosaguirre.videogame.dto.ReturnInfoResponseDto;
import es.marcosaguirre.videogame.dto.ReturnInputDto;
import es.marcosaguirre.videogame.service.ICustomerReturnService;

@RestController
@RequestMapping("/returns")
public class CustomerReturnController {
	
	@Autowired
	private ICustomerReturnService returnService;
	
	@PostMapping("/partial/{id}")
	public ResponseEntity<Response<ReturnInfoResponseDto>> showReturnInfoResponse(@Valid @RequestBody ReturnInputDto inputDto, @PathVariable Long id){
		Response<ReturnInfoResponseDto> response = new Response<>();
		HttpStatus status = null;
		try {
			response.setData(returnService.showPartialReturnInfo(inputDto, id));
			response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_RETURN_INFO));
			status = HttpStatus.OK;
		} catch (BaseException e) {
			response.addMessage(new Message(e.getCode(), e.getMessage(), "Error", e.getOrigin()));
			status = HttpStatus.OK;
		}
		
		return new ResponseEntity<Response<ReturnInfoResponseDto>> (response, status);
	}
	
	@PostMapping("/total/{id}")
	public ResponseEntity<Response<ReturnInfoResponseDto>> showTotalInfoResponse(@PathVariable Long id){
		Response<ReturnInfoResponseDto> response = new Response<>();
		HttpStatus status = null;
		try {
			response.setData(returnService.showTotalReturnInfo(id));
			response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_RETURN_INFO));
			status = HttpStatus.OK;
		} catch (BaseException e) {
			response.addMessage(new Message(e.getCode(), e.getMessage(), "Error", e.getOrigin()));
			status = HttpStatus.OK;
		}
		
		return new ResponseEntity<Response<ReturnInfoResponseDto>> (response, status);
	}
	
	@PostMapping("/partial/{id}/confirm")
	public ResponseEntity<Response<ConfirmedReturnResponseDto>> confirmPartialReturnResponse(@Valid @RequestBody ReturnInputDto inputDto, @PathVariable Long id){
		Response<ConfirmedReturnResponseDto> response = new Response<>();
		HttpStatus status = null;
		try {
			response.setData(returnService.confirmPartialReturn(inputDto, id));
			response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_RETURN_INFO));
			status = HttpStatus.OK;
		} catch (BaseException e) {
			response.addMessage(new Message(e.getCode(), e.getMessage(), "Error", e.getOrigin()));
			status = HttpStatus.OK;
		}
		
		return new ResponseEntity<Response<ConfirmedReturnResponseDto>> (response, status);
	}
	
	@PostMapping("/total/{id}/confirm")
	public ResponseEntity<Response<ConfirmedReturnResponseDto>> confirmTotalReturnResponse(@PathVariable Long id){
		Response<ConfirmedReturnResponseDto> response = new Response<>();
		HttpStatus status = null;
		try {
			response.setData(returnService.confirmTotalReturn(id));
			response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_RETURN_INFO));
			status = HttpStatus.OK;
		} catch (BaseException e) {
			response.addMessage(new Message(e.getCode(), e.getMessage(), "Error", e.getOrigin()));
			status = HttpStatus.OK;
		}
		
		return new ResponseEntity<Response<ConfirmedReturnResponseDto>> (response, status);
	}
}
