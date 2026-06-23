package com.ureca.user.service;

import java.util.Optional;

import com.ureca.user.dto.UserDto;
import com.ureca.user.dto.UserResultDto;

public interface UserService {
	UserResultDto addUser(UserDto userDto);     // 회원가입 (이메일 중복 체크)
	UserResultDto listUsers();
	UserResultDto removeUser(int id);
	Optional<UserDto> login(UserDto userDto);   // email + password 검증
}
