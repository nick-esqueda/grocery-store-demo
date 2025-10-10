package com.nickesqueda.grocerystoredemo.util;

import com.nickesqueda.grocerystoredemo.dto.UserDto;
import com.nickesqueda.grocerystoredemo.model.entity.Role;
import com.nickesqueda.grocerystoredemo.model.entity.RoleName;
import com.nickesqueda.grocerystoredemo.model.entity.User;
import java.util.Set;
import java.util.stream.Collectors;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

public final class ModelMapperUtil {

  private static final ModelMapper modelMapper = new ModelMapper();

  static {
    var typeMap = modelMapper.createTypeMap(User.class, UserDto.class);
    Converter<Set<Role>, Set<RoleName>> rolesConverter =
        context -> context.getSource().stream().map(Role::getName).collect(Collectors.toSet());
    typeMap.addMappings(
        mapper -> mapper.using(rolesConverter).map(User::getRoles, UserDto::setRoles));
  }

  private ModelMapperUtil() {}

  public static <T, U> U map(T source, Class<U> destClass) {
    return modelMapper.map(source, destClass);
  }
}
