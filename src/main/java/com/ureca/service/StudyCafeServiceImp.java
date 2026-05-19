package com.ureca.service;

import java.sql.SQLException;
import java.util.List;

import com.ureca.dao.UserDao;
import com.ureca.dao.PaymentHistoryDao;
import com.ureca.dto.User;
import com.ureca.dto.PaymentHistory;
import com.ureca.util.StudyCafeFactory;

public class StudyCafeServiceImp implements StudyCafeService {

    private UserDao userDao = StudyCafeFactory.getUserDao();
    private PaymentHistoryDao paymentHistoryDao = StudyCafeFactory.getPaymentHistoryDao();

    // User
    // 유저 등록
    @Override
    public void addUser(User user) {
    	try {
            // 이메일 중복 체크
            User find = userDao.searchByEmail(user.getEmail());
            if (find != null) {
                throw new RuntimeException("이미 등록된 이메일입니다.");
            }
            userDao.add(user);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("유저 등록 중 오류 발생");
        }
    }

    // 유저 전체 조회
    @Override
    public List<User> searchAllUsers() {
        try {
            return userDao.searchAll();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("유저 전체 조회 중 오류 발생");
        }
    }

    // 결제
    // 유저별 사용 내역 조회
    @Override
    public List<PaymentHistory> searchPaymentByUser(int userId) {
        try {
            return paymentHistoryDao.searchByUser(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("사용 내역 조회 중 오류 발생");
        }
    }
}