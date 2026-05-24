package com.orderapp.supplier;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.orderapp.supplier.mapper")
@SpringBootApplication
public class SupplierBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupplierBackendApplication.class, args);
    }
}
