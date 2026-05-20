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
    private JButton insertBt, deleteBt, searchBt;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private String[] header = {"ID", "이름", "전화번호", "이메일"};
    private StudyCafeService service = new StudyCafeServiceImp();

    public UserPanel() {
        dialog = new MessageDialog();
        setLayout(new BorderLayout(0, 0));
        setBackground(Theme.BG_PRIMARY);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        // ── 입력 폼 카드 ──────────────────────────────────────
        JPanel formCard = new JPanel(new BorderLayout(0, 12)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.BG_SECONDARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), Theme.R_MEDIUM, Theme.R_MEDIUM);
                g2.dispose();
            }
        };
        formCard.setOpaque(false);
        formCard.setBorder(Theme.cardBorder("유저 정보 입력"));

        // ── 폼 그리드 ─────────────────────────────────────────
        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 8, 5, 8);

        // 필드 정의: [레이블, placeholder 역할]
        String[][] fieldDefs = {
            {"이름",       "이름을 입력하세요"},
            {"전화번호",   "전화번호를 입력하세요"},
            {"이메일",     "이메일을 입력하세요"},
            {"ID (삭제용)","삭제할 유저 ID"}
        };

        JTextField[] fields = new JTextField[4];
        for (int i = 0; i < fieldDefs.length; i++) {
            // 레이블 — 밝고 볼드
            JLabel lbl = Theme.formLabel(fieldDefs[i][0]);
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0; gbc.ipadx = 80;
            formGrid.add(lbl, gbc);

            // 둥근 텍스트필드
            fields[i] = Theme.roundedTextField();
            fields[i].setPreferredSize(new Dimension(240, 34));
            gbc.gridx = 1; gbc.gridy = i; gbc.weightx = 1; gbc.ipadx = 0;
            formGrid.add(fields[i], gbc);
        }
        nameTf = fields[0]; telTf = fields[1]; emailTf = fields[2]; searchIdTf = fields[3];

        // ── 버튼 영역 ─────────────────────────────────────────
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setOpaque(false);
        btnRow.setBorder(BorderFactory.createEmptyBorder(6, 0, 2, 0));

        insertBt = Theme.accentButton("등록");
        deleteBt = Theme.dangerButton("삭제");
        searchBt = Theme.defaultButton("전체 조회");

        btnRow.add(searchBt);
        btnRow.add(deleteBt);
        btnRow.add(insertBt);

        formCard.add(formGrid, BorderLayout.CENTER);
        formCard.add(btnRow,   BorderLayout.SOUTH);

        // ── 테이블 카드 ───────────────────────────────────────
        JPanel tableCard = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.BG_SECONDARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), Theme.R_MEDIUM, Theme.R_MEDIUM);
                g2.dispose();
            }
        };
        tableCard.setOpaque(false);
        tableCard.setBorder(Theme.cardBorder("유저 목록"));

        tableModel = new DefaultTableModel(header, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        userTable = new JTable(tableModel);
        Theme.styleTable(userTable);
        tableCard.add(Theme.styledScrollPane(userTable), BorderLayout.CENTER);

        // 테이블 클릭 → 폼 자동 채우기
        userTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = userTable.getSelectedRow();
                if (row < 0) return;
                searchIdTf.setText((String) tableModel.getValueAt(row, 0));
                nameTf.setText    ((String) tableModel.getValueAt(row, 1));
                telTf.setText     ((String) tableModel.getValueAt(row, 2));
                emailTf.setText   ((String) tableModel.getValueAt(row, 3));
            }
        });

        // ── 이벤트 ────────────────────────────────────────────
        insertBt.addActionListener(e -> {
            try {
                User user = new User();
                user.setName(nameTf.getText());
                user.setTel(telTf.getText());
                user.setEmail(emailTf.getText());
                service.addUser(user);
                dialog.show("등록 성공");
                showList(); clear();
            } catch (Exception ex) { dialog.show(ex.getMessage()); }
        });

        deleteBt.addActionListener(e -> {
            try {
                int id = Integer.parseInt(searchIdTf.getText());
                service.removeUser(id);
                dialog.show("삭제 성공");
                showList(); clear();
            } catch (NumberFormatException ex) {
                dialog.show("ID를 입력해주세요");
            } catch (Exception ex) { dialog.show(ex.getMessage()); }
        });

        searchBt.addActionListener(e -> {
            try { showList(); }
            catch (Exception ex) { dialog.show(ex.getMessage()); }
        });

        // ── 조립 ──────────────────────────────────────────────
        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(Theme.BG_PRIMARY);
        center.add(formCard,  BorderLayout.NORTH);
        center.add(tableCard, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);
    }

    private void clear() {
        nameTf.setText(""); telTf.setText("");
        emailTf.setText(""); searchIdTf.setText("");
    }

    public void showList() {
        List<User> list = service.searchAllUsers();
        tableModel.setRowCount(0);
        for (User u : list)
            tableModel.addRow(new Object[]{
                String.valueOf(u.getId()), u.getName(), u.getTel(), u.getEmail()});
    }
}
