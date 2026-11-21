package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.SanPham;
import util.DBConnection;

public class SanPhamDAO {

    // 1. Lấy toàn bộ danh sách sản phẩm
    public List<SanPham> getAllSanPham() {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM SanPham";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                SanPham sp = new SanPham(
                    rs.getString("maSP"),
                    rs.getString("tenSP"),
                    rs.getString("loaiSP"),
                    rs.getString("size"),
                    rs.getString("mauSac"),
                    rs.getDouble("giaBan"),
                    rs.getInt("soLuong")
                );
                list.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm sản phẩm mới
    public boolean themSanPham(SanPham sp) {
        String sql = "INSERT INTO SanPham (maSP, tenSP, loaiSP, size, mauSac, giaBan, soLuong) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, sp.getMaSP());
            ps.setString(2, sp.getTenSP());
            ps.setString(3, sp.getLoaiSP());
            ps.setString(4, sp.getSize());
            ps.setString(5, sp.getMauSac());
            ps.setDouble(6, sp.getGiaBan());
            ps.setInt(7, sp.getSoLuong());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Cập nhật thông tin sản phẩm
    public boolean suaSanPham(SanPham sp) {
        String sql = "UPDATE SanPham SET tenSP=?, loaiSP=?, size=?, mauSac=?, giaBan=?, soLuong=? WHERE maSP=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, sp.getTenSP());
            ps.setString(2, sp.getLoaiSP());
            ps.setString(3, sp.getSize());
            ps.setString(4, sp.getMauSac());
            ps.setDouble(5, sp.getGiaBan());
            ps.setInt(6, sp.getSoLuong());
            ps.setString(7, sp.getMaSP());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Xóa sản phẩm theo mã
    public boolean xoaSanPham(String maSP) {
        String sql = "DELETE FROM SanPham WHERE maSP=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Tìm kiếm sản phẩm theo tên hoặc mã
    public List<SanPham> timKiemSanPham(String tuKhoa) {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM SanPham WHERE tenSP LIKE ? OR maSP LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, "%" + tuKhoa + "%");
            ps.setString(2, "%" + tuKhoa + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(new SanPham(
                    rs.getString("maSP"),
                    rs.getString("tenSP"),
                    rs.getString("loaiSP"),
                    rs.getString("size"),
                    rs.getString("mauSac"),
                    rs.getDouble("giaBan"),
                    rs.getInt("soLuong")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}