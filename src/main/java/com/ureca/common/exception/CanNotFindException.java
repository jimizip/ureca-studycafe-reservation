package com.ureca.common.exception;

// User, Room, 예약 데이터를 찾지 못했을 때
public class CanNotFindException extends RuntimeException {
	public CanNotFindException(String message) {
		super(message);
	}
}
