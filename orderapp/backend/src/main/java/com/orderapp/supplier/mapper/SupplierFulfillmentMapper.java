package com.orderapp.supplier.mapper;

import com.orderapp.supplier.common.mapper.BaseMapper;
import com.orderapp.supplier.entity.SupplierFulfillment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SupplierFulfillmentMapper extends BaseMapper<SupplierFulfillment> {
    List<SupplierFulfillment> selectClientFulfillments(@Param("orderNo") String orderNo,
                                                       @Param("status") String status,
                                                       @Param("offset") Integer offset,
                                                       @Param("pageSize") Integer pageSize);

    int countClientFulfillments(@Param("orderNo") String orderNo,
                                @Param("status") String status);

    SupplierFulfillment selectClientFulfillmentByOrderNo(@Param("orderNo") String orderNo);
}
