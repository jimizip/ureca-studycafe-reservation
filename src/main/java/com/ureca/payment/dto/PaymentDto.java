package com.ureca.payment.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

// 결제 내역
public class PaymentDto {
	private int id;
	private int userId;
	private int roomId;
	private int price;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime paymentDate;

	private int roomHistoryId;

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public int getUserId() { return userId; }
	public void setUserId(int userId) { this.userId = userId; }

	public int getRoomId() { return roomId; }
	public void setRoomId(int roomId) { this.roomId = roomId; }

	public int getPrice() { return price; }
	public void setPrice(int price) { this.price = price; }

	public LocalDateTime getPaymentDate() { return paymentDate; }
	public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

	public int getRoomHistoryId() { return roomHistoryId; }
	public void setRoomHistoryId(int roomHistoryId) { this.roomHistoryId = roomHistoryId; }

	@Override
	public String toString() {
		return "PaymentDto [id=" + id + ", userId=" + userId + ", roomId=" + roomId
				+ ", price=" + price + ", paymentDate=" + paymentDate + ", roomHistoryId=" + roomHistoryId + "]";
	}
}
