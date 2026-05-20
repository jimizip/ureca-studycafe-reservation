package com.ureca.dao;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.sql.Connection;
import com.ureca.dto.Room_history;
public interface RoomHistoryDao {
	
	List<Room_history> getReservation(int room_id,LocalDateTime date) throws SQLException;
	
	List<Room_history> getHistory(int user_id) throws SQLException;
	
	void setReserve(Room_history reserve) throws SQLException;

	void updateReserve(int id, Room_history reserve, Connection con) throws SQLException;
	
	void removeReserve(int id) throws SQLException;
}
