<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="true" %>
<%@ page import="com.ureca.user.dto.UserDto" %>
<% UserDto userDto = (UserDto) session.getAttribute("userDto"); %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>예약하기 - 스터디카페</title>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
	<script src="/assets/js/util.js"></script>
	<style>
		.slot { display:inline-block; width:48px; padding:6px 0; margin:2px; text-align:center;
			border-radius:6px; cursor:pointer; font-size:13px; border:1px solid #ccc; }
		.slot.free { background:#d1e7dd; }
		.slot.booked { background:#f8d7da; color:#842029; cursor:not-allowed; }
		.slot.sel { background:#0d6efd; color:#fff; }
	</style>
</head>
<body>
	<nav class="navbar navbar-expand-lg bg-primary" data-bs-theme="dark">
		<div class="container">
			<a class="navbar-brand" href="/pages/main">스터디카페</a>
			<ul class="navbar-nav me-auto">
				<li class="nav-item"><a class="nav-link active" href="/pages/reservation">예약하기</a></li>
				<li class="nav-item"><a class="nav-link" href="/pages/payment">결제내역</a></li>
			</ul>
			<span class="navbar-text text-white me-3"><%= userDto.getName() %> 님</span>
			<a class="btn btn-outline-light btn-sm" href="/pages/logout">로그아웃</a>
		</div>
	</nav>

	<div class="container mt-4">
		<h4>회의실 예약</h4>
		<div class="row g-2 align-items-end mt-2">
			<div class="col-auto">
				<label class="form-label">회의실</label>
				<select class="form-select" id="roomSelect"></select>
			</div>
			<div class="col-auto">
				<label class="form-label">날짜</label>
				<input type="date" class="form-control" id="dateInput">
			</div>
			<div class="col-auto">
				<label class="form-label">인원</label>
				<input type="number" class="form-control" id="userCount" value="1" min="1" style="width:90px;">
			</div>
			<div class="col-auto">
				<button class="btn btn-secondary" id="btnSearch">조회</button>
			</div>
		</div>

		<div class="mt-4">
			<p class="text-muted mb-1">시간 슬롯 (초록=가능, 빨강=예약됨) — 시작/종료를 클릭하세요</p>
			<div id="slotWrapper"></div>
		</div>

		<div class="mt-3">
			<span id="selInfo" class="me-3"></span>
			<span id="priceInfo" class="fw-bold text-primary me-3"></span>
			<button class="btn btn-primary" id="btnReserve" disabled>예약하기</button>
		</div>
	</div>

	<script>
		let ROOMS = [];
		let SELECTED_START = null; // hour int
		let SELECTED_END = null;   // hour int (exclusive end = endHour)
		let EDIT_ID = null;        // 수정 모드면 예약 id

		window.onload = async function() {
			await loadRooms();
			document.querySelector("#btnSearch").onclick = loadSlots;
			document.querySelector("#btnReserve").onclick = reserve;

			const p = new URLSearchParams(location.search);
			if (p.get("editId")) {
				// 수정 모드: 기존 예약값 프리필
				EDIT_ID = parseInt(p.get("editId"));
				document.querySelector("h4").textContent = "회의실 예약 수정";
				document.querySelector("#btnReserve").textContent = "수정하기";
				document.querySelector("#roomSelect").value = p.get("roomId");
				document.querySelector("#dateInput").value = p.get("date");
				document.querySelector("#userCount").value = p.get("userCount");
				await loadSlots();
				SELECTED_START = parseInt(p.get("start"));
				SELECTED_END = parseInt(p.get("end"));
				repaint();
				updateSelInfo();
			} else {
				document.querySelector("#dateInput").value = todayStr();
			}
		}

		async function loadRooms() {
			const res = await fetch("/rooms/list");
			const data = await res.json();
			ROOMS = data.list || [];
			let html = "";
			ROOMS.forEach(r => {
				html += `<option value="${r.id}" data-price="${r.price}" data-size="${r.roomSize}">${r.id}번 (${r.roomSize}인, ${r.price.toLocaleString()}원/시간)</option>`;
			});
			document.querySelector("#roomSelect").innerHTML = html;
		}

		function selectedRoom() {
			const opt = document.querySelector("#roomSelect").selectedOptions[0];
			return { id: parseInt(opt.value), price: parseInt(opt.dataset.price) };
		}

		async function loadSlots() {
			SELECTED_START = SELECTED_END = null;
			updateSelInfo();
			const roomId = selectedRoom().id;
			const date = document.querySelector("#dateInput").value;
			let url = "/reservations/booked-hours?roomId=" + roomId + "&date=" + date;
			if (EDIT_ID !== null) url += "&excludeId=" + EDIT_ID;
			const res = await fetch(url);
			const data = await res.json();
			const booked = data.bookedHours || [];
			let html = "";
			for (let h = 0; h < 24; h++) {
				const cls = booked[h] ? "booked" : "free";
				html += `<span class="slot ${cls}" data-h="${h}" onclick="pickSlot(${h}, ${booked[h]})">${String(h).padStart(2,"0")}</span>`;
			}
			document.querySelector("#slotWrapper").innerHTML = html;
		}

		function pickSlot(h, isBooked) {
			if (isBooked) return;
			if (SELECTED_START === null || SELECTED_END !== null) {
				// 새 선택 시작
				SELECTED_START = h;
				SELECTED_END = null;
			} else {
				// 종료 선택 (시작보다 뒤여야 함)
				if (h < SELECTED_START) { SELECTED_START = h; }
				else { SELECTED_END = h + 1; } // end exclusive
			}
			repaint();
			updateSelInfo();
		}

		function repaint() {
			document.querySelectorAll(".slot").forEach(el => {
				el.classList.remove("sel");
				const h = parseInt(el.dataset.h);
				const end = SELECTED_END !== null ? SELECTED_END : (SELECTED_START !== null ? SELECTED_START + 1 : -1);
				if (SELECTED_START !== null && h >= SELECTED_START && h < end && !el.classList.contains("booked")) {
					el.classList.add("sel");
				}
			});
		}

		function updateSelInfo() {
			const btn = document.querySelector("#btnReserve");
			if (SELECTED_START === null) {
				document.querySelector("#selInfo").textContent = "";
				document.querySelector("#priceInfo").textContent = "";
				btn.disabled = true;
				return;
			}
			const end = SELECTED_END !== null ? SELECTED_END : SELECTED_START + 1;
			const hours = end - SELECTED_START;
			document.querySelector("#selInfo").textContent =
				`${String(SELECTED_START).padStart(2,"0")}:00 ~ ${String(end).padStart(2,"0")}:00 (${hours}시간)`;
			document.querySelector("#priceInfo").textContent = (selectedRoom().price * hours).toLocaleString() + "원";
			btn.disabled = false;
		}

		// hour>=24 면 다음날 00:00 로 변환 (자정 예약 지원)
		function buildTime(date, hour) {
			if (hour >= 24) {
				const d = new Date(date + "T00:00:00");
				d.setDate(d.getDate() + 1);
				const y = d.getFullYear();
				const m = String(d.getMonth() + 1).padStart(2, "0");
				const dd = String(d.getDate()).padStart(2, "0");
				return y + "-" + m + "-" + dd + "T" + String(hour - 24).padStart(2, "0") + ":00:00";
			}
			return date + "T" + String(hour).padStart(2, "0") + ":00:00";
		}

		async function reserve() {
			const roomId = selectedRoom().id;
			const date = document.querySelector("#dateInput").value;
			const end = SELECTED_END !== null ? SELECTED_END : SELECTED_START + 1;
			const userCount = document.querySelector("#userCount").value;
			const startTime = buildTime(date, SELECTED_START);
			const endTime = buildTime(date, end);

			const isEdit = EDIT_ID !== null;
			const url = isEdit ? "/reservations/update" : "/reservations/insert";
			const params = { roomId, startTime, endTime, userCount };
			if (isEdit) params.id = EDIT_ID;

			const res = await fetch(url, {
				method: "POST",
				headers: { "Content-Type": "application/x-www-form-urlencoded" },
				body: new URLSearchParams(params).toString()
			});
			const data = await res.json();
			if (data.result === "success") {
				alert(isEdit ? "예약 수정 완료!" : "예약 완료!");
				location.href = "/pages/main";
			} else {
				alert((isEdit ? "수정" : "예약") + " 실패: " + (data.message || ""));
			}
		}
	</script>
</body>
</html>
