package com.ureca.util;

import com.ureca.dao.UserDao;
import com.ureca.dao.UserDaoImp;
import com.ureca.dao.PaymentHistoryDao;
import com.ureca.dao.PaymentHistoryDaoImp;

// DAO 객체 하나씩만 생성해서 재사용
public class StudyCafeFactory {
    private static final UserDao userDao = new UserDaoImp();
    private static final PaymentHistoryDao paymentHistoryDao = new PaymentHistoryDaoImp();

    public static UserDao getUserDao() { return userDao; }
    public static PaymentHistoryDao getPaymentHistoryDao() { return paymentHistoryDao; }
    
    // 나중에 추가
    /**
    private static final RoomDao roomDao = new RoomDaoImp();
    private static final RoomHistoryDao roomHistoryDao = new RoomHistoryDaoImp();

    public static RoomDao getRoomDao() { return roomDao; }
    public static RoomHistoryDao getRoomHistoryDao() { return roomHistoryDao; }
    **/
}