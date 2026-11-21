package view;

import dao.SanPhamDAO;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.SanPham;

public class QuanLySanPhamFrame extends JFrame {
    private JTextField txtMaSP, txtTenSP, txtMauSac, txtGiaBan, txtSoLuong, txtTimKiem;
    private JComboBox<String> cboLoaiSP, cboSize;
    private JTable table;
    private DefaultTableModel tableModel;
    private SanPhamDAO spDAO = new SanPhamDAO();

    public QuanLySanPhamFrame() {
        setTitle("QUAN LY CUA HANG QUAN AO - QUAN LY SAN PHAM");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- PHAN 1: FORM NHAP LIEU (BEN TREN) ---
        JPanel pnlTop = new JPanel(new BorderLayout(10, 10));
        pnlTop.setBorder(BorderFactory.createTitledBorder("Thong tin san pham"));

        JPanel pnlForm = new JPanel(new GridLayout(4, 4, 10, 10));
        pnlForm.add(new JLabel("Ma SP:"));
        txtMaSP = new JTextField();
        pnlForm.add(txtMaSP);

        pnlForm.add(new JLabel("Ten SP:"));
        txtTenSP = new JTextField();
        pnlForm.add(txtTenSP);

        pnlForm.add(new JLabel("Loai SP:"));
        cboLoaiSP = new JComboBox<>(new String[]{"Ao thun", "Quan dai", "Quan ngan", "Ao khoac", "Ao so mi", "Vay"});
        pnlForm.add(cboLoaiSP);

        pnlForm.add(new JLabel("Size:"));
        cboSize = new JComboBox<>(new String[]{"S", "M", "L", "XL", "XXL"});
        pnlForm.add(cboSize);

        pnlForm.add(new JLabel("Mau sac:"));
        txtMauSac = new JTextField();
        pnlForm.add(txtMauSac);

        pnlForm.add(new JLabel("Gia ban (VND):"));
        txtGiaBan = new JTextField();
        pnlForm.add(txtGiaBan);

        pnlForm.add(new JLabel("So luong hang:"));
        txtSoLuong = new JTextField();
        pnlForm.add(txtSoLuong);

        pnlTop.add(pnlForm, BorderLayout.CENTER);

        // --- CAC NUT CHUC NANG ---
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton btnThem = new JButton("Them");
        JButton btnSua = new JButton("Sua");
        JButton btnXoa = new JButton("Xoa");
        JButton btnLamMoi = new JButton("Lam moi");
        txtTimKiem = new JTextField(15);
        JButton btnTim = new JButton("Tim kiem");

        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLamMoi);
        pnlButtons.add(new JLabel(" | Tim theo ten/ma:"));
        pnlButtons.add(txtTimKiem);
        pnlButtons.add(btnTim);

        pnlTop.add(pnlButtons, BorderLayout.SOUTH);
        add(pnlTop, BorderLayout.NORTH);

        // --- PHAN 2: BANG HIEN THI DU LIEU (O GIUA) ---
        String[] columns = {"Ma SP", "Ten SP", "Loai SP", "Size", "Mau sac", "Gia ban", "So luong"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Tai du lieu tu MySQL len bang khi mo form
        loadDataToTable(spDAO.getAllSanPham());

        // --- XU LY SU KIEN ---
        // 1. Click vao dong tren bang -> do du lieu len o nhap
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtMaSP.setText(tableModel.getValueAt(row, 0).toString());
                    txtMaSP.setEditable(false); // Khong cho sua khoa chinh
                    txtTenSP.setText(tableModel.getValueAt(row, 1).toString());
                    cboLoaiSP.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                    cboSize.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                    txtMauSac.setText(tableModel.getValueAt(row, 4).toString());
                    txtGiaBan.setText(tableModel.getValueAt(row, 5).toString());
                    txtSoLuong.setText(tableModel.getValueAt(row, 6).toString());
                }
            }
        });

        // 2. Nut Them
        btnThem.addActionListener(e -> {
            try {
                SanPham sp = getSanPhamFromForm();
                if (spDAO.themSanPham(sp)) {
                    JOptionPane.showMessageDialog(this, "Them san pham thanh cong!");
                    loadDataToTable(spDAO.getAllSanPham());
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Them that bai (Co the trung Ma SP)!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Vui long nhap dung dinh dang so cho Gia va So luong!");
            }
        });

        // 3. Nut Sua
        btnSua.addActionListener(e -> {
            try {
                SanPham sp = getSanPhamFromForm();
                if (spDAO.suaSanPham(sp)) {
                    JOptionPane.showMessageDialog(this, "Cap nhat thanh cong!");
                    loadDataToTable(spDAO.getAllSanPham());
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Cap nhat that bai!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Loi nhap lieu!");
            }
        });

        // 4. Nut Xoa
        btnXoa.addActionListener(e -> {
            String maSP = txtMaSP.getText().trim();
            if (maSP.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui long chon san pham can xoa!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Ban co chac muon xoa " + maSP + "?", "Xac nhan", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (spDAO.xoaSanPham(maSP)) {
                    JOptionPane.showMessageDialog(this, "Xoa thanh cong!");
                    loadDataToTable(spDAO.getAllSanPham());
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Khong the xoa (San pham da co trong hoa don)!");
                }
            }
        });

        // 5. Nut Lam moi
        btnLamMoi.addActionListener(e -> {
            clearForm();
            loadDataToTable(spDAO.getAllSanPham());
        });

        // 6. Nut Tim kiem
        btnTim.addActionListener(e -> {
            String tuKhoa = txtTimKiem.getText().trim();
            loadDataToTable(spDAO.timKiemSanPham(tuKhoa));
        });
    }

    private void loadDataToTable(List<SanPham> list) {
        tableModel.setRowCount(0);
        for (SanPham sp : list) {
            tableModel.addRow(new Object[]{
                sp.getMaSP(), sp.getTenSP(), sp.getLoaiSP(),
                sp.getSize(), sp.getMauSac(), sp.getGiaBan(), sp.getSoLuong()
            });
        }
    }

    private SanPham getSanPhamFromForm() {
        return new SanPham(
            txtMaSP.getText().trim(),
            txtTenSP.getText().trim(),
            cboLoaiSP.getSelectedItem().toString(),
            cboSize.getSelectedItem().toString(),
            txtMauSac.getText().trim(),
            Double.parseDouble(txtGiaBan.getText().trim()),
            Integer.parseInt(txtSoLuong.getText().trim())
        );
    }

    private void clearForm() {
        txtMaSP.setText("");
        txtMaSP.setEditable(true);
        txtTenSP.setText("");
        txtMauSac.setText("");
        txtGiaBan.setText("");
        txtSoLuong.setText("");
        txtTimKiem.setText("");
    }

    public void reloadData() {
        loadDataToTable(spDAO.getAllSanPham());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuanLySanPhamFrame().setVisible(true));
    }
}