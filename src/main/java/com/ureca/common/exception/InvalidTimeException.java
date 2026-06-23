package com.ureca.common.exception;

// 시작 > 종료 등 잘못된 시간 범위
public class InvalidTimeException extends RuntimeException {
	public InvalidTimeException(String message) {
		super(message);
	}
}
