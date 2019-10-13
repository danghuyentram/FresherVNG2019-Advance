package com.example.demomockobject.dao;

import com.example.demomockobject.model.Category;
import com.example.demomockobject.model.Product;

import java.util.List;

public interface CategoryDao {
    List<Category> getList();
    List<Product> getProductsByCatID(String id);
}
