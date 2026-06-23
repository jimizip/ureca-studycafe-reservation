package com.ureca.payment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ureca.payment.dto.PaymentDto;

@Mapper
public interface PaymentDao {
	int add(PaymentDto payment);
	List<PaymentDto> searchByUser(int userId);
	int removeByHistory(int roomHistoryId);
	int updateByHistory(@Param("roomHistoryId") int roomHistoryId, @Param("price") int price);
}
