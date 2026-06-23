package com.ureca.common.exception;

// 이메일 중복 등록, 시간대 중복 예약 시
public class DuplicateException extends RuntimeException {
	public DuplicateException(String message) {
		super(message);
	}
}
