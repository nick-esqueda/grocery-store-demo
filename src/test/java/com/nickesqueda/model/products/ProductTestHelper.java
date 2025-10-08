package com.nickesqueda.model.products;

import com.nickesqueda.model.DaoTestHelper;
import com.nickesqueda.model.product.Product;
import com.nickesqueda.testutils.EntityTestUtils;

public class ProductTestHelper extends DaoTestHelper<Product, String> {

  public ProductTestHelper() {
    super(Product.class, EntityTestUtils.createRandomProduct());
  }

  @Override
  public void runEntityUpdate() {
    entity.setDescription("updated description");
  }

  @Override
  public String getExpectedAssertValue() {
    return entity.getDescription();
  }

  @Override
  public String getActualAssertValue(Product result) {
    return result.getDescription();
  }
}
