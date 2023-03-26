package com.Util;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Component
public class Util {
    private static final String hostName = "localhost";
    private static final String port = "3306";
    private static final String dataBaseName = "userdb";
    private static final String userName = "root";
    private static final String password = "root";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + hostName + ":" + port + "/" + dataBaseName + "?serverTimezone=UTC";
            connection = DriverManager.getConnection(url, userName, password);
            if (!connection.isClosed()) {
                System.out.println("Connected complete with database: " + dataBaseName);
            } else {
                System.out.println("Connection failed");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }



    public static EntityManagerFactory getEntityManagerFactory() {

        return null;
    }





}
