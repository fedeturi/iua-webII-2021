package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.AccountDTO;
import util.Conexion;

public class AccountDAO implements IntCRUD<AccountDTO> {
    public static final Conexion con = Conexion.createConnection();
    public static final String SQL_INSERT = "insert into cuentas(dni,apellido,nroCuenta,tipoCuenta,monto,dias,capital) values (?,?,?,?,?,?,?)";
    public static final String SQL_DELETE = "delete from cuentas where dni=?";
    public static final String SQL_UPDATE = "update cuentas set apellido=?,nroCuenta=?,tipoCuenta=?,monto=?,dias=?,capital=? where dni=?";
    public static final String SQL_READ = "select * from cuentas where dni=?";
    public static final String SQL_READALL = "select * from cuentas";

    @Override
    public boolean create(AccountDTO e) {
        try {
            int control = 0;
            PreparedStatement ps = con.getCnn().prepareCall(SQL_INSERT);
            ps.setInt(1, e.getDni());
            ps.setString(2, e.getSurname());
            ps.setInt(3, e.getAccountNumber());
            ps.setString(4, e.getAccountType());
            ps.setFloat(5, e.getStartingCapital());
            ps.setInt(6, e.getDays());
            ps.setFloat(7, e.getEndingCapital());

            control = ps.executeUpdate();
            if (control > 0) {
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            con.closeConnection();
        }
        return false;
    }

    @Override
    public boolean delete(Object key) {
        try {
            int control = 0;
            PreparedStatement ps = con.getCnn().prepareCall(SQL_DELETE);
            ps.setInt(1, (int) key);

            control = ps.executeUpdate();
            if (control > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            con.closeConnection();
        }
        return false;

    }

    @Override
    public boolean update(AccountDTO e) {
        try {
            int control = 0;
            PreparedStatement ps = con.getCnn().prepareCall(SQL_UPDATE);
            ps.setInt(1, e.getDni());
            ps.setString(2, e.getSurname());
            ps.setInt(3, e.getAccountNumber());
            ps.setString(4, e.getAccountType());
            ps.setFloat(5, e.getStartingCapital());
            ps.setInt(6, e.getDays());
            ps.setFloat(7, e.getEndingCapital());
            control = ps.executeUpdate();
            if (control > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            con.closeConnection();
        }
        return false;

    }

    @Override
    public AccountDTO read(Object key) {

        AccountDTO account = null;
        try {

            ResultSet rs = null;
            PreparedStatement ps = con.getCnn().prepareCall(SQL_READ);
            ps.setInt(1, (int) key);

            rs = ps.executeQuery();
            if (rs.next()) {
                account = new AccountDTO();
                account.setDni(rs.getInt("dni"));
                account.setSurname(rs.getString("apellido"));
                account.setAccountNumber(rs.getInt("nroCuenta"));
                account.setAccountType(rs.getString("tipoCuenta"));
                account.setStartingCapital(rs.getFloat("monto"));
                account.setDays(rs.getInt("dias"));
                account.setEndingCapital(rs.getFloat("capital"));
                return account;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            con.closeConnection();
        }
        return account;

    }

    @Override
    public List<AccountDTO> readAll() {
        AccountDTO account;
        List<AccountDTO> lista = new ArrayList<>();

        try {

            ResultSet rs = null;
            PreparedStatement ps = con.getCnn().prepareCall(SQL_READALL);

            rs = ps.executeQuery();

            while (rs.next()) {

                account = new AccountDTO();
                account.setDni(rs.getInt("dni"));
                account.setSurname(rs.getString("apellido"));
                account.setAccountNumber(rs.getInt("nroCuenta"));
                account.setAccountType(rs.getString("tipoCuenta"));
                account.setStartingCapital(rs.getFloat("monto"));
                account.setDays(rs.getInt("dias"));
                account.setEndingCapital(rs.getFloat("capital"));
                lista.add(account);

            }
        } catch (SQLException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        finally {
            con.closeConnection();
        }
        return lista;

    }

}
