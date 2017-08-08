/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.testControl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alewicki
 */
public class getListFromFile {

    public static ArrayList<TestWordEntity> getListTestEntityFromTextFile(String filePath, String username, String testname) {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader bReader = null;
        ArrayList<TestWordEntity> listResult = new ArrayList<>();
        String line = null;
        String delimiter = " - ";
        String[] strTestEntity = null;

        try {
            fis = new FileInputStream(filePath);

            while (true) {
                line = bReader.readLine();
                if (line == null) {
                    break;
                } else {
                    strTestEntity = line.split(delimiter);
                    listResult.add(new TestWordEntity(username, testname, strTestEntity[0], strTestEntity[1]));

                }
            }
        } catch (Exception e) {
            System.out.println("xyz.testControl.getListFromFile.getListTestEntityFromTextFile()" + e.getStackTrace());
        } finally {
            try {
                bReader.close();
            } catch (IOException ex) {
                System.out.println("xyz.testControl.getListFromFile.getListTestEntityFromTextFile()" + ex.getStackTrace());
            }

        }
        return listResult;

    }

}
