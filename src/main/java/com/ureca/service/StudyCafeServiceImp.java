package com.ureca.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.ureca.dto.*;
import com.ureca.dao.*;
import com.ureca.util.*;

public class StudyCafeServiceImp implements StudyCafeService {

    private UserDao userDao = StudyCafeFactory.getUserDao();
    private PaymentHistoryDao paymentHistoryDao = StudyCafeFactory.getPaymentHistoryDao();
    private RoomDao roomDao = StudyCafeFactory.getRoomDao();                      
    private RoomHistoryDao roomHistoryDao = StudyCafeFactory.getRoomHistoryDao();
    private DBUtil dbutil = DBUtil.getInstance();                                 

    // User
    // 유저 등록
    @Override
    public void addUser(User user) {
    	try {
            // 이메일 중복 체크
            User find = userDao.searchByEmail(user.getEmail());
            if (find != null) {
            	throw new DuplicateException("이미 등록된 이메일입니다.");
            }
            userDao.add(user);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("유저 등록 중 오류 발생");
        }
    }
    
    @Override
    public User searchUser(int id) {
        try {
            User user = userDao.search(id);
            if (user == null) throw new CanNotFindException("존재하지 않는 유저입니다.");
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("유저 조회 중 오류 발생");
        }
    }
    
    @Override
    public User searchUserByEmail(String email) {
        try {
            User user = userDao.searchByEmail(email);
            if (user == null) throw new CanNotFindException("존재하지 않는 이메일입니다.");
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("유저 조회 중 오류 발생");
        }
    }

    // 유저 전체 조회
    @Override
    public List<User> searchAllUsers() {
        try {
            return userDao.searchAll();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("유저 전체 조회 중 오류 발생");
        }
    }
    
    // 유저 삭제
    @Override
    public void removeUser(int id) {
        try {
            userDao.remove(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("유저 삭제 중 오류 발생");
        }
    }

    // 결제
    // 유저별 사용 내역 조회
    @Override
    public List<PaymentHistory> searchPaymentByUser(int userId) {
        try {
            return paymentHistoryDao.searchByUser(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ReservationException("사용 내역 조회 중 오류 발생");
        }
    }
  
    // 예약 수정
    @Override
    public void updateReservation(Room_history history) {
    	Connection con = null;
        try {
            con = dbutil.getConnection();
            con.setAutoCommit(false); // 트랜잭션 시작

            // 중복 예약 체크 (본인 예약 제외)
            
            // 해당 룸의 해당 날짜 예약 목록 가져오기
            List<Room_history> reserves = roomHistoryDao.getReservation(
                history.getRoom_id(), history.getStart_time()
            );
            for (Room_history r : reserves) {
                // 본인 예약은 제외하고 체크
                if (r.getId() == history.getId()) continue;
                // 시간 겹치는지 체크
                if (r.getStart_time().isBefore(history.getEnd_time()) &&
                    r.getEnd_time().isAfter(history.getStart_time())) {
                    throw new DuplicateException("이미 예약된 시간입니다.");
                }
            }

            roomHistoryDao.updateReserve(history.getId(), history, con);

            // 금액 재계산 (시간 * 룸 가격)
            Room room = roomDao.search(history.getRoom_id());
            long hours = java.time.temporal.ChronoUnit.HOURS.between(
                history.getStart_time(), history.getEnd_time()
            );
            
            int price = (int) (room.getPrice() * hours);

            paymentHistoryDao.updateByHistory(con, history.getId(), price);

            con.commit(); // 전부 성공 시 커밋
            
        } catch (DuplicateException e) {
            try { if (con != null) con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            throw e;
        } catch (SQLException e) {
            try { if (con != null) con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            e.printStackTrace();
            throw new ReservationException("예약 수정 중 오류 발생");
        } finally {
            dbutil.close(con);
        }
    }

    // 예약 취소
    @Override
    public void cancel(int historyId) {
        try {
            roomHistoryDao.removeReserve(historyId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ReservationException("예약 취소 중 오류 발생");
        }
    }

    // 유저별 예약 조회
    @Override
    public List<Room_history> searchHistoryByUser(int userId) {
        try {
            // user_id로 RoomHistory 조회
            return roomHistoryDao.getHistory(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ReservationException("예약 조회 중 오류 발생");
        }
    }
    
    // 예약 등록
    public void reserve(Room_history history) {
    	Connection con = null;
    	DBUtil dbutil = DBUtil.getInstance();
    	
    	try {
    		con = dbutil.getConnection();
    		//트랜잭션 시작
    		con.setAutoCommit(false);
    		
    		// RoomHistory INSERT -> 생성된 id 받아오기
            int generatedId = roomHistoryDao.setReserve(history, con);
            history.setId(generatedId); // history에 id 세팅
    		
    		// 금액 재계산 (시간 * 룸 가격)
            Room room = roomDao.search(history.getRoom_id());
            long hours = java.time.temporal.ChronoUnit.HOURS.between(
                history.getStart_time(), history.getEnd_time()
            );
            
            int price = (int) (room.getPrice() * hours);
            // Payment INSERT
            PaymentHistory payment = new PaymentHistory();
            payment.setUserId(history.getUser_id());
            payment.setRoomId(history.getRoom_id());
            payment.setPrice(price);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setRoomHistoryId(history.getId());
            paymentHistoryDao.add(payment, con);

    		//성공 시 커밋
    		con.commit();
    	}
    	catch(SQLException e) {
    		try { 
    			if (con != null) { con.rollback();}
    			} catch(Exception ex) {ex.printStackTrace(); }
    			e.printStackTrace();
    			throw new ReservationException("예약 수정 중 오류 발생");
            }
    	finally {
    		dbutil.close(con);
    	}
    	
    }
	// 룸 조회
    public List<Room> searchAvailableRooms(LocalDateTime start, LocalDateTime end){
    	try {
    		return roomDao.searchAll();
    	}
    	catch(SQLException e) {
    		e.printStackTrace();
            throw new ReservationException("예약할 룸 목록 조회 중 오류 발생");
    	}
    } 	
    
    // 룸, 날짜 기준 예약 가능 시간 조회
    @Override
    public List<Boolean> getBookedHours(int roomId, LocalDateTime date){
    	 try {
    	     List<Boolean> isUsed = new ArrayList<>();
    	     for (int i = 0; i < 24; i++) {
    	         isUsed.add(false);
    	     }
    	     List<Room_history> history = roomHistoryDao.getReservation(roomId, date);

    	     for (Room_history his : history) {
    	         LocalTime start = his.getStart_time().toLocalTime();
    	         LocalTime end = his.getEnd_time().toLocalTime();

    	         for (int i = 0; i < 24; i++) {
    	             LocalTime slotStart = LocalTime.of(i, 0);
    	             LocalTime slotEnd = (i == 23)	? LocalTime.MAX
    	                							: LocalTime.of(i + 1, 0);
    	             if (start.isBefore(slotEnd) &&
    	                 end.isAfter(slotStart)) {
    	            	 //회의실 예약 여부 list return
    	                 isUsed.set(i, true);
    	             }
    	         }
    	     }
    	     return isUsed;
    	    }
    	catch(SQLException e) {
    		e.printStackTrace();
    		throw new ReservationException("예약 가능 시간 조회 중 오류 발생");
    	}
    }
}