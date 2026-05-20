package com.ureca.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.ureca.dto.PaymentHistory;
import com.ureca.dto.User;
import com.ureca.service.StudyCafeService;
import com.ureca.service.StudyCafeServiceImp;

public class PaymentPanel extends JPanel {
    private MessageDialog dialog;
    private JButton searchBt;
    private JTable paymentTable;
    private DefaultTableModel tableModel;
    private String[] header = {"ID", "유저ID", "룸ID", "결제금액", "결제시간"};
    private StudyCafeService service = new StudyCafeServiceImp();
    private User currentUser;

    public PaymentPanel(User currentUser) {
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

        JPanel titleArea = new JPanel(new GridLayout(2, 1, 0, 4));
        titleArea.setBackground(Theme.BG_SECONDARY);

        JLabel titleLbl = Theme.titleLabel("결제 내역");
        titleLbl.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        JLabel subLbl = new JLabel(currentUser.getName() + "님의 결제 내역을 조회합니다");
        subLbl.setFont(new Font("Malgun Gothic", Font.PLAIN, 12));
        subLbl.setForeground(Color.WHITE);

        titleArea.add(titleLbl);
        titleArea.add(subLbl);

        searchBt = Theme.accentButton("내 결제 내역 조회");

        actionBar.add(titleArea, BorderLayout.WEST);
        actionBar.add(searchBt,  BorderLayout.EAST);

        // ── 테이블 영역 ───────────────────────────────────────
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Theme.BG_SECONDARY);
        tableCard.setBorder(Theme.cardBorder("결제 내역"));

        tableModel = new DefaultTableModel(header, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        paymentTable = new JTable(tableModel);
        Theme.styleTable(paymentTable);
        tableCard.add(Theme.styledScrollPane(paymentTable), BorderLayout.CENTER);

        // ── 이벤트 ────────────────────────────────────────────
        searchBt.addActionListener(e -> {
            try {
                showList(service.searchPaymentByUser(currentUser.getId()));
            } catch (Exception ex) { dialog.show(ex.getMessage()); }
        });

        // ── 조립 ──────────────────────────────────────────────
        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(Theme.BG_PRIMARY);
        center.add(actionBar, BorderLayout.NORTH);
        center.add(tableCard, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);
    }

    public void showList(List<PaymentHistory> list) {
        tableModel.setRowCount(0);
        for (PaymentHistory p : list)
            tableModel.addRow(new Object[]{
                p.getId(), p.getUserId(), p.getRoomId(),
                p.getPrice(), p.getPaymentDate()});
    }
}
