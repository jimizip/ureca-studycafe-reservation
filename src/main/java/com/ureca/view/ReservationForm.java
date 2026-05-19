package com.ureca.view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.ureca.dto.Room_history;
import com.ureca.dto.User;
import com.ureca.service.StudyCafeService;

public class ReservationForm {
    private JFrame form;
    private MessageDialog dialog;

    // 날짜 스피너
    private JSpinner yearSp, monthSp, daySp;

    // 룸 버튼 패널
    private JPanel roomButtonPan;

    // 시간 버튼 패널
    private JPanel timeButtonPan;
    private List<JButton> timeButtons = new ArrayList<>();
    private List<Integer> selectedHours = new ArrayList<>(); // 선택된 시간 목록

    // 예약 입력
    private JSpinner userCountSp;
    private JLabel selectedRoomL, selectedTimeL;
    private JButton confirmBt, cancelBt;
    private int selectedRoomId = -1;
    private User currentUser;
    private StudyCafeService service;

    public ReservationForm(User currentUser, StudyCafeService service) {
        this.currentUser = currentUser;
        this.service = service;
        dialog = new MessageDialog();

        form = new JFrame("회의실 예약");
        form.setSize(700, 700);
        form.setLayout(new BorderLayout(10, 10));
        form.setLocationRelativeTo(null);

        // ===== 날짜 선택 패널 =====
        JPanel calendarPan = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        calendarPan.setBorder(BorderFactory.createTitledBorder("날짜 선택"));

        yearSp  = new JSpinner(new SpinnerNumberModel(2026, 2024, 2030, 1));
        monthSp = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        daySp   = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));

        yearSp.setPreferredSize(new Dimension(70, 30));
        monthSp.setPreferredSize(new Dimension(55, 30));
        daySp.setPreferredSize(new Dimension(55, 30));

        JButton dateBt = new JButton("룸 조회");

        calendarPan.add(yearSp);  calendarPan.add(new JLabel("년"));
        calendarPan.add(monthSp); calendarPan.add(new JLabel("월"));
        calendarPan.add(daySp);   calendarPan.add(new JLabel("일"));
        calendarPan.add(dateBt);

        // ===== 룸 버튼 패널 =====
        roomButtonPan = new JPanel(new GridLayout(0, 2, 5, 5));
        roomButtonPan.setBorder(BorderFactory.createTitledBorder("예약 가능 회의실"));

        // ===== 시간 버튼 패널 =====
        timeButtonPan = new JPanel(new GridLayout(0, 4, 5, 5));
        timeButtonPan.setBorder(BorderFactory.createTitledBorder("시간 선택 (1시간 단위, 중복 선택 가능)"));

        // 날짜 선택 후 룸 조회
        dateBt.addActionListener(e -> {
            roomButtonPan.removeAll();
            timeButtonPan.removeAll();
            timeButtons.clear();
            selectedHours.clear();
            selectedRoomId = -1;
            selectedRoomL.setText("선택된 룸: 없음");
            selectedTimeL.setText("선택된 시간: 없음");

            // TODO: service.searchAvailableRooms() 연결
            // 임시 룸 버튼
            for (int i = 1; i <= 3; i++) {
                final int roomId = i;
                JButton roomBt = new JButton(
                    "룸" + roomId + " | 수용인원: 4명 | 시간당: 5,000원"
                );
                roomBt.addActionListener(ev -> {
                    selectedRoomId = roomId;
                    selectedRoomL.setText("룸" + roomId + " (4명 / 5,000원)");
                    showTimeButtons(roomId);
                });
                roomButtonPan.add(roomBt);
            }
            roomButtonPan.revalidate();
            roomButtonPan.repaint();
        });

        // ===== 예약 정보 패널 =====
        JPanel inputPan = new JPanel(new GridBagLayout());
        inputPan.setBorder(BorderFactory.createTitledBorder("예약 정보"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        selectedRoomL = new JLabel("선택된 룸: 없음");
        selectedTimeL = new JLabel("선택된 시간: 없음");
        userCountSp   = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        inputPan.add(new JLabel("선택 룸"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7; gbc.gridwidth = 3;
        inputPan.add(selectedRoomL, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        inputPan.add(new JLabel("선택 시간"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7; gbc.gridwidth = 3;
        inputPan.add(selectedTimeL, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        inputPan.add(new JLabel("인원수"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.7; gbc.gridwidth = 3;
        inputPan.add(userCountSp, gbc);
        gbc.gridwidth = 1;

        // ===== 버튼 =====
        JPanel buttonPan = new JPanel(new FlowLayout());
        confirmBt = new JButton("예약 확정");
        cancelBt  = new JButton("취소");

        confirmBt.addActionListener(e -> {
            try {
                if (selectedRoomId == -1) {
                    dialog.show("회의실을 선택해주세요");
                    return;
                }
                if (selectedHours.isEmpty()) {
                    dialog.show("시간을 선택해주세요");
                    return;
                }

                int year  = (int) yearSp.getValue();
                int month = (int) monthSp.getValue();
                int day   = (int) daySp.getValue();
                int count = (int) userCountSp.getValue();

                Collections.sort(selectedHours);
                int startH = selectedHours.get(0);
                int endH   = selectedHours.get(selectedHours.size() - 1) + 1;

                LocalDateTime start = LocalDateTime.of(year, month, day, startH, 0);
                LocalDateTime end   = LocalDateTime.of(year, month, day, endH, 0);

                Room_history history = new Room_history();
                history.setRoom_id(selectedRoomId);
                history.setUser_id(currentUser.getId());
                history.setStart_time(start);
                history.setEnd_time(end);
                history.setUser_count(count);

                // TODO: service.reserve(history) 연결
                // service.reserve(history);

                dialog.show("예약 완료! " + startH + ":00 ~ " + endH + ":00");
                form.setVisible(false);
            } catch (Exception ex) {
                dialog.show(ex.getMessage());
            }
        });

        cancelBt.addActionListener(e -> form.setVisible(false));
        buttonPan.add(confirmBt);
        buttonPan.add(cancelBt);

        // ===== 조립 =====
        JPanel centerPan = new JPanel(new BorderLayout(5, 5));
        JPanel roomTimePan = new JPanel(new GridLayout(2, 1, 5, 5));
        roomTimePan.add(roomButtonPan);
        roomTimePan.add(timeButtonPan);
        centerPan.add(roomTimePan, BorderLayout.NORTH);
        centerPan.add(inputPan, BorderLayout.CENTER);

        form.add(calendarPan, BorderLayout.NORTH);
        form.add(centerPan, BorderLayout.CENTER);
        form.add(buttonPan, BorderLayout.SOUTH);
    }

    // 룸 클릭 시 시간 버튼 생성
    private void showTimeButtons(int roomId) {
        timeButtonPan.removeAll();
        timeButtons.clear();
        selectedHours.clear();
        selectedTimeL.setText("선택된 시간: 없음");

        // TODO: 실제 예약된 시간 service에서 가져오기
        // 임시로 10:00, 11:00 예약된 것으로 처리
        List<Integer> bookedHours = new ArrayList<>();
        bookedHours.add(10);
        bookedHours.add(11);

        for (int hour = 9; hour < 22; hour++) {
            final int h = hour;
            JButton timeBt = new JButton(hour + ":00");
            timeBt.setPreferredSize(new Dimension(80, 40));

            if (bookedHours.contains(hour)) {
                // 이미 예약된 시간 - 빨간색 비활성화
                timeBt.setBackground(Color.RED);
                timeBt.setForeground(Color.WHITE);
                timeBt.setEnabled(false);
            } else {
                // 예약 가능한 시간 - 클릭 시 파란색
            	timeBt.addActionListener(ev -> {
            	    if (selectedHours.contains(h)) {
            	        // 선택 해제
            	        selectedHours.remove(Integer.valueOf(h));
            	        timeBt.setBackground(null);
            	        timeBt.setBorder(UIManager.getBorder("Button.border"));
            	    } else {
            	        // 연속성 체크
            	        if (!selectedHours.isEmpty()) {
            	            int min = Collections.min(selectedHours);
            	            int max = Collections.max(selectedHours);
            	            if (h != min - 1 && h != max + 1) {
            	                dialog.show("연속된 시간만 선택 가능합니다.");
            	                return;
            	            }
            	        }
            	        selectedHours.add(h);
            	        // 색깔 대신 테두리로 표시
            	        timeBt.setBackground(new Color(173, 216, 230)); // 연한 파란색
            	        timeBt.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
            	    }
            	    updateSelectedTimeLabel();
            	});
            }
            timeButtons.add(timeBt);
            timeButtonPan.add(timeBt);
        }
        timeButtonPan.revalidate();
        timeButtonPan.repaint();
    }

    // 선택된 시간 라벨 업데이트
    private void updateSelectedTimeLabel() {
        if (selectedHours.isEmpty()) {
            selectedTimeL.setText("선택된 시간: 없음");
        } else {
            Collections.sort(selectedHours);
            int start = selectedHours.get(0);
            int end   = selectedHours.get(selectedHours.size() - 1) + 1;
            selectedTimeL.setText(start + ":00 ~ " + end + ":00");
        }
    }

    public void show() { form.setVisible(true); }
}