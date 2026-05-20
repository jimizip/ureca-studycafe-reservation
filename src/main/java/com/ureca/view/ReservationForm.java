package com.ureca.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.ureca.dto.Room_history;
import com.ureca.dto.Room;
import com.ureca.dto.User;
import com.ureca.service.StudyCafeService;

public class ReservationForm {
    private JFrame form;
    private MessageDialog dialog;

    private JSpinner yearSp, monthSp, daySp;
    private JPanel roomButtonPan, timeButtonPan;
    private List<JButton> timeButtons = new ArrayList<>();
    private List<Integer> selectedHours = new ArrayList<>();

    private JSpinner userCountSp;
    private JLabel selectedRoomL, selectedTimeL, priceL;
    private JButton confirmBt, cancelBt;
    private int selectedRoomId = -1;
    private int selectedRoomPrice = 0;  // 시간당 가격
    private User currentUser;
    private StudyCafeService service;

    public ReservationForm(User currentUser, StudyCafeService service) {
        this.currentUser = currentUser;
        this.service = service;
        dialog = new MessageDialog();

        form = new JFrame("회의실 예약");
        form.setUndecorated(true);
        form.setSize(700, 680);
        form.setLocationRelativeTo(null);
        form.setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_PRIMARY);
        form.setContentPane(root);

        // ── 헤더 ──────────────────────────────────────────────
        JPanel accentLine = new JPanel();
        accentLine.setBackground(Theme.BG_PRIMARY);
        accentLine.setPreferredSize(new Dimension(0, 1));

