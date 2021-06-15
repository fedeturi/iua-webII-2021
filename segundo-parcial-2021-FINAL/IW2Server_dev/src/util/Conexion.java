/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author csimes
 */
public class Conexion {
    public static Conexion instance; // objeto instancia de tipo conexion para aplicar Singleton
    private Connection cnn;

    private Conexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try {
                cnn = DriverManager.getConnection("jdbc:mysql://localhost:3306/libreria", "root", "adminroot");
            } catch (SQLException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ahora hacemos el Singleton
    public synchronized static Conexion crearConexion() {
        if (instance == null) {

            instance = new Conexion();
        }

        return instance;

    }

    public void cerrarConexion() {
        instance = null;
    }

    public Connection getCnn() {
        return cnn;
    }
}
