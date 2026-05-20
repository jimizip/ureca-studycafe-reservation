package com.ureca.dao;

import java.sql.SQLException;
import java.util.List;
import com.ureca.dto.Room;

public interface RoomDao {
	//해당 id에 해당하는 room 조회
	Room search(int id) throws SQLException;
	//모든 room 조회
	List<Room> searchAll() throws SQLException;
	
}
