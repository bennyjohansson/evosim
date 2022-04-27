/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.benny.evosim;

import java.util.Random;
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
    int age;
    int visionLength;
    int outputSize;
    int[] position;
    CreatureWorld world;
    SimpleMatrix W1;
    SimpleMatrix W2;
    SimpleMatrix W3;

    public DigitalCreature() {
    }

    public DigitalCreature(int setId, String setType, int setEnergy, int setStrength, int setVision, CreatureWorld myWorld) {
        id = setId;
        type = setType;
        energy = setEnergy;
        strength = setStrength;
        age = 1;
        visionLength = setVision;
        outputSize = 4;

        world = myWorld;

        /* Initializing neural newwork, should perhaps be own class
        * Takes visionVector as input in the neural network and compress to   
        * 4 output actions (fight, eat, mate, nothing (?))
         */
        Random rand = new Random();
        int visionVectorSize = (2 * visionLength + 1) * (2 * visionLength + 1);

        int W2size = Math.min(10, visionVectorSize);

        W1 = SimpleMatrix.random_DDRM(W2size, visionVectorSize, -1, 1, rand);
        W2 = SimpleMatrix.random_DDRM(outputSize, W2size, -1, 1, rand);
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
    
    public int getVisionLength() {
        //System.out.println(id);
        return visionLength;
    }
    
    public SimpleMatrix getWeights(int myLayer) {
        
        
        if(myLayer == 1) {
            return W1;
        } else if(myLayer == 2) {
            return W2;
        } else {
            return null;
        }
        
    }

    public void setPosition(int[] myPosition) {

        position = myPosition;

    }
    
    public void setW1(SimpleMatrix Wx) {

        W1 = Wx;

    }
    
    public void setW2(SimpleMatrix Wx) {

        W2 = Wx;

    }

    /*
    * Action functions
     */
    public String  getAction() {

        double threshold = 0.5;
        int indexMax = 0;
        double maxValue = 0;
        String action = "";

        SimpleMatrix visionVector = scanSurrounding();
        SimpleMatrix W1output = new SimpleMatrix(W2.numCols(), 1);
        SimpleMatrix W2output = new SimpleMatrix(outputSize, 1);
        //SimpleMatrix W2outputRELU = new SimpleMatrix(outputSize,1);

        //Layer 1
        W1output = W1.mult(visionVector);
        W1output = Functions.sigmoidFunction(W1output);
        //Need to add + b

        //Layer 2
        W2output = W2.mult(W1output);
        W2output = Functions.sigmoidFunction(W2output);
        //Need to add + b

        W2output = Functions.sigmoidFunction(W2output);

        //W2output.print();
        indexMax = Functions.findVectorIndexMax(W2output);
        maxValue = W2output.get(indexMax, 0);

        //System.out.println(maxValue);
        if (!(maxValue >= threshold)) {
            //Noth high enough activation
            action = "doNothing";
        } else {

            switch (indexMax) {
                case (0): {
                    action = "Fight";
                    break;

                }
                case (1): {
                    action = "Eat";
                    break;

                }
                case (2): {
                    action = "Reproduce";
                    break;

                }
                case (3): {
                    action = "doNothing";
                    break;
                }
                default:
                //What now
            }

        }

        System.out.println("Action: " + action);

        return action;

    }

    //Eats all the food within 1 step
    public int eat() {
        int totalAmount = 0;
        int positionAmount = 0;
        int[] tmpPosition = {0, 0};

        //System.out.println("Living at " + position[0] + ", " + position[1]);
        for (int j = -1; j < 2; j++) {
            tmpPosition[1] = position[1] + j;
            for (int i = -1; i < 2; i++) {

                tmpPosition[0] = position[0] + i;
                positionAmount = world.getFoodReserve(tmpPosition);
                if (positionAmount > 0) {
                    world.removeFoodReserve(tmpPosition);
                }
                //System.out.println("Eating " + positionAmount + " at " + tmpPosition[0] + ", " + tmpPosition[1]);

                totalAmount += positionAmount;
            }
        }

        energy += totalAmount;

        return totalAmount;
    }

    //Tries to kill first found neighbour
    public DigitalCreature fight() {

        int[] tmpPosition = {0, 0};
        DigitalCreature tmpCreature = null;

        //System.out.println("Fighting from " + position[0] + ", " + position[1]);
        for (int j = -1; j < 2; j++) {
            tmpPosition[1] = position[1] + j;
            for (int i = -1; i < 2; i++) {

                tmpPosition[0] = position[0] + i;

                //Finding opponent
                tmpCreature = world.getCreatureFromGrid(tmpPosition);

                //If opponent available, fight and exit
                if (!(tmpCreature == null)) {

                    //Attacker stronger and wins
                    if (strength > tmpCreature.getStrength()) {

                        System.out.println("Creature killed by: " + tmpPosition[0] + "," + tmpPosition[1]);

                        return tmpCreature;
                    }

                    //Opponent stronger and wins
                    if (strength < tmpCreature.getStrength()) {

                        System.out.println("Attacker got killed at: " + position[0] + "," + position[1]);
                        return this;

                    } else {
                        return null;
                    }
                } else {
                    //System.out.println("No creature here");
                }

            }
        }

        return null;
    }
    
    //Tries to shag first found neighbour
    public boolean reproduce() {

        int[] tmpPosition = {0, 0};
        int[] newPosition = {0,0};
        DigitalCreature tmpCreature = null;
        DigitalCreature theBaby = null;

        //System.out.println("Fighting from " + position[0] + ", " + position[1]);
        for (int j = -1; j < 2; j++) {
            tmpPosition[1] = position[1] + j;
            for (int i = -1; i < 2; i++) {

                tmpPosition[0] = position[0] + i;

                //Finding opponent
                tmpCreature = world.getCreatureFromGrid(tmpPosition);

                //If partner available, reproduce and exit
                if (!(tmpCreature == null)) {
                    
                   
                    //Setting values from active part
                    int setId = 0;
                    String setType = getType();
                    int setEnergy = 10;
                    int setVision = getVisionLength();
                    
                    //Setting average values from mum and dad
                    int setStrength = Math.round((getStrength() + tmpCreature.getStrength())/2);
                    SimpleMatrix W11 = getWeights(1).plus(tmpCreature.getWeights(1)).scale(1/2);
                    SimpleMatrix W22 = getWeights(2).plus(tmpCreature.getWeights(2)).scale(1/2);;
                    
                    
                    //Creating the baby
                    theBaby = new DigitalCreature(setId, setType, setEnergy, setStrength, setVision, world);
                    
                    //Setting W1 and W2
                    theBaby.setW1(W11);
                    theBaby.setW2(W22);
                    
                    //Adding creature, looping over nearby cells
                    for (int b = -1; b < 2; b++) {
                        newPosition[1] = position[1] + b;
                        for (int a = -1; a < 2; a++) {

                        newPosition[0] = position[0] + a;
                            
                            if(world.addCreature(theBaby, newPosition)) {
                                System.out.println("YES - WE MADE A BABY");
                                
                                return true;
                                
                            }
                        }
                    }
                    
                } else {
                    return false;
                    //System.out.println("No creature here");
                }

            }
        }

        return false;
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
    public SimpleMatrix scanSurrounding() {
        /*
        *returns an array {int distCreature, str dirCreature, int distFoor, str distFood}
         */

        //Object[] ret = new Object[4];

        /*
        *Scanning area of "vision" around the creature for other creatures and food. 
         */
        SimpleMatrix surrounding = new SimpleMatrix((2 * visionLength + 1) * (2 * visionLength + 1), 1);
        int matrixCounter = 0;

        int[] scanPosition = {0, 0};

        //Checking world boundaries
        int startX = position[0] - visionLength; //Math.max(position[0] - vision, 0);
        int stopX = position[0] + visionLength; //Math.min(position[0] + vision, world.getSize()[0] - 1);

        int startY = position[1] - visionLength; //Math.max(position[1] - vision, 0);
        int stopY = position[1] + visionLength; //Math.min(position[1] + vision, world.getSize()[1] - 1);

        /*Scanning surrounding for food and creatures */
        //System.out.println("Creature at position " + position[0] + "," + position[1]);
        for (int j = startY; j <= stopY; j++) {
            for (int i = startX; i <= stopX; i++) {

                scanPosition[0] = i;
                scanPosition[1] = j;

                if (!(world.positionExists(scanPosition))) {
                    //Checking that we are inside the world
                    surrounding.set(matrixCounter, 3);
                    //System.out.println("Outside range at position " + scanPosition[0] + "," + scanPosition[1]);

                } else if (world.positionHasCreature(scanPosition) && (!(scanPosition[0] == position[0] && scanPosition[1] == position[1]))) {
                    //Checking for creatures
                    //System.out.println("Creature found at position " + scanPosition[0] + "," + scanPosition[1]);
                    surrounding.set(matrixCounter, 1);

                } else if (world.getFoodReserve(scanPosition) > 0) {
                    //Checking for food
                    //System.out.println("Food found at position " + scanPosition[0] + "," + scanPosition[1]);
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
