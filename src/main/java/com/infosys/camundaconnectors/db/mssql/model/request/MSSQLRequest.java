/*
 * Copyright (c) 2023 Infosys Ltd.
 * Use of this source code is governed by MIT license that can be found in the LICENSE file
 * or at https://opensource.org/licenses/MIT
 */
package com.infosys.camundaconnectors.db.mssql.model.request;

import com.infosys.camundaconnectors.db.mssql.model.response.MSSQLResponse;
import com.infosys.camundaconnectors.db.mssql.utility.DatabaseClient;
import io.camunda.connector.api.annotation.Secret;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MSSQLRequest<T extends MSSQLRequestData> {
  @NotNull @Valid @Secret private DatabaseConnection databaseConnection;
  @NotBlank private String operation;
  @Valid @NotNull private T data;

  public MSSQLResponse invoke(DatabaseClient databaseClient) throws SQLException {
    Connection connection;
    if (operation.equalsIgnoreCase("mssql.create-database"))
      connection = databaseClient.getConnectionObject(databaseConnection, "master");
    else
      connection = databaseClient.getConnectionObject(databaseConnection, data.getDatabaseName());
    return data.invoke(connection);
  }

  public DatabaseConnection getDatabaseConnection() {
    return databaseConnection;
  }

  public void setDatabaseConnection(DatabaseConnection databaseConnection) {
    this.databaseConnection = databaseConnection;
  }

  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MSSQLRequest<?> that = (MSSQLRequest<?>) o;
    return Objects.equals(databaseConnection, that.databaseConnection)
        && Objects.equals(operation, that.operation)
        && Objects.equals(data, that.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(databaseConnection, operation, data);
  }

  @Override
  public String toString() {
    return "MSSQLRequest{"
        + ", databaseConnection="
        + databaseConnection
        + ", operation='"
        + operation
        + '\''
        + ", data="
        + data
        + '}';
  }
}
