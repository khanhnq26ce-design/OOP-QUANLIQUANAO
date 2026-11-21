package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import util.DBConnection;

public class HoaDonDAO {

    // Lớp phụ trợ để lưu tạm 1 dòng hàng trong giỏ hàng trên giao diện
    public static class GioHangItem {
        public String maSP;
        public String tenSP;
        public int soLuongMua;
        public double donGia;

        public GioHangItem(String maSP, String tenSP, int soLuongMua, double donGia) {
            this.maSP = maSP;
            this.tenSP = tenSP;
            this.soLuongMua = soLuongMua;
            this.donGia = donGia;
        }
        public double getThanhTien() { return soLuongMua * donGia; }
    }

    // Hàm xuất hóa đơn: Lưu HoaDon -> Lưu ChiTietHoaDon -> Trừ tồn kho SanPham
    public boolean xuatHoaDon(String maHD, String maNV, String maKH, double tongTien, List<GioHangItem> gioHang) {
        String sqlHoaDon = "INSERT INTO HoaDon (maHD, ngayLap, maNV, maKH, tongTien) VALUES (?, CURDATE(), ?, ?, ?)";
        String sqlChiTiet = "INSERT INTO ChiTietHoaDon (maHD, maSP, soLuongMua, donGia) VALUES (?, ?, ?, ?)";
        String sqlTruKho = "UPDATE SanPham SET soLuong = soLuong - ? WHERE maSP = ? AND soLuong >= ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu giao dịch

            // 1. Lưu bảng HoaDon
            try (PreparedStatement psHD = conn.prepareStatement(sqlHoaDon)) {
                psHD.setString(1, maHD);
                psHD.setString(2, maNV);
                psHD.setString(3, maKH);
                psHD.setDouble(4, tongTien);
                psHD.executeUpdate();
            }

            // 2. Lưu từng món vào ChiTietHoaDon và trừ tồn kho
            try (PreparedStatement psCT = conn.prepareStatement(sqlChiTiet);
                 PreparedStatement psKho = conn.prepareStatement(sqlTruKho)) {
                
                for (GioHangItem item : gioHang) {
                    // Thêm chi tiết
                    psCT.setString(1, maHD);
                    psCT.setString(2, item.maSP);
                    psCT.setInt(3, item.soLuongMua);
                    psCT.setDouble(4, item.donGia);
                    psCT.executeUpdate();

                    // Trừ tồn kho
                    psKho.setInt(1, item.soLuongMua);
                    psKho.setString(2, item.maSP);
                    psKho.setInt(3, item.soLuongMua);
                    int updated = psKho.executeUpdate();
                    if (updated == 0) {
                        throw new SQLException("San pham " + item.maSP + " khong du ton kho!");
                    }
                }
            }

            conn.commit(); // Xác nhận lưu toàn bộ vào MySQL
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        }
    }
}