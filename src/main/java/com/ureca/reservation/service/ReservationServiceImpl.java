package com.ureca.reservation.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.ureca.common.exception.DuplicateException;
import com.ureca.common.exception.InvalidTimeException;
import com.ureca.payment.dao.PaymentDao;
import com.ureca.payment.dto.PaymentDto;
import com.ureca.reservation.dao.ReservationDao;
import com.ureca.reservation.dto.ReservationDto;
import com.ureca.reservation.dto.ReservationResultDto;
import com.ureca.room.dao.RoomDao;
import com.ureca.room.dto.RoomDto;

@Service
public class ReservationServiceImpl implements ReservationService {

	private final ReservationDao reservationDao;
	private final PaymentDao paymentDao;
	private final RoomDao roomDao;

	public ReservationServiceImpl(ReservationDao reservationDao, PaymentDao paymentDao, RoomDao roomDao) {
		this.reservationDao = reservationDao;
		this.paymentDao = paymentDao;
		this.roomDao = roomDao;
	}

	// 예약 등록 + 결제 INSERT (트랜잭션)
	@Override
	@Transactional
	public ReservationResultDto reserve(ReservationDto reservation) {
		ReservationResultDto resultDto = new ReservationResultDto();
		try {
			validateTime(reservation);
			checkOverlap(reservation);

			// 예약 INSERT (생성 id가 reservation.id 에 채워짐)
			reservationDao.insertReserve(reservation);

			int price = calcPrice(reservation);

			PaymentDto payment = new PaymentDto();
			payment.setUserId(reservation.getUserId());
			payment.setRoomId(reservation.getRoomId());
			payment.setPrice(price);
			payment.setPaymentDate(LocalDateTime.now());
			payment.setRoomHistoryId(reservation.getId());
			paymentDao.add(payment);

			resultDto.setDto(reservation);
			resultDto.setResult("success");
		} catch (DuplicateException | InvalidTimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			resultDto.setResult("fail");
			resultDto.setMessage(e.getMessage());
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			resultDto.setResult("fail");
			resultDto.setMessage("예약 등록 중 오류 발생");
		}
		return resultDto;
	}

	// 예약 수정 + 결제 금액 갱신 (트랜잭션)
	@Override
	@Transactional
	public ReservationResultDto updateReservation(ReservationDto reservation) {
		ReservationResultDto resultDto = new ReservationResultDto();
		try {
			validateTime(reservation);
			checkOverlap(reservation); // 본인 예약은 제외

			reservationDao.updateReserve(reservation);

			int price = calcPrice(reservation);
			paymentDao.updateByHistory(reservation.getId(), price);

			resultDto.setDto(reservation);
			resultDto.setResult("success");
		} catch (DuplicateException | InvalidTimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			resultDto.setResult("fail");
			resultDto.setMessage(e.getMessage());
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			resultDto.setResult("fail");
			resultDto.setMessage("예약 수정 중 오류 발생");
		}
		return resultDto;
	}

	// 예약 취소: 결제내역 먼저 삭제 후 예약 삭제 (트랜잭션)
	@Override
	@Transactional
	public ReservationResultDto cancel(int reservationId) {
		ReservationResultDto resultDto = new ReservationResultDto();
		try {
			paymentDao.removeByHistory(reservationId);
			reservationDao.removeReserve(reservationId);
			resultDto.setResult("success");
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			resultDto.setResult("fail");
			resultDto.setMessage("예약 취소 중 오류 발생");
		}
		return resultDto;
	}

	@Override
	public ReservationResultDto listByUser(int userId) {
		ReservationResultDto resultDto = new ReservationResultDto();
		resultDto.setList(reservationDao.getHistory(userId));
		resultDto.setResult("success");
		return resultDto;
	}

	// 룸/날짜 기준 24시간 슬롯 예약 여부
	@Override
	public ReservationResultDto getBookedHours(int roomId, LocalDateTime date, Integer excludeId) {
		ReservationResultDto resultDto = new ReservationResultDto();
		List<Boolean> isUsed = new ArrayList<>();
		for (int i = 0; i < 24; i++) {
			isUsed.add(false);
		}
		List<ReservationDto> history = reservationDao.getReservation(roomId, date);
		for (ReservationDto his : history) {
			// 수정 모드: 본인 예약 슬롯은 제외 (재선택 가능하도록)
			if (excludeId != null && his.getId() == excludeId) continue;
			LocalTime start = his.getStartTime().toLocalTime();
			LocalTime end = his.getEndTime().toLocalTime();
			for (int i = 0; i < 24; i++) {
				LocalTime slotStart = LocalTime.of(i, 0);
				LocalTime slotEnd = (i == 23) ? LocalTime.MAX : LocalTime.of(i + 1, 0);
				if (start.isBefore(slotEnd) && end.isAfter(slotStart)) {
					isUsed.set(i, true);
				}
			}
		}
		resultDto.setBookedHours(isUsed);
		resultDto.setResult("success");
		return resultDto;
	}

	// --- helpers ---

	private void validateTime(ReservationDto r) {
		if (r.getStartTime() == null || r.getEndTime() == null
				|| !r.getEndTime().isAfter(r.getStartTime())) {
			throw new InvalidTimeException("종료 시간은 시작 시간보다 늦어야 합니다.");
		}
	}

	// 같은 룸/날짜의 기존 예약과 시간 겹침 체크 (본인 예약 id 제외)
	private void checkOverlap(ReservationDto r) {
		List<ReservationDto> reserves = reservationDao.getReservation(r.getRoomId(), r.getStartTime());
		for (ReservationDto ex : reserves) {
			if (ex.getId() == r.getId()) continue;
			if (ex.getStartTime().isBefore(r.getEndTime()) && ex.getEndTime().isAfter(r.getStartTime())) {
				throw new DuplicateException("이미 예약된 시간입니다.");
			}
		}
	}

	private int calcPrice(ReservationDto r) {
		RoomDto room = roomDao.search(r.getRoomId());
		long hours = ChronoUnit.HOURS.between(r.getStartTime(), r.getEndTime());
		return (int) (room.getPrice() * hours);
	}
}