        JPanel headerBar = new JPanel(new BorderLayout());
        headerBar.setBackground(Theme.BG_SECONDARY);
        headerBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)));
        JLabel titleLbl = Theme.titleLabel("회의실 예약");
        titleLbl.setFont(new Font("Malgun Gothic", Font.BOLD, 15));
        headerBar.add(titleLbl, BorderLayout.WEST);

        JButton closeBtn = makeCloseButton();
        closeBtn.addActionListener(e -> form.setVisible(false));
        JPanel closePan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        closePan.setBackground(Theme.BG_SECONDARY);
        closePan.add(closeBtn);
        headerBar.add(closePan, BorderLayout.EAST);

        JPanel headerWrap = new JPanel(new BorderLayout());
        headerWrap.setBackground(Theme.BG_PRIMARY);
        headerWrap.add(accentLine, BorderLayout.NORTH);
        headerWrap.add(headerBar,  BorderLayout.CENTER);

        // ── 날짜 선택 (roundedSpinner 사용) ──────────────────
        JPanel calendarPan = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 12));
        calendarPan.setBackground(Theme.BG_SECONDARY);
        calendarPan.setBorder(Theme.cardBorder("날짜 선택"));

        yearSp  = Theme.roundedSpinner(2026, 2024, 2030, 1);
        monthSp = Theme.roundedSpinner(1, 1, 12, 1);
        daySp   = Theme.roundedSpinner(1, 1, 31, 1);

        yearSp.setPreferredSize(new Dimension(80, 32));
        monthSp.setPreferredSize(new Dimension(64, 32));
        daySp.setPreferredSize(new Dimension(64, 32));

        JButton dateBt = Theme.accentButton("룸 조회");

        calendarPan.add(yearSp);  calendarPan.add(Theme.dimLabel("년"));
        calendarPan.add(monthSp); calendarPan.add(Theme.dimLabel("월"));
        calendarPan.add(daySp);   calendarPan.add(Theme.dimLabel("일"));
        calendarPan.add(Box.createHorizontalStrut(6));
        calendarPan.add(dateBt);

        // ── 룸 버튼 패널 ──────────────────────────────────────
        roomButtonPan = new JPanel(new GridLayout(0, 1, 0, 6));
        roomButtonPan.setBackground(Theme.BG_SECONDARY);
        roomButtonPan.setBorder(Theme.cardBorder("예약 가능 회의실"));

        // ── 시간 버튼 패널 ────────────────────────────────────
        timeButtonPan = new JPanel(new GridLayout(0, 5, 6, 6));
        timeButtonPan.setBackground(Theme.BG_SECONDARY);
        timeButtonPan.setBorder(Theme.cardBorder("시간 선택  (1시간 단위 · 연속 선택 가능)"));

        // ── 예약 정보 패널 ────────────────────────────────────
        JPanel inputPan = new JPanel(new GridBagLayout());
        inputPan.setBackground(Theme.BG_SECONDARY);
        inputPan.setBorder(Theme.cardBorder("예약 정보 확인"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        selectedRoomL = new JLabel("선택된 룸: 없음");
        selectedRoomL.setFont(new Font("Malgun Gothic", Font.PLAIN, 13));
        selectedRoomL.setForeground(Color.WHITE);

        selectedTimeL = new JLabel("선택된 시간: 없음");
        selectedTimeL.setFont(new Font("Malgun Gothic", Font.PLAIN, 13));
        selectedTimeL.setForeground(Color.WHITE);

        // 인원수 스피너 - roundedSpinner
        userCountSp = Theme.roundedSpinner(1, 1, 20, 1);
        userCountSp.setPreferredSize(new Dimension(90, 32));

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.ipadx = 70;
        inputPan.add(makeInfoLabel("선택 회의실"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.ipadx = 0;
        inputPan.add(selectedRoomL, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        inputPan.add(makeInfoLabel("선택 시간"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        inputPan.add(selectedTimeL, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        inputPan.add(makeInfoLabel("인원수"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.anchor = GridBagConstraints.WEST; gbc.fill = GridBagConstraints.NONE;
        inputPan.add(userCountSp, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.CENTER;

        // ── 가격 행 ───────────────────────────────────────────
        priceL = new JLabel("");
        priceL.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        priceL.setForeground(Theme.ACCENT);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        inputPan.add(makeInfoLabel("예상 금액"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        inputPan.add(priceL, gbc);

        // ── 하단 버튼 ─────────────────────────────────────────
        cancelBt  = Theme.defaultButton("취소");
        confirmBt = Theme.accentButton("예약 확정");

        JPanel buttonPan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        buttonPan.setBackground(Theme.BG_SECONDARY);
        buttonPan.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER));
        buttonPan.add(cancelBt);
        buttonPan.add(confirmBt);

        // ── 이벤트: 룸 조회 ───────────────────────────────────
        dateBt.addActionListener(e -> {
            roomButtonPan.removeAll();
            timeButtonPan.removeAll();
            timeButtons.clear();
            selectedHours.clear();
            selectedRoomId = -1;
            selectedRoomL.setText("선택된 룸: 없음"); selectedRoomL.setForeground(Theme.TEXT_DIM);
            selectedTimeL.setText("선택된 시간: 없음"); selectedTimeL.setForeground(Theme.TEXT_DIM);
            selectedRoomPrice = 0;
            priceL.setText(""); priceL.setForeground(Theme.ACCENT);
            try {
                int year  = (int) yearSp.getValue();
                int month = (int) monthSp.getValue();
                int day   = (int) daySp.getValue();
                List<Room> rooms = service.searchAvailableRooms(
                    LocalDateTime.of(year, month, day, 0, 0),
                    LocalDateTime.of(year, month, day, 23, 59));
                for (Room room : rooms) {
                    final int roomId = room.getId();
                    JButton roomBt = makeRoomButton(room);
                    roomBt.addActionListener(ev -> {
                        for (Component c : roomButtonPan.getComponents()) {
                            if (c instanceof JButton) {
                                c.setBackground(Theme.BG_CARD);
                                ((JButton)c).putClientProperty("selected", false);
                                c.repaint();
                            }
                        }
                        roomBt.setBackground(new Color(0x1A2E1A));
                        roomBt.putClientProperty("selected", true);
                        roomBt.repaint();
                        selectedRoomId = roomId;
                        selectedRoomPrice = room.getPrice();
                        selectedRoomL.setText("룸 " + roomId + "  (최대 " + room.getRoom_size()
                            + "명 · 시간당 " + String.format("%,d", room.getPrice()) + "원)");
                        selectedRoomL.setForeground(Theme.SUCCESS);
                        showTimeButtons(roomId, LocalDateTime.of(year, month, day, 0, 0));
                    });
                    roomButtonPan.add(roomBt);
                }
                roomButtonPan.revalidate();
                roomButtonPan.repaint();
            } catch (Exception ex) { dialog.show(ex.getMessage()); }
        });

        // ── 이벤트: 예약 확정 ─────────────────────────────────
        confirmBt.addActionListener(e -> {
            try {
                if (selectedRoomId == -1)    { dialog.show("회의실을 선택해주세요"); return; }
                if (selectedHours.isEmpty()) { dialog.show("시간을 선택해주세요");   return; }
                int year  = (int) yearSp.getValue();
                int month = (int) monthSp.getValue();
                int day   = (int) daySp.getValue();
                int count = (int) userCountSp.getValue();
                Collections.sort(selectedHours);
                int startH = selectedHours.get(0);
                int endH   = selectedHours.get(selectedHours.size()-1) + 1;
                Room_history history = new Room_history();
                history.setRoom_id(selectedRoomId);
                history.setUser_id(currentUser.getId());
                history.setStart_time(LocalDateTime.of(year, month, day, startH, 0));
                history.setEnd_time(LocalDateTime.of(year, month, day, endH, 0));
                history.setUser_count(count);
                service.reserve(history);
                dialog.show("예약 완료!  " + startH + ":00 ~ " + endH + ":00");
                form.setVisible(false);
            } catch (Exception ex) { dialog.show(ex.getMessage()); }
        });

        cancelBt.addActionListener(e -> form.setVisible(false));

        // ── 스크롤 영역 ───────────────────────────────────────
        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Theme.BG_PRIMARY);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        scrollContent.add(calendarPan);
        scrollContent.add(Box.createVerticalStrut(10));
        scrollContent.add(roomButtonPan);
        scrollContent.add(Box.createVerticalStrut(10));
        scrollContent.add(timeButtonPan);
        scrollContent.add(Box.createVerticalStrut(10));
        scrollContent.add(inputPan);

        JScrollPane mainScroll = new JScrollPane(scrollContent);
        mainScroll.setBackground(Theme.BG_PRIMARY);
        mainScroll.getViewport().setBackground(Theme.BG_PRIMARY);
        mainScroll.setBorder(BorderFactory.createEmptyBorder());
        Theme.styleScrollBar(mainScroll.getVerticalScrollBar());

        root.add(headerWrap, BorderLayout.NORTH);
        root.add(mainScroll, BorderLayout.CENTER);
        root.add(buttonPan,  BorderLayout.SOUTH);
    }

    private JButton makeRoomButton(Room room) {
        JButton btn = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), Theme.R_SMALL, Theme.R_SMALL);
                boolean sel = Boolean.TRUE.equals(getClientProperty("selected"));
                g2.setColor(sel ? Theme.SUCCESS : Theme.BORDER);
                g2.setStroke(new java.awt.BasicStroke(sel ? 1.5f : 1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, Theme.R_SMALL, Theme.R_SMALL);
                g2.dispose();
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        btn.setLayout(new BorderLayout());
        btn.setBackground(Theme.BG_CARD);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        JLabel nameL = new JLabel("룸 " + room.getId());
        nameL.setFont(Theme.FONT_HEADER);
        nameL.setForeground(Theme.TEXT_PRIMARY);

        JLabel infoL = new JLabel("최대 " + room.getRoom_size() + "명  ·  시간당 "
            + String.format("%,d", room.getPrice()) + "원");
        infoL.setFont(Theme.FONT_SMALL);
        infoL.setForeground(Theme.TEXT_DIM);

        btn.add(nameL, BorderLayout.WEST);
        btn.add(infoL, BorderLayout.EAST);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!Boolean.TRUE.equals(btn.getClientProperty("selected"))) {
                    btn.setBackground(Theme.BG_HOVER); btn.repaint();
                }
            }
            public void mouseExited(MouseEvent e) {
                if (!Boolean.TRUE.equals(btn.getClientProperty("selected"))) {
                    btn.setBackground(Theme.BG_CARD); btn.repaint();
                }
            }
        });
        return btn;
    }

    private void showTimeButtons(int roomId, LocalDateTime date) {
        timeButtonPan.removeAll();
        timeButtons.clear();
        selectedHours.clear();
        selectedTimeL.setText("선택된 시간: 없음");
        selectedTimeL.setForeground(Theme.TEXT_DIM);

        List<Boolean> bookedHours = service.getBookedHours(roomId, date);

        for (int hour = 9; hour < 22; hour++) {
            final int h = hour;
            JButton timeBt = Theme.timeButton(hour + ":00");
            timeBt.setPreferredSize(new Dimension(80, 42));

            if (bookedHours.get(hour)) {
                Theme.timeBtn_booked(timeBt);
                timeBt.setEnabled(false);
                timeBt.setToolTipText("이미 예약된 시간입니다");
            } else {
                timeBt.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        if (!selectedHours.contains(h)) { timeBt.setBackground(Theme.BG_HOVER); timeBt.repaint(); }
                    }
                    public void mouseExited(MouseEvent e) {
                        if (!selectedHours.contains(h)) Theme.timeBtn_default(timeBt);
                    }
                });
                timeBt.addActionListener(ev -> toggleTime(h, timeBt));
            }
            timeButtons.add(timeBt);
            timeButtonPan.add(timeBt);
        }
        timeButtonPan.revalidate();
        timeButtonPan.repaint();
    }

    private void toggleTime(int h, JButton timeBt) {
        if (selectedHours.contains(h)) {
            Collections.sort(selectedHours);
            int min = Collections.min(selectedHours), max = Collections.max(selectedHours);
            if (h != min && h != max) { dialog.show("중간 시간은 제거할 수 없습니다. 끝부분부터 제거해주세요."); return; }
            selectedHours.remove(Integer.valueOf(h));
            Theme.timeBtn_default(timeBt);
        } else {
            if (!selectedHours.isEmpty()) {
                int min = Collections.min(selectedHours), max = Collections.max(selectedHours);
                if (h != min-1 && h != max+1) { dialog.show("연속된 시간만 선택 가능합니다."); return; }
            }
            selectedHours.add(h);
            Theme.timeBtn_selected(timeBt);
        }
        updateSelectedTimeLabel();
    }

    private void updateSelectedTimeLabel() {
        if (selectedHours.isEmpty()) {
            selectedTimeL.setText("선택된 시간: 없음"); selectedTimeL.setForeground(Theme.TEXT_DIM);
            priceL.setText("");
        } else {
            Collections.sort(selectedHours);
            int s = selectedHours.get(0), e = selectedHours.get(selectedHours.size()-1)+1;
            int hours = selectedHours.size();
            selectedTimeL.setText(s + ":00 ~ " + e + ":00  (" + hours + "시간)");
            selectedTimeL.setForeground(Theme.ACCENT);
            // 가격 계산: 시간당 가격 × 시간 수
            if (selectedRoomPrice > 0) {
                int total = selectedRoomPrice * hours;
                priceL.setText(String.format("%,d원", total)
                    + "  (" + String.format("%,d", selectedRoomPrice) + "원 × " + hours + "시간)");
            }
        }
    }

    private JButton makeCloseButton() {
        JButton btn = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                int cx = getWidth() / 2, cy = getHeight() / 2;
                g2.setColor(Color.WHITE);
                g2.setStroke(new java.awt.BasicStroke(1.5f,
                    java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
                g2.drawLine(cx - 4, cy - 4, cx + 4, cy + 4);
                g2.drawLine(cx + 4, cy - 4, cx - 4, cy + 4);
                g2.dispose();
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        btn.setBackground(new Color(0x7A2020));
        btn.setOpaque(false); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false); btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(32, 22));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(Theme.DANGER); btn.repaint(); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(new Color(0x7A2020)); btn.repaint(); }
        });
        return btn;
    }

    private JLabel makeInfoLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
        l.setForeground(Color.WHITE);
        return l;
    }

    public void show() { form.setVisible(true); }
}
