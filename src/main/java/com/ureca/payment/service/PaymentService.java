package com.ureca.payment.service;

import com.ureca.payment.dto.PaymentResultDto;

public interface PaymentService {
	PaymentResultDto listByUser(int userId);
}
