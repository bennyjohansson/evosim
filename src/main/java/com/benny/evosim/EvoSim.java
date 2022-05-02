/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.benny.evosim;

import java.util.*;
//import java.util.concurrent.ThreadLocalRandom;
import org.ejml.simple.SimpleMatrix;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.RandomMatrices_DDRM;


/**
 *
 * @author bennyjohansson
 */
public class EvoSim {

    public static void main(String[] args) {

        EvoSim evoSimInstance = new EvoSim();
        evoSimInstance.start();
    }

    public void start() {
        
//        Random rand = new Random();
//        SimpleMatrix A = new SimpleMatrix(3,3);
//        DMatrixRMaj​ D  = new DMatrixRMaj(3,3) ; 
//        
//        DMatrixRMaj d2 = new RandomMatrices_DDRM().createSymmetric(20,-2,3,rand);
//        SimpleMatrix S2 = new SimpleMatrix(20,20).random64(20,20,-2,3,rand);
//        
//        A.print();
//       


        /*
        *Setting initiatl parameters
        */
        int[] worldSize = {30, 30};
        int numberOfCreatures = 300;
        int numberOfFoodSpots = 30;
        int numberOfNewFoodSpots = 20;
        int foodAmount = 20;
        int creatureVision = 2;
        
        
        /* Create new World */
        CreatureWorld theWorld = new CreatureWorld(worldSize);
        System.out.println("New world created with size " + worldSize[0] + "x" + worldSize[1]);

        /* Adding creatures to the world */
        theWorld.addRandomCreatures(numberOfCreatures);
        
        /*Adding food to the world */
        theWorld.addRandomFood(numberOfFoodSpots, foodAmount);
        
        /* Getting random creature to test with */
        DigitalCreature tmpCreature = theWorld.getCreatureFromList(1);
        
        /* Checking surrounding - just for test */
        tmpCreature.scanSurrounding();
        
        
        /* Initiating eat-and-move process */
        theWorld.printWorld();
        System.out.println("Creatures before: " + theWorld.getNumberOfCreatures());
        for (int i = 0; i<1000; i++) {
            
            System.out.println("-------------------------------");
            System.out.println("YEAR " + i);
            System.out.println("-------------------------------");
            
            
            theWorld.doRandomMoveCycle();
            
            theWorld.doActionCycle();
            theWorld.doUpdateCycle();
            
            //If the colony is about to go extinct, clone the last 25 remaining creatures
            if(theWorld.getNumberOfCreatures() <= 25) {
                System.out.println("Initiating clone cycle");
                theWorld.doCloneCycle();
            } else {
                theWorld.addCloneCount(0);
            }
            
            if(i>500) {
                numberOfNewFoodSpots =10;
            }
            
            theWorld.addRandomFood(numberOfNewFoodSpots, foodAmount);
            //theWorld.printWorld();
            
        }
        
        System.out.println();
        System.out.println("Eat and move cycle complete");
        System.out.println("Creatures after: " + theWorld.getNumberOfCreatures());
        System.out.println();
        
        theWorld.printWorld();
        
        theWorld.printStats();
        
      

    }
}