package es.marcosaguirre.videogame.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.marcosaguirre.videogame.service.IOperationService;

@WebMvcTest(OperationController.class)
public class OperationControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private IOperationService operationService;
	
	@Test
	public void getOperationsTest() {
		
	}
	
	@Test
	public void getOperationByIdTest() {
		
	}
}
