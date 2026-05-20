package com.ureca.view;

import javax.swing.*;
import java.awt.*;
import com.ureca.dto.User;
import com.ureca.service.StudyCafeService;
import com.ureca.service.StudyCafeServiceImp;

public class LoginPanel extends JFrame {
    private JTextField emailTf;  // userIdTf → emailTf
    private JButton loginBt;
    private MessageDialog dialog;
    private StudyCafeService service = new StudyCafeServiceImp();

    public LoginPanel() {
        dialog = new MessageDialog();
        setTitle("스터디카페 예약 시스템");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPan = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPan.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        inputPan.add(new JLabel("이메일"));  // "유저 ID" → "이메일"
        emailTf = new JTextField();
        inputPan.add(emailTf);

        JPanel buttonPan = new JPanel(new FlowLayout());
        loginBt = new JButton("로그인");
        buttonPan.add(loginBt);

        loginBt.addActionListener(e -> {
            try {
                String email = emailTf.getText();
                if (email.isEmpty()) {
                    dialog.show("이메일을 입력해주세요");
                    return;
                }
                User user = service.searchUserByEmail(email);
                new MainFrame(user);
                setVisible(false);
            } catch (Exception ex) {
                dialog.show(ex.getMessage());
            }
        });

        add(inputPan, BorderLayout.CENTER);
        add(buttonPan, BorderLayout.SOUTH);
        setVisible(true);
    }
}