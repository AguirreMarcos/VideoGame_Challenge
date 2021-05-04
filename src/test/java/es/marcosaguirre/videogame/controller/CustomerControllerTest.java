package es.marcosaguirre.videogame.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.marcosaguirre.videogame.common.Constants;
import es.marcosaguirre.videogame.common.Message;
import es.marcosaguirre.videogame.common.Response;
import es.marcosaguirre.videogame.dto.CustomerDto;
import es.marcosaguirre.videogame.service.ICustomerService;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private ICustomerService customerService;
	
	@Test
	public void getCustomersTest() throws Exception{
		
		List<CustomerDto> listCustomersDto = new ArrayList<>();
		
		Response<List<CustomerDto>> response = new Response<>();
		
		listCustomersDto.add(new CustomerDto(1L, "Marcos", "Aguirre", "maguirreminarro@gmail.com", 14, null));
		listCustomersDto.add(new CustomerDto(2L, "Sandra", "Duque", "sduqueduniol@gmail.com", 14, null));
		listCustomersDto.add(new CustomerDto(3L, "Emma", "Aguirre", "emma@gmail.com", 14, null));
		
		response.setData(listCustomersDto);
		
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_LIST, Constants.TYPE_INFO, "Customers List"));
		
		Mockito.when(customerService.getAll()).thenReturn(listCustomersDto);
		
		String url = "/customers";
		
		MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();
		
		String actualJsonResponse = mvcResult.getResponse().getContentAsString();
		
		System.out.println(actualJsonResponse);
		
		String expectedJsonResponse = objectMapper.writeValueAsString(response);
		
		System.out.println(expectedJsonResponse);
		
		assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
	}
	
	@Test
	public void shouldReturnErrorResponseOnEmptyGetList() throws Exception {
	
		Response<List<CustomerDto>> response = new Response<>();
		
		List<CustomerDto> listOfCustomers = Collections.emptyList();
		
		response.setData(listOfCustomers);
		
		response.addMessage(new Message(Constants.ERROR_CODE_ELEMENT_NOT_FOUND,Constants.MSJ_EMPTY_LIST, Constants.TYPE_ERROR, "Customers List"));
		
		Mockito.when(customerService.getAll()).thenReturn(listOfCustomers);
		
		String url = "/customers";
		
		MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();
		
		String actualJsonResponse = mvcResult.getResponse().getContentAsString();
		
		String expectedJsonResponse = objectMapper.writeValueAsString(response);
		
		assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
	}
	
	@Test
	public void addCustomerTest() throws JsonProcessingException, Exception {
		
		CustomerDto newCustomer = new CustomerDto("Marcos", "Aguirre", "maguirreminarro@gmail.com", 14, null);
		CustomerDto savedCustomer = new CustomerDto(1L, "Marcos", "Aguirre", "maguirreminarro@gmail.com", 14, null);
		Response<CustomerDto> response = new Response<>();
		response.setData(savedCustomer);
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_ENTRY_CREATED, "INFO", "Customer"));
		Mockito.when(customerService.register(newCustomer)).thenReturn(savedCustomer);
		
		String url = "/customers";
		mockMvc.perform(
				post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(newCustomer))
		).andExpect(status().isCreated())
		.andExpect(content().string(objectMapper.writeValueAsString(response)));
	}
	
	
	@Test
	public void editCustomerTest() throws JsonProcessingException, Exception {
		
		CustomerDto existingCustomer = new CustomerDto(1L, "Marcos", "Aguirre", "maguirreminarro@gmail.com", 14, null);
		CustomerDto updatedCustomer = new CustomerDto(1L, "Pepe", "Aguirre", "maguirreminarro@gmail.com", 14, null);
		
		Long existingCustomerId = 1L;
		
		Response<CustomerDto> response = new Response<>();
		response.setData(updatedCustomer);
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_ENTRY_UPDATED, Constants.TYPE_INFO, "Customer with id; " + existingCustomerId));
		
		Mockito.when(customerService.getById(existingCustomerId)).thenReturn(existingCustomer);
		
		Mockito.when(customerService.update(updatedCustomer, existingCustomerId)).thenReturn(updatedCustomer);
		
		String url_update = "/customers/" + existingCustomerId;
		mockMvc.perform(
				put(url_update)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(updatedCustomer))
		).andExpect(status().isCreated())
		.andExpect(content().string(objectMapper.writeValueAsString(response)))
		.andDo(print());
	}
	
	@Test
	public void deleteCustomerByIdTest() throws JsonProcessingException, Exception {
		
		Long customerId = 1L;
		String url = "/customers/" + customerId;
		
		Response<Void> response = new Response<>();
		response.addMessage(new Message(Constants.CODE_OK, Constants.MSJ_ENTRY_DELETED, Constants.TYPE_INFO, "Customer with id: " + customerId));
		
		Mockito.when(customerService.delete(customerId)).thenReturn(true);
		
		mockMvc.perform(delete(url))
			.andExpect(status().isCreated())
			.andExpect(content().string(objectMapper.writeValueAsString(response)))
			.andDo(print());
	}
	
	@Test
	public void customerNameMustNotBeBlank() throws JsonProcessingException, Exception {
		CustomerDto customer = new CustomerDto("", "Aguirre", "maguirreminarro@gmail.com", 14, null);
		
		String url = "/customers";
		mockMvc.perform(
				post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(customer))
				).andExpect(status().isBadRequest());
		
		Mockito.verify(customerService, times(0)).register(customer);
	}
	
	@Test
	public void customerLastNameMustNotBeBlank() throws JsonProcessingException, Exception {
		CustomerDto customer = new CustomerDto("Marcos", "", "maguirreminarro@gmail.com", 14, null);
		
		String url = "/customers";
		mockMvc.perform(
				post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(customer))
				).andExpect(status().isBadRequest());
		
		Mockito.verify(customerService, times(0)).register(customer);
	}
	
	@Test
	public void customerMailMustNotBeBlank() throws JsonProcessingException, Exception {
		CustomerDto customer = new CustomerDto("Marcos", "Aguirre", "", 14, null);
		
		String url = "/customers";
		mockMvc.perform(
				post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(customer))
				).andExpect(status().isBadRequest());
		
		Mockito.verify(customerService, times(0)).register(customer);
	}
	
	@Test
	public void customerNameMustBeUnder30Characters() throws JsonProcessingException, Exception {
		CustomerDto customer = new CustomerDto(Constants.CHARACTERS_31_STRING, "Aguirre", "maguirreminarro@gmail.com", 14, null);
		
		String url = "/customers";
		mockMvc.perform(
				post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(customer))
				).andExpect(status().isBadRequest());
		
		Mockito.verify(customerService, times(0)).register(customer);
	}
	
	@Test
	public void customerSurnameMustBeUnder60Characters() throws JsonProcessingException, Exception {
		CustomerDto customer = new CustomerDto("Marcos", Constants.CHARACTERS_61_STRING, "maguirreminarro@gmail.com", 14, null);
		
		String url = "/customers";
		mockMvc.perform(
				post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(customer))
				).andExpect(status().isBadRequest());
		
		Mockito.verify(customerService, times(0)).register(customer);
	}
	
	@Test
	public void customerMailMustBeUnder60Characters() throws JsonProcessingException, Exception {
		CustomerDto customer = new CustomerDto("Marcos", "Aguirre", Constants.CHARACTERS_61_STRING, 14, null);
		
		String url = "/customers";
		mockMvc.perform(
				post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(customer))
				).andExpect(status().isBadRequest());
		
		Mockito.verify(customerService, times(0)).register(customer);
	}
	
	@Test
	public void customerNameMustMatchPattern() throws JsonProcessingException, Exception {
		CustomerDto customer = new CustomerDto(Constants.ONLY_CHARACTERS_REGEX_STRING, "Aguirre", "maguirreminarro@gmail.com", 14, null);
		
		String url = "/customers";
		mockMvc.perform(
				post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(customer))
				).andExpect(status().isBadRequest());
		
		Mockito.verify(customerService, times(0)).register(customer);
	}
	
	@Test
	public void customerSurnameMustMatchPattern() throws JsonProcessingException, Exception {
		CustomerDto customer = new CustomerDto("Marcos", Constants.ONLY_CHARACTERS_REGEX_STRING, "maguirreminarro@gmail.com", 14, null);
		
		String url = "/customers";
		mockMvc.perform(
				post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(customer))
				).andExpect(status().isBadRequest());
		
		Mockito.verify(customerService, times(0)).register(customer);
	}
	
	@Test
	public void customerMailMustMatchMailPattern() throws JsonProcessingException, Exception {
		CustomerDto customer = new CustomerDto("Marcos", "Aguirre", Constants.BAD_EMAIL_FORMATTING_STRING, 14, null);
		
		String url = "/customers";
		mockMvc.perform(
				post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(customer))
				).andExpect(status().isBadRequest());
		
		Mockito.verify(customerService, times(0)).register(customer);
	}
	
	
}
