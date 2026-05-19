package com.ureca.dto;

// 잘못된 예약 시간 (종료 시간이 시작 시간보다 앞일 때 등)
public class InvalidTimeException extends RuntimeException {
    public InvalidTimeException() {}
    public InvalidTimeException(String msg) {
        super(msg);
    }
}