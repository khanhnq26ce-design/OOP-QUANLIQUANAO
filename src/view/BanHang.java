package view;

import dao.HoaDonDAO;
import dao.HoaDonDAO.GioHangItem;
import dao.SanPhamDAO;
import java.awt.*;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.SanPham;

public class BanHang extends JFrame {
    private JTable tblSanPham, tblGioHang;
    private DefaultTableModel modelSP, modelGioHang;
    private JTextField txtMaHD, txtMaNV, txtMaKH, txtSoLuongMua;
    private JLabel lblTongTien;

    private SanPhamDAO spDAO = new SanPhamDAO();
    private HoaDonDAO hdDAO = new HoaDonDAO();
    private List<GioHangItem> gioHang = new ArrayList<>();
    private double tongTien = 0;
    
 // Thêm constructor này ngay trên constructor public BanHangFrame() cũ
    public BanHang(String maNhanVienDangNhap) {
        this(); // Gọi lại toàn bộ giao diện cũ
        txtMaNV.setText(maNhanVienDangNhap);
        txtMaNV.setEditable(false); // Khóa không cho nhân viên tự sửa mã của mình
    }

    public BanHang() {
        setTitle("BAN HANG & XUAT HOA DON");
        setSize(1050, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2, 15, 10));

        // ================= BÊN TRÁI: DANH SÁCH SẢN PHẨM =================
        JPanel pnlLeft = new JPanel(new BorderLayout(5, 5));
        pnlLeft.setBorder(BorderFactory.createTitledBorder("1. Chon san pham trong kho"));

     // 1. Khóa không cho sửa bảng Sản phẩm bên trái
        modelSP = new DefaultTableModel(new String[]{"Ma SP", "Ten SP", "Size", "Gia ban", "Ton kho"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa bất kỳ ô nào
            }
        };
        tblSanPham = new JTable(modelSP);
        pnlLeft.add(new JScrollPane(tblSanPham), BorderLayout.CENTER);

        JPanel pnlChonMua = new JPanel(new FlowLayout());
        pnlChonMua.add(new JLabel("So luong mua:"));
        txtSoLuongMua = new JTextField("1", 5);
        JButton btnThemVaoGio = new JButton("Them vao gio >>");
        pnlChonMua.add(txtSoLuongMua);
        pnlChonMua.add(btnThemVaoGio);
        pnlLeft.add(pnlChonMua, BorderLayout.SOUTH);

        // ================= BÊN PHẢI: GIỎ HÀNG & XUẤT HÓA ĐƠN =================
        JPanel pnlRight = new JPanel(new BorderLayout(5, 5));
        pnlRight.setBorder(BorderFactory.createTitledBorder("2. Thong tin Hoa Don"));

        JPanel pnlInfoHD = new JPanel(new GridLayout(3, 2, 5, 5));
        pnlInfoHD.add(new JLabel("Ma Hoa Don (VD: HD03):"));
        txtMaHD = new JTextField("HD" + System.currentTimeMillis() % 10000); // Tự sinh mã HD ngẫu nhiên
        pnlInfoHD.add(txtMaHD);

        pnlInfoHD.add(new JLabel("Ma Nhan Vien lap:"));
        txtMaNV = new JTextField("NV01");
        pnlInfoHD.add(txtMaNV);

        pnlInfoHD.add(new JLabel("Ma Khach Hang (KH00 = Khach le):"));
        txtMaKH = new JTextField("KH00");
        pnlInfoHD.add(txtMaKH);
        pnlRight.add(pnlInfoHD, BorderLayout.NORTH);

     // 2. Khóa luôn bảng Giỏ hàng bên phải cho chắc chắn
        modelGioHang = new DefaultTableModel(new String[]{"Ma SP", "Ten SP", "SL", "Don gia", "Thanh tien"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblGioHang = new JTable(modelGioHang);
        pnlRight.add(new JScrollPane(tblGioHang), BorderLayout.CENTER);

        JPanel pnlThanhToan = new JPanel(new GridLayout(2, 1, 5, 5));
        lblTongTien = new JLabel("TONG TIEN: 0 VND", SwingConstants.RIGHT);
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 16));
        lblTongTien.setForeground(Color.RED);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnXoaMon = new JButton("Xoa khoi gio");
        JButton btnXuatHD = new JButton("THANH TOAN & XUAT HOA DON");
        btnXuatHD.setBackground(new Color(40, 167, 69));
        btnXuatHD.setForeground(Color.WHITE);

        pnlBtn.add(btnXoaMon);
        pnlBtn.add(btnXuatHD);
        pnlThanhToan.add(lblTongTien);
        pnlThanhToan.add(pnlBtn);
        pnlRight.add(pnlThanhToan, BorderLayout.SOUTH);

        add(pnlLeft);
        add(pnlRight);

        loadSanPham();

