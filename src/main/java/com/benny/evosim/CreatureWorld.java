/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.benny.evosim;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.CopyOnWriteArrayList;
        /**
         *
         * @author bennyjohansson
         */

public class CreatureWorld {

    /*
        *Constructors
     */
    public CreatureWorld() {
    }

    public CreatureWorld(int[] theWorldSize) {
        
        int sizeX = theWorldSize[0];
        int sizeY = theWorldSize[1];
        
        worldSize[0] = sizeX;
        worldSize[1] = sizeY;

        //ArrayList<ArrayList<CreatureContainer>> theGrid = new ArrayList<>();
        for (int i = 0; i < sizeX; i++) {
            theGrid.add(new ArrayList());
        }

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                theGrid.get(i).add(new GridContainer());
            }
        }
    }

    /*
        * Parameters
     */
    int[] worldSize = {0, 0};
    ArrayList<ArrayList<GridContainer>> theGrid = new ArrayList<>();
    List<DigitalCreature> creatureList = new CopyOnWriteArrayList<DigitalCreature>();
    StatisticsModule stats = new StatisticsModule();

    /*
        * Functions
     */
    public int[] getSize() {

        return worldSize;
    }
    
    public int getNumberOfCreatures() {
        return creatureList.size();
    }
    
    public StatisticsModule getStatsModule() {
        
        return stats;
    }
    
    public void addRandomCreatures(int numberOfCreatures) {
        
        //Finding next available Id
        int myId = 0;
        
        
        for (int i = 0; i < numberOfCreatures; i++) {

            int[] myPosition = getRandomLocation();
            int setStrength = ThreadLocalRandom.current().nextInt(0, 10);
            int setEnergy = 40;
            int setVision = 2;
            myId = stats.getCreatureIdCount() + 1;

            if (addCreature(new DigitalCreature(myId, "killer", setEnergy, setStrength,setVision, this), myPosition)) {
                System.out.println("Creature " + myId + " added at position (" + myPosition[0] + "," + myPosition[1] + ")");
                //stats.setCreatureIdCount(myId);
            }
        }

    }

    public void addRandomFood(int numberOfFoodSpots, int foodAmount) {
        for (int i = 0; i < numberOfFoodSpots; i++) {

            int[] myPosition = getRandomLocation();

            if (addFoodReserve(myPosition, foodAmount)) {
                //System.out.println("Food added at position (" + myPosition[0] + "," + myPosition[1] + ")");
            }
        }

    }
    
    public boolean addFoodReserve(int[] myPosition, int foodAmount) {

        GridContainer tmpGridContainer = theGrid.get(myPosition[0]).get(myPosition[1]);

        if ((tmpGridContainer.isEmpty())) {
            tmpGridContainer.addFoodReserve(foodAmount);
            return true;
        }
        else {
            return false;
        }
    }
    
    public void doEatCycle() {
        
        
        Iterator<DigitalCreature>  iter= creatureList.iterator();

        while (iter.hasNext()) {
            iter.next().eat();
        }
        
    }
    
    public void doFightCycle() {

        Iterator<DigitalCreature> iter = creatureList.iterator();
        DigitalCreature theKilledCreature;
   

        while (iter.hasNext()) {

            theKilledCreature = iter.next().fight();
            if (!(theKilledCreature == null)) {
                removeCreature(theKilledCreature);
            }

        }

    }
    
    public void doActionCycle() {

        Iterator<DigitalCreature> iter = creatureList.iterator();
        DigitalCreature theKilledCreature;
        DigitalCreature theActionCreature;
        String action = "";
        int fightCounter = 0;
        int reproduceCounter = 0;
        int eatCounter = 0;
        int doNothingCounter = 0;
        int killedCount = 0;
        int birthCount = 0;
        

        while (iter.hasNext()) {

            theActionCreature = iter.next();
            action = theActionCreature.getAction();

            switch (action) {
                case ("Eat"): {
                    theActionCreature.eat();
                    eatCounter++;
                    break;
                }
                case ("Fight"): {

                    theKilledCreature = theActionCreature.fight();
                    if (!(theKilledCreature == null)) {
                        removeCreature(theKilledCreature);
                        killedCount++;
                    }
                    fightCounter++;
                    break;
                }
                case ("Reproduce"): {
                    if(theActionCreature.reproduce()){
                        birthCount++;
                    }
                    reproduceCounter++;
                    break;
                }
                case ("doNothing"): {
                    //Doing notheing
                    doNothingCounter++;
                    break;
                }

            }


        }
        
        stats.addToEatCount(eatCounter);
        stats.addToReproduceCount(reproduceCounter);
        stats.addToFightCount(fightCounter);
        stats.addToDoNothingCount(doNothingCounter);
        stats.addToKilledCount(killedCount);
        stats.addToBirthCount(birthCount);

    }

    
    public void doRandomMoveCycle() {
        
        
        Iterator<DigitalCreature>  iter= creatureList.iterator();

        while (iter.hasNext()) {
            //iter.next().move("random");
            iter.next().moveNN();
        }
        
    }
    
    public void addCloneCount(int count) {
         stats.addToCloneCount(count);
    }
    
    public void doCloneCycle() {
        
        int cloneCount = 0;
        Iterator<DigitalCreature>  iter= creatureList.iterator();

        while (iter.hasNext()) {
            iter.next().createClone();
            cloneCount++;
        }
        stats.addToCloneCount(cloneCount);
        
    }
    
    public void doUpdateCycle() {

        Iterator<DigitalCreature> iter = creatureList.iterator();
        DigitalCreature theKilledCreature;
        int totalDied = 0;
   

        while (iter.hasNext()) {

            theKilledCreature = iter.next().update();
            if (!(theKilledCreature == null)) {
                removeCreature(theKilledCreature);
                totalDied++;
            }
            
        }
        stats.addToDiedCount(totalDied);

    }

    public boolean addCreature(DigitalCreature myCreature, int[] myPosition) {

        GridContainer tmpContainer = new GridContainer();

        if (positionExists(myPosition)) {
            tmpContainer = theGrid.get(myPosition[0]).get(myPosition[1]);

            if (tmpContainer.isEmpty()) {
                tmpContainer.addCreature(myCreature);
                creatureList.add(myCreature);
                myCreature.setPosition(myPosition);
                stats.setCreatureIdCount(myCreature.getId());
                return true;
            } else {
                //System.out.println("Unable to add creature " + myCreature.getId() + " - position taken");
                return false;
            }
        } else {
            System.out.println("Unable to add creature " + myCreature.getId() + " - position does not exist");
            return false;
        }
    }
    
    public boolean positionExists(int[] myPosition) {
        boolean posExists = false;
        
        //Checking bounderies
        if(myPosition[0] >= 0 && (myPosition[0] <= worldSize[0] - 1) && myPosition[1] >= 0 && myPosition[1] <= worldSize[1] - 1) {
            posExists = true;
        }
        
        return posExists;
    }
    
    public int[] getRandomLocation() {
        int posX = ThreadLocalRandom.current().nextInt(0, worldSize[0]-1);
        int posY = ThreadLocalRandom.current().nextInt(0, worldSize[1]-1);
        int[] myPos = {posX, posY};
        return myPos;
    }
    
    public boolean positionHasCreature(int[] myPosition) {
        if(positionExists(myPosition)) {
            if(theGrid.get(myPosition[0]).get(myPosition[1]).hasCreature()) {
                return true;
            } else {
            return false;
        }
        } else {
            return false;
        }
    }
    
    public int getFoodReserve(int[] myPosition) {
        
        if (positionExists(myPosition)) {
            int theFood = theGrid.get(myPosition[0]).get(myPosition[1]).getFoodReserve();
            return theFood;
        } else{
            return 0;
        }
        
    }
    
    public DigitalCreature getCreatureFromList(int listIndex) {
        if(listIndex < creatureList.size()) {
            return creatureList.get(listIndex);
        }
        else {
            System.out.println("Index larger than list size");
            return null;
        }
    }
    
   
    
    /* Returning the creature is exists, otherwise null*/
    public DigitalCreature getCreatureFromGrid(int[] myPosition) {
        
        DigitalCreature theCreature = null;
        if(positionExists(myPosition)) {
            theCreature = theGrid.get(myPosition[0]).get(myPosition[1]).getCreature();
            return theCreature;
        } else {
            //System.out.println("Position out fo bounds " + myPosition[0] + ", " + myPosition[1]);
            return null;
        }
        
    }
    
    public void removeCreature(DigitalCreature theCreature) {
        int[] tmpPosition = {0,0};
        tmpPosition[0] = theCreature.getPosition()[0];
        tmpPosition[1] = theCreature.getPosition()[1];
        removeCreatureFromGrid(tmpPosition);
        //System.out.println("Removed from Grid");
        removeCreatureFromList(theCreature);
        //System.out.println("Removed from List");
        
    }
    
    public boolean removeCreatureFromGrid(int[] myLocation) {
        GridContainer tmpContainer = theGrid.get(myLocation[0]).get(myLocation[1]);

        if (tmpContainer.isEmpty()) {
            //System.out.println("No creature here");
            
            return false;
        } else {
            tmpContainer.removeCreature();
            //System.out.println("creature removed from grid");
            return true;
        }
    }
    

    public void removeCreatureFromList(DigitalCreature myCreature) {

        creatureList.remove(myCreature);
        /*
        for (Iterator<DigitalCreature> iterator = creatureList.iterator(); iterator.hasNext(); ) {
            DigitalCreature creature = iterator.next();
            if (creature == myCreature) {
                // Remove the current element from the iterator and the list.
                iterator.remove();
            }
        }
        */
    }
    
    public boolean removeFoodReserve(int[] myLocation) {
        GridContainer tmpContainer = theGrid.get(myLocation[0]).get(myLocation[1]);

        if (tmpContainer.getFoodReserve() > 0) {
            tmpContainer.removeFoodReserve();
            
            return true;
        } else {
            System.out.println("No food here");
            return false;
        }
    }
    
    public void chooseMovement(){
        
        
    }
    
    public boolean moveCreature(DigitalCreature theCreature, int[] newPosition) {
        
        int[] myPosition = theCreature.getPosition();
        //System.out.println("Creature " + theCreature.getId() + " moved from (" + myPosition[0] + " , " + myPosition[1] + ") to (" + newPosition[0] + " , " + newPosition[1] + ")");
//        System.out.println("Creature " + theCreature.getId() + " moved from (" + myPosition[0] + " , "+ myPosition[1]);
//        System.out.println("Creature " + theCreature.getId() + " moving to (" + newPosition[0] + " , "+ newPosition[1]);
        GridContainer oldContainer = theGrid.get(myPosition[0]).get(myPosition[1]);
        GridContainer newContainer = theGrid.get(newPosition[0]).get(newPosition[1]);
        

        if (newContainer.isEmpty()) {
            newContainer.addCreature(theCreature);
            oldContainer.removeCreature();
            theCreature.setPosition(newPosition);
            //System.out.println("Creature " + theCreature.getId() + " moved from (" + myPosition[0] + " , " + myPosition[1] + ") to (" + newPosition[0] + " , " + newPosition[1] + ")");
            return true;
        } else {
            //System.out.println("Unable to move here - position taken");
            return false;
        }
    }

    public void printWorld() {

        worldSize = this.getSize();
        String gridContent = "0";
        for (int j = 0; j < worldSize[1]; j++) {
            for (int i = 0; i < worldSize[0]; i++) {
                GridContainer tmpContainer = theGrid.get(i).get(j);
                if (tmpContainer.isEmpty()) {
                    gridContent = "0";
                } else {
                    gridContent = "c";
                }
                if (tmpContainer.getFoodReserve() > 0) {
                    gridContent = "f";
                }
                

                System.out.print(gridContent);
                if (i != worldSize[0] - 1) {
                    System.out.print(",");
                }

            }
            System.out.println("");
        }
    }
    
    public void printStats() {
        stats.printStats();
    }
}
