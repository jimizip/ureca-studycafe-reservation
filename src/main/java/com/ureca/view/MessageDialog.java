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
        confirm = new JButton("확인");
        msgL = new JLabel();
        msgL.setHorizontalAlignment(JLabel.CENTER);
        confirm.addActionListener(e -> hide());
        JPanel south = new JPanel();
        south.add(confirm);
        main.add(msgL, "Center");
        main.add(south, "South");
        Toolkit tool = main.getToolkit();
        Dimension screen = tool.getScreenSize();
        cx = (int) screen.getWidth() >> 1;
        cy = (int) screen.getHeight() >> 1;
    }

    public MessageDialog() {}
    public MessageDialog(JFrame owner) { this.owner = owner; }
    public void hide() { main.setVisible(false); }

    public void show(String msg) {
        msgL.setText(msg);
        main.pack();
        int mx, my;
        if (owner == null) {
            mx = cx - (main.getWidth() >> 1);
            my = cy - (main.getHeight() >> 1);
        } else {
            mx = owner.getX() + (owner.getWidth() >> 1) - (main.getWidth() >> 1);
            my = owner.getY() + (owner.getHeight() >> 1) - (main.getHeight() >> 1);
        }
        main.setLocation(mx, my);
        main.setVisible(true);
    }
}