/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.DbConnect;

/**
 *
 * @author alewicki
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.sqlite.*;
import xyz.testControl.TestEntity;

public class DbConnectionAndDbInstructions {

    public DbConnectionAndDbInstructions() {
        conn = ConnectDb();
    }

    private Connection conn;

    private Connection ConnectDb() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:users.db");
            return conn;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }

    public boolean isUsernameExistInDb(String username) {
        String sqlCheckUsernameInDb = "SELECT username FROM users WHERE username= ?";
        ResultSet rs;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlCheckUsernameInDb);
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
        ResultSet rs;

        try {
            PreparedStatement stmt = conn.prepareStatement(sqlAuthentication);
            stmt.setString(1, username);
            stmt.setString(2, pass);
            rs = stmt.executeQuery();
            if (rs.isBeforeFirst()) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println("xyz.DbConnect.JavaConnect.AuthenticateUser()" + e);
            return false;
        }
        return false;
    }

    public void createUser(String username, String name, String surname, String email, String pass) {

        String sqlInsertUser = "INSERT INTO users (username, name, surname, email, password ) values (?,?,?,?,?);";
        try {
            PreparedStatement stmt = conn.prepareStatement(sqlInsertUser);
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
        try {
            PreparedStatement stmt = conn.prepareStatement(sqlUserTestList);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("xyz.DbConnect.JavaConnect.createUser()" + e.getMessage());
        }

    }

    public DefaultTableModel getQueryToDefTable(String query) {

        String sqlGetQueryUserTests = query;
        DefaultTableModel model = new  DefaultTableModel(); 

        ResultSet rs;

        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlGetQueryUserTests);
            if (rs != null) {
                ResultSetMetaData columns = rs.getMetaData();
                ArrayList<String> columnsNamesAL = new ArrayList<>();
                for (int i = 1; i <= columns.getColumnCount(); i++) {
                    columnsNamesAL.add(columns.getColumnName(i));
                    System.out.println("xyz.DbConnect.DbConnectionAndDbInstructions.getQueryUserTests() dodawanie nazw kolumn, iteracja : " + i + " " + columnsNamesAL.toString());
                }
                Object[] columnsNamesObArray = columnsNamesAL.toArray(new Object[columnsNamesAL.size()]);

                System.out.println("Object[] columnsNamesObArray to: " + Arrays.toString(columnsNamesObArray));

                model.setColumnCount(columns.getColumnCount());
                model.setColumnIdentifiers(columnsNamesObArray);

                System.out.println("columns.getColumnCount(): " + columns.getColumnCount());
                while (rs.next()) {
                    Object[] objects = new Object[columns.getColumnCount()];
                    // tanks to umit ozkan for the bug fix!
                    for (int i = 0; i < columns.getColumnCount(); i++) {
                        objects[i] = rs.getObject(i + 1);
                        System.out.println("Element " + i + " w tablicy objects: " + objects[i]);
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

}
