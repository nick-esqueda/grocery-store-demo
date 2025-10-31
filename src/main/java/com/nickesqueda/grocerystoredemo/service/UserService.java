package com.nickesqueda.grocerystoredemo.service;

import com.nickesqueda.grocerystoredemo.dto.UserDto;
import com.nickesqueda.grocerystoredemo.model.dao.Dao;
import com.nickesqueda.grocerystoredemo.model.entity.User;
import com.nickesqueda.grocerystoredemo.security.AuthValidator;
import com.nickesqueda.grocerystoredemo.security.SessionContext;
import com.nickesqueda.grocerystoredemo.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {

  private final Dao<User> userDao;
  private final AuthService authService;

  public User getUserEntity(String username) {
    return userDao.findOneByValue("username", username);
  }

  public void updateUserData(UserDto userDto) {
    AuthValidator.requireSubjectSessionMatch(userDto.getId());

    Integer userId = SessionContext.getSessionUser().getId();
    User user = userDao.findOneByValue("id", userId);

    ModelMapperUtil.map(userDto, user);
    userDao.update(user);

    SessionContext.updateSessionContext(userDto);
  }

  public void deleteCurrentAccount() {
    AuthValidator.requireActiveSession();

    Integer userId = SessionContext.getSessionUser().getId();
    User user = userDao.findOneByValue("id", userId);

    userDao.delete(user);
    authService.logOut();
  }
}
