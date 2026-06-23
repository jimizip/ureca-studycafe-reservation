package com.ureca.reservation.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ureca.reservation.dto.ReservationDto;
import com.ureca.reservation.dto.ReservationResultDto;
import com.ureca.reservation.service.ReservationService;
import com.ureca.user.dto.UserDto;

import jakarta.servlet.http.HttpSession;

@Controller
@ResponseBody
@RequestMapping("/reservations")
public class ReservationController {

	private final ReservationService reservationService;

	public ReservationController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	// 로그인 유저의 예약 목록
	@GetMapping("/list")
	public ReservationResultDto listReservation(HttpSession session) {
		int userId = loginUserId(session);
		return reservationService.listByUser(userId);
	}

	// 룸/날짜 기준 시간대별 예약 여부 (date: yyyy-MM-dd)
	@GetMapping("/booked-hours")
	public ReservationResultDto bookedHours(@RequestParam int roomId, @RequestParam String date,
			@RequestParam(required = false) Integer excludeId) {
		LocalDateTime dt = LocalDateTime.parse(date + "T00:00:00",
				DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
		return reservationService.getBookedHours(roomId, dt, excludeId);
	}

	@PostMapping("/insert")
	public ReservationResultDto insertReservation(ReservationDto reservationDto, HttpSession session) {
		reservationDto.setUserId(loginUserId(session));
		return reservationService.reserve(reservationDto);
	}

	@PostMapping("/update")
	public ReservationResultDto updateReservation(ReservationDto reservationDto, HttpSession session) {
		reservationDto.setUserId(loginUserId(session));
		return reservationService.updateReservation(reservationDto);
	}

	@PostMapping("/cancel/{id}")
	public ReservationResultDto cancelReservation(@PathVariable int id) {
		return reservationService.cancel(id);
	}

	private int loginUserId(HttpSession session) {
		return ((UserDto) session.getAttribute("userDto")).getId();
	}
}
