package com.ureca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.ureca.dto.PaymentHistory;
import com.ureca.util.DBUtil;

public class PaymentHistoryDaoImp implements PaymentHistoryDao {
    private DBUtil dbutil = DBUtil.getInstance();

    // 결제 내역 등록
    @Override
    public void add(PaymentHistory payment) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = dbutil.getConnection();
            String sql = "INSERT INTO payment_history(user_id, room_id, price, payment_date) VALUES(?, ?, ?, ?)";
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setInt(idx++, payment.getUserId());
            stmt.setInt(idx++, payment.getRoomId());
            stmt.setInt(idx++, payment.getPrice());
            stmt.setObject(idx++, payment.getPaymentDate());
            stmt.executeUpdate();
        } finally {
            dbutil.close(stmt, con);
        }
    }

    // 결제 내역 전체 조회
    @Override
    public List<PaymentHistory> searchAll() throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<PaymentHistory> list = new ArrayList<>();
        try {
            con = dbutil.getConnection();
            String sql = "SELECT id, user_id, room_id, price, payment_date FROM payment_history";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                PaymentHistory p = new PaymentHistory();
                p.setId(rs.getInt("id"));
                p.setUserId(rs.getInt("user_id"));
                p.setRoomId(rs.getInt("room_id"));
                p.setPrice(rs.getInt("price"));
                p.setPaymentDate(rs.getObject("payment_date", java.time.LocalDateTime.class));
                list.add(p);
            }
        } finally {
            dbutil.close(rs, stmt, con);
        }
        return list;
    }

    // 특정 유저 결제 내역 조회
    @Override
    public List<PaymentHistory> searchByUser(int userId) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<PaymentHistory> list = new ArrayList<>();
        try {
            con = dbutil.getConnection();
            String sql = "SELECT id, user_id, room_id, price, payment_date FROM payment_history WHERE user_id = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                PaymentHistory p = new PaymentHistory();
                p.setId(rs.getInt("id"));
                p.setUserId(rs.getInt("user_id"));
                p.setRoomId(rs.getInt("room_id"));
                p.setPrice(rs.getInt("price"));
                p.setPaymentDate(rs.getObject("payment_date", java.time.LocalDateTime.class));
                list.add(p);
            }
        } finally {
            dbutil.close(rs, stmt, con);
        }
        return list;
    }

    // 결제 취소 (삭제)
    @Override
    public void remove(int id) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = dbutil.getConnection();
            String sql = "DELETE FROM payment_history WHERE id = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } finally {
            dbutil.close(stmt, con);
        }
    }
}