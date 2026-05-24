package com.orderapp.supplier.mapper;

import com.orderapp.supplier.common.mapper.BaseMapper;
import com.orderapp.supplier.entity.SupplierProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface SupplierProductMapper extends BaseMapper<SupplierProduct> {
    List<SupplierProduct> selectClientProducts(@Param("keyword") String keyword,
                                               @Param("categoryId") Long categoryId,
                                               @Param("category") String category,
                                               @Param("minPrice") BigDecimal minPrice,
                                               @Param("maxPrice") BigDecimal maxPrice,
                                               @Param("material") String material,
                                               @Param("style") String style,
                                               @Param("supportInstallation") Boolean supportInstallation,
                                               @Param("offset") Integer offset,
                                               @Param("pageSize") Integer pageSize);

    int countClientProducts(@Param("keyword") String keyword,
                            @Param("categoryId") Long categoryId,
                            @Param("category") String category,
                            @Param("minPrice") BigDecimal minPrice,
                            @Param("maxPrice") BigDecimal maxPrice,
                            @Param("material") String material,
                            @Param("style") String style,
                            @Param("supportInstallation") Boolean supportInstallation);

    SupplierProduct selectClientProductById(@Param("id") Long id);

    List<Map<String, Object>> selectClientCategories();

    List<SupplierProduct> selectClientHotProducts(@Param("limit") Integer limit);

    int deductStock(@Param("id") Long id, @Param("quantity") Integer quantity);
}
