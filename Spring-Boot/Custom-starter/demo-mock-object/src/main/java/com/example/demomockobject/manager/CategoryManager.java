package com.example.demomockobject.manager;

import com.example.demomockobject.model.Category;
import com.example.demomockobject.model.Product;

import java.util.List;

public interface CategoryManager {
    List<Category> getCategories();
    List<Product> getProducts(String id);
}
