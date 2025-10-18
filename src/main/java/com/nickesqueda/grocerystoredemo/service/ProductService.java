package com.nickesqueda.grocerystoredemo.service;

import com.nickesqueda.grocerystoredemo.dto.ProductDto;
import com.nickesqueda.grocerystoredemo.model.dao.Dao;
import com.nickesqueda.grocerystoredemo.model.entity.Category;
import com.nickesqueda.grocerystoredemo.model.entity.Product;
import com.nickesqueda.grocerystoredemo.security.AuthValidator;
import com.nickesqueda.grocerystoredemo.util.ModelMapperUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductService {

  private final Dao<Product> productDao;
  private final Dao<Category> categoryDao;

  public ProductDto getProductDetails(Integer productId) {
    Product product = productDao.findOneByValue("id", productId);
    return ModelMapperUtil.map(product, ProductDto.class);
  }

  public List<ProductDto> getProductsInCategory(Integer categoryId) {
    List<Product> products = productDao.findAllByValue("category.id", categoryId);
    return products.stream()
        .map(product -> ModelMapperUtil.map(product, ProductDto.class))
        .toList();
  }

  public ProductDto addProduct(ProductDto productDto) {
    AuthValidator.requireAdminRole();

    Category category = categoryDao.findOneByValue("id", productDto.getCategoryId());
    Product product = ModelMapperUtil.map(productDto, Product.class);
    product.setCategory(category);

    productDao.save(product);

    return ModelMapperUtil.map(product, ProductDto.class);
  }

  public void updateProductDetails(ProductDto productDto) {
    AuthValidator.requireAdminRole();

    Product product = productDao.findOneByValue("id", productDto.getId());
    Category category = categoryDao.findOneByValue("id", productDto.getCategoryId());

    product.setCategory(category);
    ModelMapperUtil.map(productDto, product);

    productDao.update(product);
  }

  public void deleteProduct(Integer productId) {
    AuthValidator.requireAdminRole();

    Product product = productDao.findOneByValue("id", productId);
    productDao.delete(product);
  }
}
