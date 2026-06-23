package com.ureca.reservation.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ureca.reservation.dto.ReservationDto;

@Mapper
public interface ReservationDao {
	// 특정 방의 특정 날짜 예약 목록
	List<ReservationDto> getReservation(@Param("roomId") int roomId, @Param("date") LocalDateTime date);

	// 유저의 다가오는 예약 (start_time > now)
	List<ReservationDto> getHistory(int userId);

	// 예약 등록 (생성 id를 dto.id 에 채움)
	int insertReserve(ReservationDto reservation);

	int updateReserve(ReservationDto reservation);

	int removeReserve(int id);
}
