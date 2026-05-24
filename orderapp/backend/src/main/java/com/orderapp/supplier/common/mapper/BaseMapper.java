package com.orderapp.supplier.common.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseMapper<T> {

    int insert(T entity);

    int insertBatch(@Param("list") List<T> list);

    int updateById(T entity);

    List<T> selectByCondition(T condition);

    T selectById(@Param("id") Long id);

    List<T> selectByIds(@Param("ids") List<Long> ids);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List<Long> ids);
}
