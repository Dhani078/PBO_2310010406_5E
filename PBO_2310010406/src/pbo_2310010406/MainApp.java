/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pbo_2310010406;

import form.FrmLogin;
import javax.swing.UIManager;

/**
 *
 * @author Anomali
 */
public class MainApp {
    public static void main(String[] args) {
        try {
            // tampilkan tampilan native OS
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // buka form LOGIN dulu
        new FrmLogin().setVisible(true);
    }
}
