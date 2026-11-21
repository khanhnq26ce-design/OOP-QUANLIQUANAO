package model;

public class SanPham {
    private String maSP;
    private String tenSP;
    private String loaiSP;
    private String size;
    private String mauSac;
    private double giaBan;
    private int soLuong;

    public SanPham() {
    }

    public SanPham(String maSP, String tenSP, String loaiSP, String size, String mauSac, double giaBan, int soLuong) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.loaiSP = loaiSP;
        this.size = size;
        this.mauSac = mauSac;
        this.giaBan = giaBan;
        this.soLuong = soLuong;
    }

    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }
    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }
    public String getLoaiSP() { return loaiSP; }
    public void setLoaiSP(String loaiSP) { this.loaiSP = loaiSP; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public String getMauSac() { return mauSac; }
    public void setMauSac(String mauSac) { this.mauSac = mauSac; }
    public double getGiaBan() { return giaBan; }
    public void setGiaBan(double giaBan) { this.giaBan = giaBan; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
}