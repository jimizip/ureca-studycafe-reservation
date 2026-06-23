package com.ureca.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ureca.common.exception.DuplicateException;
import com.ureca.user.dao.UserDao;
import com.ureca.user.dto.UserDto;
import com.ureca.user.dto.UserResultDto;

@Service
public class UserServiceImpl implements UserService {

	private final UserDao userDao;

	public UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public UserResultDto addUser(UserDto userDto) {
		UserResultDto resultDto = new UserResultDto();
		try {
			// 이메일 중복 체크
			UserDto find = userDao.searchByEmail(userDto.getEmail());
			if (find != null) {
				throw new DuplicateException("이미 등록된 이메일입니다.");
			}
			userDao.add(userDto);
			resultDto.setResult("success");
		} catch (DuplicateException e) {
			resultDto.setResult("fail");
			resultDto.setMessage(e.getMessage());
		} catch (Exception e) {
			resultDto.setResult("fail");
			resultDto.setMessage("유저 등록 중 오류 발생");
		}
		return resultDto;
	}

	@Override
	public UserResultDto listUsers() {
		UserResultDto resultDto = new UserResultDto();
		List<UserDto> list = userDao.searchAll();
		resultDto.setList(list);
		resultDto.setResult("success");
		return resultDto;
	}

	@Override
	public UserResultDto removeUser(int id) {
		UserResultDto resultDto = new UserResultDto();
		userDao.remove(id);
		resultDto.setResult("success");
		return resultDto;
	}

	@Override
	public Optional<UserDto> login(UserDto userDto) {
		UserDto find = userDao.searchByEmail(userDto.getEmail());
		if (find != null && find.getPassword() != null
				&& find.getPassword().equals(userDto.getPassword())) {
			return Optional.of(find);
		}
		return Optional.empty();
	}
}
