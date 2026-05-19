package com.ureca.dto;

// 유저, 룸, 예약 등 데이터를 찾지 못했을 때 예외 처리
public class CanNotFindException extends RuntimeException {
	public CanNotFindException() {}
    public CanNotFindException(String msg) {
        super(msg);
    }
}