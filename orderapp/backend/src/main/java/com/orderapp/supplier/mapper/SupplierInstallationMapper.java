package com.orderapp.supplier.mapper;

import com.orderapp.supplier.common.mapper.BaseMapper;
import com.orderapp.supplier.entity.SupplierInstallation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SupplierInstallationMapper extends BaseMapper<SupplierInstallation> {
    SupplierInstallation selectClientInstallationByOrderNo(@Param("orderNo") String orderNo);
}
