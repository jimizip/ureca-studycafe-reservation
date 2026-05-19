package com.ureca.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    static final String URL = "jdbc:mysql://localhost:3306/studycafedb?serverTimezone=UTC&useUniCode=yes&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true";
    static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String ID = "root";
    static final String PW = "1234";

    private static DBUtil instance = new DBUtil();

    private DBUtil() {
        try {
            Class.forName(DRIVER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DBUtil getInstance() {
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, ID, PW);
    }

    public void close(AutoCloseable... acs) {
        for (AutoCloseable c : acs) {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
        }
    }
}