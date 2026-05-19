package com.ureca.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Room_history {
	/**
	 * id : room_history 번호
	 * room_id : 예약된 방 번호
	 * user_id : 예약한 유저 이름
	 * start_time : 예약 시작 시간
	 * end_time : 예약 끝 시간
	 * user_count : 예약 인원
	 */
	private int id;
	private int room_id;
	private int user_id;
	private LocalDateTime start_time;
	private LocalDateTime end_time;
	private int user_count;
	
	public Room_history() {}
	public Room_history(int id, int room_id, int user_id, LocalDateTime start_time, LocalDateTime end_time, int user_count) {
		this.id = id;
		this.room_id = room_id;
		this.user_id = user_id;
		this.start_time = start_time;
		this.end_time = end_time;
		this.user_count = user_count;
	}
	
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return this.id;
	}
	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}
	public int getRoom_id() {
		return this.room_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getUser_id(int user_id) {
		return this.user_id;
	}
	public void setStart_time(LocalDateTime start_time) {
		this.start_time = start_time;
	}
	public LocalDateTime getStart_time() {
		return this.start_time;
	}
	public void setEnd_time(LocalDateTime end_time) {
		this.end_time = end_time;
	}
	public LocalDateTime getEnd_time() {
		return this.end_time;
	}
	public void setUser_count(int user_count) {
		this.user_count = user_count;
	}
	public int getUser_count() {
		return this.user_count;
	}
	
	@Override
	public String toString() {
		return "Room_history [id=" + id + ", room_id=" + room_id + ", user_id=" + user_id + ", start_time=" + start_time
				+ ", end_time=" + end_time + ", user_count=" + user_count + "]";
	}
	
}
