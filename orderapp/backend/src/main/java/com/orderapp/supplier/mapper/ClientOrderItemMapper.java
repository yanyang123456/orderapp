package com.orderapp.supplier.mapper;

import com.orderapp.supplier.common.mapper.BaseMapper;
import com.orderapp.supplier.entity.ClientOrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClientOrderItemMapper extends BaseMapper<ClientOrderItem> {
    List<ClientOrderItem> selectByOrderId(@Param("orderId") Long orderId);

    List<ClientOrderItem> selectByOrderNo(@Param("orderNo") String orderNo);
}
