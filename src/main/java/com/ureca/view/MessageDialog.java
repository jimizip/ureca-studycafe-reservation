package com.ureca.view;

import java.awt.*;
import javax.swing.*;

public class MessageDialog {
    private JFrame main, owner;
    private JButton confirm;
    private JLabel msgL;
    private int cx, cy;

    {
        main = new JFrame("알림");
        main.getContentPane().setBackground(Theme.BG_SECONDARY);

        // 상단 골드 포인트 바
        JPanel accentBar = new JPanel();
        accentBar.setBackground(Theme.ACCENT);
        accentBar.setPreferredSize(new Dimension(0, 3));

        msgL = new JLabel();
        msgL.setHorizontalAlignment(JLabel.CENTER);
        msgL.setFont(Theme.FONT_BODY);
        msgL.setForeground(Theme.TEXT_PRIMARY);
        msgL.setBorder(BorderFactory.createEmptyBorder(18, 24, 12, 24));

        confirm = Theme.accentButton("확인");
        confirm.addActionListener(e -> hide());

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        south.setBackground(Theme.BG_SECONDARY);
        south.add(confirm);

        main.add(accentBar, BorderLayout.NORTH);
        main.add(msgL,      BorderLayout.CENTER);
        main.add(south,     BorderLayout.SOUTH);

        Toolkit tool = main.getToolkit();
        Dimension screen = tool.getScreenSize();
        cx = (int) screen.getWidth()  >> 1;
        cy = (int) screen.getHeight() >> 1;
    }

    public MessageDialog() {}
    public MessageDialog(JFrame owner) { this.owner = owner; }
    public void hide() { main.setVisible(false); }

    public void show(String msg) {
        msgL.setText(msg);
        main.pack();
        main.setMinimumSize(new Dimension(260, 120));
        int mx, my;
        if (owner == null) {
            mx = cx - (main.getWidth() >> 1);
            my = cy - (main.getHeight() >> 1);
        } else {
            mx = owner.getX() + (owner.getWidth()  >> 1) - (main.getWidth()  >> 1);
            my = owner.getY() + (owner.getHeight() >> 1) - (main.getHeight() >> 1);
        }
        main.setLocation(mx, my);
        main.setVisible(true);
    }
}
