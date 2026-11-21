package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/quan_ly_ban_quan_ao";
    private static final String USER = "root";
    private static final String PASS = "Khanh@2008";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.out.println("Loi: Chua them thu vien mysql-connector-j vao Project!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Loi: Khong ket noi duoc MySQL (Kiem tra lai XAMPP/MySQL hoac ten DB)!");
            e.printStackTrace();
        }
        return conn;
    }
    public static void main(String[] args) {
        Connection c = getConnection();
        if (c != null) {
            System.out.println("==> KET NOI THANH CONG VOI MYSQL!");
        } else {
            System.out.println("==> KET NOI THAT BAI!");
        }
    }
}