<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="true" %>
<%@ page import="com.ureca.user.dto.UserDto" %>
<% UserDto userDto = (UserDto) session.getAttribute("userDto"); %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>회의실 예약 - STUDY CAFÉ</title>
	<link href="/assets/css/app.css" rel="stylesheet">
	<script src="/assets/js/util.js"></script>
</head>
<body>
	<div class="app">
		<aside class="sidebar">
			<div class="brand">STUDY CAFÉ</div>
			<div class="menu-label">메뉴</div>
			<nav>
				<a href="/pages/main">내 예약</a>
				<a class="active" href="/pages/reservation">회의실 예약</a>
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
					<h2 class="card-title" id="pageTitle">회의실 예약</h2>
				</div>
				<div class="toolbar">
					<div class="field">
						<label>회의실</label>
						<select class="input" id="roomSelect"></select>
					</div>
					<div class="field">
						<label>날짜</label>
						<input type="date" class="input" id="dateInput">
					</div>
					<div class="field">
						<label>인원</label>
						<input type="number" class="input w-sm" id="userCount" value="1" min="1">
					</div>
					<button class="btn btn-ghost" id="btnSearch">조회</button>
				</div>
			</div>

			<div class="card">
				<p class="section-label">시간 선택 <span style="color:var(--muted);font-weight:400;">(1시간 단위 · 연속 선택 가능)</span></p>
				<div class="slot-grid" id="slotWrapper"></div>
				<p class="card-sub" style="margin-top:12px;">
					<span style="color:#e7b3b3;">■</span> 예약됨 &nbsp;
					<span style="color:#7fa8d6;">■</span> 선택 &nbsp;
					<span style="color:#6bbf7f;">■</span> 기존 예약(수정)
				</p>
			</div>

			<div class="card">
				<p class="section-label">예약 정보 확인</p>
				<div class="summary">
					<span id="selInfo" class="muted">시간을 선택하세요</span>
					<span id="priceInfo" class="price"></span>
					<button class="btn btn-gold" id="btnReserve" style="margin-left:auto;" disabled>예약하기</button>
				</div>
			</div>
		</main>
	</div>

	<script>
		let ROOMS = [];
		let SELECTED_START = null; // hour int
		let SELECTED_END = null;   // hour int (exclusive end = endHour)
		let EDIT_ID = null;        // 수정 모드면 예약 id
		let ORIGIN_START = null;   // 수정 모드 기존 예약 구간 (그린 표시용)
		let ORIGIN_END = null;

		window.onload = async function() {
			await loadRooms();
			document.querySelector("#btnSearch").onclick = loadSlots;
			document.querySelector("#btnReserve").onclick = reserve;

			const p = new URLSearchParams(location.search);
			if (p.get("editId")) {
				// 수정 모드: 기존 예약값 프리필
				EDIT_ID = parseInt(p.get("editId"));
				ORIGIN_START = parseInt(p.get("start"));
				ORIGIN_END = parseInt(p.get("end"));
				document.querySelector("#pageTitle").textContent = "회의실 예약 수정";
				document.querySelector("#btnReserve").textContent = "수정 확정";
				document.querySelector("#roomSelect").value = p.get("roomId");
				document.querySelector("#dateInput").value = p.get("date");
				document.querySelector("#userCount").value = p.get("userCount");
				await loadSlots();
				SELECTED_START = ORIGIN_START;
				SELECTED_END = ORIGIN_END;
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
				html += `<option value="${r.id}" data-price="${r.price}" data-size="${r.roomSize}">룸 ${r.id} · 최대 ${r.roomSize}명 · 시간당 ${r.price.toLocaleString()}원</option>`;
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
				let cls = booked[h] ? "booked" : "free";
				if (EDIT_ID !== null && ORIGIN_START !== null && h >= ORIGIN_START && h < ORIGIN_END) cls += " origin";
				html += `<span class="slot ${cls}" data-h="${h}" onclick="pickSlot(${h}, ${booked[h]})">${String(h).padStart(2,"0")}:00</span>`;
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
				document.querySelector("#selInfo").textContent = "시간을 선택하세요";
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
