package es.marcosaguirre.videogame.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import es.marcosaguirre.videogame.common.BaseException;
import es.marcosaguirre.videogame.dto.CustomerDto;
import es.marcosaguirre.videogame.dto.mapper.CustomerMapper;
import es.marcosaguirre.videogame.model.Customer;
import es.marcosaguirre.videogame.repository.ICustomerRepo;
import es.marcosaguirre.videogame.service.impl.CustomerServiceImpl;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CustomerServiceTest {

	@MockBean
	private ICustomerRepo repo;

	@InjectMocks
	private ICustomerService customerService = new CustomerServiceImpl();

	@Test
	public void registerTest() throws BaseException {
		CustomerDto inputDto = new CustomerDto("Marcos", "Aguirre", "maguirreminarro@gmail.com", 0, null);

		customerService.register(inputDto);

		verify(repo, times(1)).save(CustomerMapper.toCustomerEntity(inputDto));
	}

	@Test
	public void updateTest() throws BaseException {

		Customer customer = new Customer(1L, "Marcos", "Aguirre", "maguirreminarro@gmail.com", 0, null);

		Mockito.when(repo.findById(1L)).thenReturn(Optional.of(customer));

		customer.setLastName("Duque");

		customerService.update(CustomerMapper.toCustomerDto(customer), 1L);

		CustomerDto updatedCustomer = customerService.getById(1L);

		assertEquals("Duque", updatedCustomer.getLastName());

	}

	@Test
	public void getAllCustomersTest() throws BaseException {

		Customer firstCustomer = new Customer(1L, "Marcos", "Aguirre", "maguirreminarro@gmail.com", 0, null);
		Customer secondCustomer = new Customer(2L, "Sandra", "Duque", "sduque@gmail.com", 0, null);
		List<Customer> listOfCustomers = new ArrayList<>();
		listOfCustomers.add(firstCustomer);
		listOfCustomers.add(secondCustomer);

		Mockito.when(repo.findAll()).thenReturn(listOfCustomers);

		List<CustomerDto> customersList = customerService.getAll();

		assertEquals(2, customersList.size());
		verify(repo, times(1)).findAll();
	}

	@Test
	public void getByIdTest() throws BaseException {
		Customer customer = new Customer(1L, "Marcos", "Aguirre", "maguirreminarro@gmail.com", 0, null);
		Mockito.when(repo.findById(1L)).thenReturn(Optional.of(customer));
		
		CustomerDto myCustomer = customerService.getById(1L);
		
		assertEquals("Marcos", myCustomer.getName());
		assertEquals("Aguirre", myCustomer.getLastName());
		assertEquals("maguirreminarro@gmail.com", myCustomer.getMail());
		
	}
	
	@Test
	public void deleteTest() throws BaseException {
		Customer customer = new Customer(1L, "Marcos", "Aguirre", "maguirreminarro@gmail.com", 0, null);
		Mockito.when(repo.findById(1L)).thenReturn(Optional.of(customer));
		customerService.delete(1L);
		verify(repo, times(1)).deleteById(customer.getId());
	}

}
