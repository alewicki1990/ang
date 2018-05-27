/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.website.lesson;

import java.io.Serializable;

/**
 *
 * @author alewicki1990
 */
public final class AnswerQuestion implements Serializable{

    private String primalWord;
    private String translatedWord;
    private int secLeft = 30;

    public AnswerQuestion(String primalWord, String translatedWord) {
        this.translatedWord = translatedWord;
        this.primalWord = primalWord;
    }

    public String getTranslatedWord() {
        return translatedWord;
    }

    public String getPrimalWord() {
        return primalWord;
    }

    public void setPrimalWord(String notTranslatedWord) {
        this.primalWord = notTranslatedWord;
    }

    public void setTranslatedWord(String translatedWord) {
        this.translatedWord = translatedWord;
    }

    public void resetSecLeft() {
        secLeft = 30;
    }
    
    public void secLeftMinusSecond(){
        secLeft--;
    }
    
    public String getSecLeft(){
        return Integer.toString(secLeft);
    }
}
