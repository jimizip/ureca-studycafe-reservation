package com.ureca.room.dto;

// 회의실 정보 (id=방번호, roomSize=수용인원, price=시간당 가격)
public class RoomDto {
	private int id;
	private int roomSize;
	private int price;

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public int getRoomSize() { return roomSize; }
	public void setRoomSize(int roomSize) { this.roomSize = roomSize; }

	public int getPrice() { return price; }
	public void setPrice(int price) { this.price = price; }

	@Override
	public String toString() {
		return "RoomDto [id=" + id + ", roomSize=" + roomSize + ", price=" + price + "]";
	}
}
