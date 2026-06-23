package com.ureca.payment.dto;

import java.util.List;

public class PaymentResultDto {
	private String result;
	private List<PaymentDto> list;

	public String getResult() { return result; }
	public void setResult(String result) { this.result = result; }

	public List<PaymentDto> getList() { return list; }
	public void setList(List<PaymentDto> list) { this.list = list; }
}
