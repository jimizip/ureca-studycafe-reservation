package com.ureca.dto;

// 중복 데이터 등록 시 (유저 이메일 중복, 예약 시간 중복 등) 예외 처리
public class DuplicateException extends RuntimeException {
    public DuplicateException() {}
    public DuplicateException(String msg) {
        super(msg);
    }
}