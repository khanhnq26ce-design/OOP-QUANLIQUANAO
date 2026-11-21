package view;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import util.DBConnection;

public class LoginFrame extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;

    public LoginFrame() {
        setTitle("DANG NHAP HE THONG");
        setSize(380, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("QUAN LY SHOP QUAN AO", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlForm = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlForm.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        pnlForm.add(new JLabel("Tai khoan:"));
        txtUser = new JTextField("");
        pnlForm.add(txtUser);

        pnlForm.add(new JLabel("Mat khau:"));
        txtPass = new JPasswordField("");
        pnlForm.add(txtPass);
        add(pnlForm, BorderLayout.CENTER);

        JButton btnLogin = new JButton("Dang nhap");
        JPanel pnlBottom = new JPanel();
        pnlBottom.add(btnLogin);
        add(pnlBottom, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> xuLyDangNhap());
    }

    private void xuLyDangNhap() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();
        String sql = "SELECT maNV, tenNV, chucVu FROM NhanVien WHERE taiKhoan=? AND matKhau=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String maNV = rs.getString("maNV");
                String tenNV = rs.getString("tenNV");
                String chucVu = rs.getString("chucVu");
                
                this.dispose();
                new MainFrame(maNV, tenNV, chucVu).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Sai tai khoan hoac mat khau!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}