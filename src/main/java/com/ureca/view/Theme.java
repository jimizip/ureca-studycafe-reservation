package com.ureca.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.plaf.basic.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class Theme {

    // ── 색상 ──────────────────────────────────────────────────
    public static final Color BG_PRIMARY   = new Color(0x0F0F0F);
    public static final Color BG_SECONDARY = new Color(0x1A1A1A);
    public static final Color BG_CARD      = new Color(0x242424);
    public static final Color BG_HOVER     = new Color(0x2E2E2E);
    public static final Color ACCENT       = new Color(0xC9A84C);
    public static final Color ACCENT_DIM   = new Color(0x8A6E2F);
    public static final Color TEXT_PRIMARY = new Color(0xE8E8E8);
    public static final Color TEXT_DIM     = new Color(0x9A9A9A);
    public static final Color BORDER       = new Color(0x3A3A3A);
    public static final Color SUCCESS      = new Color(0x4CAF82);
    public static final Color DANGER       = new Color(0xC9504C);
    public static final Color SEL_BG       = new Color(0x1E3A5F);
    public static final Color SEL_BORDER   = new Color(0x4A90D9);

    // ── 둥근 반지름 ───────────────────────────────────────────
    public static final int R_SMALL  = 8;
    public static final int R_MEDIUM = 12;
    public static final int R_LARGE  = 16;

    // ── 폰트 ──────────────────────────────────────────────────
    public static final Font FONT_TITLE  = new Font("Malgun Gothic", Font.BOLD,  18);
    public static final Font FONT_HEADER = new Font("Malgun Gothic", Font.BOLD,  13);
    public static final Font FONT_BODY   = new Font("Malgun Gothic", Font.PLAIN, 12);
    public static final Font FONT_SMALL  = new Font("Malgun Gothic", Font.PLAIN, 11);
    public static final Font FONT_BTN    = new Font("Malgun Gothic", Font.BOLD,  12);

    // ── 전역 UIManager ────────────────────────────────────────
    public static void applyGlobalDefaults() {
        UIManager.put("Panel.background",              BG_SECONDARY);
        UIManager.put("Label.foreground",              TEXT_PRIMARY);
        UIManager.put("Label.font",                    FONT_BODY);
        UIManager.put("TextField.background",          BG_CARD);
        UIManager.put("TextField.foreground",          TEXT_PRIMARY);
        UIManager.put("TextField.caretForeground",     ACCENT);
        UIManager.put("TextField.font",                FONT_BODY);
        UIManager.put("TextField.border",              roundedInputBorder(BORDER));
        UIManager.put("Spinner.background",            BG_CARD);
        UIManager.put("Spinner.foreground",            TEXT_PRIMARY);
        UIManager.put("Table.background",              BG_CARD);
        UIManager.put("Table.foreground",              TEXT_PRIMARY);
        UIManager.put("Table.selectionBackground",     SEL_BG);
        UIManager.put("Table.selectionForeground",     TEXT_PRIMARY);
        UIManager.put("Table.gridColor",               BORDER);
        UIManager.put("Table.font",                    FONT_BODY);
        UIManager.put("TableHeader.background",        BG_SECONDARY);
        UIManager.put("TableHeader.foreground",        ACCENT);
        UIManager.put("TableHeader.font",              FONT_HEADER);
        UIManager.put("ScrollPane.background",         BG_SECONDARY);
        UIManager.put("TabbedPane.background",         BG_PRIMARY);
        UIManager.put("TabbedPane.foreground",         TEXT_DIM);
        UIManager.put("TabbedPane.selected",           BG_SECONDARY);
        UIManager.put("TabbedPane.selectedForeground", ACCENT);
        UIManager.put("TabbedPane.font",               FONT_HEADER);
    }

    // ── 테두리 ────────────────────────────────────────────────
    public static Border roundedInputBorder(Color borderColor) {
        return new Border() {
            private final Insets ins = new Insets(5, 10, 5, 10);
            @Override public Insets getBorderInsets(Component c) { return ins; }
            @Override public boolean isBorderOpaque() { return false; }
            @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(x, y, w - 1, h - 1, R_SMALL, R_SMALL);
                g2.dispose();
            }
        };
    }

    public static Border cardBorder(String title) {
        TitledBorder tb = BorderFactory.createTitledBorder(
            new Border() {
                private final Insets ins = new Insets(0, 0, 0, 0);
                @Override public Insets getBorderInsets(Component c) { return ins; }
                @Override public boolean isBorderOpaque() { return false; }
                @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(BORDER);
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(x, y, w - 1, h - 1, R_MEDIUM, R_MEDIUM);
                    g2.dispose();
                }
            }, title);
        tb.setTitleColor(ACCENT);
        tb.setTitleFont(FONT_HEADER);
        return BorderFactory.createCompoundBorder(tb,
            BorderFactory.createEmptyBorder(8, 10, 10, 10));
    }

    // ── 버튼 팩토리 ───────────────────────────────────────────
    public static JButton accentButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), R_SMALL, R_SMALL);
                g2.dispose();
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        btn.setFont(FONT_BTN);
        btn.setForeground(BG_PRIMARY);
        btn.setBackground(ACCENT);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(0xDFB850)); btn.repaint(); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(ACCENT);              btn.repaint(); }
            public void mousePressed(MouseEvent e) { btn.setBackground(ACCENT_DIM);          btn.repaint(); }
            public void mouseReleased(MouseEvent e){ btn.setBackground(ACCENT);              btn.repaint(); }
        });
        return btn;
    }

    public static JButton defaultButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), R_SMALL, R_SMALL);
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, R_SMALL, R_SMALL);
                g2.dispose();
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        btn.setFont(FONT_BTN);
        btn.setForeground(TEXT_PRIMARY);
        btn.setBackground(BG_CARD);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(BG_HOVER); btn.repaint(); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(BG_CARD);  btn.repaint(); }
        });
        return btn;
    }

    public static JButton dangerButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), R_SMALL, R_SMALL);
                g2.setColor(DANGER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, R_SMALL, R_SMALL);
                g2.dispose();
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        btn.setFont(FONT_BTN);
        btn.setForeground(new Color(0xFFAAAA));
        btn.setBackground(new Color(0x3A1010));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(0x5A1A1A)); btn.repaint(); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(new Color(0x3A1010)); btn.repaint(); }
        });
        return btn;
    }

    // ── 네비게이션 버튼 (사이드바용) ─────────────────────────
    public static JButton navButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), R_SMALL, R_SMALL);
                g2.dispose();
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        btn.setFont(new Font("Malgun Gothic", Font.PLAIN, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(BG_PRIMARY);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(BG_HOVER); btn.repaint(); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(BG_PRIMARY); btn.repaint(); }
        });
        return btn;
    }

    public static void setNavActive(JButton btn, boolean active) {
        if (active) {
            btn.setForeground(ACCENT);
            btn.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
            btn.setBackground(new Color(0x2A2010));
        } else {
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Malgun Gothic", Font.PLAIN, 12));
            btn.setBackground(BG_PRIMARY);
        }
        btn.repaint();
    }

    // ── 둥근 텍스트필드 ──────────────────────────────────────
    public static JTextField roundedTextField() {
        JTextField tf = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), R_SMALL, R_SMALL);
                g2.dispose();
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, R_SMALL, R_SMALL);
                g2.dispose();
            }
        };
        tf.setBackground(BG_CARD);
        tf.setForeground(TEXT_PRIMARY);
        tf.setCaretColor(ACCENT);
        tf.setFont(FONT_BODY);
        tf.setOpaque(false);
        tf.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        return tf;
    }

    // ── 스피너 (화살표 통합 스타일) ──────────────────────────
    public static JSpinner roundedSpinner(int value, int min, int max, int step) {
        JSpinner sp = new JSpinner(new SpinnerNumberModel(value, min, max, step));
        sp.setOpaque(false);
        sp.setBorder(BorderFactory.createEmptyBorder());

        // 텍스트 편집창
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) sp.getEditor();
        JTextField tf = editor.getTextField();
        tf.setBackground(BG_CARD);
        tf.setForeground(TEXT_PRIMARY);
        tf.setCaretColor(ACCENT);
        tf.setFont(FONT_BODY);
        tf.setHorizontalAlignment(JTextField.CENTER);
        tf.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0)); // 화살표에 바짝 붙임
        tf.setOpaque(true);

        // 커스텀 SpinnerUI - 골드 삼각형 화살표 직접 그리기
        sp.setUI(new javax.swing.plaf.basic.BasicSpinnerUI() {
            @Override protected Component createNextButton() {
                JButton btn = makeGoldArrow(true);
                installNextButtonListeners(btn);
                return btn;
            }
            @Override protected Component createPreviousButton() {
                JButton btn = makeGoldArrow(false);
                installPreviousButtonListeners(btn);
                return btn;
            }
            @Override public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), R_SMALL, R_SMALL);
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, c.getWidth()-1, c.getHeight()-1, R_SMALL, R_SMALL);
                g2.dispose();
                super.paint(g, c);
            }
        });

        return sp;
    }

    // 골드 삼각형 화살표 버튼 생성
    private static JButton makeGoldArrow(boolean up) {
        JButton btn = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // 배경
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
                // 골드 삼각형
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                int w = 6, h = 4;
                int[] xp, yp;
                if (up) {
                    xp = new int[]{cx - w, cx + w, cx};
                    yp = new int[]{cy + h/2, cy + h/2, cy - h/2};
                } else {
                    xp = new int[]{cx - w, cx + w, cx};
                    yp = new int[]{cy - h/2, cy - h/2, cy + h/2};
                }
                g2.setColor(ACCENT);
                g2.fillPolygon(xp, yp, 3);
                g2.dispose();
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        btn.setBackground(new Color(0x1E1E1E));
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(1, 4, 1, 4));
        btn.setPreferredSize(new Dimension(20, 14));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(0x2A2010)); btn.repaint(); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(new Color(0x1E1E1E)); btn.repaint(); }
        });
        return btn;
    }

    // ── 레이블 팩토리 ─────────────────────────────────────────
    public static JLabel titleLabel(String text) {
        JLabel l = new JLabel(text); l.setFont(FONT_TITLE); l.setForeground(ACCENT); return l;
    }
    public static JLabel bodyLabel(String text) {
        JLabel l = new JLabel(text); l.setFont(FONT_BODY); l.setForeground(TEXT_PRIMARY); return l;
    }
    public static JLabel dimLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_SMALL);
        l.setForeground(Color.WHITE);
        return l;
    }
    public static JLabel formLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
        l.setForeground(Color.WHITE);
        return l;
    }

    // ── 테이블 스타일 (둥근 스크롤패널로 감쌈) ───────────────
    public static void styleTable(JTable table) {
        table.setBackground(BG_CARD);
        table.setForeground(TEXT_PRIMARY);
        table.setFont(FONT_BODY);
        table.setSelectionBackground(SEL_BG);
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setGridColor(new Color(0x2C2C2C));
        table.setRowHeight(32);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setShowVerticalLines(false);    // 세로선 제거 → 깔끔
        table.setShowHorizontalLines(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // 헤더
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0x1E1E1E));
        header.setForeground(ACCENT);
        header.setFont(FONT_HEADER);
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean focus, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t,val,sel,focus,row,col);
                lbl.setBackground(new Color(0x1E1E1E));
                lbl.setForeground(ACCENT);
                lbl.setFont(FONT_HEADER);
                lbl.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                    BorderFactory.createEmptyBorder(6, 10, 6, 10)));
                lbl.setOpaque(true);
                return lbl;
            }
        });

        // 셀 렌더러: 홀짝 행 + 좌우 패딩
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setFont(FONT_BODY);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0x2C2C2C)),
                    BorderFactory.createEmptyBorder(0, 10, 0, 10)));
                if (sel) { setBackground(SEL_BG); setForeground(TEXT_PRIMARY); }
                else     { setBackground(row % 2 == 0 ? BG_CARD : new Color(0x202020)); setForeground(TEXT_PRIMARY); }
                return this;
            }
        });
    }

    // ── 둥근 스크롤패널 (테이블용) ───────────────────────────
    public static JScrollPane styledScrollPane(JTable table) {
        // 코너 처리를 위해 커스텀 뷰포트
        JScrollPane sp = new JScrollPane(table) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), R_MEDIUM, R_MEDIUM);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getViewport().setBackground(BG_CARD);
        sp.setBorder(new Border() {
            private final Insets ins = new Insets(0,0,0,0);
            @Override public Insets getBorderInsets(Component c) { return ins; }
            @Override public boolean isBorderOpaque() { return false; }
            @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(x, y, w-1, h-1, R_MEDIUM, R_MEDIUM);
                g2.dispose();
            }
        });
        styleScrollBar(sp.getVerticalScrollBar());
        // 우측 하단 코너 채우기
        JPanel corner = new JPanel();
        corner.setBackground(BG_CARD);
        sp.setCorner(JScrollPane.LOWER_RIGHT_CORNER, corner);
        return sp;
    }

    public static void styleScrollBar(JScrollBar sb) {
        sb.setBackground(BG_SECONDARY);
        sb.setPreferredSize(new Dimension(6, 0));
        sb.setUI(new BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = new Color(0x444444); trackColor = BG_CARD;
            }
            @Override protected JButton createDecreaseButton(int o) { return zeroBtn(); }
            @Override protected JButton createIncreaseButton(int o) { return zeroBtn(); }
            @Override protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);
                g2.fillRoundRect(r.x+1, r.y+2, r.width-2, r.height-4, 4, 4);
                g2.dispose();
            }
            @Override protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
                g.setColor(trackColor); g.fillRect(r.x, r.y, r.width, r.height);
            }
            private JButton zeroBtn() {
                JButton b = new JButton(); b.setPreferredSize(new Dimension(0,0)); return b;
            }
        });
    }

    // ── 기존 styleSpinner (하위 호환) ─────────────────────────
    public static void styleSpinner(JSpinner sp) {
        // roundedSpinner 사용 권장, 여기선 최소 스타일만
        sp.setBorder(roundedInputBorder(BORDER));
        JComponent editor = sp.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            tf.setBackground(BG_CARD);
            tf.setForeground(TEXT_PRIMARY);
            tf.setCaretColor(ACCENT);
            tf.setFont(FONT_BODY);
            tf.setBorder(BorderFactory.createEmptyBorder(0,4,0,4));
        }
    }

    // ── 시간 버튼 ─────────────────────────────────────────────
    public static JButton timeButton(String label) {
        JButton btn = new JButton(label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), R_SMALL, R_SMALL);
                Color bc = (Color) getClientProperty("borderColor");
                if (bc == null) bc = BORDER;
                int sw = getClientProperty("borderWidth") instanceof Integer
                    ? (int) getClientProperty("borderWidth") : 1;
                g2.setColor(bc);
                g2.setStroke(new BasicStroke(sw));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, R_SMALL, R_SMALL);
                g2.dispose();
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        btn.setFont(FONT_SMALL);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        timeBtn_default(btn);
        return btn;
    }

    public static void timeBtn_default(JButton btn) {
        btn.setBackground(BG_CARD); btn.setForeground(TEXT_PRIMARY);
        btn.putClientProperty("borderColor", BORDER); btn.putClientProperty("borderWidth", 1); btn.repaint();
    }
    public static void timeBtn_selected(JButton btn) {
        btn.setBackground(new Color(0x1A2E48)); btn.setForeground(SEL_BORDER);
        btn.putClientProperty("borderColor", SEL_BORDER); btn.putClientProperty("borderWidth", 2); btn.repaint();
    }
    public static void timeBtn_booked(JButton btn) {
        btn.setBackground(new Color(0x2A0E0E)); btn.setForeground(new Color(0x884040));
        btn.putClientProperty("borderColor", new Color(0x5A2020)); btn.putClientProperty("borderWidth", 1); btn.repaint();
    }
    public static void timeBtn_original(JButton btn) {
        btn.setBackground(new Color(0x163020)); btn.setForeground(SUCCESS);
        btn.putClientProperty("borderColor", SUCCESS); btn.putClientProperty("borderWidth", 2); btn.repaint();
    }
}
