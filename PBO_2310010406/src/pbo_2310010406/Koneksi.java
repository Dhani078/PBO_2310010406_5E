/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pbo_2310010406;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author Anomali
 */
public class Koneksi {
    private static Connection conn;

    public static Connection getKoneksi() {
        if (conn == null) {
            try {
                // SESUAIKAN port dan nama DB kalau beda
                String url  = "jdbc:mysql://localhost:3306/pbo_2310010406?useSSL=false";
                String user = "root";
                String pass = "";

                // === PENTING: driver lama untuk 5.1.x ===
                Class.forName("com.mysql.jdbc.Driver");

                conn = DriverManager.getConnection(url, user, pass);
                System.out.println("[OK] Koneksi (driver 5.1.x)");
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null,
                    "Driver 5.1.x tidak ditemukan di Libraries.\n" + e.getMessage());
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,
                    "Gagal konek ke DB.\nCek MySQL jalan, DB 'pbo_npm', port 3306.\n" + e.getMessage());
            }
        }
        return conn;
    }
    
}
