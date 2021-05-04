package es.marcosaguirre.videogame.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.marcosaguirre.videogame.model.Customer;

public interface ICustomerRepo extends JpaRepository<Customer, Long> {
}
