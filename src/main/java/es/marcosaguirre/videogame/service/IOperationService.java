package es.marcosaguirre.videogame.service;

import java.util.List;

import es.marcosaguirre.videogame.common.BaseException;
import es.marcosaguirre.videogame.dto.OperationDto;
import es.marcosaguirre.videogame.model.Operation;

public interface IOperationService {
	public Operation register(Operation operation) throws BaseException;
	public List<OperationDto> getAll() throws BaseException;
	public OperationDto getById(Long id) throws BaseException;
}
