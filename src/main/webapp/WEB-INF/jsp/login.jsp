<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>로그인 - STUDY CAFÉ</title>
	<link href="/assets/css/app.css" rel="stylesheet">
</head>
<body>
	<div class="center-wrap">
		<h1 class="brand auth-title">STUDY CAFÉ</h1>
		<p class="auth-sub">예약 관리 시스템</p>

		<div class="auth-card">
			<div class="field">
				<label>이메일</label>
				<input type="email" class="input" id="email" placeholder="email@ureca.com">
			</div>
			<div class="field">
				<label>비밀번호</label>
				<input type="password" class="input" id="password">
			</div>
			<button class="btn btn-gold btn-block" id="btnLogin">로그인</button>
			<div class="auth-foot">
				계정이 없으신가요? <a href="/pages/register">회원가입</a>
			</div>
		</div>
	</div>

	<script>
		document.querySelector("#btnLogin").onclick = login;
		document.querySelector("#password").addEventListener("keyup", e => { if (e.key === "Enter") login(); });

		async function login() {
			const email = document.querySelector("#email").value;
			const password = document.querySelector("#password").value;
			if (!email || !password) { alert("이메일과 비밀번호를 입력하세요."); return; }

			const res = await fetch("/auth/login", {
				method: "POST",
				headers: { "Content-Type": "application/x-www-form-urlencoded" },
				body: new URLSearchParams({ email, password }).toString()
			});
			const data = await res.json();
			if (data.result === "success") {
				location.href = "/pages/main";
			} else {
				alert("로그인 실패: 이메일/비밀번호를 확인하세요.");
			}
		}
	</script>
</body>
</html>
