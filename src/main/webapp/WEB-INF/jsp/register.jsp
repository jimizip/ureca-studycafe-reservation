<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>회원가입 - STUDY CAFÉ</title>
	<link href="/assets/css/app.css" rel="stylesheet">
</head>
<body>
	<div class="center-wrap">
		<h1 class="brand auth-title">STUDY CAFÉ</h1>
		<p class="auth-sub">회원가입</p>

		<div class="auth-card">
			<div class="field">
				<label>이름</label>
				<input type="text" class="input" id="name">
			</div>
			<div class="field">
				<label>전화번호</label>
				<input type="text" class="input" id="tel" placeholder="010-1234-5678">
			</div>
			<div class="field">
				<label>이메일</label>
				<input type="email" class="input" id="email">
			</div>
			<div class="field">
				<label>비밀번호</label>
				<input type="password" class="input" id="password">
			</div>
			<button class="btn btn-gold btn-block" id="btnRegister">가입하기</button>
			<div class="auth-foot">
				이미 계정이 있으신가요? <a href="/pages/login">로그인</a>
			</div>
		</div>
	</div>

	<script>
		document.querySelector("#btnRegister").onclick = async function() {
			const name = document.querySelector("#name").value;
			const tel = document.querySelector("#tel").value;
			const email = document.querySelector("#email").value;
			const password = document.querySelector("#password").value;
			if (!name || !tel || !email || !password) { alert("모든 항목을 입력하세요."); return; }

			const res = await fetch("/users/insert", {
				method: "POST",
				headers: { "Content-Type": "application/x-www-form-urlencoded" },
				body: new URLSearchParams({ name, tel, email, password }).toString()
			});
			const data = await res.json();
			if (data.result === "success") {
				alert("가입 완료! 로그인하세요.");
				location.href = "/pages/login";
			} else {
				alert("가입 실패: " + (data.message || ""));
			}
		}
	</script>
</body>
</html>
