package com.ureca.dao;

import java.sql.SQLException;
import java.util.List;
import com.ureca.dto.Room;

public interface RoomDao {
	
	Room search(int id) throws SQLException;
	List<Room> searchAll() throws SQLException;
	
}
