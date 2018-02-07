/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.testControl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author alewicki
 */

public class TestEntity {

    private ArrayList<PairOfWords> testList;
    private int mistakes = 0;
    private int numberOfPairsToEnd = 0;

    public TestEntity(ArrayList<PairOfWords> testList) {
        this.testList = testList;
        numberOfPairsToEnd = countPairsOfWords();
    }

    public PairOfWords getOnePair() {
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
        if (testList.get(0).getTranslatedWord().equals(typedWord)) {
            return true;
        } else {
            return false;
        }
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

}
