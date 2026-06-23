package com.ureca.common.exception;

// 예약/수정/취소 처리 오류
public class ReservationException extends RuntimeException {
	public ReservationException(String message) {
		super(message);
	}
}
