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

    public void createNewTest(String userName, String newTestName) {

        String sqlInsertUser = "INSERT INTO " + userName + "_tests (test_name) values ( ? );";
        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(sqlInsertUser);) {

            stmt.setString(1, newTestName);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("xyz.DbConnect.JavaConnect.createUser()" + e.getMessage());
        }

        String sqlUserTestContentTable = "CREATE TABLE " + userName + "_" + newTestName + " (primal_word CHAR NOT NULL, derivate_word CHAR NOT NULL, "
                + "PRIMARY KEY (primal_word, derivate_word))";
        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(sqlUserTestContentTable)) {

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("xyz.DbConnect.JavaConnect.createUser()" + e.getMessage());
        }

        String sqlUserTestContentTableTmp = "CREATE TABLE " + userName + "_" + newTestName + "_temporary_table AS SELECT* FROM " + userName + "_" + newTestName + ";";
        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(sqlUserTestContentTableTmp)) {

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("xyz.DbConnect.JavaConnect.createUser()" + e.getMessage());
        }

    }

    public void deleteTest(String username, String testName) {
        String deleteFromListTableQuery = "DELETE FROM " + username + "_tests " + "WHERE test_name='" + testName + "';";
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

    public void chgSelectedTestName(String userName, String oldTestName, String newTestName) throws Exception {
        
        String sqlCheckIfTestNameInListExist = "SELECT test_name FROM " + userName + "_tests WHERE test_name='" + oldTestName + "';";
        try (Connection conn = DbConncect.getDbConnInstance();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlCheckIfTestNameInListExist)) {

            if (!rs.isBeforeFirst()) {
                throw new SQLException();
            }

        } catch (SQLException e) {
            System.out.println("xyz.DbConnect.JavaConnect.sqlCheckIfTestNameInListExist()" + e);
            throw new Exception();
        }
        
        String sqlCheckIfContestTestTableExist = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + userName + "_" + oldTestName + "';";
        try (Connection conn = DbConncect.getDbConnInstance();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlCheckIfContestTestTableExist)) {

            if (!rs.isBeforeFirst()) {
                throw new SQLException();
            }

        } catch (SQLException e) {
            System.out.println("xyz.DbConnect.JavaConnect.sqlCheckIfContestTestTableExist()" + e);
            throw new Exception();
        }
        
        String sqlChgTestName = "ALTER TABLE " + userName + "_" + oldTestName + " RENAME TO " + userName + "_" + newTestName + ";";
        try (Connection conn = DbConncect.getDbConnInstance();
                Statement stmt = conn.prepareStatement(sqlChgTestName)) {

            stmt.executeUpdate(sqlChgTestName);

        } catch (SQLException e) {
            System.out.println("xyz.DbConnect.JavaConnect.sqlChgTestName()" + e.getMessage());
            throw new Exception();
        }
        
        String sqlChgTestNameInTestList = "UPDATE " + userName + "_tests SET test_name='" + newTestName + "' WHERE test_name='" + oldTestName + "';";
        try (Connection conn = DbConncect.getDbConnInstance();
                Statement stmt = conn.prepareStatement(sqlChgTestNameInTestList)) {

            stmt.executeUpdate(sqlChgTestNameInTestList);
                    
        } catch (SQLException e) {
            System.out.println("xyz.DbConnect.JavaConnect.sqlChgTestNameInTestList()" + e.getMessage());
            throw new Exception();
        }
        
    }
}
