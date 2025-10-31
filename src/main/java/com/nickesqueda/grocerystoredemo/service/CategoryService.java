package com.nickesqueda.grocerystoredemo.service;

import com.nickesqueda.grocerystoredemo.dto.CategoryDto;
import com.nickesqueda.grocerystoredemo.dto.CategoryWithProductsDto;
import com.nickesqueda.grocerystoredemo.model.dao.Dao;
import com.nickesqueda.grocerystoredemo.model.entity.Category;
import com.nickesqueda.grocerystoredemo.security.AuthValidator;
import com.nickesqueda.grocerystoredemo.util.ModelMapperUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CategoryService {

  private final Dao<Category> categoryDao;

  public Category getCategoryEntity(Integer id) {
    return categoryDao.findOneByValue("id", id);
  }

  public CategoryDto getCategory(Integer id) {
    Category category = categoryDao.findOneByValue("id", id);
    return ModelMapperUtil.map(category, CategoryDto.class);
  }

  public CategoryWithProductsDto getCategoryWithProducts(Integer id) {
    Category category = categoryDao.findOneByValueWithRelations("id", id, "products");
    return ModelMapperUtil.map(category, CategoryWithProductsDto.class);
  }

  public List<CategoryDto> getAllCategories() {
    List<Category> categories = categoryDao.findAll();
    return categories.stream()
        .map(category -> ModelMapperUtil.map(category, CategoryDto.class))
        .toList();
  }

  public CategoryDto createCategory(CategoryDto categoryDto) {
    AuthValidator.requireAdminRole();

    Category categoryEntity = ModelMapperUtil.map(categoryDto, Category.class);
    categoryDao.save(categoryEntity);

    return ModelMapperUtil.map(categoryEntity, CategoryDto.class);
  }

  public void updateCategory(CategoryDto categoryDto) {
    AuthValidator.requireAdminRole();

    Category category = categoryDao.findOneByValue("id", categoryDto.getId());
    ModelMapperUtil.map(categoryDto, category);
    categoryDao.update(category);
  }

  public void deleteCategory(Integer id) {
    AuthValidator.requireAdminRole();

    Category category = categoryDao.findOneByValue("id", id);
    categoryDao.delete(category);
  }
}
