/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.testControl;

/**
 *
 * @author alewicki
 */
public class TestEntity {
    
    public TestEntity(String user, String testName) {
        this.user = user;
        this.testName = testName;
    }

    public TestEntity(String user, String testName, String createDate, String chgDate) {
        this.user = user;
        this.testName = testName;
        this.createDate = createDate;
        this.chgDate = chgDate;
    }

    private String user = null;
    private String testName = null;
    private String createDate = null;
    private String chgDate = null;    

    public String getUser() {
        return user;
    }

    public String getTestName() {
        return testName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getChgDate() {
        return chgDate;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setChgDate(String chgDate) {
        this.chgDate = chgDate;
    }
    
    
}
