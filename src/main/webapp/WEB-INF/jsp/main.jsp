<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="true" %>
<%@ page import="com.ureca.user.dto.UserDto" %>
<% UserDto userDto = (UserDto) session.getAttribute("userDto"); %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>내 예약 - STUDY CAFÉ</title>
	<link href="/assets/css/app.css" rel="stylesheet">
	<script src="/assets/js/util.js"></script>
</head>
<body>
	<div class="app">
		<aside class="sidebar">
			<div class="brand">STUDY CAFÉ</div>
			<div class="menu-label">메뉴</div>
			<nav>
				<a class="active" href="/pages/main">내 예약</a>
				<a href="/pages/reservation">회의실 예약</a>
				<a href="/pages/payment">결제 내역</a>
			</nav>
			<div class="version">v1.0</div>
		</aside>

		<header class="topbar">
			<span class="user"><b><%= userDto.getName() %></b>님 &nbsp;|&nbsp; <%= userDto.getEmail() %></span>
			<a class="btn btn-ghost btn-sm" href="/pages/logout">로그아웃</a>
		</header>

		<main class="main">
			<div class="card">
				<div class="card-head">
					<div>
						<h2 class="card-title">내 예약 현황</h2>
						<p class="card-sub">예약을 수정하거나 취소할 수 있습니다</p>
					</div>
					<a class="btn btn-gold" href="/pages/reservation">+ 회의실 예약</a>
				</div>
				<table class="data-table">
					<thead>
						<tr><th>예약번호</th><th>방</th><th>시작</th><th>종료</th><th>인원</th><th></th></tr>
					</thead>
					<tbody id="reservationTbody"></tbody>
				</table>
			</div>
		</main>
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
						<td class="gold">${r.roomId}번</td>
						<td>${fmtDateTime(r.startTime)}</td>
						<td>${fmtDateTime(r.endTime)}</td>
						<td>${r.userCount}명</td>
						<td>
							<button class="btn btn-ghost btn-sm" onclick="editReservation(${r.id}, ${r.roomId}, '${r.startTime}', '${r.endTime}', ${r.userCount})">수정</button>
							<button class="btn btn-danger btn-sm" onclick="cancelReservation(${r.id})">취소</button>
						</td>
					</tr>`;
			});
			document.querySelector("#reservationTbody").innerHTML = html || `<tr class="muted-row"><td colspan="6">예약 없음</td></tr>`;
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
