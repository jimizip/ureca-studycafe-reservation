<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>회원가입 - 스터디카페</title>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
	<div class="container" style="max-width: 420px; margin-top: 80px;">
		<h3 class="text-center mb-4">회원가입</h3>
		<div class="card shadow-sm">
			<div class="card-body">
				<div class="mb-3">
					<label class="form-label">이름</label>
					<input type="text" class="form-control" id="name">
				</div>
				<div class="mb-3">
					<label class="form-label">전화번호</label>
					<input type="text" class="form-control" id="tel" placeholder="010-1234-5678">
				</div>
				<div class="mb-3">
					<label class="form-label">이메일</label>
					<input type="email" class="form-control" id="email">
				</div>
				<div class="mb-3">
					<label class="form-label">비밀번호</label>
					<input type="password" class="form-control" id="password">
				</div>
				<button class="btn btn-primary w-100" id="btnRegister">가입하기</button>
				<div class="text-center mt-3">
					<a href="/pages/login">로그인으로</a>
				</div>
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
