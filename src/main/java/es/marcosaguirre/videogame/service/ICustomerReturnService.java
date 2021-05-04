package es.marcosaguirre.videogame.service;

import es.marcosaguirre.videogame.common.BaseException;
import es.marcosaguirre.videogame.dto.ConfirmedReturnResponseDto;
import es.marcosaguirre.videogame.dto.ReturnInfoResponseDto;
import es.marcosaguirre.videogame.dto.ReturnInputDto;

public interface ICustomerReturnService {
	
	public ReturnInfoResponseDto showPartialReturnInfo(ReturnInputDto inputDto, Long id) throws BaseException;
	
	public ReturnInfoResponseDto showTotalReturnInfo(Long id) throws BaseException;
	
	public ConfirmedReturnResponseDto confirmPartialReturn(ReturnInputDto inputDto, Long id) throws BaseException;
	
	public ConfirmedReturnResponseDto confirmTotalReturn(Long id) throws BaseException;
}
