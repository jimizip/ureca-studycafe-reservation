package com.ureca.room.service;

import org.springframework.stereotype.Service;

import com.ureca.room.dao.RoomDao;
import com.ureca.room.dto.RoomResultDto;

@Service
public class RoomServiceImpl implements RoomService {

	private final RoomDao roomDao;

	public RoomServiceImpl(RoomDao roomDao) {
		this.roomDao = roomDao;
	}

	@Override
	public RoomResultDto listRooms() {
		RoomResultDto resultDto = new RoomResultDto();
		resultDto.setList(roomDao.searchAll());
		resultDto.setResult("success");
		return resultDto;
	}

	@Override
	public RoomResultDto searchAvailableRooms() {
		// 기존 로직 보존: 전체 룸 반환
		return listRooms();
	}
}
