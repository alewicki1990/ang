/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.DbOperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Użytkownik
 */
public class DbInstructions {

    public boolean isUsernameExistInDb(String username) {
        String sqlCheckUsernameInDb = "SELECT username FROM users WHERE username= ?";
        try (Connection conn = DbConncect.getDbConnInstance();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlCheckUsernameInDb)) {

            if (rs.isBeforeFirst()) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println("xyz.DbConnect.JavaConnect.isUsernameExistInDb()" + e);
            return false;
        }
        return false;
    }

    public boolean authenticateUser(String username, String pass) {
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
            System.out.println("xyz.DbConnect.JavaConnect.AuthenticateUser()" + e);
            return false;
        }
        return false;
    }

    public void createUser(String username, String name, String surname, String email, String pass) {

        String sqlInsertUser = "INSERT INTO users (username, name, surname, email, password ) values (?,?,?,?,?);";
        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(sqlInsertUser);) {

            stmt.setString(1, username);
            stmt.setString(2, name);
            stmt.setString(3, surname);
            stmt.setString(4, email);
            stmt.setString(5, pass);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("xyz.DbConnect.JavaConnect.createUser()" + e.getMessage());
        }

        String sqlUserTestList = "CREATE TABLE " + username + "_tests (test_name CHAR, active_date DATE, chg_date DATE)";
        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(sqlUserTestList)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("xyz.DbConnect.JavaConnect.createUser()" + e.getMessage());
        }

    }

    public DefaultTableModel getQueryToDefTable(String query) {

        String sqlGetQueryUserTests = query;
        DefaultTableModel model = new DefaultTableModel();

        try (Connection conn = DbConncect.getDbConnInstance();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlGetQueryUserTests)) {

            if (rs != null) {
                ResultSetMetaData columns = rs.getMetaData();
                ArrayList<String> columnsNamesAL = new ArrayList<>();

                for (int i = 1; i <= columns.getColumnCount(); i++) {
                    columnsNamesAL.add(columns.getColumnName(i));
                }
                Object[] columnsNamesObArray = columnsNamesAL.toArray(new Object[columnsNamesAL.size()]);

                model.setColumnCount(columns.getColumnCount());
                model.setColumnIdentifiers(columnsNamesObArray);

                while (rs.next()) {
                    Object[] objects = new Object[columns.getColumnCount()];
                    // tanks to umit ozkan for the bug fix!
                    for (int i = 0; i < columns.getColumnCount(); i++) {
                        objects[i] = rs.getObject(i + 1);
                    }
                    model.addRow(objects);
                }
            }
            return model;

        } catch (Exception e) {
            System.out.println("\"xyz.DbConnect.JavaConnect.getQueryUserTests()\": " + e.getMessage() + e);
        }
        return model;
    }

    public String createNewTest(String username, String newTestName) {

        String sqlInsertUser = "INSERT INTO " + username + "_tests (test_name) values ( ? );";
        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(sqlInsertUser);) {

            stmt.setString(1, newTestName);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("xyz.DbConnect.JavaConnect.createUser()" + e.getMessage());
            return null;
        }
        System.out.println("tu syf");
        String sqlUserTestList = "CREATE TABLE " + username + "_" + newTestName + " (primal_word CHAR NOT NULL, derivate_word CHAR NOT NULL, "
                + "PRIMARY KEY (primal_word, derivate_word))";
        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(sqlUserTestList)) {

            stmt.executeUpdate();
            return username + "_" + newTestName;

        } catch (SQLException e) {
            System.out.println("xyz.DbConnect.JavaConnect.createUser()" + e.getMessage());
            return null;
        }
    }

    public void deleteTest(String username, String testName) {
        String deleteFromListTableQuery = "DELETE FROM " + username + "_tests" + "WHERE test_name = " + testName;
        String dropTableQuery = "DROP TABLE " + username + "_" + testName;

        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(deleteFromListTableQuery);) {
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("xyz.DbConnect.JavaConnect.deleteTest()" + e.getMessage());
            return;
        }

        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(dropTableQuery);) {
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("xyz.DbConnect.JavaConnect.deleteTest()" + e.getMessage());
            return;
        }

    }

}
