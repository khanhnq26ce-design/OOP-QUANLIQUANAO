package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import util.DBConnection;

public class ThongKeDAO {

    // 1. Lấy danh sách hóa đơn để hiện lên bảng thống kê
    public List<Object[]> getDanhSachHoaDon() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT maHD, ngayLap, maNV, maKH, tongTien FROM HoaDon ORDER BY ngayLap DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("maHD"),
                    rs.getDate("ngayLap"),
                    rs.getString("maNV"),
                    rs.getString("maKH"),
                    rs.getDouble("tongTien")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Tính tổng doanh thu toàn cửa hàng
    public double getTongDoanhThu() {
        String sql = "SELECT SUM(tongTien) FROM HoaDon";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 3. Lấy dữ liệu vẽ biểu đồ doanh thu theo ngày
    public Map<String, Double> getDoanhThuTheoNgay() {
        Map<String, Double> data = new LinkedHashMap<>();
        String sql = "SELECT ngayLap, SUM(tongTien) AS doanhThu FROM HoaDon GROUP BY ngayLap ORDER BY ngayLap ASC LIMIT 7";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                data.put(rs.getString("ngayLap"), rs.getDouble("doanhThu"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}