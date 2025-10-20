package com.nickesqueda.grocerystoredemo.service;

import com.nickesqueda.grocerystoredemo.dto.StoreDto;
import com.nickesqueda.grocerystoredemo.model.dao.Dao;
import com.nickesqueda.grocerystoredemo.model.entity.Store;
import com.nickesqueda.grocerystoredemo.security.AuthValidator;
import com.nickesqueda.grocerystoredemo.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StoreService {

  private final Dao<Store> storeDao;

  public StoreDto getStoreDetails(Integer id) {
    Store store = storeDao.findOneByValue("id", id);
    return ModelMapperUtil.map(store, StoreDto.class);
  }

  public StoreDto createStore(StoreDto storeDto) {
    AuthValidator.requireAdminRole();

    Store store = ModelMapperUtil.map(storeDto, Store.class);
    storeDao.save(store);

    return ModelMapperUtil.map(store, StoreDto.class);
  }

  public void updateStoreDetails(StoreDto storeDto) {
    AuthValidator.requireAdminRole();

    Store store = storeDao.findOneByValue("id", storeDto.getId());
    ModelMapperUtil.map(storeDto, store);
    storeDao.update(store);
  }

  public void deleteStore(Integer id) {
    AuthValidator.requireAdminRole();

    Store store = storeDao.findOneByValue("id", id);
    storeDao.delete(store);
  }
}
