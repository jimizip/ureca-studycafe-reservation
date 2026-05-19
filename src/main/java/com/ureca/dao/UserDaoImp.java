package com.ureca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.ureca.dto.User;
import com.ureca.util.DBUtil;

public class UserDaoImp implements UserDao {
    private DBUtil dbutil = DBUtil.getInstance();

    @Override
    public void add(User user) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = dbutil.getConnection();
            String sql = "INSERT INTO User(name, tel, email) VALUES(?, ?, ?)";
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, user.getName());
            stmt.setString(idx++, user.getTel());
            stmt.setString(idx++, user.getEmail());
            stmt.executeUpdate();
        } finally {
            dbutil.close(stmt, con);
        }
    }

    @Override
    public void remove(int id) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = dbutil.getConnection();
            String sql = "DELETE FROM User WHERE id = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } finally {
            dbutil.close(stmt, con);
        }
    }

    @Override
    public User search(int id) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = dbutil.getConnection();
            String sql = "SELECT id, name, tel, email FROM User WHERE id = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setTel(rs.getString("tel"));
                user.setEmail(rs.getString("email"));
                return user;
            }
        } finally {
            dbutil.close(rs, stmt, con);
        }
        return null;
    }

    @Override
    public List<User> searchAll() throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();
        try {
            con = dbutil.getConnection();
            String sql = "SELECT id, name, tel, email FROM User";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setTel(rs.getString("tel"));
                user.setEmail(rs.getString("email"));
                users.add(user);
            }
        } finally {
            dbutil.close(rs, stmt, con);
        }
        return users;
    }
}