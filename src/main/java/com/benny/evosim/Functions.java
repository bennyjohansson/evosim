/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.benny.evosim;

import org.ejml.simple.SimpleMatrix;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;


/**
 *
 * @author bennyjohansson
 */
public class Functions {

    public static SimpleMatrix reluFunction(SimpleMatrix inputMatrix) {

        SimpleMatrix returnMatrix = new SimpleMatrix(inputMatrix.numRows(), inputMatrix.numCols());

        for (int i = 0; i < inputMatrix.numRows(); i++) {
            for (int j = 0; j < inputMatrix.numCols(); j++) {
                //System.out.println(i + ", " + j);
                returnMatrix.set(i, j, Math.max(0, inputMatrix.get(i, j)));
            }
        }
        // System.out.println(returnMatrix);
        return returnMatrix;

    }
    
     public static SimpleMatrix sigmoidFunction(SimpleMatrix inputMatrix) {

        SimpleMatrix returnMatrix = new SimpleMatrix(inputMatrix.numRows(), inputMatrix.numCols());

        for (int i = 0; i < inputMatrix.numRows(); i++) {
            for (int j = 0; j < inputMatrix.numCols(); j++) {
                //System.out.println(i + ", " + j);
                returnMatrix.set(i, j, 1 / (1 + Math.exp(-inputMatrix.get(i, j))));
            }
        }

        return returnMatrix;

    }


    public static int findVectorIndexMax(SimpleMatrix myVector) {
        int index = 0;
        double tmpValue = 0;
        double maxValue = 0;
        
        for (int i = 0; i < myVector.numRows(); i++) {
                tmpValue = myVector.get(i, 0);
                if(tmpValue > maxValue) {
                    index = i;
                    maxValue = tmpValue;
                }
            }
        
        return index;

    }

    /*
     * Reading a matrix from a file
     * The file should be a csv file with the matrix values separated by commas and new rows separated by new lines
     */
    public static SimpleMatrix readMatrixFromFile(String fileName) {
        List<double[]> rowsList = new ArrayList<>();
    
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(",");
                double[] row = Arrays.stream(line).mapToDouble(Double::parseDouble).toArray();
                rowsList.add(row);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        if (rowsList.isEmpty()) {
            return new SimpleMatrix(1, 1);
        }
    
        double[][] matrixData = new double[rowsList.size()][];
        for (int i = 0; i < rowsList.size(); i++) {
            matrixData[i] = rowsList.get(i);
        }
    
        return new SimpleMatrix(matrixData);
    }

    /*
     * Saving a string to a file
     */
    public static void saveStringToFile(String fileName, String myString) {
        try {
            java.io.FileWriter myWriter = new java.io.FileWriter(fileName);
            myWriter.write(myString);
            myWriter.close();
        } catch (java.io.IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
