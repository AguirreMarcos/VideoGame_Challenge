package es.marcosaguirre.videogame.integration;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import es.marcosaguirre.videogame.common.Constants;
import es.marcosaguirre.videogame.common.Message;
import es.marcosaguirre.videogame.common.Response;
import es.marcosaguirre.videogame.dto.CustomerDto;
import es.marcosaguirre.videogame.service.ICustomerService;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ICustomerService customerService;

	@Test
	public void addCustomerTest() throws JsonProcessingException, Exception {

		CustomerDto newCustomer = new CustomerDto("Sandra", "Duque", "sduque@gmail.com", 14, null);

		String url = "/customers";
		MvcResult mvcResult = mockMvc
				.perform(
						post(url).contentType("application/json").content(objectMapper.writeValueAsString(newCustomer)))
				.andExpect(status().isCreated()).andDo(print()).andReturn();

		Integer id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.data.id");

		String name = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.data.name");

		CustomerDto savedCustomer = customerService.getById(id.longValue());

		assertThat(name.equals(savedCustomer.getName()));

		customerService.delete(id.longValue());
	}

	@Test
	public void editCustomerTest() throws JsonProcessingException, Exception {

		CustomerDto createdCustomer = customerService
				.register(new CustomerDto("Sandra", "Duque", "sduque@gmail.com", 14, null));

		CustomerDto updatedCustomer = new CustomerDto("Emma", "Duque", "sduque@gmail.com", 14, null);

		String url_update = "/customers/" + createdCustomer.getId();

		MvcResult mvcResult = mockMvc
				.perform(put(url_update).contentType("application/json")
						.content(objectMapper.writeValueAsString(updatedCustomer)))
				.andExpect(status().isCreated()).andDo(print()).andReturn();

		String name = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.data.name");

		assertThat(name.equals(updatedCustomer.getName()));

		customerService.delete(createdCustomer.getId());
	}

	@Test
	public void deleteCustomerByIdTest() throws JsonProcessingException, Exception {

		CustomerDto createdCustomer = customerService
				.register(new CustomerDto("Sandra", "Duque", "sduque@gmail.com", 14, null));

		String url = "/customers/" + createdCustomer.getId();

		Response<Void> response = new Response<>();
		response.addMessage(
				new Message(Constants.CODE_OK, Constants.MSJ_ENTRY_DELETED, Long.toString(createdCustomer.getId())));

		mockMvc.perform(delete(url)).andExpect(status().isCreated())
				.andExpect(content().string(objectMapper.writeValueAsString(response))).andDo(print());

	}

	@Test
	public void getCustomersTest() throws Exception {

		// clear customers
		List<CustomerDto> previousList = customerService.getAll();

		for (CustomerDto customer : previousList) {
			customerService.delete(customer.getId());
		}

		// -----

		// add elements to database

		List<CustomerDto> actualList = new ArrayList<>();

		actualList.add(new CustomerDto("Marcos", "Aguirre", "maguirreminarro@gmail.com", 14, null));
		actualList.add(new CustomerDto("Sandra", "Duque", "sduqueduniol@gmail.com", 14, null));
		actualList.add(new CustomerDto("Emma", "Aguirre", "emma@gmail.com", 14, null));

		for (CustomerDto customer : actualList) {
			customerService.register(customer);
		}

		List<CustomerDto> responseList = customerService.getAll();

		String url = "/customers";

		MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();

		String actualJsonResponse = mvcResult.getResponse().getContentAsString();

		Response<List<CustomerDto>> expectedResponse = new Response<>();

		expectedResponse.setData(responseList);

		String expectedJsonResponse = objectMapper.writeValueAsString(expectedResponse);

		assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
	}

	@Test
	public void shouldReturnErrorResponseOnEmptyGetList() throws Exception {

		// clear customers
		List<CustomerDto> previousList = customerService.getAll();

		for (CustomerDto customer : previousList) {
			customerService.delete(customer.getId());
		}

		Response<List<CustomerDto>> response = new Response<>();

		List<CustomerDto> listOfCustomers = Collections.emptyList();

		response.setData(listOfCustomers);

		response.addMessage(new Message(Constants.MSJ_EMPTY_LIST));

		String url = "/customers";

		MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();

		String actualJsonResponse = mvcResult.getResponse().getContentAsString();

		String expectedJsonResponse = objectMapper.writeValueAsString(response);

		assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
	}

	@Test
	public void customerNameMustNotBeBlank() throws JsonProcessingException, Exception {
		CustomerDto customer = new CustomerDto("", "Aguirre", "maguirreminarro@gmail.com", 14, null);

		String url = "/customers";
		mockMvc.perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(customer)))
				.andExpect(status().isBadRequest()).andDo(print());
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
	}
}
