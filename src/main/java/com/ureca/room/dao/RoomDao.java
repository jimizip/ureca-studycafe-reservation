package com.ureca.room.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ureca.room.dto.RoomDto;

@Mapper
public interface RoomDao {
	RoomDto search(int id);
	List<RoomDto> searchAll();
}
