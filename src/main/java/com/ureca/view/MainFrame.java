package com.ureca.view;

import javax.swing.*;
import java.awt.*;
import com.ureca.dto.User;

public class MainFrame {
    private JFrame main;
    private JTabbedPane tabbedPane;
    private UserPanel userPanel;
    private ReservationPanel reservationPanel;
    private PaymentPanel paymentPanel;

    public MainFrame(User currentUser) {
        main = new JFrame("스터디카페 예약 관리 - " + currentUser.getName());
        tabbedPane = new JTabbedPane();

        userPanel = new UserPanel();
        reservationPanel = new ReservationPanel(currentUser);
        paymentPanel = new PaymentPanel(currentUser);

        tabbedPane.addTab("유저 관리", userPanel);
        tabbedPane.addTab("회의실 예약", reservationPanel);
        tabbedPane.addTab("결제 내역", paymentPanel);

        main.add(tabbedPane);
        main.setSize(1000, 700);
        main.setLocationRelativeTo(null);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setVisible(true);
    }
}