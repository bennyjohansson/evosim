/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.benny.evosim;

import java.util.concurrent.ThreadLocalRandom;
//import java.math.*;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author bennyjohansson
 */
public class DigitalCreature {

    int id;
    String type;
    int energy;
    int strength;
    int[] position;
    CreatureWorld world;

    public DigitalCreature() {
    }

    public DigitalCreature(int setId, String setType, int setEnergy, int setStrength, CreatureWorld myWorld) {
        id = setId;
        type = setType;
        world = myWorld;
        strength = setStrength;
        energy = setEnergy;
    }

    public int getId() {
        //System.out.println(id);
        return id;
    }

    public int[] getPosition() {
        //System.out.println(id);
        return position;
    }
    
     public int getEnergy() {
        //System.out.println(id);
        return energy;
    }
     
     public int getStrength() {
        //System.out.println(id);
        return strength;
    }

    public void setPosition(int[] myPosition) {

        position = myPosition;

    }

    /*
    * Action functions
    */
    
    
    //Eats all the food within 1 step
    public int eat() {
        int totalAmount = 0;
        int positionAmount = 0;
        int[] tmpPosition = {0,0};
        
        
        
        //System.out.println("Living at " + position[0] + ", " + position[1]);
        for (int j = -1; j < 2; j++) {
            tmpPosition[1] = position[1]+j;
            for (int i = -1; i < 2; i++) {
                
                tmpPosition[0] = position[0]+i;
                positionAmount = world.getFoodReserve(tmpPosition);
                if(positionAmount > 0) {
                    world.removeFoodReserve(tmpPosition);
                }
                //System.out.println("Eating " + positionAmount + " at " + tmpPosition[0] + ", " + tmpPosition[1]);
               
               totalAmount += positionAmount;
            }
        }
        
        energy += totalAmount;
        
        return totalAmount;
    }
    
    //Tries to kill nearest neighbour
    public DigitalCreature fight() { //DigitalCreature
        
        int[] tmpPosition = {0,0};
        DigitalCreature tmpCreature=null;
        
        
        System.out.println("Fighting from " + position[0] + ", " + position[1]);
        for (int j = -1; j < 2; j++) {
            tmpPosition[1] = position[1]+j;
            for (int i = -1; i < 2; i++) {

                tmpPosition[0] = position[0]+i;
                
                //Finding opponent
                tmpCreature = world.getCreatureFromGrid(tmpPosition);
                
                //If opponent available, fight and exit
                if(!(tmpCreature == null)) {
                    
                    //Attacker stronger and wins
                    if(strength > tmpCreature.getStrength()) {
                        
                        System.out.println("Creature killed by: " + tmpPosition[0] + "," + tmpPosition[1]);
                        /*
                        world.removeCreatureFromGrid(tmpPosition);
                        world.removeCreatureFromList(tmpCreature);*/
                        return tmpCreature;
                        //return true;
                    }
                    
                    //Opponent stronger and wins
                    if(strength < tmpCreature.getStrength()) {
                        
                        System.out.println("Attacker got killed at: " + position[0] + "," + position[1]);
                        return this;
                        //return true;
                    } else {
                        return null;
                    }
                    
                } else {
                    //System.out.println("No creature here");
                }
                //System.out.println("Eating " + positionAmount + " at " + tmpPosition[0] + ", " + tmpPosition[1]);
               
               
            }
        }
        
        return null;
    }
    
    
    
