package com.nickesqueda.grocerystoredemo.util;

import com.nickesqueda.grocerystoredemo.dto.CategoryWithProductsDto;
import com.nickesqueda.grocerystoredemo.dto.ProductDto;
import com.nickesqueda.grocerystoredemo.dto.UserDto;
import com.nickesqueda.grocerystoredemo.model.entity.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

public final class ModelMapperUtil {

  private static final ModelMapper modelMapper = new ModelMapper();

  static {
    var userTypeMap = modelMapper.createTypeMap(User.class, UserDto.class);
    Converter<Set<Role>, Set<RoleName>> rolesConverter =
        context -> context.getSource().stream().map(Role::getName).collect(Collectors.toSet());
    userTypeMap.addMappings(
        mapper -> mapper.using(rolesConverter).map(User::getRoles, UserDto::setRoles));

    var categoryTypeMap = modelMapper.createTypeMap(Category.class, CategoryWithProductsDto.class);
    Converter<List<Product>, List<ProductDto>> productListConverter =
        context ->
            context.getSource().stream()
                .map(productEntity -> ModelMapperUtil.map(productEntity, ProductDto.class))
                .toList();
    categoryTypeMap.addMappings(
        mapper ->
            mapper
                .using(productListConverter)
                .map(Category::getProducts, CategoryWithProductsDto::setProducts));
  }

  private ModelMapperUtil() {}

  public static <T, U> U map(T src, Class<U> dstClass) {
    return modelMapper.map(src, dstClass);
  }

  public static <T, U> void map(T src, U dst) {
    modelMapper.map(src, dst);
  }
}
