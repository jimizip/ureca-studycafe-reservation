package com.ureca.view;

import javax.swing.*;
import java.awt.*;
import com.ureca.dto.User;
import com.ureca.service.StudyCafeService;
import com.ureca.service.StudyCafeServiceImp;

public class LoginPanel extends JFrame {
    private JTextField userIdTf;
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
        inputPan.add(new JLabel("유저 ID"));
        userIdTf = new JTextField();
        inputPan.add(userIdTf);

        JPanel buttonPan = new JPanel(new FlowLayout());
        loginBt = new JButton("로그인");
        buttonPan.add(loginBt);

        loginBt.addActionListener(e -> {
            try {
                int userId = Integer.parseInt(userIdTf.getText());
                User user = service.searchUser(userId);
                new MainFrame(user);
                setVisible(false);
            } catch (NumberFormatException ex) {
                dialog.show("ID는 숫자로 입력해주세요");
            } catch (Exception ex) {
                dialog.show(ex.getMessage());
            }
        });

        add(inputPan, BorderLayout.CENTER);
        add(buttonPan, BorderLayout.SOUTH);
        setVisible(true);
    }
}