/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.website.dboperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import pl.website.lesson.AnswerQuestion;

/**
 *
 * @author alewicki1990
 */
public class DbInstructions {

    String userId;

    public DbInstructions(String username) {
        this.userId = getUserId(username);
    }

    public ArrayList<AnswerQuestion> getUsersTestListArrayList(String testName) {
        ArrayList<AnswerQuestion> recordData = new ArrayList<>();
        AnswerQuestion record;
        String sqlTestContent = "SELECT  primal_word, derivate_word FROM tests_content WHERE test_id='" + getTestId(testName) + "';";

        try (Connection conn = DbConncect.getDbConnInstance();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlTestContent)) {
            while (rs.next()) {
                record = new AnswerQuestion(rs.getString("primal_word"), rs.getString("derivate_word"));
                recordData.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return model;
    }

    public void createNewTest(String newTestName) {

        String instruction = "INSERT INTO list_of_tests (test_name, user_id, create_date ) values ( ?, ?, DateTime('now') );";
        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(instruction);) {

            stmt.setString(1, newTestName);
            stmt.setString(2, userId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTest(String testName) {
        deleteTestContent(testName);

        String deleteTestFromTestList = "DELETE FROM list_of_tests WHERE test_id='" + getTestId(testName) + "' AND user_id='" + userId + "';";

        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(deleteTestFromTestList);) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

    }

    public void deleteTestContent(String testName) {
        String deleteWordsFromContentTable = "DELETE FROM tests_content " + " WHERE test_id='" + getTestId(testName) + "';";

        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(deleteWordsFromContentTable);) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }

    public void insertContentIntoTest(String testName, String primalWord, String derivateWord) throws SQLException {
        String chgTestChgDate = "UPDATE list_of_tests SET chg_date=DateTime('now') WHERE test_id='" + getTestId(testName) + "';";
        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(chgTestChgDate)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }

        String insertWordsToTable = "INSERT INTO tests_content (test_id, primal_word, derivate_word) "
                + "VALUES (?,?,?);";

        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(insertWordsToTable);) {

            stmt.setString(1, getTestId(testName));
            stmt.setString(2, primalWord);
            stmt.setString(3, derivateWord);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void chgSelectedTestName(String oldTestName, String newTestName) throws Exception {

        String instruction = "UPDATE list_of_tests SET test_name='" + newTestName + "', chg_date=DateTime('now') WHERE test_id='" + getTestId(oldTestName) + "';";
        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(instruction)) {

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception();
        }
    }

    public void updateTestsScore(String testName, String score) throws Exception {

        String instruction = "UPDATE list_of_tests"
                + " SET last_completion=DateTime('now'),"
                + " mistakes_counter=" + score
                + " WHERE test_id='" + getTestId(testName) + "' "
                + " AND user_id ='" + userId + "';";
        try (Connection conn = DbConncect.getDbConnInstance();
                PreparedStatement stmt = conn.prepareStatement(instruction)) {

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception();
        }
    }

    public DefaultTableModel getIndicatedTestsContent(String testName) {
        String instruction = "SELECT primal_word, derivate_word FROM tests_content WHERE test_id='" + getTestId(testName) + "';";
        return getQueryToDefTable(instruction);
    }

    public DefaultTableModel getShortInfoAboutTests() {
        String instruction = "SELECT test_name, create_date, last_completion, mistakes_counter FROM list_of_tests WHERE user_id='" + userId + "';";
        return getQueryToDefTable(instruction);
    }

    public DefaultTableModel getShortestInfoAboutTests() {
        String instruction = "SELECT test_name, create_date, chg_date FROM list_of_tests WHERE user_id='" + userId + "';";
        return getQueryToDefTable(instruction);
    }
/*
    public DefaultTableModel getFullInfoAboutTests() {
        String instruction = "SELECT * FROM list_of_tests WHERE user_id='" + userId + "';";
        return getQueryToDefTable(instruction);
    }
*/
    public String getUserId(String username) {
        String instruction = "SELECT user_id FROM users WHERE username='" + username + "';";
        try (Connection conn = DbConncect.getDbConnInstance();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(instruction)) {

            while (rs.next()) {
                return rs.getString("user_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getTestId(String testName) {
        String instruction = "SELECT test_id FROM list_of_tests WHERE user_id='" + userId + "' and test_name='" + testName + "';";
        try (Connection conn = DbConncect.getDbConnInstance();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(instruction)) {

            while (rs.next()) {
                return rs.getString("test_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}
