package com.nickesqueda.testutils;

/**
 * Holds SQL query strings used for unit tests in a centralized location.
 *
 * <p>Change these query strings based on the SQL dialect of the underlying database.
 *
 * <p>CURRENT DIALECT: MySQL
 */
public final class SqlTestQuery {

  public static final String SELECT_ALL_TABLES =
      """
      SELECT table_name
      FROM information_schema.tables
      WHERE table_schema = DATABASE()
      AND table_name NOT IN ('flyway_schema_history');
      """;

  public static final String DISABLE_FOREIGN_KEY_CHECKS = "SET FOREIGN_KEY_CHECKS = 0;";

  public static final String ENABLE_FOREIGN_KEY_CHECKS = "SET FOREIGN_KEY_CHECKS = 1;";

  public static final String STR_FMT_TRUNCATE_TABLE = "TRUNCATE TABLE %s;";
}
