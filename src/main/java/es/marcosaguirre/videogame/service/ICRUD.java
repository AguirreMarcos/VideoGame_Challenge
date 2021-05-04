package es.marcosaguirre.videogame.service;

import java.util.List;

import es.marcosaguirre.videogame.common.BaseException;

public interface ICRUD<T, V> {
	
	T register(T obj) throws BaseException;
	
	T update(T obj, V id) throws BaseException;
	
	List<T> getAll() throws BaseException;
	
	T getById(V id) throws BaseException;
	
	boolean delete(V id) throws BaseException;
}
