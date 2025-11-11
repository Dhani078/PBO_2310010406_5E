/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pbo_2310010406;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author Anomali
 */
public class Crud {
    private final Connection conn;

    public Crud() {
        conn = Koneksi.getKoneksi();
    }

    // ============ PELANGGAN ============

    public int simpanPelanggan(String id, String nama, String hp, String alamat) {
        String sql = "INSERT INTO tb_pelanggan(id_pelanggan, nama_pelanggan, no_hp, alamat) "
                   + "VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, nama);
            ps.setString(3, hp);
            ps.setString(4, alamat);
            return ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal simpan pelanggan: " + e.getMessage());
            return 0;
        }
    }

    public int ubahPelanggan(String id, String nama, String hp, String alamat) {
        String sql = "UPDATE tb_pelanggan SET nama_pelanggan=?, no_hp=?, alamat=? "
                   + "WHERE id_pelanggan=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nama);
            ps.setString(2, hp);
            ps.setString(3, alamat);
            ps.setString(4, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal ubah pelanggan: " + e.getMessage());
            return 0;
        }
    }

    public int hapusPelanggan(String id) {
        String sql = "DELETE FROM tb_pelanggan WHERE id_pelanggan=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal hapus pelanggan: " + e.getMessage());
            return 0;
        }
    }

    // ============ JASA ============

    public int simpanJasa(String nama, String jenisKendaraan, int harga, String ket) {
        String sql = "INSERT INTO tb_jasa(nama_jasa, jenis_kendaraan, harga, keterangan) "
                   + "VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nama);
            ps.setString(2, jenisKendaraan);
            ps.setInt(3, harga);
            ps.setString(4, ket);
            return ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal simpan jasa: " + e.getMessage());
            return 0;
        }
    }

    public int ubahJasa(int id, String nama, String jenisKendaraan, int harga, String ket) {
        String sql = "UPDATE tb_jasa SET nama_jasa=?, jenis_kendaraan=?, harga=?, keterangan=? "
                   + "WHERE id_jasa=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nama);
            ps.setString(2, jenisKendaraan);
            ps.setInt(3, harga);
            ps.setString(4, ket);
            ps.setInt(5, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal ubah jasa: " + e.getMessage());
            return 0;
        }
    }

    public int hapusJasa(int id) {
        String sql = "DELETE FROM tb_jasa WHERE id_jasa=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal hapus jasa: " + e.getMessage());
            return 0;
        }
    }

    // ============ KENDARAAN ============

    public int simpanKendaraan(String idPelanggan, String nopol, String jenis,
                               String merk, String warna) {
        String sql = "INSERT INTO tb_kendaraan(id_pelanggan, no_polisi, jenis, merk, warna) "
                   + "VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idPelanggan);
            ps.setString(2, nopol);
            ps.setString(3, jenis);
            ps.setString(4, merk);
            ps.setString(5, warna);
            return ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal simpan kendaraan: " + e.getMessage());
            return 0;
        }
    }

    public int ubahKendaraan(int idKendaraan, String idPelanggan, String nopol, String jenis,
                             String merk, String warna) {
        String sql = "UPDATE tb_kendaraan SET id_pelanggan=?, no_polisi=?, jenis=?, "
                   + "merk=?, warna=? WHERE id_kendaraan=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idPelanggan);
            ps.setString(2, nopol);
            ps.setString(3, jenis);
            ps.setString(4, merk);
            ps.setString(5, warna);
            ps.setInt(6, idKendaraan);
            return ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal ubah kendaraan: " + e.getMessage());
            return 0;
        }
    }

    public int hapusKendaraan(int idKendaraan) {
        String sql = "DELETE FROM tb_kendaraan WHERE id_kendaraan=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idKendaraan);
            return ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal hapus kendaraan: " + e.getMessage());
            return 0;
        }
    }

}
