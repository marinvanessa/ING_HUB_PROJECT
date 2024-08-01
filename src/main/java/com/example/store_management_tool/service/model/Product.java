package com.example.store_management_tool.service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @NonNull
    private UUID id;

    @NonNull
    private String name;

    private String description;

    private double price;
}
