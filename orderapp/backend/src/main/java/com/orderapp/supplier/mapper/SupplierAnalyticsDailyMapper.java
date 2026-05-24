package com.orderapp.supplier.mapper;

import com.orderapp.supplier.common.mapper.BaseMapper;
import com.orderapp.supplier.entity.SupplierAnalyticsDaily;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

@Mapper
public interface SupplierAnalyticsDailyMapper extends BaseMapper<SupplierAnalyticsDaily> {
    int upsertOrderAnalytics(@Param("supplierId") Long supplierId,
                             @Param("statDate") LocalDate statDate,
                             @Param("orderCount") Integer orderCount,
                             @Param("salesAmount") BigDecimal salesAmount);
}
