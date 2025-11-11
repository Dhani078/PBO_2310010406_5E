/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package form;

import pbo_2310010406.Koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class FrmTransaksi extends javax.swing.JFrame {
    
     private Connection conn;
    private DefaultTableModel tabmode;
    
    public FrmTransaksi() {
     initComponents();
        setLocationRelativeTo(null);

        conn = Koneksi.getKoneksi();
        if (conn == null) {
            JOptionPane.showMessageDialog(this,
                    "Koneksi DB gagal. Cek Koneksi.java / MySQL / DB pbo_npm.");
            return;
        }
        tampilData();
    }

    // ================== HELPER METHOD ==================

    private void tampilData() {
        if (conn == null) return;

        String[] kolom = {
                "ID", "Kode", "Tanggal",
                "ID Pelanggan", "ID Petugas",
                "Total", "Status"
        };

        tabmode = new DefaultTableModel(null, kolom) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblTransaksi.setModel(tabmode);

        String sql = "SELECT * FROM tb_transaksi ORDER BY id_transaksi DESC";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Object[] data = {
                        rs.getInt("id_transaksi"),
                        rs.getString("kode_transaksi"),
                        rs.getString("tgl_transaksi"),
                        rs.getString("id_pelanggan"),
                        rs.getInt("id_petugas"),
                        rs.getInt("total"),
                        rs.getString("status_bayar")
                };
                tabmode.addRow(data);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menampilkan data: " + e.getMessage());
        }
    }

    private void kosong() {
        txtId.setText("");
        txtKode.setText("");
        txtTgl.setText("");
        txtIdPelanggan.setText("");
        txtIdPetugas.setText("");
        txtTotal.setText("");
        cmbStatus.setSelectedIndex(0);
        txtKode.requestFocus();
    }

    private void simpan() {
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi belum siap.");
            return;
        }

        // Wajib isi header
        if (txtKode.getText().trim().isEmpty()
                || txtTgl.getText().trim().isEmpty()
                || txtIdPelanggan.getText().trim().isEmpty()
                || txtIdPetugas.getText().trim().isEmpty()
                || txtTotal.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Lengkapi Kode, Tanggal, ID Pelanggan, ID Petugas, dan Total.");
            return;
        }

        try {
            int idPetugas = Integer.parseInt(txtIdPetugas.getText().trim());
            int total = Integer.parseInt(txtTotal.getText().trim());

            String sql = "INSERT INTO tb_transaksi "
                    + "(kode_transaksi, tgl_transaksi, id_pelanggan, id_petugas, total, status_bayar) "
                    + "VALUES (?,?,?,?,?,?)";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, txtKode.getText().trim());
                ps.setString(2, txtTgl.getText().trim()); // YYYY-MM-DD
                ps.setString(3, txtIdPelanggan.getText().trim());
                ps.setInt(4, idPetugas);
                ps.setInt(5, total);
                ps.setString(6, cmbStatus.getSelectedItem().toString());
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Data transaksi berhasil disimpan.");
            tampilData();
            kosong();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "ID Petugas dan Total harus berupa angka.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menyimpan data: " + e.getMessage());
        }
    }

    private void ubah() {
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi belum siap.");
            return;
        }

        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Pilih data pada tabel yang akan diubah.");
            return;
        }

        if (txtKode.getText().trim().isEmpty()
                || txtTgl.getText().trim().isEmpty()
                || txtIdPelanggan.getText().trim().isEmpty()
                || txtIdPetugas.getText().trim().isEmpty()
                || txtTotal.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Lengkapi Kode, Tanggal, ID Pelanggan, ID Petugas, dan Total.");
            return;
        }

        try {
            int idTrans = Integer.parseInt(txtId.getText().trim());
            int idPetugas = Integer.parseInt(txtIdPetugas.getText().trim());
            int total = Integer.parseInt(txtTotal.getText().trim());

            String sql = "UPDATE tb_transaksi SET "
                    + "kode_transaksi=?, tgl_transaksi=?, id_pelanggan=?, "
                    + "id_petugas=?, total=?, status_bayar=? "
                    + "WHERE id_transaksi=?";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, txtKode.getText().trim());
                ps.setString(2, txtTgl.getText().trim());
                ps.setString(3, txtIdPelanggan.getText().trim());
                ps.setInt(4, idPetugas);
                ps.setInt(5, total);
                ps.setString(6, cmbStatus.getSelectedItem().toString());
                ps.setInt(7, idTrans);
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Data transaksi berhasil diubah.");
            tampilData();
            kosong();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "ID / ID Petugas / Total harus angka.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal mengubah data: " + e.getMessage());
        }
    }

    private void hapus() {
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi belum siap.");
            return;
        }

        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Pilih data pada tabel yang akan dihapus.");
            return;
        }

        int ok = JOptionPane.showConfirmDialog(this,
                "Yakin hapus data ini?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;

        try {
            int idTrans = Integer.parseInt(txtId.getText().trim());
            String sql = "DELETE FROM tb_transaksi WHERE id_transaksi=?";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idTrans);
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Data transaksi berhasil dihapus.");
            tampilData();
            kosong();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "ID tidak valid.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menghapus data: " + e.getMessage());
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSimpan = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnKeluar = new javax.swing.JButton();
        txtId = new javax.swing.JTextField();
        btnHitung = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnBersih = new javax.swing.JButton();
        txtKode = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTgl = new javax.swing.JTextField();
        txtIdPelanggan = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTransaksi = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        txtIdPetugas = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        jLabel1.setText("Id Transaksi");

        btnKeluar.setText("Keluar");
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });

        btnHitung.setText("Hitung");
        btnHitung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHitungActionPerformed(evt);
            }
        });

        jLabel2.setText("Kode Transaksi");

        btnBersih.setText("Bersih");
        btnBersih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBersihActionPerformed(evt);
            }
        });

        jLabel3.setText("(YYYY-MM-DD)");

        tblTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTransaksiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblTransaksi);

        jLabel4.setText("Id Pelanggan");

        jLabel5.setText("Id Petugas");

        jLabel6.setText("Total");

        jLabel7.setText("Status");

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Belum", "Lunas" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtIdPetugas, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtKode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTgl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdPelanggan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSimpan)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnHitung)
                                .addGap(18, 18, 18)
                                .addComponent(btnKeluar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnBersih))
                            .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 552, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtTgl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtIdPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtIdPetugas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSimpan)
                            .addComponent(btnHitung)
                            .addComponent(btnKeluar)
                            .addComponent(btnBersih)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        simpan();// TODO add your handling code here:
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnHitungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHitungActionPerformed
        ubah();//TODO add your handling code here:
    }//GEN-LAST:event_btnHitungActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        hapus();// TODO add your handling code here:
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void btnBersihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBersihActionPerformed
        kosong();// TODO add your handling code here:
    }//GEN-LAST:event_btnBersihActionPerformed

    private void tblTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTransaksiMouseClicked
        int baris = tblTransaksi.getSelectedRow();
        if (baris < 0) return;

        txtId.setText(tabmode.getValueAt(baris, 0).toString());
        txtKode.setText(tabmode.getValueAt(baris, 1).toString());
        txtTgl.setText(tabmode.getValueAt(baris, 2).toString());
        txtIdPelanggan.setText(tabmode.getValueAt(baris, 3).toString());
        txtIdPetugas.setText(tabmode.getValueAt(baris, 4).toString());
        txtTotal.setText(tabmode.getValueAt(baris, 5).toString());
        cmbStatus.setSelectedItem(tabmode.getValueAt(baris, 6).toString());
       
    }//GEN-LAST:event_tblTransaksiMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
      } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
    java.util.logging.Logger.getAnonymousLogger()
        .log(java.util.logging.Level.SEVERE, null, ex);
}     
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FrmTransaksi().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBersih;
    private javax.swing.JButton btnHitung;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblTransaksi;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtIdPelanggan;
    private javax.swing.JTextField txtIdPetugas;
    private javax.swing.JTextField txtKode;
    private javax.swing.JTextField txtTgl;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
