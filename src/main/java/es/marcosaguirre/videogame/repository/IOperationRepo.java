package es.marcosaguirre.videogame.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.marcosaguirre.videogame.model.Operation;

public interface IOperationRepo extends JpaRepository<Operation, Long>{

}
