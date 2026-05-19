package com.ureca.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
        setLayout(new BorderLayout(10, 10));

        // 상단 버튼
        JPanel topPan = new JPanel(new FlowLayout(FlowLayout.LEFT));
        reserveBt = new JButton("회의실 예약");
        updateBt  = new JButton("예약 수정");
        deleteBt  = new JButton("예약 취소");
        searchBt  = new JButton("내 예약 조회");

        topPan.add(reserveBt);
        topPan.add(searchBt);
        topPan.add(updateBt);
        topPan.add(deleteBt);

        // 테이블
        tableModel = new DefaultTableModel(header, 0);
        historyTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("예약 목록"));

        // 예약 버튼 → ReservationForm 팝업
        reserveBt.addActionListener(e -> {
            new ReservationForm(currentUser, service).show();
        });

        // 내 예약 조회
        searchBt.addActionListener(e -> {
            try {
                // TODO: service.searchHistoryByUser(currentUser.getId()) 연결
                // showList(service.searchHistoryByUser(currentUser.getId()));
                dialog.show("서비스 연결 후 동작");
            } catch (Exception ex) {
                dialog.show(ex.getMessage());
            }
        });

        // 예약 수정
        updateBt.addActionListener(e -> {
            int row = historyTable.getSelectedRow();
            if (row == -1) {
                dialog.show("수정할 예약을 선택해주세요");
                return;
            }
            // TODO: service.updateReservation() 연결
        });

        // 예약 취소
        deleteBt.addActionListener(e -> {
            int row = historyTable.getSelectedRow();
            if (row == -1) {
                dialog.show("취소할 예약을 선택해주세요");
                return;
            }
            // TODO
            // int historyId = (int) tableModel.getValueAt(row, 0);
            // service.cancel(historyId);
            dialog.show("서비스 연결 후 동작");
        });

        add(topPan, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void showList(java.util.List<Room_history> list) {
        tableModel.setRowCount(0);
        for (Room_history r : list) {
            tableModel.addRow(new Object[]{
                r.getId(),
                r.getRoom_id(),
                r.getUser_id(),
                r.getStart_time(),
                r.getEnd_time(),
                r.getUser_count()
            });
        }
    }
}