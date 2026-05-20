package com.ureca.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import com.ureca.dto.User;
import com.ureca.dto.Room_history;
import com.ureca.service.StudyCafeService;
import com.ureca.service.StudyCafeServiceImp;

public class ReservationPanel extends JPanel {
    private MessageDialog dialog;
    private JButton reserveBt, updateBt, deleteBt, searchBt;
    private JTable historyTable;
    private DefaultTableModel tableModel;
    private String[] header = {"ID", "룸ID", "유저ID", "시작시간", "종료시간", "인원수"};
    private StudyCafeService service = new StudyCafeServiceImp();
    private User currentUser;

    public ReservationPanel(User currentUser) {
        this.currentUser = currentUser;
        dialog = new MessageDialog();
        setLayout(new BorderLayout(0, 0));
        setBackground(Theme.BG_PRIMARY);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        // ── 상단 액션 바 ──────────────────────────────────────
        JPanel actionBar = new JPanel(new BorderLayout());
        actionBar.setBackground(Theme.BG_SECONDARY);
        actionBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)));

        JLabel titleLbl = Theme.titleLabel("회의실 예약 관리");
        titleLbl.setFont(new Font("Malgun Gothic", Font.BOLD, 14));

        JPanel btnGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnGroup.setBackground(Theme.BG_SECONDARY);

        reserveBt = Theme.accentButton("+ 회의실 예약");
        searchBt  = Theme.defaultButton("내 예약 조회");
        updateBt  = Theme.defaultButton("예약 수정");
        deleteBt  = Theme.dangerButton("예약 취소");

        btnGroup.add(searchBt);
        btnGroup.add(updateBt);
        btnGroup.add(deleteBt);
        btnGroup.add(reserveBt);

        actionBar.add(titleLbl, BorderLayout.WEST);
        actionBar.add(btnGroup, BorderLayout.EAST);

        // ── 테이블 영역 ───────────────────────────────────────
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Theme.BG_SECONDARY);
        tableCard.setBorder(Theme.cardBorder("예약 목록"));

        tableModel = new DefaultTableModel(header, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        historyTable = new JTable(tableModel);
        Theme.styleTable(historyTable);
        tableCard.add(Theme.styledScrollPane(historyTable), BorderLayout.CENTER);

        // ── 하단 힌트 ─────────────────────────────────────────
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
        footer.setBackground(Theme.BG_PRIMARY);
        footer.add(Theme.dimLabel("※ 행을 선택한 후 수정 / 취소 버튼을 이용하세요"));

        // ── 이벤트 ────────────────────────────────────────────
        reserveBt.addActionListener(e -> new ReservationForm(currentUser, service).show());

        searchBt.addActionListener(e -> {
            try {
                showList(service.searchHistoryByUser(currentUser.getId()));
            } catch (Exception ex) { dialog.show(ex.getMessage()); }
        });

        updateBt.addActionListener(e -> {
            int row = historyTable.getSelectedRow();
            if (row == -1) { dialog.show("수정할 예약을 선택해주세요"); return; }
            try {
                Room_history selected = new Room_history();
                selected.setId(Integer.parseInt(tableModel.getValueAt(row, 0).toString()));
                selected.setRoom_id(Integer.parseInt(tableModel.getValueAt(row, 1).toString()));
                selected.setUser_id(Integer.parseInt(tableModel.getValueAt(row, 2).toString()));
                selected.setStart_time((LocalDateTime) tableModel.getValueAt(row, 3));
                selected.setEnd_time((LocalDateTime) tableModel.getValueAt(row, 4));
                selected.setUser_count(Integer.parseInt(tableModel.getValueAt(row, 5).toString()));

                new UpdateReservationForm(currentUser, selected, service, () -> {
                    List<Room_history> list = service.searchHistoryByUser(currentUser.getId());
                    showList(list);
                }).show();
            } catch (Exception ex) { dialog.show(ex.getMessage()); }
        });

        deleteBt.addActionListener(e -> {
            int row = historyTable.getSelectedRow();
            if (row == -1) { dialog.show("취소할 예약을 선택해주세요"); return; }
            try {
                int historyId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                service.cancel(historyId);
                dialog.show("예약이 취소되었습니다.");
                showList(service.searchHistoryByUser(currentUser.getId()));
            } catch (Exception ex) { dialog.show(ex.getMessage()); }
        });

        // ── 조립 ──────────────────────────────────────────────
        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(Theme.BG_PRIMARY);
        center.add(actionBar, BorderLayout.NORTH);
        center.add(tableCard, BorderLayout.CENTER);
        center.add(footer,    BorderLayout.SOUTH);

        add(center, BorderLayout.CENTER);
    }

    public void showList(java.util.List<Room_history> list) {
        tableModel.setRowCount(0);
        for (Room_history r : list)
            tableModel.addRow(new Object[]{
                r.getId(), r.getRoom_id(), r.getUser_id(),
                r.getStart_time(),
                r.getEnd_time(),
                r.getUser_count()});
    }
}