        // --- SỰ KIỆN 1: THÊM VÀO GIỎ HÀNG ---
        btnThemVaoGio.addActionListener(e -> {
            int row = tblSanPham.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui long chon 1 san pham ben bang trai!");
                return;
            }
            try {
                String maSP = modelSP.getValueAt(row, 0).toString();
                String tenSP = modelSP.getValueAt(row, 1).toString();
                double giaBan = Double.parseDouble(modelSP.getValueAt(row, 3).toString());
                int tonKho = Integer.parseInt(modelSP.getValueAt(row, 4).toString());
                int slMua = Integer.parseInt(txtSoLuongMua.getText().trim());

                if (slMua <= 0 || slMua > tonKho) {
                    JOptionPane.showMessageDialog(this, "So luong mua khong hop le hoac vuot qua ton kho!");
                    return;
                }

                // Kiểm tra nếu sản phẩm đã có trong giỏ thì cộng dồn số lượng
                boolean daCo = false;
                for (GioHangItem item : gioHang) {
                    if (item.maSP.equals(maSP)) {
                        if (item.soLuongMua + slMua > tonKho) {
                            JOptionPane.showMessageDialog(this, "Tong so luong vuot qua ton kho!");
                            return;
                        }
                        item.soLuongMua += slMua;
                        daCo = true;
                        break;
                    }
                }
                if (!daCo) {
                    gioHang.add(new GioHangItem(maSP, tenSP, slMua, giaBan));
                }
                capNhatBangGioHang();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "So luong phai la so nguyen!");
            }
        });

        // --- SỰ KIỆN 2: XÓA KHỎI GIỎ ---
        btnXoaMon.addActionListener(e -> {
            int row = tblGioHang.getSelectedRow();
            if (row >= 0) {
                gioHang.remove(row);
                capNhatBangGioHang();
            }
        });

        // --- SỰ KIỆN 3: THANH TOÁN & XUẤT HÓA ĐƠN ---
        btnXuatHD.addActionListener(e -> {
            if (gioHang.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Gio hang dang trong!");
                return;
            }
            String maHD = txtMaHD.getText().trim();
            String maNV = txtMaNV.getText().trim();
            String maKH = txtMaKH.getText().trim();

            if (hdDAO.xuatHoaDon(maHD, maNV, maKH, tongTien, gioHang)) {
                inPhieuHoaDon(maHD, maNV, maKH);
                gioHang.clear();
                capNhatBangGioHang();
                loadSanPham(); // Tải lại bảng sản phẩm để thấy số lượng tồn kho đã bị trừ
                txtMaHD.setText("HD" + System.currentTimeMillis() % 10000);
            } else {
                JOptionPane.showMessageDialog(this, "Xuat hoa don that bai (Kiem tra lai Ma HD, Ma NV hoac Ma KH)!");
            }
        });
    }

    private void loadSanPham() {
        modelSP.setRowCount(0);
        for (SanPham sp : spDAO.getAllSanPham()) {
            modelSP.addRow(new Object[]{sp.getMaSP(), sp.getTenSP(), sp.getSize(), sp.getGiaBan(), sp.getSoLuong()});
        }
    }

    private void capNhatBangGioHang() {
        modelGioHang.setRowCount(0);
        tongTien = 0;
        for (GioHangItem item : gioHang) {
            double thanhTien = item.getThanhTien();
            tongTien += thanhTien;
            modelGioHang.addRow(new Object[]{item.maSP, item.tenSP, item.soLuongMua, item.donGia, thanhTien});
        }
        lblTongTien.setText("TONG TIEN: " + String.format("%,.0f", tongTien) + " VND");
    }

    // Xuất hóa đơn ra thông báo màn hình & lưu thành file .txt
    private void inPhieuHoaDon(String maHD, String maNV, String maKH) {
        StringBuilder sb = new StringBuilder();
        sb.append("========== HOA DON BAN HANG ==========\n");
        sb.append("Ma HD: ").append(maHD).append("\n");
        sb.append("Ngay lap: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())).append("\n");
        sb.append("Nhan vien: ").append(maNV).append(" | Khach hang: ").append(maKH).append("\n");
        sb.append("--------------------------------------\n");
        for (GioHangItem item : gioHang) {
            sb.append(String.format("%-20s x%d : %,.0f VND\n", item.tenSP, item.soLuongMua, item.getThanhTien()));
        }
        sb.append("--------------------------------------\n");
        sb.append(String.format("TONG CONG: %,.0f VND\n", tongTien));
        sb.append("======================================\n");

        
        // Lưu ra file txt trong thư mục project
        java.io.File thuMuc = new java.io.File("hoadon");
        if (!thuMuc.exists()) {
            thuMuc.mkdirs(); // Tạo thư mục nếu chưa tồn tại
        }
        try (FileWriter fw = new FileWriter("hoadon/" + maHD + ".txt")) {
            fw.write(sb.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Hiển thị phiếu hóa đơn lên màn hình
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Xuat Hoa Don Thanh Cong (Da luu file " + maHD + ".txt)", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BanHang().setVisible(true));
    }
}