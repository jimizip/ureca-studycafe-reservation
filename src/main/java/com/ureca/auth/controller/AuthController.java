package com.ureca.auth.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ureca.user.dto.UserDto;
import com.ureca.user.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@ResponseBody
@RequestMapping("/auth")
public class AuthController {

	private final UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

	// email + password 검증 → 세션 저장
	@PostMapping("/login")
	public Map<String, String> login(UserDto userDto, HttpSession session) {
		Map<String, String> map = new HashMap<>();
		Optional<UserDto> optional = userService.login(userDto);
		optional.ifPresentOrElse(
			found -> {
				found.setPassword(null); // 세션에 비밀번호 노출 방지
				session.setAttribute("userDto", found);
				map.put("result", "success");
			},
			() -> map.put("result", "fail")
		);
		return map;
	}

	@PostMapping("/logout")
	public Map<String, String> logout(HttpSession session) {
		session.invalidate();
		Map<String, String> map = new HashMap<>();
		map.put("result", "success");
		return map;
	}
}
