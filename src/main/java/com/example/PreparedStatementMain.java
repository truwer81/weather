package com.example;

import java.sql.*;

public class PreparedStatementMain {

    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
        PreparedStatement preparedStatement = connection.prepareStatement("select * from animal where name = ?");
        preparedStatement.setString(1, "Benio");
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            System.out.println("Dane psa: " + id + " " + name + " " + age);
        }

        resultSet.close();
        preparedStatement.close();
        connection.close();
    }
}