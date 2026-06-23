package com.ureca.room.dto;

import java.util.List;

public class RoomResultDto {
	private String result;
	private RoomDto dto;
	private List<RoomDto> list;

	public String getResult() { return result; }
	public void setResult(String result) { this.result = result; }

	public RoomDto getDto() { return dto; }
	public void setDto(RoomDto dto) { this.dto = dto; }

	public List<RoomDto> getList() { return list; }
	public void setList(List<RoomDto> list) { this.list = list; }
}
