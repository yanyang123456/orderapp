package com.orderapp.supplier.mapper;

import com.orderapp.supplier.common.mapper.BaseMapper;
import com.orderapp.supplier.entity.SupplierProductMetric;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

@Mapper
public interface SupplierProductMetricMapper extends BaseMapper<SupplierProductMetric> {
    int upsertOrderMetrics(@Param("supplierId") Long supplierId,
                           @Param("productId") Long productId,
                           @Param("statDate") LocalDate statDate,
                           @Param("orders") Integer orders,
                           @Param("salesAmount") BigDecimal salesAmount);
}
