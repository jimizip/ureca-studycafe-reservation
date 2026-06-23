package com.ureca.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

// JSP forward 전용 (뷰 이름 반환)
@Controller
@RequestMapping("/pages")
public class PageController {

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@GetMapping("/main")
	public String main() {
		return "main";
	}

	@GetMapping("/reservation")
	public String reservation() {
		return "reservation";
	}

	@GetMapping("/payment")
	public String payment() {
		return "payment";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "login";
	}
}
