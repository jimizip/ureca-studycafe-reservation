package com.ureca.dto;

// 방 예약/수정/취소 중 오류 발생 시 예외
public class ReservationException extends RuntimeException {
    public ReservationException() {}
    public ReservationException(String msg) {
        super(msg);
    }
}