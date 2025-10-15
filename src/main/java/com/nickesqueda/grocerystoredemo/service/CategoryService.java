package com.nickesqueda.grocerystoredemo.service;

import com.nickesqueda.grocerystoredemo.dto.CategoryDto;
import com.nickesqueda.grocerystoredemo.dto.CategoryWithProductsDto;
import com.nickesqueda.grocerystoredemo.dto.UserDto;
import com.nickesqueda.grocerystoredemo.exception.UnauthenticatedException;
import com.nickesqueda.grocerystoredemo.exception.UnauthorizedException;
import com.nickesqueda.grocerystoredemo.model.dao.Dao;
import com.nickesqueda.grocerystoredemo.model.entity.Category;
import com.nickesqueda.grocerystoredemo.model.entity.RoleName;
import com.nickesqueda.grocerystoredemo.security.SessionContext;
import com.nickesqueda.grocerystoredemo.util.ModelMapperUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CategoryService {

  private final Dao<Category> categoryDao;

  public CategoryDto getCategory(Integer id) {
    Category category = categoryDao.findOneByValue("id", id);
    return ModelMapperUtil.map(category, CategoryDto.class);
  }

  public CategoryWithProductsDto getCategoryWithProducts(Integer id) {
    Category category = categoryDao.findOneByValueWithChildren("id", id, "products");
    return ModelMapperUtil.map(category, CategoryWithProductsDto.class);
  }

  public List<CategoryDto> getAllCategories() {
    List<Category> categories = categoryDao.findAll();
    return categories.stream()
        .map(category -> ModelMapperUtil.map(category, CategoryDto.class))
        .toList();
  }

  public CategoryDto createCategory(CategoryDto categoryDto) {
    if (!SessionContext.isSessionActive()) {
      throw new UnauthenticatedException();
    }

    UserDto sessionUser = SessionContext.getSessionUser();
    if (!sessionUser.getRoles().contains(RoleName.ROLE_ADMIN)) {
      throw new UnauthorizedException(sessionUser.getId(), RoleName.ROLE_ADMIN);
    }

    Category categoryEntity = ModelMapperUtil.map(categoryDto, Category.class);
    categoryDao.save(categoryEntity);

    return ModelMapperUtil.map(categoryEntity, CategoryDto.class);
  }

  public void updateCategory(CategoryDto categoryDto) {
    if (!SessionContext.isSessionActive()) {
      throw new UnauthenticatedException();
    }

    UserDto sessionUser = SessionContext.getSessionUser();
    if (!sessionUser.getRoles().contains(RoleName.ROLE_ADMIN)) {
      throw new UnauthorizedException(sessionUser.getId(), RoleName.ROLE_ADMIN);
    }

    Category category = categoryDao.findOneByValue("id", categoryDto.getId());
    ModelMapperUtil.map(categoryDto, category);
    categoryDao.update(category);
  }

  public void deleteCategory(Integer id) {
    if (!SessionContext.isSessionActive()) {
      throw new UnauthenticatedException();
    }

    UserDto sessionUser = SessionContext.getSessionUser();
    if (!sessionUser.getRoles().contains(RoleName.ROLE_ADMIN)) {
      throw new UnauthorizedException(sessionUser.getId(), RoleName.ROLE_ADMIN);
    }

    Category category = categoryDao.findOneByValue("id", id);

    categoryDao.delete(category);
  }
}
