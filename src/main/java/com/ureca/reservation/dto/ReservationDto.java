package com.ureca.reservation.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

// 예약 (Room_history). id=예약번호, roomId=방, userId=유저, 시작/종료 시간, userCount=인원
public class ReservationDto {
	private int id;
	private int roomId;
	private int userId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime startTime;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime endTime;

	private int userCount;

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public int getRoomId() { return roomId; }
	public void setRoomId(int roomId) { this.roomId = roomId; }

	public int getUserId() { return userId; }
	public void setUserId(int userId) { this.userId = userId; }

	public LocalDateTime getStartTime() { return startTime; }
	public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

	public LocalDateTime getEndTime() { return endTime; }
	public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

	public int getUserCount() { return userCount; }
	public void setUserCount(int userCount) { this.userCount = userCount; }

	@Override
	public String toString() {
		return "ReservationDto [id=" + id + ", roomId=" + roomId + ", userId=" + userId
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", userCount=" + userCount + "]";
	}
}
