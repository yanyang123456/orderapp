package com.orderapp.supplier.common.service;

import com.orderapp.supplier.common.mapper.BaseMapper;

import java.util.List;

public abstract class BaseService<T> {

    protected abstract BaseMapper<T> mapper();

    public int insert(T entity) {
        return mapper().insert(entity);
    }

    public int insertBatch(List<T> list) {
        return mapper().insertBatch(list);
    }

    public int updateById(T entity) {
        return mapper().updateById(entity);
    }

    public List<T> selectByCondition(T condition) {
        return mapper().selectByCondition(condition);
    }

    public T selectById(Long id) {
        return mapper().selectById(id);
    }

    public List<T> selectByIds(List<Long> ids) {
        return mapper().selectByIds(ids);
    }

    public int deleteById(Long id) {
        return mapper().deleteById(id);
    }

    public int deleteByIds(List<Long> ids) {
        return mapper().deleteByIds(ids);
    }
}
