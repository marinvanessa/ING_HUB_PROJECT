package com.example.store_management_tool.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class ProductDto {

    private UUID id;
    private String name;
    private String description;
    private double price;
}