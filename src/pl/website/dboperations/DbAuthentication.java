/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.website.dboperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author alewicki1990
 */
public class DbAuthentication {

    static public boolean isUsernameExistInDb(String username) {
        String sqlCheckUsernameInDb = "SELECT username FROM users WHERE username='" + username + "';";
        try (Connection conn = DbConncect.getDbConnInstance();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlCheckUsernameInDb)) {

            if (rs.isBeforeFirst()) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    static public boolean authenticateUser(String username, String pass) {
        String sqlAuthentication = "SELECT username FROM users WHERE username= ? AND password = ? ";
        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(sqlAuthentication)) {

            stmt.setString(1, username);
            stmt.setString(2, pass);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.isBeforeFirst()) {
                    return true;
                }
            } catch (SQLException e) {
                System.out.println("xyz.DbConnect.JavaConnect.AuthenticateUser()" + e);
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    static public void createUser(String username, String name, String surname, String email, String pass) throws SQLException, Exception {

        String sqlInsertUser = "INSERT INTO users (username, name, surname, email, password ) values (?,?,?,?,?);";
        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(sqlInsertUser);) {

            stmt.setString(1, username);
            stmt.setString(2, name);
            stmt.setString(3, surname);
            stmt.setString(4, email);
            stmt.setString(5, pass);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }
    }
}
