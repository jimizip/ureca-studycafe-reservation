package com.ureca.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ureca.user.dto.UserDto;
import com.ureca.user.dto.UserResultDto;
import com.ureca.user.service.UserService;

@Controller
@ResponseBody
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	// 회원가입 (Interceptor 제외 경로)
	@PostMapping("/insert")
	public UserResultDto insertUser(UserDto userDto) {
		return userService.addUser(userDto);
	}

	@GetMapping("/list")
	public UserResultDto listUser() {
		return userService.listUsers();
	}

	@PostMapping("/delete/{id}")
	public UserResultDto deleteUser(@PathVariable int id) {
		return userService.removeUser(id);
	}
}
