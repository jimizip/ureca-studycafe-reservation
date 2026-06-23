<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="true" %>
<%@ page import="com.ureca.user.dto.UserDto" %>
<% UserDto userDto = (UserDto) session.getAttribute("userDto"); %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>결제 내역 - STUDY CAFÉ</title>
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
				<a href="/pages/reservation">회의실 예약</a>
				<a class="active" href="/pages/payment">결제 내역</a>
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
						<h2 class="card-title">결제 내역</h2>
						<p class="card-sub"><%= userDto.getName() %>님의 결제 내역을 조회합니다</p>
					</div>
				</div>
				<table class="data-table">
					<thead>
						<tr><th>결제번호</th><th>방</th><th>금액</th><th>결제일시</th></tr>
					</thead>
					<tbody id="paymentTbody"></tbody>
				</table>
			</div>
		</main>
	</div>

	<script>
		window.onload = async function() {
			const res = await fetch("/payments/list");
			const data = await res.json();
			let html = "";
			(data.list || []).forEach(p => {
				html += `
					<tr>
						<td>${p.id}</td>
						<td>${p.roomId}번</td>
						<td class="gold">${p.price.toLocaleString()}원</td>
						<td>${fmtDateTime(p.paymentDate)}</td>
					</tr>`;
			});
			document.querySelector("#paymentTbody").innerHTML = html || `<tr class="muted-row"><td colspan="4">결제 내역 없음</td></tr>`;
		}
	</script>
</body>
</html>
