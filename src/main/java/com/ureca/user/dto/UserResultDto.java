package com.ureca.user.dto;

import java.util.List;

// 표준 응답 DTO
public class UserResultDto {
	private String result; // "success" | "fail"
	private String message;
	private UserDto dto;
	private List<UserDto> list;

	public String getResult() { return result; }
	public void setResult(String result) { this.result = result; }

	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }

	public UserDto getDto() { return dto; }
	public void setDto(UserDto dto) { this.dto = dto; }

	public List<UserDto> getList() { return list; }
	public void setList(List<UserDto> list) { this.list = list; }
}
