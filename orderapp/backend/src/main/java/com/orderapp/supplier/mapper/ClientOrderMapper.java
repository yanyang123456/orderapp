package com.orderapp.supplier.mapper;

import com.orderapp.supplier.common.mapper.BaseMapper;
import com.orderapp.supplier.entity.ClientOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClientOrderMapper extends BaseMapper<ClientOrder> {
    List<ClientOrder> selectClientOrders(@Param("userId") Long userId,
                                         @Param("status") String status,
                                         @Param("offset") Integer offset,
                                         @Param("pageSize") Integer pageSize);

    int countClientOrders(@Param("userId") Long userId,
                          @Param("status") String status);

    ClientOrder selectByOrderNo(@Param("orderNo") String orderNo);
}
