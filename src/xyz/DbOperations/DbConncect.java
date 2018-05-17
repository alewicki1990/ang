/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.DbOperations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author alewicki1990
 */
class DbConncect {

    private Connection conn;

    DbConncect() {
        conn = ConnectDb();
    }

    private Connection ConnectDb() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:users.db");
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }
    
    Connection getDbConnection(){
        return conn;
    }
    
    static Connection getDbConnInstance(){
        DbConncect newInstance = new DbConncect();
        Connection conn = newInstance.getDbConnection();
        return conn;
        
    }
    
}
