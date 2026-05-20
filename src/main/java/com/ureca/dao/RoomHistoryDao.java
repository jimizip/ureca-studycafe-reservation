package com.ureca.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.sql.Connection;
import com.ureca.dto.Room_history;
public interface RoomHistoryDao {
	
	//해당 room의 예약 정보 조회
	List<Room_history> getReservation(int room_id,LocalDateTime date) throws SQLException;
	//해당 유저의 예약 정보 조회
	List<Room_history> getHistory(int user_id) throws SQLException;
	//예약 설정
    int setReserve(Room_history reserve, Connection con) throws SQLException;
    //예약 수정
	void updateReserve(int id, Room_history reserve, Connection con) throws SQLException;
	//예약 삭제
	void removeReserve(int id) throws SQLException;
}
