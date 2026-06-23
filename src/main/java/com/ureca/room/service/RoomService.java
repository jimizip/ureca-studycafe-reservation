package com.ureca.room.service;

import com.ureca.room.dto.RoomResultDto;

public interface RoomService {
	RoomResultDto listRooms();
	// 시간대 기반 가용 룸 조회 (현재 전체 룸 반환 - 슬롯 가용여부는 reservation/booked-hours 사용)
	RoomResultDto searchAvailableRooms();
}