    public boolean move(String myDirection) {

        int[] worldSize = world.getSize();
        //System.out.println("World size. " + worldSize[0] + ", " + worldSize[1]);

        int[] tmpPosition = {0, 0};
        tmpPosition[0] = position[0];
        tmpPosition[1] = position[1];

        if (myDirection == "N") {
            tmpPosition[1] += 1;
        } else if (myDirection == "S") {
            tmpPosition[1] -= 1;
        } else if (myDirection == "E") {
            tmpPosition[0] += 1;
        } else if (myDirection == "W") {
            tmpPosition[0] -= 1;
        } else if (myDirection == "NE") {
            tmpPosition[1] += 1;
            tmpPosition[0] += 1;
        } else if (myDirection == "NW") {
            tmpPosition[1] += 1;
            tmpPosition[0] -= 1;
        } else if (myDirection == "SE") {
            tmpPosition[1] -= 1;
            tmpPosition[0] += 1;
        } else if (myDirection == "SW") {
            tmpPosition[1] -= 1;
            tmpPosition[1] -= 1;
        } else if (myDirection == "random") {
            int moveX = ThreadLocalRandom.current().nextInt(-1, 2);
            int moveY = ThreadLocalRandom.current().nextInt(-1, 2);
            //System.out.println("Moving (" + moveX + ", " + moveY + ") from (" + position[0] + ", " + position[1] + ")");
            tmpPosition[0] += moveX;
            tmpPosition[1] += moveY;
        } else {
            System.out.println("Invalid movement direction");
        }

        /*
        * Checking grid boundaries
         */
        if (tmpPosition[0] >= worldSize[0]) {
            tmpPosition[0] = worldSize[0] - 1;
            //System.out.println("Trying to move outside x-range > max");
        }
        if (tmpPosition[0] < 0) {
            tmpPosition[0] = 0;
            //System.out.println("Trying to move outside x-range < 0");
        }

        if (tmpPosition[1] >= worldSize[1]) {
            tmpPosition[1] = worldSize[1] - 1;
            //System.out.println("Trying to move outside y-range < max");
        }
        if (tmpPosition[1] < 0) {
            tmpPosition[1] = 0;
            //System.out.println("Trying to move outside y-range < 0");
        }

        /*
        * Checking if random move generated move at all and if new location available
         */
        if (!(tmpPosition[0] == position[0] && tmpPosition[1] == position[1])) {
            if (world.moveCreature(this, tmpPosition)) {
                return true;
            } else {
                //System.out.println("No move");
                return false;
            }
        } else {
            //System.out.println("random move = 0");
            return false;
        }

    }
    /*
    * Looks "vision" steps in either direction and returns a column vector with the contents of the cells
    */
    public SimpleMatrix scanSurrounding(int vision) {
        /*
        *returns an array {int distCreature, str dirCreature, int distFoor, str distFood}
         */

        //Object[] ret = new Object[4];

        /*
        *Scanning area of "vision" around the creature for other creatures and food. 
         */
        vision = 2;
        SimpleMatrix surrounding = new SimpleMatrix((2 * vision + 1) * (2 * vision + 1), 1);
        int matrixCounter = 0;

   
        int[] scanPosition = {0, 0};

        //Checking world boundaries
        int startX = position[0] - vision; //Math.max(position[0] - vision, 0);
        int stopX = position[0] + vision; //Math.min(position[0] + vision, world.getSize()[0] - 1);

        int startY = position[1] - vision; //Math.max(position[1] - vision, 0);
        int stopY = position[1] + vision; //Math.min(position[1] + vision, world.getSize()[1] - 1);

        /*Scanning surrounding for food and creatures */
        System.out.println("Creature at position " + position[0] + "," + position[1]);
        for (int j = startY; j <= stopY; j++) {
            for (int i = startX; i <= stopX; i++) {

                scanPosition[0] = i;
                scanPosition[1] = j;

                if (!(world.positionExists(scanPosition))) {
                    //Checking that we are inside the world
                    surrounding.set(matrixCounter, 3);
                    System.out.println("Outside range at position " + scanPosition[0] + "," + scanPosition[1]);

                } else if (world.positionHasCreature(scanPosition) && (!(scanPosition[0] == position[0] && scanPosition[1] == position[1]))) {
                    //Checking for creatures
                    System.out.println("Creature found at position " + scanPosition[0] + "," + scanPosition[1]);
                    surrounding.set(matrixCounter, 1);

                } else if (world.getFoodReserve(scanPosition) > 0) {
                    //Checking for food
                    System.out.println("Food found at position " + scanPosition[0] + "," + scanPosition[1]);
                    surrounding.set(matrixCounter, 2);
                }
                matrixCounter++;
            }

        }
        //surrounding.print();
        return surrounding;
    }

    public String getType() {
        //System.out.println(type);
        return type;
    }
}
