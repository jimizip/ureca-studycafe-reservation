package com.ureca.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import com.ureca.dto.Room_history;
public interface RoomHistoryDao {
	
	List<Room_history> getReservation(Connection con, int room_id,LocalDateTime date) throws SQLException;
	
	List<Room_history> getHistory(int user_id) throws SQLException;
	
	void setReserve(Room_history reserve) throws SQLException;

	void updateRserve(int room_id, Room_history reserve) throws SQLException;
	
	void removeReserve(int room_id) throws SQLException;
}
