package com.ureca.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.ureca.dto.PaymentHistory;

public interface PaymentHistoryDao {
    // 결제 내역 등록
	void add(PaymentHistory payment, Connection con) throws SQLException;
    
    // 결제 내역 전체 조회
    List<PaymentHistory> searchAll()              throws SQLException;
    
    // 특정 유저 결제 내역 조회
    List<PaymentHistory> searchByUser(int userId) throws SQLException;
    
    // 결제 취소
    void remove(int historyId, Connection con)                           throws SQLException;
    
    // 예약 수정 시 금액 업데이트 
    void updateByHistory(Connection con, int historyId, int price) throws SQLException;
}