package es.marcosaguirre.videogame.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import es.marcosaguirre.videogame.common.Constants;
import es.marcosaguirre.videogame.model.Customer;

@DataJpaTest
public class CustomerRepositoryTest {
	
	@Autowired
	private ICustomerRepo repo;
	
	@Test
	public void registerTest() {
		Customer customer = repo.save(new Customer("Marcos", "Aguirre", "maguirre@gmail.com"));
		assertThat(customer).isNotEqualTo(null);
		assertThat(customer.getId()).isGreaterThan(0);
	}
	
	@Test
	public void findByIdTest() {
		Customer customer = repo.save(new Customer("Marcos", "Aguirre", "maguirre@gmail.com"));
		Long customerId = customer.getId();
		Optional<Customer> findedCustomer = repo.findById(customerId);
		assertThat(findedCustomer.get().getMail()).isEqualTo(customer.getMail());
	}
	
	@Test
	public void findAllTest() {
		repo.save(new Customer("Marcos", "Aguirre", "maguirre@gmail.com"));
		repo.save(new Customer("Sandra", "Duque", "sduque@gmail.com"));
		List<Customer> allCustomersList = repo.findAll();
		assertThat(allCustomersList.size()).isEqualTo(2);
	}
	
	@Test
	public void deleteByIdTest() {
		Customer customer = repo.save(new Customer("Marcos", "Aguirre", "maguirre@gmail.com"));
		assertThat(repo.findAll().size()).isEqualTo(1);
		repo.deleteById(customer.getId());
		assertThat(repo.findAll().size()).isEqualTo(0);
	}
	
	@Test
	public void createShouldFailOnLargeName() {
		assertThrows(DataIntegrityViolationException.class, () ->{
			repo.save(new Customer(Constants.CHARACTERS_51_STRING, "Aguirre", "maguirre@gmail.com"));
		});
	}
	
	@Test
	public void createShouldFailOnLargeLastName() {
		assertThrows(DataIntegrityViolationException.class, () ->{
			repo.save(new Customer("Marcos", Constants.CHARACTERS_61_STRING, "maguirre@gmail.com"));
		});
	}
	
	@Test
	public void createShouldFailOnLargeMail() {
		assertThrows(DataIntegrityViolationException.class, () ->{
			repo.save(new Customer("Marcos", "Aguirre", Constants.CHARACTERS_61_MAIL_STRING));
		});
	}
	
	@Test
	public void createShouldFailOnExistingMail() {
		repo.save(new Customer("Sandra", "Aguirre", "maguirre@gmail.com"));
		assertThrows(DataIntegrityViolationException.class, () ->{
			repo.save(new Customer("Marcos", "Aguirre", "maguirre@gmail.com"));
		});
	}
	
	
	
}
