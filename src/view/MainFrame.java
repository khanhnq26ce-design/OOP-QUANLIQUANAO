package view;

import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame(String maNV, String tenNhanVien, String chucVu) {
        setTitle("HE THONG QUAN LY CUA HANG QUAN AO - " + tenNhanVien + " (" + chucVu + ")");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Tab 1: Ai đăng nhập cũng thấy màn hình Bán hàng (tự điền sẵn mã nhân viên)
        BanHang banHang = new BanHang(maNV);
        tabbedPane.addTab("1. Ban Hang & Xuat Hoa Don", banHang.getContentPane());

        // Chỉ tài khoản 'QuanLy' mới được mở Tab Quản lý kho và Tab Thống kê
        if ("QuanLy".equalsIgnoreCase(chucVu)) {
            QuanLySanPhamFrame qlSanPham = new QuanLySanPhamFrame();
            ThongKePanel thongKe = new ThongKePanel();

            tabbedPane.addTab("2. Quan Ly Kho San Pham", qlSanPham.getContentPane());
            tabbedPane.addTab("3. Thong Ke & Bieu Do", thongKe);

            tabbedPane.addChangeListener(e -> {
                if (tabbedPane.getSelectedIndex() == 2) {
                    thongKe.loadThongKe();
                } else if (tabbedPane.getSelectedIndex() == 1) {
                    // Tự động tải lại bảng sản phẩm để cập nhật tồn kho mới nhất
                    qlSanPham.reloadData(); 
                }
            });
        }

        add(tabbedPane);
    }
}