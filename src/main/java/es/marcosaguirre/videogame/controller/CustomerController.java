package es.marcosaguirre.videogame.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.marcosaguirre.videogame.common.BaseException;
import es.marcosaguirre.videogame.common.Constants;
import es.marcosaguirre.videogame.common.Message;
import es.marcosaguirre.videogame.common.Response;
import es.marcosaguirre.videogame.dto.CustomerDto;
import es.marcosaguirre.videogame.dto.UpdatableCustomerDto;
import es.marcosaguirre.videogame.service.ICustomerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/customers")
public class CustomerController {
	
	@Autowired
	private ICustomerService customerService;
	
	@PostMapping
	public ResponseEntity<Response<CustomerDto>> addCustomer(@Valid @RequestBody CustomerDto customerDto){
		
		Response<CustomerDto> response = new Response<>();
		HttpStatus status = null;
		
		try {
			response.setData(customerService.register(customerDto));
			response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_ENTRY_CREATED, Constants.TYPE_INFO, "Customer"));
			status = HttpStatus.CREATED;
		} catch (BaseException e) {
			response.addMessage(new Message(e.getCode(), e.getMessage(), Constants.TYPE_ERROR, e.getOrigin()));
			status = HttpStatus.OK;
		}
		
		return new ResponseEntity<Response<CustomerDto>>(response, status);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Response<CustomerDto>> editCustomer(@Valid @RequestBody UpdatableCustomerDto customerDto, @PathVariable Long id) throws BaseException{
		
		Response<CustomerDto> response = new Response<>();
		HttpStatus status = null;
		CustomerDto updatedCustomerDto = null;
		
		try {
			updatedCustomerDto = customerService.getById(id);
		} catch (BaseException e) {
			log.warn("Customer with id " + id + " doesn't exists");
		}
		
		if(updatedCustomerDto == null) {
			response.addMessage(new Message(Constants.ERROR_CODE_ELEMENT_NOT_FOUND, Constants.MSJ_ENTRY_DOES_NOT_EXISTS, Constants.TYPE_ERROR, "Customer id: " + Long.toString(id)));
			return new ResponseEntity<Response<CustomerDto>>(response, HttpStatus.NOT_FOUND);
		}
		
		if(customerDto.getName() != null) {
			updatedCustomerDto.setName(customerDto.getName());
		}
		
		if(customerDto.getLastName() != null) {
			updatedCustomerDto.setLastName(customerDto.getLastName());
		}
		
		if(customerDto.getMail() != null) {
			updatedCustomerDto.setMail(customerDto.getMail());
		}
		
		if(customerDto.getLoyaltyPoints() != null) {
			updatedCustomerDto.setLoyaltyPoints(customerDto.getLoyaltyPoints());
		}
		
		if(customerDto.getRentedGames() != null) {
			updatedCustomerDto.setRentedGames(customerDto.getRentedGames());
		}
		
		updatedCustomerDto = customerService.update(updatedCustomerDto, id);
		response.setData(updatedCustomerDto);
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_ENTRY_UPDATED, Constants.TYPE_INFO, "Customer with id; " + id));
		status = HttpStatus.CREATED;
		
		return new ResponseEntity<Response<CustomerDto>>(response, status);
	}
	
	@GetMapping
	public  ResponseEntity<Response<List<CustomerDto>>> getCustomers() throws BaseException {
		
		List<CustomerDto> customers = new ArrayList<>();
		customers.addAll(customerService.getAll());
		Response<List<CustomerDto>> response = new Response<>();
		
		if(customers.isEmpty()) {
			response.setData(Collections.emptyList());
			response.addMessage(new Message(Constants.ERROR_CODE_ELEMENT_NOT_FOUND,Constants.MSJ_EMPTY_LIST, Constants.TYPE_ERROR, "Customers List"));
			return new ResponseEntity<Response<List<CustomerDto>>>(response, HttpStatus.OK);
		}
		
		response.setData(customers);
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_LIST, Constants.TYPE_INFO, "Customers List"));
		return new ResponseEntity<Response<List<CustomerDto>>>(response, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Response<CustomerDto>> getCustomerById(@PathVariable Long id){
		
		CustomerDto customerDto = null;
		
		try {
			customerDto = customerService.getById(id);
		} catch (BaseException e) {
			log.warn("Customer with id " + id + " doesn't exists");
		}
		
		Response<CustomerDto> response = new Response<>();
		
		if(customerDto == null) {
			response.addMessage(new Message(Constants.ERROR_CODE_ELEMENT_NOT_FOUND, Constants.MSJ_ENTRY_DOES_NOT_EXISTS, Constants.TYPE_ERROR, "Customer with id: " + id));
			return new ResponseEntity<Response<CustomerDto>>(response, HttpStatus.NOT_FOUND);
		}
		
		response.setData(customerDto);
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_ENTRY_FOUNDED, Constants.TYPE_INFO, "Customer with id: " + id));
		
		return new ResponseEntity<Response<CustomerDto>>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Response<Void>> deleteCustomerById(@PathVariable Long id){
		
		Response<Void> response = new Response<>();
		HttpStatus status = null;
		
		try {
			customerService.delete(id);
			response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_ENTRY_DELETED, Constants.TYPE_INFO, "Customer with id: " + id));
			status = HttpStatus.CREATED;
		} catch (BaseException e) {
			response.addMessage(new Message(e.getCode(), e.getMessage(), "Error", e.getOrigin()));
			if( e.getCode() == Constants.ERROR_CODE_ELEMENT_NOT_FOUND) {
				status = HttpStatus.NOT_FOUND;
			} else {
				status = HttpStatus.OK;
			}
		}
		
		return new ResponseEntity<Response<Void>>(response, status);
	}
	
}
