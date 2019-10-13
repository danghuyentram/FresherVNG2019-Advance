package com.example.demomockobject.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    String id;
    String name;
    String catID;
}
