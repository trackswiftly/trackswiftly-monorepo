package com.trackswiftly.client_service.dao.interfaces;

import java.util.List;

/***
 * 
 * 
 * Base generic interface , that take entity and its ID (I =ID) , (T = type)
 */
public interface BaseDao <T, I>{

    List<T> insertInBatch(List<T> entities);
    
    int deleteByIds(List<I> ids);
    
    List<T> findByIds(List<I> ids);
    
    List<T> findWithPagination(int page, int pageSize);
    
    Long count();

    int updateInBatch(List<I> ids, T entity) ;

    List<T> search(String keyword , int page, int pageSize) ;
}