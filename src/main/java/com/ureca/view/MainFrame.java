package com.ureca.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.ureca.dto.User;

public class MainFrame {
    private JFrame main;
    private JButton navUser, navReserve, navPayment;
    private JPanel contentArea;
    private CardLayout cardLayout;
    private UserPanel userPanel;
    private ReservationPanel reservationPanel;
    private PaymentPanel paymentPanel;

    private static final String CARD_USER    = "USER";
    private static final String CARD_RESERVE = "RESERVE";
    private static final String CARD_PAYMENT = "PAYMENT";

    // 드래그용
    private int dragX, dragY;

    public MainFrame(User currentUser) {
        main = new JFrame();
        main.setUndecorated(true);   // OS 타이틀바 제거
        main.getContentPane().setBackground(Theme.BG_PRIMARY);

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(Theme.BG_SECONDARY);
        titleBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
            BorderFactory.createEmptyBorder(9, 20, 9, 16)));

        // 왼쪽: 로고
        JLabel logoLbl = Theme.titleLabel("STUDY CAFÉ");
        logoLbl.setFont(new Font("Malgun Gothic", Font.BOLD, 16));

        // 가운데: 유저 정보
        JLabel userLbl = new JLabel(currentUser.getName() + "님  |  " + currentUser.getEmail());
        userLbl.setFont(Theme.FONT_SMALL);
        userLbl.setForeground(Color.WHITE);
        userLbl.setHorizontalAlignment(JLabel.CENTER);

        // 오른쪽: 최소화 / 최대화 / 닫기
        JPanel winBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        winBtns.setBackground(Theme.BG_SECONDARY);

        JButton minBtn   = winControlBtn("min",   new Color(0x3A3A3A), new Color(0x555555));
        JButton maxBtn   = winControlBtn("max",   new Color(0x3A3A3A), new Color(0x555555));
        JButton closeBtn = winControlBtn("close", new Color(0x7A2020), Theme.DANGER);

        minBtn.addActionListener(e   -> main.setState(JFrame.ICONIFIED));
        maxBtn.addActionListener(e   -> {
            if (main.getExtendedState() == JFrame.MAXIMIZED_BOTH)
                main.setExtendedState(JFrame.NORMAL);
            else
                main.setExtendedState(JFrame.MAXIMIZED_BOTH);
        });
        closeBtn.addActionListener(e -> System.exit(0));

        winBtns.add(minBtn);
        winBtns.add(maxBtn);
        winBtns.add(closeBtn);

        titleBar.add(logoLbl, BorderLayout.WEST);
        titleBar.add(userLbl, BorderLayout.CENTER);
        titleBar.add(winBtns, BorderLayout.EAST);

        // 드래그로 창 이동
        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { dragX = e.getX(); dragY = e.getY(); }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                main.setLocation(main.getX() + e.getX() - dragX,
                                 main.getY() + e.getY() - dragY);
            }
        });

        JPanel topWrap = new JPanel(new BorderLayout());
        topWrap.setBackground(Theme.BG_PRIMARY);
        topWrap.add(titleBar, BorderLayout.CENTER);

        // ════════════════════════════════════
        // 좌측 사이드 네비게이션
        // ════════════════════════════════════
        JPanel sidebar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.BG_SECONDARY);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Theme.BORDER);
                g2.fillRect(getWidth()-1, 0, 1, getHeight());
                g2.dispose();
            }
        };
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(160, 0));
        sidebar.setOpaque(false);
        sidebar.setBorder(BorderFactory.createEmptyBorder(16, 8, 16, 8));

        JLabel navTitle = new JLabel("메뉴");
        navTitle.setFont(Theme.FONT_SMALL);
        navTitle.setForeground(Color.WHITE);
        navTitle.setBorder(BorderFactory.createEmptyBorder(0, 6, 10, 0));
        navTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(navTitle);

        navUser    = makeNavBtn("👤  유저 관리");
        navReserve = makeNavBtn("📅  회의실 예약");
        navPayment = makeNavBtn("💳  결제 내역");

        sidebar.add(navUser);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(navReserve);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(navPayment);
        sidebar.add(Box.createVerticalGlue());

        JLabel verLbl = new JLabel("v1.0");
        verLbl.setFont(Theme.FONT_SMALL);
        verLbl.setForeground(Color.WHITE);
        verLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        verLbl.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
        sidebar.add(verLbl);

        // ════════════════════════════════════
        // 콘텐츠 영역
        // ════════════════════════════════════
        cardLayout  = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(Theme.BG_PRIMARY);

        userPanel        = new UserPanel();
        reservationPanel = new ReservationPanel(currentUser);
        paymentPanel     = new PaymentPanel(currentUser);

        contentArea.add(userPanel,        CARD_USER);
        contentArea.add(reservationPanel, CARD_RESERVE);
        contentArea.add(paymentPanel,     CARD_PAYMENT);

        navUser.addActionListener(e    -> switchTo(CARD_USER));
        navReserve.addActionListener(e -> switchTo(CARD_RESERVE));
        navPayment.addActionListener(e -> switchTo(CARD_PAYMENT));

        switchTo(CARD_RESERVE);

        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Theme.BG_PRIMARY);
        body.add(sidebar,     BorderLayout.WEST);
        body.add(contentArea, BorderLayout.CENTER);

        main.add(topWrap, BorderLayout.NORTH);
        main.add(body,    BorderLayout.CENTER);
        main.setSize(1100, 720);
        main.setMinimumSize(new Dimension(900, 600));
        main.setLocationRelativeTo(null);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setVisible(true);
    }

    // 윈도우 컨트롤 버튼 - 도형으로 직접 그림 (텍스트 없음)
    private JButton winControlBtn(String type, Color bg, Color hover) {
        JButton btn = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                int cx = getWidth() / 2, cy = getHeight() / 2;
                g2.setColor(Color.WHITE);
                g2.setStroke(new java.awt.BasicStroke(1.5f,
                    java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
                switch (type) {
                    case "min":
                        g2.drawLine(cx - 5, cy, cx + 5, cy);
                        break;
                    case "max":
                        g2.drawRect(cx - 5, cy - 4, 10, 8);
                        break;
                    case "close":
                        g2.drawLine(cx - 4, cy - 4, cx + 4, cy + 4);
                        g2.drawLine(cx + 4, cy - 4, cx - 4, cy + 4);
                        break;
                }
                g2.dispose();
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        btn.setBackground(bg);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(32, 22));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); btn.repaint(); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(bg);    btn.repaint(); }
        });
        return btn;
    }

    private JButton makeNavBtn(String text) {
        JButton btn = Theme.navButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }

    private void switchTo(String card) {
        cardLayout.show(contentArea, card);
        Theme.setNavActive(navUser,    card.equals(CARD_USER));
        Theme.setNavActive(navReserve, card.equals(CARD_RESERVE));
        Theme.setNavActive(navPayment, card.equals(CARD_PAYMENT));
    }
}
