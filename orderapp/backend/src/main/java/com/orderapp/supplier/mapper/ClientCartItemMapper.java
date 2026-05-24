package com.orderapp.supplier.mapper;

import com.orderapp.supplier.common.mapper.BaseMapper;
import com.orderapp.supplier.entity.ClientCartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClientCartItemMapper extends BaseMapper<ClientCartItem> {
    List<ClientCartItem> selectActiveByUserId(@Param("userId") Long userId);

    ClientCartItem selectActiveByUserIdAndProductId(@Param("userId") Long userId,
                                                    @Param("productId") Long productId);

    int countActiveByUserId(@Param("userId") Long userId);

    int deleteActiveByUserId(@Param("userId") Long userId);
}
