<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="true" %>
<%@ page import="com.ureca.user.dto.UserDto" %>
<% UserDto userDto = (UserDto) session.getAttribute("userDto"); %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>메인 - 스터디카페</title>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
	<script src="/assets/js/util.js"></script>
</head>
<body>
	<nav class="navbar navbar-expand-lg bg-primary" data-bs-theme="dark">
		<div class="container">
			<a class="navbar-brand" href="/pages/main">스터디카페</a>
			<ul class="navbar-nav me-auto">
				<li class="nav-item"><a class="nav-link" href="/pages/reservation">예약하기</a></li>
				<li class="nav-item"><a class="nav-link" href="/pages/payment">결제내역</a></li>
			</ul>
			<span class="navbar-text text-white me-3"><%= userDto.getName() %> 님</span>
			<a class="btn btn-outline-light btn-sm" href="/pages/logout">로그아웃</a>
		</div>
	</nav>

	<div class="container mt-4">
		<h4>내 예약 현황</h4>
		<table class="table table-hover mt-3">
			<thead>
				<tr><th>예약번호</th><th>방</th><th>시작</th><th>종료</th><th>인원</th><th></th></tr>
			</thead>
			<tbody id="reservationTbody"></tbody>
		</table>
	</div>

	<script>
		window.onload = listReservation;

		async function listReservation() {
			const res = await fetch("/reservations/list");
			const data = await res.json();
			if (data.result !== "success") return;
			let html = "";
			(data.list || []).forEach(r => {
				html += `
					<tr>
						<td>${r.id}</td>
						<td>${r.roomId}번</td>
						<td>${fmtDateTime(r.startTime)}</td>
						<td>${fmtDateTime(r.endTime)}</td>
						<td>${r.userCount}명</td>
						<td>
							<button class="btn btn-sm btn-outline-primary" onclick="editReservation(${r.id}, ${r.roomId}, '${r.startTime}', '${r.endTime}', ${r.userCount})">수정</button>
							<button class="btn btn-sm btn-outline-danger" onclick="cancelReservation(${r.id})">취소</button>
						</td>
					</tr>`;
			});
			document.querySelector("#reservationTbody").innerHTML = html || `<tr><td colspan="6" class="text-center text-muted">예약 없음</td></tr>`;
		}

		function editReservation(id, roomId, startTime, endTime, userCount) {
			const date = startTime.substring(0, 10);
			const start = parseInt(startTime.substring(11, 13));
			const end = parseInt(endTime.substring(11, 13));
			location.href = "/pages/reservation?editId=" + id + "&roomId=" + roomId
				+ "&date=" + date + "&start=" + start + "&end=" + end + "&userCount=" + userCount;
		}

		async function cancelReservation(id) {
			if (!confirm("예약을 취소하시겠습니까?")) return;
			const res = await fetch("/reservations/cancel/" + id, { method: "POST" });
			const data = await res.json();
			if (data.result === "success") listReservation();
			else alert("취소 실패: " + (data.message || ""));
		}
	</script>
</body>
</html>
