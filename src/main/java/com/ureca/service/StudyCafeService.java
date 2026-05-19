package com.ureca.service;

import java.util.List;
import com.ureca.dto.User;
import com.ureca.dto.PaymentHistory;
// import com.ureca.dto.RoomHistory;
// import com.ureca.dto.Room;

public interface StudyCafeService {

    // User
    void addUser(User user);                    // 유저 등록
    List<User> searchAllUsers();                // 유저 전체 조회
    User searchUser(int id);

    // 나중에 추가 - 예약 예시
    // void reserve(RoomHistory history);          // 예약 등록 
    // void updateReservation(RoomHistory history);// 예약 수정
    // void cancel(int historyId);                 // 예약 취소
    // List<RoomHistory> searchAllHistory();       // 전체 예약 조회
    // List<RoomHistory> searchHistoryByUser(int userId); // 유저별 예약 조회
    // List<Room> searchAvailableRooms(java.time.LocalDateTime start, java.time.LocalDateTime end); // 예약 가능 룸 조회

    // 결제
    List<PaymentHistory> searchPaymentByUser(int userId); // 유저별 결제 내역 조회
}