package com.ureca.room.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ureca.room.dto.RoomResultDto;
import com.ureca.room.service.RoomService;

@Controller
@ResponseBody
@RequestMapping("/rooms")
public class RoomController {

	private final RoomService roomService;

	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}

	@GetMapping("/list")
	public RoomResultDto listRoom() {
		return roomService.listRooms();
	}

	@GetMapping("/available")
	public RoomResultDto availableRoom() {
		return roomService.searchAvailableRooms();
	}
}
