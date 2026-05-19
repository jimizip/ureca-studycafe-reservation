package com.ureca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ureca.dto.Room;
import com.ureca.util.DBUtil;

public class RoomDaolmp implements RoomDao {
    private DBUtil dbutil = DBUtil.getInstance();
    
    @Override
    public Room search(int id) throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = dbutil.getConnection();
            String sql = "SELECT id, room_size, price FROM Room WHERE id = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                 Room room = new Room();
                room.setId(rs.getInt("id"));
                room.setRoom_size(rs.getInt("room_size"));
                room.setPrice(rs.getInt("price"));
                return room;
            }
        } finally {
            dbutil.close(rs, stmt, con);
        }
        return null;
    }
    
    @Override
    public List<Room> searchAll() throws SQLException{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Room> rooms = new ArrayList<>();
        try {
            con = dbutil.getConnection();
            String sql = "SELECT id, room_id, price FROM Room";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt("id"));
                room.setRoom_size(rs.getInt("room_size"));
                room.setPrice(rs.getInt("price"));
                rooms.add(room);
            }
        } finally {
            dbutil.close(rs, stmt, con);
        }
        return rooms;
    }
    
}
