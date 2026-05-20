package com.ureca.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.ureca.dao.UserDao;
import com.ureca.dao.PaymentHistoryDao;
import com.ureca.dao.RoomDao;
import com.ureca.dao.RoomHistoryDao;
import com.ureca.util.DBUtil;
import com.ureca.util.StudyCafeFactory;
import com.ureca.dto.*;

public class StudyCafeServiceImp implements StudyCafeService {

    private UserDao userDao = StudyCafeFactory.getUserDao();
    private PaymentHistoryDao paymentHistoryDao = StudyCafeFactory.getPaymentHistoryDao();
    private RoomDao roomDao = StudyCafeFactory.getRoomDao();
    private RoomHistoryDao roomHistoryDao = StudyCafeFactory.getRoomHistoryDao();

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

    // 결제
    // 유저별 사용 내역 조회
    @Override
    public List<PaymentHistory> searchPaymentByUser(int userId) {
        try {
            return paymentHistoryDao.searchByUser(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("사용 내역 조회 중 오류 발생");
        }
    }
    
    // 예약 등록
    public void reserve(Room_history history) {
    	try { 
    		roomHistoryDao.setReserve(history);
    	}
    	catch(SQLException e) {
    		e.printStackTrace();
            throw new RuntimeException("룸 예약 중 오류 발생" + history.toString());
    	}
    	
    }
	// 룸 조회
    public List<Room> searchAvailableRooms(LocalDateTime start, LocalDateTime end){
    	try {
    		return roomDao.searchAll();
    	}
    	catch(SQLException e) {
    		e.printStackTrace();
            throw new RuntimeException("예약할 룸 목록 조회 중 오류 발생");
    	}
    } 	
    
    // 룸, 날짜 기준 예약 가능 시간 조회
    @Override
    public List<Boolean> getBookedHours(int roomId, LocalDateTime date){	
    	Connection con = null;
    	DBUtil dbutil = DBUtil.getInstance();
    	 try {
    		 
    		 con = dbutil.getConnection();
    	     List<Boolean> isUsed = new ArrayList<>();
    	     con.setAutoCommit(false);

    	     for (int i = 0; i < 24; i++) {
    	         isUsed.add(false);
    	     }

    	     List<Room_history> history =
    	             roomHistoryDao.getReservation(con, roomId, date);

    	     for (Room_history his : history) {

    	         LocalTime start = his.getStart_time().toLocalTime();
    	         LocalTime end = his.getEnd_time().toLocalTime();

    	         for (int i = 0; i < 24; i++) {

    	             LocalTime slotStart = LocalTime.of(i, 0);
    	             LocalTime slotEnd = (i == 23)	? LocalTime.MAX
    	                								: LocalTime.of(i + 1, 0);

    	             if (start.isBefore(slotEnd) &&
    	                 end.isAfter(slotStart)) {
    	                 isUsed.set(i, true);
    	             }
    	         }
    	     }
    	     con.commit();
    	     return isUsed;

    	    }
    	
    	catch(SQLException e) {
    		try {

                if (con != null) {
                    con.rollback();
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new RuntimeException("예약 가능 시간 조회 중 롤백 오류");
            }
    		e.printStackTrace();
    		throw new RuntimeException("예약 가능 시간 조회 중 오류 발생");
    	}
    }
}