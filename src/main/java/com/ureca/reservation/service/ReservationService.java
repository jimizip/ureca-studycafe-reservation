package com.ureca.reservation.service;

import java.time.LocalDateTime;

import com.ureca.reservation.dto.ReservationDto;
import com.ureca.reservation.dto.ReservationResultDto;

public interface ReservationService {
	ReservationResultDto reserve(ReservationDto reservation);
	ReservationResultDto updateReservation(ReservationDto reservation);
	ReservationResultDto cancel(int reservationId);
	ReservationResultDto listByUser(int userId);
	ReservationResultDto getBookedHours(int roomId, LocalDateTime date, Integer excludeId);
}
