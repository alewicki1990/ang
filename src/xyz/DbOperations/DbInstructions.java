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
import javax.swing.table.DefaultTableModel;
import xyz.testControl.PairOfWords;

/**
 *
 * @author alewicki1990
 */
public class DbInstructions {

    String username;

    public DbInstructions(String username) {
        this.username = username;
    }

    public ArrayList<PairOfWords> getTestListArrayList(String testName) {
        ArrayList<PairOfWords> recordData = new ArrayList<>();
        PairOfWords record;
        String sqlAuthentication = "SELECT * FROM " + username + "_" + testName;
        try (Connection conn = DbConncect.getDbConnInstance();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlAuthentication)) {

            while (rs.next()) {

                record = new PairOfWords(rs.getString("primal_word"), rs.getString("derivate_word"));
                recordData.add(record);
            }

        } catch (SQLException e) {
            System.out.println("xyz.DbConnect.JavaConnect.AuthenticateUser()" + e);
        }
        return recordData;
    }

    private DefaultTableModel getQueryToDefTable(String query) {

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

    public void createNewTest(String newTestName) {

        String sqlInsertUser = "INSERT INTO " + username + "_tests (test_name, create_date ) values ( ?, DateTime('now') );";
        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(sqlInsertUser);) {

            stmt.setString(1, newTestName);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("xyz.DbConnect.JavaConnect.createUser()" + e.getMessage());
        }

        String sqlUserTestContentTable = "CREATE TABLE " + username + "_" + newTestName + " (primal_word CHAR NOT NULL, derivate_word CHAR NOT NULL, "
                + "PRIMARY KEY (primal_word, derivate_word));";
        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(sqlUserTestContentTable)) {

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("xyz.DbConnect.JavaConnect.createUser()" + e.getMessage());
        }

        /*
        String sqlUserTestContentTableTmp = "CREATE TABLE " + username + "_" + newTestName + "_temporary_table AS SELECT* FROM " + username + "_" + newTestName + ";";
        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(sqlUserTestContentTableTmp)) {

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("xyz.DbConnect.JavaConnect.createUser()" + e.getMessage());
        }
         */
    }

    public void deleteTest(String testName) {
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

    public void deleteTestContent(String testName) {
        String deleteWordsFromTable = "DELETE FROM " + username + "_" + testName + ";";

        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(deleteWordsFromTable);) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("xyz.DbConnect.JavaConnect.deleteTestContent()" + e.getMessage());
            return;
        }
    }

    public void insertContentIntoTest(String testName, String primalWord, String derivateWord) throws Exception {
        String deleteWordsFromTable = "INSERT INTO " + username + "_" + testName + "(primal_word, derivate_word)"
                + "VALUES (?,?);";

        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(deleteWordsFromTable);) {
            stmt.setString(1, primalWord);
            stmt.setString(2, derivateWord);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("xyz.DbConnect.JavaConnect.InsertContentIntoTest()" + e.getMessage());
            return;
        }

        String sqlChgTestNameInTestList = "UPDATE " + username + "_tests SET chg_date=DateTime('now') WHERE test_name='" + testName + "';";
        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(sqlChgTestNameInTestList)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("update testname xyz.DbConnect.JavaConnect.chgSelectedTestName()" + e.getMessage());
            throw new Exception();
        }

    }

    public void chgSelectedTestName(String oldTestName, String newTestName) throws Exception {

        String sqlCheckIfTestNameInListExist = "SELECT test_name FROM " + username + "_tests WHERE test_name='" + oldTestName + "';";
        try (Connection conn = DbConncect.getDbConnInstance();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlCheckIfTestNameInListExist)) {

            if (!rs.isBeforeFirst()) {
                throw new SQLException();
            }

        } catch (SQLException e) {
            System.out.println(" sqlCheckIfContestTestTableExist xyz.DbConnect.JavaConnect.sqlCheckIfTestNameInListExist()" + e);
            throw new Exception();
        }

        String sqlCheckIfContestTestTableExist = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + username + "_" + oldTestName + "';";
        try (Connection conn = DbConncect.getDbConnInstance();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlCheckIfContestTestTableExist)) {

            if (!rs.isBeforeFirst()) {
                throw new SQLException();
            }

        } catch (SQLException e) {
            System.out.println(" sqlCheckIfContestTestTableExist xyz.DbConnect.JavaConnect.sqlCheckIfContestTestTableExist()" + e);
            throw new Exception();
        }

//        String sqlChgTestName = "ALTER TABLE " + username + "_" + oldTestName + " RENAME TO " + username + "_" + newTestName + ";";
//        try (Connection conn = DbConncect.getDbConnInstance();
//                Statement stmt = conn.prepareStatement(sqlChgTestName)) {
//
//            stmt.executeUpdate(sqlChgTestName);
//
//        } catch (SQLException e) {
//            System.out.println("xyz.DbConnect.JavaConnect.chgSelectedTestName()" + e.getMessage());
//            throw new Exception();
//        }
        String sqlCreateNewTestTable = "CREATE TABLE " + username + "_" + newTestName + "\n"
                + " (primal_word CHAR NOT NULL, \n"
                + "derivate_word CHAR NOT NULL, \n"
                + "PRIMARY KEY (primal_word, derivate_word));";

        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(sqlCreateNewTestTable)) {

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("create new table xyz.DbConnect.JavaConnect.chgSelectedTestName()" + e.getMessage());
            throw new Exception();
        }

        String sqlInsertDataToNewTestTable = "INSERT INTO " + username + "_" + newTestName + " SELECT * FROM "
                + username + "_" + newTestName + "; ";

        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(sqlInsertDataToNewTestTable)) {

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("insert into table xyz.DbConnect.JavaConnect.chgSelectedTestName()" + e.getMessage());
            throw new Exception();
        }

        String sqlChgTestNameInTestList = "UPDATE " + username + "_tests SET test_name='" + newTestName + "', chg_date=DateTime('now') WHERE test_name='" + oldTestName + "';";
        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(sqlChgTestNameInTestList)) {

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("update testname xyz.DbConnect.JavaConnect.chgSelectedTestName()" + e.getMessage());
            throw new Exception();
        }

        String dropOldTable = "DROP TABLE " + username + "_" + oldTestName + ";";

        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(dropOldTable)) {

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("drop testname xyz.DbConnect.JavaConnect.chgSelectedTestName()" + e.getMessage());
            throw new Exception();
        }

    }

    public void updateTestsScore(String testName, String score) throws Exception {

        String sqlChgTestNameInTestList = "UPDATE " + username + "_tests"
                + " SET last_completion=DateTime('now'),"
                + " mistakes_counter=" + score
                + " WHERE test_name='" + testName + "';";
        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(sqlChgTestNameInTestList)) {

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("update testname xyz.DbConnect.JavaConnect.chgSelectedTestName()" + e.getMessage());
            throw new Exception();
        }
    }

    public DefaultTableModel getIndicatedTestsContent(String testName) {
        String sql = "SELECT * FROM " + username + "_" + testName;
        return getQueryToDefTable(sql);
    }

    public DefaultTableModel getShortInfoAboutTests() {
        String sql = "SELECT test_name, create_date, last_completion, mistakes_counter FROM " + username + "_tests";
        return getQueryToDefTable(sql);
    }

    public DefaultTableModel getShortestInfoAboutTests() {
        String sql = "SELECT test_name, create_date, chg_date FROM " + username + "_tests";
        return getQueryToDefTable(sql);
    }

    public DefaultTableModel getFullInfoAboutTests() {
        String sql = "SELECT * FROM " + username + "_tests";
        return getQueryToDefTable(sql);
    }

}
