/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.benny.evosim;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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
        
        /*
        *Setting initiatl parameters
        */
        int[] worldSize = {10, 10};
        int numberOfCreatures = 10;
        int numberOfFoodSpots = 30;
        int foodAmount = 3;
        int creatureVision = 2;
        
        /* Create new World */
        CreatureWorld theWorld = new CreatureWorld(worldSize);

        /* Adding creatures to the world */
        theWorld.addRandomCreatures(numberOfCreatures);
        
        /*Adding food to the world */
        theWorld.addRandomFood(numberOfFoodSpots, foodAmount);
        
        /* Getting random creature to test with */
        DigitalCreature tmpCreature = theWorld.getCreatureFromList(1);
        
        /* Checking surrounding - just for test */
        tmpCreature.scanSurrounding(creatureVision);
        
        ArrayList<String> tmpList = new ArrayList<>();
        
        tmpList.add("a");
        tmpList.add("b");
        tmpList.add("c");
        tmpList.add("d");
        tmpList.add("e");
        
        //Iterator<String> theIterator = tmpList.iterator();
        String myString = "";
        
        
        tmpList.remove("c");
        
        for (Iterator<String> theIterator = tmpList.iterator();theIterator.hasNext(); ) {
            myString= theIterator.next();
            System.out.println(myString);
        }
        
        /* Initiating eat-and-move process */
        theWorld.printWorld();
        System.out.println("Creatures before: " + theWorld.getNumberOfCreatures());
        for (int i = 0; i<50; i++) {
            
            theWorld.doEatCycle();
            theWorld.doRandomMoveCycle();
            theWorld.doFightCycle();
            
            //tmpCreature.move("random");
            //tmpCreature.eat();
            //System.out.println("Creature energy " + tmpCreature.getEnergy());
            //theWorld.printWorld();
        }
        
        System.out.println();
        System.out.println("Eat and move cycle complete");
        System.out.println("Creatures after: " + theWorld.getNumberOfCreatures());
        System.out.println();
        
        theWorld.printWorld();

    }
}