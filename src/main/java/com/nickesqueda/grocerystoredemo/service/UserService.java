package com.nickesqueda.grocerystoredemo.service;

import com.nickesqueda.grocerystoredemo.dto.UserDto;
import com.nickesqueda.grocerystoredemo.exception.UnauthenticatedException;
import com.nickesqueda.grocerystoredemo.exception.UnauthorizedException;
import com.nickesqueda.grocerystoredemo.model.dao.Dao;
import com.nickesqueda.grocerystoredemo.model.entity.User;
import com.nickesqueda.grocerystoredemo.security.SessionContext;
import com.nickesqueda.grocerystoredemo.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {

  private final Dao<User> userDao;
  private final AuthService authService;

  public void updateUserData(UserDto userDto) {
    if (!SessionContext.isSessionActive()) {
      throw new UnauthenticatedException();
    }

    Integer subjectId = userDto.getId();
    Integer sessionUserId = SessionContext.getSessionUser().getId();

    if (!subjectId.equals(sessionUserId)) {
      throw new UnauthorizedException(subjectId, sessionUserId);
    }

    User user = userDao.findOneByValue("id", sessionUserId);

    ModelMapperUtil.map(userDto, user);
    userDao.update(user);

    SessionContext.updateSessionContext(userDto);
  }

  public void deleteAccount() {
    if (!SessionContext.isSessionActive()) {
      throw new UnauthenticatedException();
    }

    Integer userId = SessionContext.getSessionUser().getId();
    User user = userDao.findOneByValue("id", userId);

    userDao.delete(user);
    authService.logOut();
  }
}
