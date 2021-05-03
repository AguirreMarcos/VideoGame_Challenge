package es.marcosaguirre.videogame.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import es.marcosaguirre.videogame.common.BaseException;
import es.marcosaguirre.videogame.common.Constants;
import es.marcosaguirre.videogame.dto.OperationDto;
import es.marcosaguirre.videogame.dto.mapper.OperationMapper;
import es.marcosaguirre.videogame.model.Operation;
import es.marcosaguirre.videogame.repository.IOperationRepo;
import es.marcosaguirre.videogame.service.IOperationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OperationServiceImpl implements IOperationService{
	
	@Autowired
	private IOperationRepo operationRepo;

	@Override
	public Operation register(Operation operation) throws BaseException {
		Operation newOperation = null;
		try {
			newOperation = operationRepo.save(operation);
		}catch (DataIntegrityViolationException dve) {
			log.error(dve.getMessage());
			BaseException e = new BaseException(Constants.ERROR_CODE_DAO, Constants.MSJ_SERVER_ERROR);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
			BaseException ex = new BaseException(Constants.ERROR_CODE_SERVER, Constants.MSJ_SERVER_ERROR);
			throw ex;
		}
		return newOperation;
	}

	@Override
	public List<OperationDto> getAll() throws BaseException {
		return OperationMapper.toOperationDto(operationRepo.findAll());
	}

	@Override
	public OperationDto getById(Long id) throws BaseException {
		Optional<Operation> operation = operationRepo.findById(id);
		if (!operation.isPresent()) {
			log.warn("Operation with id " + id + "doesn't exists");
			BaseException exception = new BaseException(Constants.ERROR_CODE_ELEMENT_NOT_FOUND,
					Constants.MSJ_ENTRY_DOES_NOT_EXISTS, "Operation with id: " + Long.toString(id));
			throw exception;
		}
		return OperationMapper.toOperationDto(operation.get());
	}

}
