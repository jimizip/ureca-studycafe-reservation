package com.ureca.dao;

import java.sql.SQLException;
import java.util.List;
import com.ureca.dto.User;

public interface UserDao {
    void add(User user)           throws SQLException;
    void remove(int id)           throws SQLException;
    User search(int id)           throws SQLException;
    List<User> searchAll()        throws SQLException;
}