package com.example.demomockobject.impl;

import com.example.demomockobject.dao.CategoryDao;
import com.example.demomockobject.manager.CategoryManager;
import com.example.demomockobject.model.Category;
import com.example.demomockobject.model.Product;
import lombok.Data;

import java.util.List;

@Data
public class CategoryManagerImpl implements CategoryManager {

    private CategoryDao categoryDao;


    @Override
    public List<Category> getCategories() {
        return categoryDao.getList();
    }

    @Override
    public List<Product> getProducts(String id) {
        return categoryDao.getProductsByCatID(id);
    }
}
