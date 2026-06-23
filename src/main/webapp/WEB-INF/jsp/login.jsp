<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>로그인 - 스터디카페</title>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
	<div class="container" style="max-width: 420px; margin-top: 100px;">
		<h3 class="text-center mb-4">스터디카페 예약</h3>
		<div class="card shadow-sm">
			<div class="card-body">
				<div class="mb-3">
					<label class="form-label">이메일</label>
					<input type="email" class="form-control" id="email" placeholder="email@ureca.com">
				</div>
				<div class="mb-3">
					<label class="form-label">비밀번호</label>
					<input type="password" class="form-control" id="password">
				</div>
				<button class="btn btn-primary w-100" id="btnLogin">로그인</button>
				<div class="text-center mt-3">
					<a href="/pages/register">회원가입</a>
				</div>
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
