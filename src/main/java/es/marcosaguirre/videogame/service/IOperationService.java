package es.marcosaguirre.videogame.service;

import java.util.List;

import es.marcosaguirre.videogame.common.BaseException;
import es.marcosaguirre.videogame.dto.OperationDto;

public interface IOperationService {
	public OperationDto register(OperationDto operation) throws BaseException;
	public List<OperationDto> getAll() throws BaseException;
	public OperationDto getById(Long id) throws BaseException;
	boolean delete(Long id) throws BaseException;
}
