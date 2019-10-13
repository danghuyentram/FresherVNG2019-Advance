package com.example.demomockobject;

import com.example.demomockobject.dao.CategoryDao;
import com.example.demomockobject.impl.CategoryManagerImpl;
import com.example.demomockobject.manager.CategoryManager;
import com.example.demomockobject.model.Category;
import com.example.demomockobject.model.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
//@SpringBootTest
public class DemoMockObjectApplicationTests {

	@InjectMocks
	CategoryManagerImpl categoryManager;

	@Mock
	CategoryDao categoryDao;
	Map<String, Category> categoryMap = new HashMap<String, Category>();
	Map<String, Category> createCategoryMap(int length){
		Map<String, Category> quesMap = new HashMap<String, Category>();
		for(int i=0;i<length;i++){
			Category question = createCategory("Category"+i,i);
			quesMap.put("Category_"+i,question);
		}
		return quesMap;

	}

	Category createCategory(String id, int i){
		Category category = new Category(id,"Category_"+i);
		return category;
	}

	@Before
	public void setUp(){
		// GIVEN
		MockitoAnnotations.initMocks(this);
		categoryMap = createCategoryMap(10);

		when(categoryManager.getCategories()).thenAnswer(new Answer<List<Category>>() {
			@Override
			public List<Category> answer(InvocationOnMock invocationOnMock) throws Throwable {
				List<Category> newList = new ArrayList<Category>();
				for(int i=0;i<categoryMap.size();i++){
					newList.add(categoryMap.get("Category_"+i));
				}
				return newList;
			}
		});
	}

	@Test
	public void getCategoriesTest(){
		// WHEN
		List<Category> list = categoryManager.getCategories();

		// THEN
		Assert.assertTrue(list.size()==10);
	}

	@Test
	public void getProductTest(){
		// WHEN
		when(categoryManager.getProducts(Mockito.anyString()))
							.thenAnswer(new Answer<List<Product>>() {
								@Override
								public List<Product> answer(InvocationOnMock invocationOnMock) throws Throwable {
									List<Product> listProduct = new ArrayList<Product>();
									for(int i=0;i<5;i++){
										Product item = new Product("Product_"+i,"Product_"+i,(String) invocationOnMock.getArguments()[0]);
										listProduct.add(item);
									}

									return listProduct;

								}
							});
		String idTest = "Category_1";
		List<Product> listProduct = categoryManager.getProducts(idTest);


		// THEN
		Assert.assertTrue(listProduct.size()==5);
	}

	@Test
	public void getProductsTest_NotFound(){
		// WHEN
		when(categoryManager.getProducts(Mockito.anyString()))
							.thenAnswer(new Answer<List<Product>>() {
								@Override
								public List<Product> answer(InvocationOnMock invocationOnMock) throws Throwable {
									throw new Exception("NotFound");
								}
							});

		// THEN
		try{
			String idTest = "Category_999";
			List<Product> listProduct = categoryManager.getProducts(idTest);
			Assert.assertNull(listProduct);
		}catch (Exception e){
			Assert.assertEquals("NotFound",e.getMessage());
		}
	}



}
