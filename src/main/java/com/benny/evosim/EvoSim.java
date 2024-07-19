/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.benny.evosim;


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
//        SimpleMatrix A = SimpleMatrix.random_DDRM(3, 3, -1, 1, rand);
//        SimpleMatrix B = SimpleMatrix.random_DDRM(3, 3, -1, 1, rand);
//        SimpleMatrix C = new SimpleMatrix(3,3);
//        
//        C = A.plus(B).scale(0.5);
//        
//        A.print();
//        B.print();
//        C.print();
//

        /*
        *Setting initiatl parameters
        */
        int[] worldSize = {60, 60};
        int numberOfCreatures = 500;
        int numberOfFoodSpots = worldSize[0] * worldSize[1] / 30;
        int numberOfNewFoodSpots = worldSize[0] * worldSize[1] / 30;
        int foodAmount = 3;
        
        
        /* Create new World */
        CreatureWorld theWorld = new CreatureWorld(worldSize);

        /* Adding creatures to the world */
        theWorld.addRandomCreatures(numberOfCreatures);
        theWorld.addCreaturesFromFile(numberOfCreatures);

        /*Adding food to the world */
        theWorld.addRandomFood(numberOfFoodSpots, foodAmount);
        
        /* Getting random creature to test with */
        DigitalCreature tmpCreature = theWorld.getCreatureFromList(1);
        
        /* Checking surrounding - just for test */
        tmpCreature.scanSurrounding();
        
        
        /* Initiating eat-and-move process */
        theWorld.printWorld();
        System.out.println("Creatures before: " + theWorld.getNumberOfCreatures());
        for (int i = 0; i<50000; i++) {
            
            System.out.println("-------------------------------");
            System.out.println("YEAR " + i);
            System.out.println("-------------------------------");
            
            theWorld.doUpdateCycle();
            theWorld.doRandomMoveCycle();
            theWorld.doActionCycle();
            
            
            //If the colony is about to go extinct, clone the remaining creatures and add a few random
            if(theWorld.getNumberOfCreatures() <= 30) {
                System.out.println("Initiating rescue cycle");
                // theWorld.doCloneCycle();
                theWorld.addRandomCreatures(numberOfCreatures);
                theWorld.addCreaturesFromFile(numberOfCreatures);
            } else {
                // theWorld.addCloneCount(0);
            }
            
            // if(theWorld.getNumberOfCreatures() <= 100) {
            //     theWorld.addRandomCreatures(numberOfCreatures);
            // }

            // if(i>20) {
            //     foodAmount =5;
            // }
            theWorld.addRandomFood(numberOfFoodSpots, foodAmount);
            theWorld.addFoodInLeftUpperCorner(20);
            theWorld.printWorld();


            //Printing oldest generation and oldest generation alive
            System.out.println("Oldest generation alive: " + theWorld.stats.getOldestGenerationAlive() + ", " + theWorld.stats.getOldestGenerationAliveType());
            System.out.println("Oldest generation ever: " + theWorld.stats.getOldestGenerationEver() + ", " + theWorld.stats.getOldestGenerationEverType());
            
        }
        
        // System.out.println();
        // System.out.println("Eat and move cycle complete");
        // System.out.println("Creatures after: " + theWorld.getNumberOfCreatures());
        // System.out.println();
        
        theWorld.printWorld();
        
        theWorld.printStats();
        
        //saving the action vectors to a file
        theWorld.stats.saveActionVectorsToFile("actionVectors.csv");
      

    }
}