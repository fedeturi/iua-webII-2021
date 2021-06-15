/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.libroDTO;
import util.Conexion;

/**
 *
 * @author csimes
 */
public class libroDAO implements intCRUD<libroDTO> {
    public static final Conexion con = Conexion.crearConexion();
    public static final String SQL_INSERT = "insert into libro(libro_id,titulo,autor,precio) values (?,?,?,?)";
    public static final String SQL_DELETE = "delete from libro where libro_id=?";
    public static final String SQL_UPDATE = "update libro set titulo=?,autor=?,precio=? where libro_id=?";
    public static final String SQL_READ = "select * from libro where libro_id=?";
    public static final String SQL_READALL = "select * from libro";

    @Override

    public boolean create(libroDTO e) {
        try {
            int control = 0;
            PreparedStatement ps = con.getCnn().prepareCall(SQL_INSERT);
            ps.setInt(1, e.getLibro_id());
            ps.setString(2, e.getTitulo());
            ps.setString(3, e.getAutor());
            ps.setFloat(4, e.getPrecio());
            control = ps.executeUpdate();
            if (control > 0) {
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(libroDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            con.cerrarConexion();
        }
        return false;
    }

    @Override
    public boolean delete(Object clave) {
        try {
            int control = 0;
            PreparedStatement ps = con.getCnn().prepareCall(SQL_DELETE);
            ps.setInt(1, (int) clave);

            control = ps.executeUpdate();
            if (control > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(libroDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            con.cerrarConexion();
        }
        return false;

    }

    @Override
    public boolean update(libroDTO e) {
        try {
            int control = 0;
            PreparedStatement ps = con.getCnn().prepareCall(SQL_UPDATE);

            ps.setString(1, e.getTitulo());
            ps.setString(2, e.getAutor());
            ps.setFloat(3, e.getPrecio());
            ps.setInt(4, e.getLibro_id());
            control = ps.executeUpdate();
            if (control > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(libroDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            con.cerrarConexion();
        }
        return false;

    }

    @Override
    public libroDTO read(Object clave) {

        libroDTO libro = null;
        try {

            ResultSet rs = null;
            PreparedStatement ps = con.getCnn().prepareCall(SQL_READ);
            ps.setInt(1, (int) clave);

            rs = ps.executeQuery();
            if (rs.next()) {
                libro = new libroDTO();
                libro.setLibro_id(rs.getInt("libro_id"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setAutor(rs.getString("autor"));
                libro.setPrecio(rs.getFloat("precio"));
                return libro;
            }
        } catch (SQLException ex) {
            Logger.getLogger(libroDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            con.cerrarConexion();
        }
        return libro;

    }

    @Override
    public List<libroDTO> readAll() {
        libroDTO libro;
        List<libroDTO> lista = new ArrayList<>();

        try {

            ResultSet rs = null;
            PreparedStatement ps = con.getCnn().prepareCall(SQL_READALL);

            rs = ps.executeQuery();

            while (rs.next()) {

                libro = new libroDTO();
                libro.setLibro_id(rs.getInt("libro_id"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setAutor(rs.getString("autor"));
                libro.setPrecio(rs.getFloat("precio"));
                lista.add(libro);

            }
        } catch (SQLException ex) {
            Logger.getLogger(libroDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        finally {
            con.cerrarConexion();
        }
        return lista;

    }

}
