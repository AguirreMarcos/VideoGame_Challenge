package es.marcosaguirre.videogame.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import es.marcosaguirre.videogame.common.OperationTypes;
import es.marcosaguirre.videogame.model.Operation;

@DataJpaTest
public class OperationRepositoryTest {
	
	@Autowired
	private IOperationRepo repo;
	
	@Test
	public void registerTest() {
		Operation operation = repo.save(new Operation(1L, OperationTypes.RENTAL, 15.0, LocalDate.now()));
		assertThat(operation).isNotEqualTo(null);
		assertThat(operation.getId()).isGreaterThan(0);
	}
	
	@Test
	public void findByIdTest() {
		Operation operation = repo.save(new Operation(1L, OperationTypes.RENTAL, 15.0, LocalDate.now()));
		Long operationId = operation.getId();
		Optional<Operation> findedOperation = repo.findById(operationId);
		assertThat(findedOperation.get().getOperationType()).isEqualTo(operation.getOperationType());
	}
	
	@Test
	public void findAllTest() {
		repo.save(new Operation(1L, OperationTypes.RENTAL, 15.0, LocalDate.now()));
		repo.save(new Operation(2L, OperationTypes.RETURN, 12.0, LocalDate.now()));
		List<Operation> allOperationsList = repo.findAll();
		assertThat(allOperationsList.size()).isEqualTo(2);
	}
	
	@Test
	public void deleteByIdTest() {
		Operation operation = repo.save(new Operation(1L, 1L, OperationTypes.RENTAL, 15.0, LocalDate.now()));
		assertThat(repo.findAll().size()).isEqualTo(1);
		repo.deleteById(operation.getId());
		assertThat(repo.findAll().size()).isEqualTo(0);
	}
	
	@Test
	public void createShouldFailOnNullCustomerId() {
		assertThrows(DataIntegrityViolationException.class, () ->{
			repo.save(new Operation(null, OperationTypes.RENTAL, 18.0, LocalDate.now()));
		});
	}
	
	@Test
	public void createShouldFailOnNullOperationType() {
		assertThrows(DataIntegrityViolationException.class, () ->{
			repo.save(new Operation(1L, null, 18.0, LocalDate.now()));
		});
	}
	
	@Test
	public void createShouldFailOnNullAmount() {
		assertThrows(DataIntegrityViolationException.class, () ->{
			repo.save(new Operation(1L, OperationTypes.RENTAL, null, LocalDate.now()));
		});
	}
	
	@Test
	public void createShouldFailOnNullDate() {
		assertThrows(DataIntegrityViolationException.class, () ->{
			repo.save(new Operation(1L, OperationTypes.RENTAL, 18.0, null));
		});
	}

	
}
