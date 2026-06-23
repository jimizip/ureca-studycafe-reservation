// 공통 유틸

// "2026-06-23T10:00:00" -> "2026-06-23 10:00"
function fmtDateTime(s) {
	if (!s) return "";
	return s.replace("T", " ").substring(0, 16);
}

// 오늘 날짜 yyyy-MM-dd
function todayStr() {
	const d = new Date();
	const m = String(d.getMonth() + 1).padStart(2, "0");
	const day = String(d.getDate()).padStart(2, "0");
	return d.getFullYear() + "-" + m + "-" + day;
}
