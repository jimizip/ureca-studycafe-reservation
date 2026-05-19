package com.ureca.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.ureca.dto.User;
import com.ureca.service.StudyCafeService;
import com.ureca.service.StudyCafeServiceImp;

public class UserPanel extends JPanel {
    private MessageDialog dialog;
    private JTextField nameTf, telTf, emailTf, searchIdTf;
    private JButton insertBt, deleteBt, searchBt, clearBt;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private String[] header = {"ID", "이름", "전화번호", "이메일"};
    private StudyCafeService service = new StudyCafeServiceImp();

    public UserPanel() {
        dialog = new MessageDialog();
        setLayout(new BorderLayout(10, 10));

        // 입력 폼
        JPanel formPan = new JPanel(new GridLayout(5, 2, 5, 5));
        formPan.setBorder(BorderFactory.createTitledBorder("유저 정보"));

        formPan.add(new JLabel("이름"));
        nameTf = new JTextField();
        formPan.add(nameTf);

        formPan.add(new JLabel("전화번호"));
        telTf = new JTextField();
        formPan.add(telTf);

        formPan.add(new JLabel("이메일"));
        emailTf = new JTextField();
        formPan.add(emailTf);

        formPan.add(new JLabel("ID (삭제용)"));
        searchIdTf = new JTextField();
        formPan.add(searchIdTf);

        // 버튼
        JPanel buttonPan = new JPanel(new GridLayout(1, 4, 5, 5));
        insertBt = new JButton("등록");
        deleteBt = new JButton("삭제");
        searchBt = new JButton("전체조회");
        clearBt  = new JButton("초기화");

        buttonPan.add(insertBt);
        buttonPan.add(deleteBt);
        buttonPan.add(searchBt);
        buttonPan.add(clearBt);

        formPan.add(new JLabel());
        formPan.add(buttonPan);

        // 테이블
        tableModel = new DefaultTableModel(header, 0);
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("유저 목록"));

        // 테이블 클릭 시 입력폼 자동 채우기
        userTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = userTable.getSelectedRow();
                searchIdTf.setText((String) tableModel.getValueAt(row, 0));
                nameTf.setText((String) tableModel.getValueAt(row, 1));
                telTf.setText((String) tableModel.getValueAt(row, 2));
                emailTf.setText((String) tableModel.getValueAt(row, 3));
            }
        });

        // 등록
        insertBt.addActionListener(e -> {
            try {
                User user = new User();
                user.setName(nameTf.getText());
                user.setTel(telTf.getText());
                user.setEmail(emailTf.getText());
                service.addUser(user);
                dialog.show("등록 성공");
                showList();
                clear();
            } catch (Exception ex) {
                dialog.show(ex.getMessage());
            }
        });

        // 삭제
        deleteBt.addActionListener(e -> {
            try {
                // TODO: service.removeUser() 연결
                dialog.show("삭제 성공");
                showList();
                clear();
            } catch (Exception ex) {
                dialog.show(ex.getMessage());
            }
        });

        // 전체 조회
        searchBt.addActionListener(e -> {
            try {
                showList();
            } catch (Exception ex) {
                dialog.show(ex.getMessage());
            }
        });

        clearBt.addActionListener(e -> clear());

        add(formPan, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void clear() {
        nameTf.setText("");
        telTf.setText("");
        emailTf.setText("");
        searchIdTf.setText("");
    }

    public void showList() {
        List<User> list = service.searchAllUsers();
        tableModel.setRowCount(0);
        for (User u : list) {
            tableModel.addRow(new Object[]{
                String.valueOf(u.getId()),
                u.getName(),
                u.getTel(),
                u.getEmail()
            });
        }
    }
}