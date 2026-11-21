CREATE DATABASE quan_ly_ban_quan_ao;
USE quan_ly_ban_quan_ao;
CREATE TABLE NhanVien (
    maNV VARCHAR(10) PRIMARY KEY,
    tenNV VARCHAR(50) NOT NULL,
    soDienThoai VARCHAR(15),
    taiKhoan VARCHAR(30) NOT NULL,
    matKhau VARCHAR(30) NOT NULL,
    chucVu VARCHAR(20) 
);
CREATE TABLE KhachHang (
    maKH VARCHAR(10) PRIMARY KEY,
    tenKH VARCHAR(50) NOT NULL,
    soDienThoai VARCHAR(15),
    diaChi VARCHAR(100)
);
CREATE TABLE SanPham (
    maSP VARCHAR(10) PRIMARY KEY,
    tenSP VARCHAR(100) NOT NULL,
    loaiSP VARCHAR(30),       -- Ví dụ: Ao thun, Quan Jean, Ao khoac
    size VARCHAR(10),         -- Ví dụ: S, M, L, XL
    mauSac VARCHAR(20),
    giaBan DOUBLE NOT NULL,
    soLuong INT NOT NULL
);
CREATE TABLE HoaDon (
    maHD VARCHAR(10) PRIMARY KEY,
    ngayLap DATE,
    maNV VARCHAR(10),
    maKH VARCHAR(10),
    tongTien DOUBLE,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH)
);
CREATE TABLE ChiTietHoaDon (
    maHD VARCHAR(10),
    maSP VARCHAR(10),
    soLuongMua INT,
    donGia DOUBLE,
    PRIMARY KEY (maHD, maSP),
    FOREIGN KEY (maHD) REFERENCES HoaDon(maHD),
    FOREIGN KEY (maSP) REFERENCES SanPham(maSP)
);

-- =============================================
-- THÊM DỮ LIỆU MẪU ĐỂ TEST CHƯƠNG TRÌNH
-- =============================================

INSERT INTO NhanVien VALUES 
('NV01', 'Nguyen Quoc Khanh', '0901112222', 'admin', 'quanli', 'QuanLy'),
('NV02', 'Tran Thi Binh', '0903334444', 'nv1', 'nhanvien', 'NhanVien');

INSERT INTO KhachHang VALUES 
('KH00', 'Khach bth', '0000000000', 'Tai cua hang'),
('KH01', 'Le Hoang Nam', '0912345678', 'Da Nang'),
('KH02', 'Pham Minh Thu', '0987654321', 'Quang Ngai');

INSERT INTO SanPham VALUES 
('SP01', 'Ao thun tron Basic', 'Ao thun', 'L', 'Den', 150000, 50),
('SP02', 'Ao thun in hinh', 'Ao thun', 'M', 'Trang', 180000, 30),
('SP03', 'Quan Jean ong rong', 'Quan Jean', 'XL', 'Xanh', 350000, 20),
('SP04', 'Ao khoac kaki', 'Ao khoac', 'L', 'Reu', 420000, 15),
('SP05', 'Ao so mi tay dai', 'Ao so mi', 'M', 'Xanh nhat', 250000, 40);

INSERT INTO HoaDon VALUES 
('HD01', '2026-11-20', 'NV01', 'KH01', 500000),
('HD02', '2026-11-21', 'NV02', 'KH02', 360000);

INSERT INTO ChiTietHoaDon VALUES 
('HD01', 'SP01', 1, 150000),
('HD01', 'SP03', 1, 350000),
('HD02', 'SP02', 2, 180000);

