package com.ureca.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ureca.user.dto.UserDto;

@Mapper
public interface UserDao {
	int add(UserDto userDto);
	int remove(int id);
	UserDto search(int id);
	List<UserDto> searchAll();
	UserDto searchByEmail(String email);
}
