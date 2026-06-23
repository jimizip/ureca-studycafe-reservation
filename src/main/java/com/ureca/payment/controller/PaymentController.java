package com.ureca.payment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ureca.payment.dto.PaymentResultDto;
import com.ureca.payment.service.PaymentService;
import com.ureca.user.dto.UserDto;

import jakarta.servlet.http.HttpSession;

@Controller
@ResponseBody
@RequestMapping("/payments")
public class PaymentController {

	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	// 로그인 유저의 결제 내역
	@GetMapping("/list")
	public PaymentResultDto listPayment(HttpSession session) {
		int userId = ((UserDto) session.getAttribute("userDto")).getId();
		return paymentService.listByUser(userId);
	}
}
