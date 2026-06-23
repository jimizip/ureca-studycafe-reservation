package com.ureca.payment.service;

import org.springframework.stereotype.Service;

import com.ureca.payment.dao.PaymentDao;
import com.ureca.payment.dto.PaymentResultDto;

@Service
public class PaymentServiceImpl implements PaymentService {

	private final PaymentDao paymentDao;

	public PaymentServiceImpl(PaymentDao paymentDao) {
		this.paymentDao = paymentDao;
	}

	@Override
	public PaymentResultDto listByUser(int userId) {
		PaymentResultDto resultDto = new PaymentResultDto();
		resultDto.setList(paymentDao.searchByUser(userId));
		resultDto.setResult("success");
		return resultDto;
	}
}
