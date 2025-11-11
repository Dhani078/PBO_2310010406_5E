/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pbo_2310010406;

/**
 *
 * @author Anomali
 */
public class Session {
    public static int    userId   = 0;
    public static String nama     = "";
    public static String username = "";
    public static String role     = ""; // "Admin" / "User"

    public static void clear() {
        userId = 0;
        nama = "";
        username = "";
        role = "";
    }
}
