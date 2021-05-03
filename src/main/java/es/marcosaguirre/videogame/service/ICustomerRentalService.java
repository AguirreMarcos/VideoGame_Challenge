package es.marcosaguirre.videogame.service;

import es.marcosaguirre.videogame.common.BaseException;
import es.marcosaguirre.videogame.dto.ConfirmedRentalResponseDto;
import es.marcosaguirre.videogame.dto.RentalInfoResponseDto;
import es.marcosaguirre.videogame.dto.RentalInputDto;

public interface ICustomerRentalService {
	public RentalInfoResponseDto showRentalInfo(RentalInputDto inputDto) throws BaseException;
	public ConfirmedRentalResponseDto confirmRentalOperation(RentalInputDto inputDto, Long customerId) throws BaseException;
}
