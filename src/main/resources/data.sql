-- =====================================================================
-- 샘플 데이터 (모든 INSERT는 ON DUPLICATE KEY 로 멱등 → 재기동 안전)
-- 기준 날짜: 2026-06-23 (오늘)
-- =====================================================================

-- ---------- 회의실 ----------
-- room 4 = 10000원/시간 : 30000(3h) → 50000(5h) 수정 시나리오용
INSERT INTO Room (id, room_size, price) VALUES
    (1, 2,  3000),
    (2, 4,  5000),
    (3, 6,  8000),
    (4, 8,  10000),
    (5, 10, 15000),
    (6, 12, 20000)
ON DUPLICATE KEY UPDATE room_size = VALUES(room_size), price = VALUES(price);

-- ---------- 유저 (비밀번호 평문 데모, 전부 1234) ----------
INSERT INTO User (id, name, tel, email, password) VALUES
    (1, '홍길동',   '010-1111-1111', 'hong@ureca.com', '1234'),
    (2, '김스터디', '010-2222-2222', 'kim@ureca.com',  '1234'),
    (3, '이영희',   '010-3333-3333', 'lee@ureca.com',  '1234'),
    (4, '박철수',   '010-4444-4444', 'park@ureca.com', '1234'),
    (5, '최지원',   '010-5555-5555', 'choi@ureca.com', '1234')
ON DUPLICATE KEY UPDATE name = VALUES(name), tel = VALUES(tel), password = VALUES(password);

-- ---------- 예약 (Room_history) ----------
-- 명시적 id → payment_history 가 참조. 시나리오별 주석 참고.
INSERT INTO Room_history (id, room_id, user_id, start_time, end_time, user_count) VALUES
    -- [버그A] 과거 예약도 목록에 떠야 함 (start_time > NOW() 필터 제거 검증)
    (1, 1, 1, '2026-06-20 09:00:00', '2026-06-20 11:00:00', 2),
    -- [수정/금액재계산] 홍길동, room4(10000) 3h=30000 → 5h=50000 으로 수정 대상
    (2, 4, 1, '2026-06-23 10:00:00', '2026-06-23 13:00:00', 4),
    -- [중복예약 차단] room2 같은 날 두 구간: 14-16, 10-12. 11-13 시도 시 빨강(겹침)
    (3, 2, 2, '2026-06-23 14:00:00', '2026-06-23 16:00:00', 3),
    (4, 2, 2, '2026-06-23 10:00:00', '2026-06-23 12:00:00', 4),
    -- [미래 예약] 정상 표시
    (5, 3, 3, '2026-06-25 09:00:00', '2026-06-25 12:00:00', 5),
    -- [긴 예약/고액] 홍길동, room5 5h=75000
    (6, 5, 1, '2026-06-30 13:00:00', '2026-06-30 18:00:00', 8),
    -- [취소 연쇄삭제] 박철수, 취소 시 payment id 7 동시 삭제 검증
    (7, 1, 4, '2026-06-23 18:00:00', '2026-06-23 19:00:00', 1),
    -- [과거 고액] 최지원, room6 3h=60000
    (8, 6, 5, '2026-06-22 20:00:00', '2026-06-22 23:00:00', 10)
ON DUPLICATE KEY UPDATE
    room_id = VALUES(room_id), user_id = VALUES(user_id),
    start_time = VALUES(start_time), end_time = VALUES(end_time), user_count = VALUES(user_count);

-- ---------- 결제 내역 (각 예약과 1:1, price = 룸가격 * 시간) ----------
INSERT INTO payment_history (id, user_id, room_id, price, payment_date, room_history_id) VALUES
    (1, 1, 1,  6000, '2026-06-19 12:00:00', 1),
    (2, 1, 4, 30000, '2026-06-23 09:30:00', 2),
    (3, 2, 2, 10000, '2026-06-23 09:40:00', 3),
    (4, 2, 2, 10000, '2026-06-23 09:41:00', 4),
    (5, 3, 3, 24000, '2026-06-24 18:00:00', 5),
    (6, 1, 5, 75000, '2026-06-23 11:00:00', 6),
    (7, 4, 1,  3000, '2026-06-23 17:00:00', 7),
    (8, 5, 6, 60000, '2026-06-21 19:00:00', 8)
ON DUPLICATE KEY UPDATE
    user_id = VALUES(user_id), room_id = VALUES(room_id),
    price = VALUES(price), payment_date = VALUES(payment_date),
    room_history_id = VALUES(room_history_id);
