package es.marcosaguirre.videogame.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import es.marcosaguirre.videogame.common.BaseException;
import es.marcosaguirre.videogame.common.Constants;
import es.marcosaguirre.videogame.dto.CustomerDto;
import es.marcosaguirre.videogame.dto.mapper.CustomerMapper;
import es.marcosaguirre.videogame.model.Customer;
import es.marcosaguirre.videogame.repository.ICustomerRepo;
import es.marcosaguirre.videogame.service.ICustomerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImpl implements ICustomerService {

	@Autowired
	private ICustomerRepo customerRepo;

	@Override
	public CustomerDto register(CustomerDto obj) throws BaseException {
		Customer newCustomer = null;
		try {
			newCustomer = customerRepo.save(CustomerMapper.toCustomerEntity(obj));
		} catch (DataIntegrityViolationException dve) {
			log.error(dve.getMessage());
			BaseException e = new BaseException(Constants.ERROR_CODE_DAO, Constants.MSJ_SERVER_ERROR);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
			BaseException ex = new BaseException(Constants.ERROR_CODE_SERVER, Constants.MSJ_SERVER_ERROR);
			throw ex;
		}
		return CustomerMapper.toCustomerDto(newCustomer);
	}

	@Override
	public CustomerDto update(CustomerDto obj,  Long id) throws BaseException {
		Optional<Customer> customer = customerRepo.findById(id);
		if (!customer.isPresent()) {
			log.warn("Customer with id " + obj.getId() + "doesn't exists");
			BaseException exception = new BaseException(Constants.ERROR_CODE_ELEMENT_NOT_FOUND,
					Constants.MSJ_ENTRY_DOES_NOT_EXISTS, Long.toString(obj.getId()));
			throw exception;
		}
		obj.setId(id);
		Customer updatedCustomer = customerRepo.save(CustomerMapper.toCustomerEntity(obj));
		return CustomerMapper.toCustomerDto(updatedCustomer);
	}

	@Override
	public List<CustomerDto> getAll() throws BaseException {
		return CustomerMapper.toCustomerDto(customerRepo.findAll());
	}

	@Override
	public CustomerDto getById(Long id) throws BaseException {
		Optional<Customer> customer = customerRepo.findById(id);
		if (!customer.isPresent()) {
			log.warn("Customer with id " + id + "doesn't exists");
			BaseException exception = new BaseException(Constants.ERROR_CODE_ELEMENT_NOT_FOUND,
					Constants.MSJ_ENTRY_DOES_NOT_EXISTS, Long.toString(id));
			throw exception;
		}
		return CustomerMapper.toCustomerDto(customer.get());
	}

	@Override
	public boolean delete(Long id) throws BaseException {
		Optional<Customer> customer = customerRepo.findById(id);
		if (!customer.isPresent()) {
			log.warn("Customer with id " + id + "doesn't exists");
			BaseException exception = new BaseException(Constants.ERROR_CODE_ELEMENT_NOT_FOUND,
					Constants.MSJ_ENTRY_DOES_NOT_EXISTS, Long.toString(id));
			throw exception;
		}
		customerRepo.deleteById(id);
		return true;
	}

}
