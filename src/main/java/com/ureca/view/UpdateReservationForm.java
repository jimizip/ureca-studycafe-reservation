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

public class UpdateReservationForm {
    private JFrame form;
    private MessageDialog dialog;

    private JSpinner yearSp, monthSp, daySp;
    private JPanel timeButtonPan;
    private List<Integer> selectedHours = new ArrayList<>();
    private JSpinner userCountSp;
    private JLabel selectedTimeL;
    private JButton confirmBt, cancelBt;

    private Room_history originalHistory;
    private User currentUser;
    private StudyCafeService service;
    private Runnable onSuccess;

    public UpdateReservationForm(User currentUser, Room_history history,
                                  StudyCafeService service, Runnable onSuccess) {
        this.currentUser = currentUser;
        this.originalHistory = history;
        this.service = service;
        this.onSuccess = onSuccess;
        dialog = new MessageDialog();

        form = new JFrame("예약 수정");
        form.setSize(600, 550);
        form.setLayout(new BorderLayout(10, 10));
        form.setLocationRelativeTo(null);

        // ===== 날짜 선택 =====
        JPanel calendarPan = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        calendarPan.setBorder(BorderFactory.createTitledBorder("날짜 선택"));

        yearSp  = new JSpinner(new SpinnerNumberModel(
            history.getStart_time().getYear(), 2024, 2030, 1));
        monthSp = new JSpinner(new SpinnerNumberModel(
            history.getStart_time().getMonthValue(), 1, 12, 1));
        daySp   = new JSpinner(new SpinnerNumberModel(
            history.getStart_time().getDayOfMonth(), 1, 31, 1));

        yearSp.setPreferredSize(new Dimension(70, 30));
        monthSp.setPreferredSize(new Dimension(55, 30));
        daySp.setPreferredSize(new Dimension(55, 30));

        JButton dateBt = new JButton("시간 조회");

        calendarPan.add(yearSp);  calendarPan.add(new JLabel("년"));
        calendarPan.add(monthSp); calendarPan.add(new JLabel("월"));
        calendarPan.add(daySp);   calendarPan.add(new JLabel("일"));
        calendarPan.add(dateBt);

        // ===== 시간 버튼 패널 =====
        timeButtonPan = new JPanel(new GridLayout(0, 4, 5, 5));
        timeButtonPan.setBorder(BorderFactory.createTitledBorder("시간 선택 (연속 선택 가능)"));

        dateBt.addActionListener(e -> {
            int year  = (int) yearSp.getValue();
            int month = (int) monthSp.getValue();
            int day   = (int) daySp.getValue();
            showTimeButtons(LocalDateTime.of(year, month, day, 0, 0));
        });

        // ===== 예약 정보 패널 =====
        JPanel inputPan = new JPanel(new GridBagLayout());
        inputPan.setBorder(BorderFactory.createTitledBorder("예약 정보"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        selectedTimeL = new JLabel("선택된 시간: 없음");
        userCountSp   = new JSpinner(new SpinnerNumberModel(
            history.getUser_count(), 1, 20, 1));

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        inputPan.add(new JLabel("예약 룸"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7; gbc.gridwidth = 3;
        inputPan.add(new JLabel("룸" + history.getRoom_id()), gbc);
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
        confirmBt = new JButton("수정 확정");
        cancelBt  = new JButton("취소");

        confirmBt.addActionListener(e -> {
            try {
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

                Room_history updated = new Room_history();
                updated.setId(originalHistory.getId());
                updated.setRoom_id(originalHistory.getRoom_id());
                updated.setUser_id(currentUser.getId());
                updated.setStart_time(start);
                updated.setEnd_time(end);
                updated.setUser_count(count);

                service.updateReservation(updated);

                dialog.show("수정 완료! " + startH + ":00 ~ " + endH + ":00");
                form.setVisible(false);
                onSuccess.run();
            } catch (Exception ex) {
                dialog.show(ex.getMessage());
            }
        });

        cancelBt.addActionListener(e -> form.setVisible(false));
        buttonPan.add(confirmBt);
        buttonPan.add(cancelBt);

        // ===== 조립 =====
        JPanel centerPan = new JPanel(new BorderLayout(5, 5));
        centerPan.add(timeButtonPan, BorderLayout.NORTH);
        centerPan.add(inputPan, BorderLayout.CENTER);

        form.add(calendarPan, BorderLayout.NORTH);
        form.add(centerPan, BorderLayout.CENTER);
        form.add(buttonPan, BorderLayout.SOUTH);
    }

    private void showTimeButtons(LocalDateTime date) {
        timeButtonPan.removeAll();
        selectedHours.clear();
        selectedTimeL.setText("선택된 시간: 없음");

        int originalStart = originalHistory.getStart_time().getHour();
        int originalEnd   = originalHistory.getEnd_time().getHour();

        // 실제 예약된 시간 가져오기
        List<Boolean> bookedHours = service.getBookedHours(
            originalHistory.getRoom_id(), date
        );

        for (int hour = 9; hour < 22; hour++) {
            final int h = hour;
            JButton timeBt = new JButton(hour + ":00");
            timeBt.setPreferredSize(new Dimension(80, 40));

            if (bookedHours.get(hour) && (hour < originalStart || hour >= originalEnd)) {
                // 다른 사람 예약된 시간 - 빨간색 비활성화
                timeBt.setBackground(Color.RED);
                timeBt.setForeground(Color.WHITE);
                timeBt.setEnabled(false);
            } else if (hour >= originalStart && hour < originalEnd) {
                // 기존 내 예약 시간 - 파란색으로 미리 선택
                timeBt.setBackground(new Color(173, 216, 230));
                timeBt.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
                selectedHours.add(hour);
                timeBt.addActionListener(ev -> toggleTime(h, timeBt));
            } else {
                timeBt.addActionListener(ev -> toggleTime(h, timeBt));
            }
            timeButtonPan.add(timeBt);
        }
        updateSelectedTimeLabel();
        timeButtonPan.revalidate();
        timeButtonPan.repaint();
    }

    private void toggleTime(int h, JButton timeBt) {
        if (selectedHours.contains(h)) {
            // 선택 해제 - 중간 제거 체크
            Collections.sort(selectedHours);
            int min = Collections.min(selectedHours);
            int max = Collections.max(selectedHours);
            if (h != min && h != max) {
                dialog.show("중간 시간은 제거할 수 없습니다. 끝부분부터 제거해주세요.");
                return;
            }
            selectedHours.remove(Integer.valueOf(h));
            timeBt.setBackground(null);
            timeBt.setBorder(UIManager.getBorder("Button.border"));
        } else {
            // 선택 - 연속성 체크
            if (!selectedHours.isEmpty()) {
                int min = Collections.min(selectedHours);
                int max = Collections.max(selectedHours);
                if (h != min - 1 && h != max + 1) {
                    dialog.show("연속된 시간만 선택 가능합니다.");
                    return;
                }
            }
            selectedHours.add(h);
            timeBt.setBackground(new Color(173, 216, 230));
            timeBt.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
        }
        updateSelectedTimeLabel();
    }

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