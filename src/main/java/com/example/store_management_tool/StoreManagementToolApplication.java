package com.example.store_management_tool;

import com.example.store_management_tool.service.enums.Role;
import com.example.store_management_tool.service.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class StoreManagementToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreManagementToolApplication.class, args);
    }

}
