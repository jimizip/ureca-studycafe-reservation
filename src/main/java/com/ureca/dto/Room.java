package com.ureca.dto;

import java.io.Serializable;

public class Room implements Comparable<Room>, Serializable{
	/**
	 * id : 회의실 번호
	 * room_size : 회의실 인원 수
	 * price : 해당 회의실 시간 당 가격
	 */
	
	private int id;
	private int room_size;
	private int price;
	
	public Room() {}
	public Room(int id, int room_size, int price) {
		this.id = id;
		this.room_size = room_size;
		this.price = price;
	}
	
	@Override
	public int compareTo(Room o) {
		return Integer.compare(this.id, o.id);
	}
	
	
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return this.id;
	}
	public void setRoom_size(int room_size) {
		this.room_size = room_size;
	}
	public int getRoom_size(int room_size) {
		return this.room_size;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getPrice() {
		return this.price;
	}
	
	@Override
	public String toString() {
		return "Room [id=" + id + ", room_size=" + room_size + ", price=" + price + "]";
	}
	
}