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
        setLayout(new BorderLayout(10, 10));

        // 검색 패널
        JPanel searchPan = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPan.setBorder(BorderFactory.createTitledBorder("결제 내역 조회"));
        searchBt = new JButton("내 결제 내역 조회");
        searchPan.add(new JLabel(currentUser.getName() + "님의 결제 내역"));
        searchPan.add(searchBt);

        // 테이블
        tableModel = new DefaultTableModel(header, 0);
        paymentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(paymentTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("결제 내역"));

        searchBt.addActionListener(e -> {
            try {
                List<PaymentHistory> list = service.searchPaymentByUser(currentUser.getId());
                showList(list);
            } catch (Exception ex) {
                dialog.show(ex.getMessage());
            }
        });

        add(searchPan, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void showList(List<PaymentHistory> list) {
        tableModel.setRowCount(0);
        for (PaymentHistory p : list) {
            tableModel.addRow(new Object[]{
                p.getId(),
                p.getUserId(),
                p.getRoomId(),
                p.getPrice(),
                p.getPaymentDate()
            });
        }
    }
}