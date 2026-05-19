package com.ureca.util;

import com.ureca.dao.*;

// DAO 객체 하나씩만 생성해서 재사용
public class StudyCafeFactory {
    private static final UserDao userDao = new UserDaoImp();
    private static final PaymentHistoryDao paymentHistoryDao = new PaymentHistoryDaoImp();

    public static UserDao getUserDao() { return userDao; }
    public static PaymentHistoryDao getPaymentHistoryDao() { return paymentHistoryDao; }
    
    private static final RoomDao roomDao = new RoomDaolmp();
    private static final RoomHistoryDao roomHistoryDao = new RoomHistoryDaolmp();

    public static RoomDao getRoomDao() { return roomDao; }
    public static RoomHistoryDao getRoomHistoryDao() { return roomHistoryDao; }
}