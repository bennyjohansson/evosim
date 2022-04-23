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
        int[] worldSize = {20, 20};
        int numberOfCreatures = 100;
        int numberOfFoodSpots = 30;
        int foodAmount = 3;
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
        tmpCreature.scanSurrounding(creatureVision);
        
        
        /* Initiating eat-and-move process */
        theWorld.printWorld();
        System.out.println("Creatures before: " + theWorld.getNumberOfCreatures());
        for (int i = 0; i<50; i++) {
            
            theWorld.doEatCycle();
            theWorld.doRandomMoveCycle();
            theWorld.doFightCycle();
            
        }
        
        System.out.println();
        System.out.println("Eat and move cycle complete");
        System.out.println("Creatures after: " + theWorld.getNumberOfCreatures());
        System.out.println();
        
        theWorld.printWorld();
        
      

    }
}