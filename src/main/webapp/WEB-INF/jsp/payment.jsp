<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="true" %>
<%@ page import="com.ureca.user.dto.UserDto" %>
<% UserDto userDto = (UserDto) session.getAttribute("userDto"); %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>결제내역 - 스터디카페</title>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
	<script src="/assets/js/util.js"></script>
</head>
<body>
	<nav class="navbar navbar-expand-lg bg-primary" data-bs-theme="dark">
		<div class="container">
			<a class="navbar-brand" href="/pages/main">스터디카페</a>
			<ul class="navbar-nav me-auto">
				<li class="nav-item"><a class="nav-link" href="/pages/reservation">예약하기</a></li>
				<li class="nav-item"><a class="nav-link active" href="/pages/payment">결제내역</a></li>
			</ul>
			<span class="navbar-text text-white me-3"><%= userDto.getName() %> 님</span>
			<a class="btn btn-outline-light btn-sm" href="/pages/logout">로그아웃</a>
		</div>
	</nav>

	<div class="container mt-4">
		<h4>결제 내역</h4>
		<table class="table table-striped mt-3">
			<thead>
				<tr><th>결제번호</th><th>방</th><th>금액</th><th>결제일시</th></tr>
			</thead>
			<tbody id="paymentTbody"></tbody>
		</table>
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
						<td>${p.price.toLocaleString()}원</td>
						<td>${fmtDateTime(p.paymentDate)}</td>
					</tr>`;
			});
			document.querySelector("#paymentTbody").innerHTML = html || `<tr><td colspan="4" class="text-center text-muted">결제 내역 없음</td></tr>`;
		}
	</script>
</body>
</html>
