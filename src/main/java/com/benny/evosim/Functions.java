/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.benny.evosim;

import org.ejml.simple.SimpleMatrix;


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

}
