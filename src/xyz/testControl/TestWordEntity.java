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
class TestWordEntity {

    public TestWordEntity(String username, String testName, String primalWord, String translatedWord) {
        this.username = username;
        this.testName = testName;
        this.primalWord = primalWord;
        this.translatedWord = translatedWord;
    }

    public String getUsername() {
        return username;
    }

    public String getTestName() {
        return testName;
    }

    public String getPrimalWord() {
        return primalWord;
    }

    public String getTranslatedWord() {
        return translatedWord;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void setPrimalWord(String notTranslatedWord) {
        this.primalWord = notTranslatedWord;
    }

    public void setTranslatedWord(String translatedWord) {
        this.translatedWord = translatedWord;
    }

    private String username;
    private String testName;
    private String primalWord;
    private String translatedWord;
    //https://www.youtube.com/watch?v=BmxI9qK3tSo
}
