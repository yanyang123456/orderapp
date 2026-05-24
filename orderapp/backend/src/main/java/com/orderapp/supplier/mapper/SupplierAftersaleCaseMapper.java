package com.orderapp.supplier.mapper;

import com.orderapp.supplier.common.mapper.BaseMapper;
import com.orderapp.supplier.entity.SupplierAftersaleCase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SupplierAftersaleCaseMapper extends BaseMapper<SupplierAftersaleCase> {
    List<SupplierAftersaleCase> selectClientAftersales(@Param("orderNo") String orderNo,
                                                       @Param("status") String status,
                                                       @Param("offset") Integer offset,
                                                       @Param("pageSize") Integer pageSize);

    int countClientAftersales(@Param("orderNo") String orderNo,
                              @Param("status") String status);

    SupplierAftersaleCase selectClientAftersaleByCaseNo(@Param("caseNo") String caseNo);
}
