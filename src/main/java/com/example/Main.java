package com.example;

import java.sql.*;

public class Main {

    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from animal");

        while (resultSet.next()) {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            System.out.println("Dane psa: " + id + " " + name + " " + age);
        }

        resultSet.close();
        statement.close();
        connection.close();
    }
}