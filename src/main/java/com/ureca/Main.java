package com.ureca;

import com.ureca.dao.UserDaoImp;
import com.ureca.dto.User;

public class Main {
    public static void main(String[] args) {
        UserDaoImp dao = new UserDaoImp();
        try {
            // 유저 등록 테스트
            User user = new User();
            user.setName("김지민");
            user.setTel("010-1234-5678");
            user.setEmail("jimin@email.com");
            dao.add(user);
            System.out.println("등록 성공");

            // 전체 조회 테스트
            for (User u : dao.searchAll()) {
                System.out.println(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}