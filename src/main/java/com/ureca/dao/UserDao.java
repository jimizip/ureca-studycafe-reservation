package com.ureca.dao;

import java.sql.SQLException;
import java.util.List;
import com.ureca.dto.User;

public interface UserDao {
	// 유저 등록
    void add(User user)           throws SQLException;
    // 유저 삭제
    void remove(int id)           throws SQLException;
    // 유저 조회
    User search(int id)           throws SQLException;
    // 모든 유저 조회
    List<User> searchAll()        throws SQLException;
    // 이메일로 유저 조회 (중복 체크)
    User searchByEmail(String email) throws SQLException;
}