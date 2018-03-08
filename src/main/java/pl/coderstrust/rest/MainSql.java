package pl.coderstrust.rest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainSql {

  public static void main(String[] argv) throws SQLException, ClassNotFoundException {

    Class.forName("org.postgresql.Driver");

    Connection connection = null;

    connection = DriverManager
        .getConnection("jdbc:postgresql://127.0.0.1:5432/accountingsystem", "postgres", "");


  }

}
