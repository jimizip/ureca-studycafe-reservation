package com.ureca.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.ureca.dto.User;
import com.ureca.service.StudyCafeService;
import com.ureca.service.StudyCafeServiceImp;

public class LoginPanel extends JFrame {
    private JTextField emailTf;
    private JButton loginBt;
    private MessageDialog dialog;
    private StudyCafeService service = new StudyCafeServiceImp();

    public LoginPanel() {
        Theme.applyGlobalDefaults();
        dialog = new MessageDialog();
        setTitle("스터디카페 예약 시스템");
        setUndecorated(true);
        setSize(400, 340);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_PRIMARY);
        setContentPane(root);

        // ── 상단 골드 라인 ────────────────────────────────────
        JPanel accentLine = new JPanel();
        accentLine.setBackground(Theme.BG_PRIMARY);
        accentLine.setPreferredSize(new Dimension(0, 1));

        // ── 타이틀 ────────────────────────────────────────────
        JPanel headerPan = new JPanel(new GridLayout(2, 1, 0, 6));
        headerPan.setBackground(Theme.BG_PRIMARY);
        headerPan.setBorder(BorderFactory.createEmptyBorder(30, 40, 18, 40));

        JLabel titleLbl = Theme.titleLabel("STUDY CAFÉ");
        titleLbl.setFont(new Font("Malgun Gothic", Font.BOLD, 22));
        titleLbl.setHorizontalAlignment(JLabel.CENTER);

        JLabel subLbl = Theme.dimLabel("예약 관리 시스템");
        subLbl.setHorizontalAlignment(JLabel.CENTER);

        headerPan.add(titleLbl);
        headerPan.add(subLbl);

        // ── 입력 카드 ─────────────────────────────────────────
        // 좌우 여유 공간을 카드 바깥에 충분히 두고, 내부도 패딩 확보
        JPanel cardOuter = new JPanel(new GridBagLayout());  // 중앙 정렬용
        cardOuter.setBackground(Theme.BG_PRIMARY);
        cardOuter.setBorder(BorderFactory.createEmptyBorder(0, 48, 0, 48)); // 좌우 여백

        JPanel cardPan = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.BG_SECONDARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), Theme.R_MEDIUM, Theme.R_MEDIUM);
                g2.setColor(Theme.BORDER);
                g2.setStroke(new java.awt.BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, Theme.R_MEDIUM, Theme.R_MEDIUM);
                g2.dispose();
            }
        };
        cardPan.setOpaque(false);
        cardPan.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 6, 0);
        cardPan.add(Theme.formLabel("이메일"), gbc);

        emailTf = Theme.roundedTextField();
        emailTf.setPreferredSize(new Dimension(260, 36)); // 고정 너비
        emailTf.setMaximumSize(new Dimension(260, 36));
        emailTf.addActionListener(e -> doLogin());
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        cardPan.add(emailTf, gbc);

        GridBagConstraints outer = new GridBagConstraints();
        outer.fill = GridBagConstraints.HORIZONTAL;
        outer.weightx = 1.0;
        cardOuter.add(cardPan, outer);

        // ── 로그인 버튼 ───────────────────────────────────────
        loginBt = Theme.accentButton("로그인");
        loginBt.setPreferredSize(new Dimension(0, 40));
        loginBt.addActionListener(e -> doLogin());

        JPanel btnWrap = new JPanel(new BorderLayout());
        btnWrap.setBackground(Theme.BG_PRIMARY);
        btnWrap.setBorder(BorderFactory.createEmptyBorder(14, 48, 28, 48));
        btnWrap.add(loginBt, BorderLayout.CENTER);

        JPanel topWrap = new JPanel(new BorderLayout());
        topWrap.setBackground(Theme.BG_PRIMARY);
        // 닫기 버튼 우상단
        JPanel closePan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 2));
        closePan.setBackground(Theme.BG_PRIMARY);
        JButton closeBtn = new JButton("✕") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground()); g2.fillRoundRect(0,0,getWidth(),getHeight(),6,6); g2.dispose();
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        closeBtn.setFont(new Font("Malgun Gothic", Font.PLAIN, 11));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBackground(new Color(0x7A2020));
        closeBtn.setOpaque(false); closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false); closeBtn.setFocusPainted(false);
        closeBtn.setPreferredSize(new Dimension(28, 22));
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { closeBtn.setBackground(Theme.DANGER); closeBtn.repaint(); }
            public void mouseExited(java.awt.event.MouseEvent e)  { closeBtn.setBackground(new Color(0x7A2020)); closeBtn.repaint(); }
        });
        closeBtn.addActionListener(e -> System.exit(0));
        closePan.add(closeBtn);
        topWrap.add(accentLine, BorderLayout.NORTH);
        topWrap.add(closePan,   BorderLayout.CENTER);
        topWrap.add(headerPan,  BorderLayout.SOUTH);

        root.add(topWrap,   BorderLayout.NORTH);
        root.add(cardOuter, BorderLayout.CENTER);
        root.add(btnWrap,   BorderLayout.SOUTH);

        setVisible(true);
    }

    private void doLogin() {
        try {
            String email = emailTf.getText().trim();
            if (email.isEmpty()) { dialog.show("이메일을 입력해주세요"); return; }
            User user = service.searchUserByEmail(email);
            new MainFrame(user);
            setVisible(false);
        } catch (Exception ex) {
            dialog.show(ex.getMessage());
        }
    }
}
