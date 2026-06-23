package com.ureca.reservation.dto;

import java.util.List;

public class ReservationResultDto {
	private String result;  // "success" | "fail"
	private String message;
	private ReservationDto dto;
	private List<ReservationDto> list;
	private List<Boolean> bookedHours; // 24개 슬롯 예약 여부

	public String getResult() { return result; }
	public void setResult(String result) { this.result = result; }

	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }

	public ReservationDto getDto() { return dto; }
	public void setDto(ReservationDto dto) { this.dto = dto; }

	public List<ReservationDto> getList() { return list; }
	public void setList(List<ReservationDto> list) { this.list = list; }

	public List<Boolean> getBookedHours() { return bookedHours; }
	public void setBookedHours(List<Boolean> bookedHours) { this.bookedHours = bookedHours; }
}
