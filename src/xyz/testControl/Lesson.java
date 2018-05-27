/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.testControl;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author alewicki1990
 */

public final class Lesson implements Serializable{

    private final ArrayList<AnswerQuestion> testList;
    private int mistakes = 0;
    private int numberOfPairsToEnd = 0;
    private final DateTimeFormatter formatter;
    private LocalTime spentTime = LocalTime.parse("00:00:00");

    public Lesson(ArrayList<AnswerQuestion> testList) {
        this.formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.testList = testList;
        numberOfPairsToEnd = countPairsOfWords();
    }

    public AnswerQuestion getOnePair() {
        return testList.get(0);
    }

    public void removeOnePairOfWords() {
        testList.remove(0);
        numberOfPairsToEnd--;
    }

    public void shuffleTestContent() {
        Collections.shuffle(testList);
    }

    public int countPairsOfWords() {
        return testList.size();
    }

    public boolean checkCorrectness(String typedWord) {
        return testList.get(0).getTranslatedWord().equals(typedWord);
    }

    public void addPointToMistakes() {
        mistakes++;
    }

    public int getNumberOfMistakes() {
        return mistakes;
    }

    public int getNumberOfPairsToEnd() {
        return numberOfPairsToEnd;
    }
    
    public void addSecToSpentTime(){
        spentTime = spentTime.plusSeconds(1);
    }
    
    public String getSpentTime(){
        return spentTime.format(formatter);
    }
    
    public boolean checkUsersAnswer(String answer){
        return testList.get(0).getTranslatedWord().equals(answer);
    }
    
    public String getCurrentQuestion(){
        return testList.get(0).getPrimalWord();
    }
    
    public String getCurrentAnswer(){
        return testList.get(0).getTranslatedWord();
    }    
    
    public void resetCurrentSecLeft() {
        testList.get(0).resetSecLeft();
    }
    
    public void currentSecLeftMinusSecond(){
        testList.get(0).secLeftMinusSecond();
    }
    
    public String getCurrentSecLeft(){
        return testList.get(0).getSecLeft();
    }
    
}
