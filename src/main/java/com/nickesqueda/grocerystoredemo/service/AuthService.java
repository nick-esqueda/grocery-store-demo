package com.nickesqueda.grocerystoredemo.service;

import com.nickesqueda.grocerystoredemo.dto.UserCredentialsDto;
import com.nickesqueda.grocerystoredemo.dto.UserDto;
import com.nickesqueda.grocerystoredemo.exception.EntityNotSavedException;
import com.nickesqueda.grocerystoredemo.exception.PasswordMismatchException;
import com.nickesqueda.grocerystoredemo.model.dao.Dao;
import com.nickesqueda.grocerystoredemo.model.dao.ReadOnlyDao;
import com.nickesqueda.grocerystoredemo.model.entity.Role;
import com.nickesqueda.grocerystoredemo.model.entity.RoleName;
import com.nickesqueda.grocerystoredemo.model.entity.User;
import com.nickesqueda.grocerystoredemo.security.PasswordHasher;
import com.nickesqueda.grocerystoredemo.security.SessionContext;
import com.nickesqueda.grocerystoredemo.util.ModelMapperUtil;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthService {

  private final ReadOnlyDao<Role> roleDao;
  private final Dao<User> userDao;

  public void registerUser(UserDto userDto, String rawPassword) {
    Role customerRole = roleDao.findOneByValue("name", RoleName.ROLE_CUSTOMER);
    User user = ModelMapperUtil.map(userDto, User.class);
    user.setRoles(Set.of(customerRole));
    user.setPassword(PasswordHasher.hash(rawPassword));

    userDao.save(user);

    UserDto newUserDto = ModelMapperUtil.map(user, UserDto.class);
    SessionContext.setSessionContext(newUserDto);
  }

  public void authenticateUser(UserCredentialsDto userCredentials) {
    String username = userCredentials.getUsername();
    String rawPassword = userCredentials.getRawPassword();

    User user = userDao.findOneByValue("username", username);

    if (PasswordHasher.compare(rawPassword, user.getPassword())) {
      UserDto userDto = ModelMapperUtil.map(user, UserDto.class);
      SessionContext.setSessionContext(userDto);
    } else {
      throw new PasswordMismatchException();
    }
  }

  public void logOut() {
    SessionContext.clearSession();
  }
}
