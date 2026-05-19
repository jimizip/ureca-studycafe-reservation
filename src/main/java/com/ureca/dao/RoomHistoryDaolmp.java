package com.ureca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ureca.dto.Room_history;
import com.ureca.util.DBUtil;

public class RoomHistoryDaolmp implements RoomHistoryDao{
	
	private DBUtil dbutil = DBUtil.getInstance();
	
	@Override
	public List<Room_history> getReservation(int room_id,LocalDateTime date) throws SQLException{
		Connection con = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    List<Room_history> reserves = new ArrayList<>();
	    try {
	        con = dbutil.getConnection();
	        String sql = "SELECT id, room_id, user_id, start_time, end_time, user_count FROM Room_history WHERE room_id = ? AND DATE(start_time) = DATE(?)";
	        stmt = con.prepareStatement(sql);
	        stmt.setInt(1, room_id);
	        stmt.setTimestamp(2, Timestamp.valueOf(date));
	        rs = stmt.executeQuery();
	        while (rs.next()) {
	            Room_history reserve = new Room_history();
	            reserve.setId(rs.getInt("id"));
	            reserve.setRoom_id(rs.getInt("room_id"));
	            reserve.setUser_id(rs.getInt("user_id"));
	            reserve.setStart_time(rs.getTimestamp("start_time").toLocalDateTime());
	            reserve.setEnd_time(rs.getTimestamp("end_time").toLocalDateTime());
	            reserve.setUser_count(rs.getInt("user_count"));
	            reserves.add(reserve);
	        }
	    } finally {
	        dbutil.close(rs, stmt, con);
	    }
	    return reserves;
	};
	
	@Override
	public List<Room_history> getHistory(int user_id) throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Room_history> reserves = new ArrayList<>();
        try {
            con = dbutil.getConnection();
            String sql = "SELECT room_id, user_id, start_time, end_time, user_count from Room_history where user_id = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, user_id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Room_history reserve = new Room_history();
                reserve.setId(rs.getInt("id"));
                reserve.setRoom_id(rs.getInt("room_id"));
                reserve.setUser_id(rs.getInt("user_id"));
                reserve.setStart_time(rs.getTimestamp("start_time").toLocalDateTime());
                reserve.setEnd_time(rs.getTimestamp("end_time").toLocalDateTime());
                reserve.setUser_count(rs.getInt("user_count"));
                reserves.add(reserve);
            }
        } finally {
            dbutil.close(rs, stmt, con);
        }
        return reserves;
	}
	
	@Override
	public void setReserve(Room_history reserve) throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = dbutil.getConnection();
            String sql = " Insert INTO Room_history(id, room_id, user_id, start_time, end_time, user_count) VALUES(?, ?, ?, ?, ?, ?) ";
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setInt(idx++, reserve.getId());
            stmt.setInt(idx++, reserve.getRoom_id());
            stmt.setInt(idx++, reserve.getUser_id());
            stmt.setTimestamp(idx++, Timestamp.valueOf(reserve.getStart_time()));
            stmt.setTimestamp(idx++, Timestamp.valueOf(reserve.getEnd_time()));
            stmt.setInt(idx++, reserve.getUser_count());
            stmt.executeUpdate();
        }
        finally {
        	dbutil.close(stmt, con);
        }
	};
	
	@Override
	public void updateRserve(Room_history reserve) throws SQLException {
	    Connection con = null;
	    PreparedStatement stmt = null;
	    try {
	        con = dbutil.getConnection();
	        String sql = "UPDATE Room_history SET room_id = ?, user_id = ?, start_time = ?, end_time = ?, user_count = ? WHERE id = ?";
	        stmt = con.prepareStatement(sql);
	        int idx = 1;
	        stmt.setInt(idx++, reserve.getRoom_id());
	        stmt.setInt(idx++, reserve.getUser_id());
	        stmt.setTimestamp(idx++, Timestamp.valueOf(reserve.getStart_time()));
	        stmt.setTimestamp(idx++, Timestamp.valueOf(reserve.getEnd_time()));
	        stmt.setInt(idx++, reserve.getUser_count());
	        stmt.setInt(idx++, reserve.getId());
	        stmt.executeUpdate();
	    } finally {
	        dbutil.close(stmt, con);
	    }
	}
	
	@Override
	public void removeReserve(int room_id) throws SQLException {
	    Connection con = null;
	    PreparedStatement stmt = null;
	    try {
	        con = dbutil.getConnection();
	        String sql = "DELETE FROM Room_history WHERE room_id = ?";
	        stmt = con.prepareStatement(sql);
	        stmt.setInt(1, room_id);
	        stmt.executeUpdate();
	    } finally {
	        dbutil.close(stmt, con);
	    }
	}
}
