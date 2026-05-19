package com.ureca.dto;

import java.time.LocalDateTime;

// 결제 내역 Dto
public class PaymentHistory {
	private int id;
    private int userId; // User FK
    private int roomId; // Room FK
    private int price; // 결제 금액 
    private LocalDateTime paymentDate; // 결제 시간 
    
    public PaymentHistory() {}

    public PaymentHistory(int id, int userId, int roomId, int price, LocalDateTime paymentDate) {
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.price = price;
        this.paymentDate = paymentDate;
    }

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

    @Override
    public String toString() {
        return "id=" + id + ", userId=" + userId + ", roomId=" + roomId + 
               ", price=" + price + ", paymentDate=" + paymentDate;
    }

}
