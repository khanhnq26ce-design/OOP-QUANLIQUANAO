package view;

import dao.ThongKeDAO;
import java.awt.*;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ThongKePanel extends JPanel {
    private JTable tblHoaDon;
    private DefaultTableModel modelHD;
    private JLabel lblTongDoanhThu, lblTongDon;
    private BieuDoPanel pnlBieuDo;
    private ThongKeDAO tkDAO = new ThongKeDAO();

    public ThongKePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- PHẦN TRÊN: THẺ TỔNG QUAN + NÚT LÀM MỚI ---
        JPanel pnlTop = new JPanel(new BorderLayout());
        JPanel pnlCards = new JPanel(new GridLayout(1, 2, 20, 10));

        lblTongDon = new JLabel("TONG SO HOA DON: 0", SwingConstants.CENTER);
        lblTongDon.setFont(new Font("Arial", Font.BOLD, 16));
        lblTongDon.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        lblTongDoanhThu = new JLabel("TONG DOANH THU: 0 VND", SwingConstants.CENTER);
        lblTongDoanhThu.setFont(new Font("Arial", Font.BOLD, 16));
        lblTongDoanhThu.setForeground(new Color(0, 128, 0));
        lblTongDoanhThu.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        pnlCards.add(lblTongDon);
        pnlCards.add(lblTongDoanhThu);

        JButton btnLamMoi = new JButton("Cap nhat so lieu");
        pnlTop.add(pnlCards, BorderLayout.CENTER);
        pnlTop.add(btnLamMoi, BorderLayout.EAST);
        add(pnlTop, BorderLayout.NORTH);

        // --- PHẦN GIỮA: CHIA ĐÔI BẢNG HÓA ĐƠN & BIỂU ĐỒ CỘT ---
        JPanel pnlCenter = new JPanel(new GridLayout(1, 2, 15, 10));

        // Bên trái: Bảng lịch sử hóa đơn
        modelHD = new DefaultTableModel(new String[]{"Ma HD", "Ngay lap", "Ma NV", "Ma KH", "Tong tien"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblHoaDon = new JTable(modelHD);
        JScrollPane scrollPane = new JScrollPane(tblHoaDon);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lich su hoa don da ban"));

        // Bên phải: Biểu đồ cột
        pnlBieuDo = new BieuDoPanel();
        pnlBieuDo.setBorder(BorderFactory.createTitledBorder("Bieu do doanh thu theo ngay"));

        pnlCenter.add(scrollPane);
        pnlCenter.add(pnlBieuDo);
        add(pnlCenter, BorderLayout.CENTER);

        loadThongKe();
        btnLamMoi.addActionListener(e -> loadThongKe());
    }

    public void loadThongKe() {
        modelHD.setRowCount(0);
        var list = tkDAO.getDanhSachHoaDon();
        for (Object[] row : list) {
            modelHD.addRow(row);
        }
        lblTongDon.setText("TONG SO HOA DON: " + list.size());
        lblTongDoanhThu.setText("TONG DOANH THU: " + String.format("%,.0f", tkDAO.getTongDoanhThu()) + " VND");
        pnlBieuDo.setData(tkDAO.getDoanhThuTheoNgay());
    }

    // Lớp con tự vẽ biểu đồ cột
    class BieuDoPanel extends JPanel {
        private Map<String, Double> data;

        public void setData(Map<String, Double> data) {
            this.data = data;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (data == null || data.isEmpty()) return;

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth() - 80;
            int height = getHeight() - 80;
            int startX = 50;
            int startY = getHeight() - 40;

            double maxVal = data.values().stream().mapToDouble(v -> v).max().orElse(1);
            int barWidth = Math.min(50, width / (data.size() * 2));
            int spacing = width / data.size();

            // Vẽ trục tọa độ
            g2.drawLine(startX, 30, startX, startY);
            g2.drawLine(startX, startY, getWidth() - 20, startY);

            int i = 0;
            for (Map.Entry<String, Double> entry : data.entrySet()) {
                int barHeight = (int) ((entry.getValue() / maxVal) * (height - 20));
                int x = startX + 20 + i * spacing;
                int y = startY - barHeight;

                g2.setColor(new Color(52, 152, 219));
                g2.fillRect(x, y, barWidth, barHeight);

                g2.setColor(Color.BLACK);
                g2.drawString(entry.getKey(), x - 5, startY + 15);
                g2.drawString(String.format("%,.0f", entry.getValue()), x - 5, y - 5);
                i++;
            }
        }
    }
}