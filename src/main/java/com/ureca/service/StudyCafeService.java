package com.ureca.service;

import java.util.List;
import java.time.LocalDateTime;
import com.ureca.dto.*;

public interface StudyCafeService {

    // User
    void addUser(User user);                    // 유저 등록
    List<User> searchAllUsers();                // 유저 전체 조회
    User searchUser(int id);

    // 예약 - 현빈
    void reserve(Room_history history); // 예약 등록
    List<Room> searchAvailableRooms(LocalDateTime start, LocalDateTime end); // 날짜 기준 예약 가능 룸 조회
    List<Boolean> getBookedHours(int roomId, LocalDateTime date); // 룸, 날짜 기준 예약 가능 시간 조회

    // 예약 - 지민
//    void updateReservation(Room_history history); // 예약 수정
//    void cancel(int historyId); // 예약 취소
//    List<Room_history> searchHistoryByUser(int userId); // 유저별 예약 조회 
//    List<Room_history> searchAllHistory(); // 전체 예약 조회

    // 결제
    List<PaymentHistory> searchPaymentByUser(int userId); // 유저별 결제 내역 조회
}