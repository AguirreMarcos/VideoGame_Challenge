package es.marcosaguirre.videogame.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import es.marcosaguirre.videogame.dto.CustomerDto;
import es.marcosaguirre.videogame.model.Customer;

public class CustomerMapper {
	public static List<CustomerDto> toCustomerDto(List<Customer> customers) {
		List<CustomerDto> dtos = new ArrayList<>();
		if (customers != null) {
			for (Customer customer : customers) {
				dtos.add(toCustomerDto(customer));
			}
		}
		return dtos;
	}
	
	public static CustomerDto toCustomerDto(Customer customer) {
		CustomerDto dto = new CustomerDto();
		if(customer != null) {
			dto.setId(customer.getId());
			dto.setName(customer.getName());
			dto.setLastName(customer.getLastName());
			dto.setMail(customer.getMail());
			dto.setLoyaltyPoints(customer.getLoyaltyPoints());
			dto.setRentedGames(customer.getRentedGames());
		}
		return dto;
	}
	
	public static Customer toCustomerEntity(CustomerDto customerDto) {
		Customer customerEntity = new Customer();
		if(customerDto != null) {
			if(customerDto.getId() != null) {
				customerEntity.setId(customerDto.getId());				
			}
			customerEntity.setName(customerDto.getName());
			customerEntity.setLastName(customerDto.getLastName());
			customerEntity.setMail(customerDto.getMail());
			if (customerDto.getLoyaltyPoints() != null) {
				customerEntity.setLoyaltyPoints(customerDto.getLoyaltyPoints());
			}else {
				customerEntity.setLoyaltyPoints(0);
			}
			if (customerDto.getRentedGames() != null) {
				customerEntity.setRentedGames(customerDto.getRentedGames());				
			}
			
		}
		return customerEntity;
	}
}
