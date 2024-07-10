/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.benny.evosim;

import java.util.ArrayList;
import java.util.Random;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
//import java.math.*;
import org.ejml.simple.SimpleMatrix;
import org.ejml.equation.Equation;

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
    int actionReach;
    int outputActionSize;
    int outputMoveSize;
    int numberOfLayers;
    int[] position;
    LinkedList<String> lastActions = new LinkedList<>();
    CreatureWorld world;
    
//    SimpleMatrix W1;
//    SimpleMatrix W2;
//    SimpleMatrix W3;
//    SimpleMatrix b1;
//    SimpleMatrix b2;

    ArrayList<SimpleMatrix> actionLayerWeights = new ArrayList<>();
    ArrayList<SimpleMatrix> actionLayerBias = new ArrayList<>();

    ArrayList<SimpleMatrix> moveLayerWeights = new ArrayList<>();
    ArrayList<SimpleMatrix> moveLayerBias = new ArrayList<>();

    public DigitalCreature() {
    }

    public DigitalCreature(int setId, String setType, int setEnergy, int setStrength, int setVision, CreatureWorld myWorld) {
        id = setId;
        type = setType;
        energy = setEnergy;
        strength = setStrength;
        age = 1;
        visionLength = setVision;
        actionReach = 1;
        outputActionSize = 5;
        outputMoveSize = 8;
        numberOfLayers = 5;
        int currentLayerSize = 0;
        int nextLayerSize = 0;
        int numberOfObjects = 4;

        world = myWorld;

        /* Initializing neural newwork, should perhaps be own class
        * Takes visionVector as input in the neural network and compress to   
        * 4 output actions (fight, eat, mate, nothing (?))
         */
        Random rand = new Random();

        //Should not be output size but number of categories!!!!
        int visionVectorSize = numberOfObjects * (2 * visionLength + 1) * (2 * visionLength + 1);
        int actionVectorSize = numberOfObjects * (2 * actionReach + 1) * (2 * actionReach + 1);

        //Setting up action layer matrices
        currentLayerSize = actionVectorSize;
        nextLayerSize = currentLayerSize - (actionVectorSize - outputActionSize) / (numberOfLayers);
        for (int i = 0; i < numberOfLayers; i++) {

            actionLayerWeights.add(SimpleMatrix.random_DDRM(nextLayerSize, currentLayerSize, -1, 1, rand));
            actionLayerBias.add(SimpleMatrix.random_DDRM(nextLayerSize, 1, -1, 1, rand));

            //System.out.println("Layer " + i + " " + currentLayerSize + "x" + nextLayerSize);
            currentLayerSize = nextLayerSize;
            nextLayerSize = currentLayerSize - (actionVectorSize - outputActionSize) / (numberOfLayers);
            if (i == numberOfLayers - 2) {
                //System.out.println("SETTING LAYER");
                nextLayerSize = outputActionSize;
            }

        }

        //Setting up vision layer matrices
        currentLayerSize = visionVectorSize;
        nextLayerSize = (currentLayerSize - (visionVectorSize - outputMoveSize) / (numberOfLayers - 1));
        for (int i = 0; i < numberOfLayers; i++) {

            moveLayerWeights.add(SimpleMatrix.random_DDRM(nextLayerSize, currentLayerSize, -1, 1, rand));
            moveLayerBias.add(SimpleMatrix.random_DDRM(nextLayerSize, 1, -1, 1, rand));

            currentLayerSize = nextLayerSize;
            nextLayerSize = (currentLayerSize - (visionVectorSize - outputMoveSize) / (numberOfLayers));
            if (i == numberOfLayers - 1) {
                nextLayerSize = outputMoveSize;
            }
            //System.out.println("Layer " + i + " " + currentLayerSize + "x" + nextLayerSize);

        }
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

    public SimpleMatrix getActionWeights(int myLayer) {

        return actionLayerWeights.get(myLayer);

    }

    public SimpleMatrix getActionBias(int myLayer) {

        return actionLayerBias.get(myLayer);

    }

    public SimpleMatrix getMoveWeights(int myLayer) {

        return moveLayerWeights.get(myLayer);

    }

    public SimpleMatrix getMoveBias(int myLayer) {

        return moveLayerBias.get(myLayer);

    }

    public void setPosition(int[] myPosition) {

        position = myPosition;

    }

    public void setActionWeights(int myLayer, SimpleMatrix Wx) {

        actionLayerWeights.set(myLayer, Wx);

    }

    public void setActionBias(int myLayer, SimpleMatrix bx) {

        actionLayerBias.set(myLayer, bx);

    }
    
    public void setMoveWeights(int myLayer, SimpleMatrix Wx) {

        moveLayerWeights.set(myLayer, Wx);

    }

    public void setMoveBias(int myLayer, SimpleMatrix bx) {

        moveLayerBias.set(myLayer, bx);

    }

    //updating type based on the lastActions list
    public void updateType() {
        //First checking if all the elements of the list are the same, then we can set the type
        //Checking if all the elements are the same
        boolean allEqual = lastActions.stream().distinct().limit(2).count() <= 1;
        String setType = getType();

        if (lastActions.size() > 0) {
            if(allEqual) {
                setType = lastActions.getFirst();
            } else {
                setType = "Switcher";
            }
        } else {
            setType = "Neutral";
        }

        type = setType;
    }

    /*
    * Action functions
     */
    public String getAction() {

        double threshold = 0.3;
        int indexMax = 0;
        double maxValue = 0;
        String action = "";

        SimpleMatrix actionVector = scanActionArea();
//        SimpleMatrix W1output = new SimpleMatrix(W2.numCols(), 1);
//        SimpleMatrix W2output = new SimpleMatrix(outputSize, 1);

        //SimpleMatrix W2outputRELU = new SimpleMatrix(outputSize,1);
        SimpleMatrix inputVector = actionVector;

        for (int l = 0; l < numberOfLayers; l++) {

            SimpleMatrix Wl = getActionWeights(l);
            SimpleMatrix bl = getActionBias(l);

            //System.out.println(Wl.numCols() + "x" + Wl.numRows());
            //System.out.println(inputVector.numCols() + "x" + inputVector.numRows());
            SimpleMatrix Wlout = Wl.mult(inputVector);
            Wlout = Wlout.plus(bl);
            inputVector = Functions.sigmoidFunction(Wlout);

        }

        indexMax = Functions.findVectorIndexMax(inputVector);
        maxValue = inputVector.get(indexMax, 0);

        //System.out.println("IndexMax: " + indexMax);
        if (!(maxValue >= threshold)) {
            //Noth high enough activation
            action = "doNothing";
        } else {

            switch (indexMax) {
                case (0): {
                    action = "Reproduce";
                    lastActions.addFirst(action);


                    break;
                }
                case (1): {
                    action = "Eat";
                    lastActions.addFirst(action);
                    //System.out.println(action);
                    break;

                }
                case (2): {
                    action = "Fight";
                    lastActions.addFirst(action);
                    //System.out.println(action);
                    break;

                }
                case (3): {
                    action = "Clone";
                    lastActions.addFirst(action);
                    //System.out.println(action);
                    break;

                }
                case (4): {
                    action = "doNothing";
                    lastActions.addFirst(action);
                    //System.out.println(action);
                    break;
                }
                default:
                //What now
            }
            //Truncating lastactions to only store 5 last actions
            // Truncate to 5 most recent
            while (lastActions.size() > 5) {
                lastActions.removeLast();
            }
            //printing the lastactions list
            // System.out.println("Last actions: " + lastActions);
            //Updating type
            updateType();

        }
        //System.out.println("Creature " + getId() + ": " + action);
        //System.out.println("Action: " + action);

        return action;

    }

    public DigitalCreature update() {

        energy--;
        if (energy <= 0) {
            // System.out.println("Creature " + id + " died by starvation");
            return this;
        }

        age++;
        int MAX_AGE = world.constants.getMaxAge();
        if (age > MAX_AGE) {
            // System.out.println("Creature " + id + " died by age");
            return this;
        }
        return null;

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
                    //return tmpCreature;

                    //Attacker stronger and wins
                    if (strength >= tmpCreature.getStrength()) {

                        // System.out.println("Creature killed by: " + tmpPosition[0] + "," + tmpPosition[1]);

                        return tmpCreature;
                    }

                    //Opponent stronger and wins
                    if (strength < tmpCreature.getStrength()) {

                        // System.out.println("Attacker got killed at: " + position[0] + "," + position[1]);
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
        int[] newPosition = {0, 0};
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
                    setId = world.getStatsModule().getCreatureIdCount() + 1;

                    String setType = getType();
                    int setEnergy = 3;
                    int setVision = getVisionLength();
                    int setStrength = Math.round((getStrength() + tmpCreature.getStrength()) / 2);

                    //Creating the baby
                    theBaby = new DigitalCreature(setId, setType, setEnergy, setStrength, setVision, world);

                    //Setting Neural nets values from mum and dad
                    for (int l = 0; l < numberOfLayers; l++) {
                        //getActionBias(l).print("%10.2f");
                        theBaby.setActionWeights(l, getActionWeights(l).plus(tmpCreature.getActionWeights(l)).scale(0.5));
                        theBaby.setActionBias(l, getActionBias(l).plus(tmpCreature.getActionBias(l)).scale(0.5));
                        
                        theBaby.setMoveWeights(l, getMoveWeights(l).plus(tmpCreature.getMoveWeights(l)).scale(0.5));
                        theBaby.setMoveBias(l, getMoveBias(l).plus(tmpCreature.getMoveBias(l)).scale(0.5));

                        //theBaby.getActionWeights(l).print("%10.2f");
                    }

                    //Checkgin world boundaries
                    int bMin = -1;
                    int bMax = 2;
                    int aMin = -1;
                    int aMax = 2;
                    
                    int[] worldSize = world.getSize();
                    
                    
                    bMin = Math.max(-1, position[1] - 1);
                    aMin = Math.max(-1, position[0] - 1);
                    bMax = Math.min(2, worldSize[1] - position[1]);
                    aMax = Math.min(2, worldSize[0] - position[0]);
                        
                    //Adding creature, looping over nearby cells
                    for (int b = bMin; b < bMax; b++) {
                        newPosition[1] = position[1] + b;
                        for (int a = aMin; a < aMax; a++) {

                            newPosition[0] = position[0] + a;
                            //System.out.println("Creature " + getId() + " trying to add " + setId + " at position " + newPosition[0] + ", " + newPosition[1]);
                            if (world.addCreature(theBaby, newPosition)) {
                                //System.out.println("YES - WE MADE A BABY " + setId);

                                return true;

                            }
                        }
                    }
                    return false;

                } else {
                    return false;
                    //System.out.println("No creature here");
                }

            }
        }

        return false;
    }

    //Tries to shag first found neighbour
    public boolean createClone(boolean rescueCycle) {

        int[] newPosition = {0, 0};
        DigitalCreature theClone = null;

        int setId = 0;
        setId = world.getStatsModule().getCreatureIdCount() + 1;
        String setType = getType();
        
        int setEnergy = getEnergy() / 2;
        if(rescueCycle) {
            setEnergy = world.constants.getInitialEnergy();
        }

        int setVision = getVisionLength();
        int setStrength = strength;
        int[] worldSize = world.getSize();

        //Creating the baby
        theClone = new DigitalCreature(setId, setType, setEnergy, setStrength, setVision, world);

        //Setting W1 and W2, b1 and b2
        for (int i = 0; i < numberOfLayers; i++) {
            theClone.setActionWeights(i, getActionWeights(i));
            theClone.setActionBias(i, getActionBias(i));
            theClone.setMoveWeights(i, getMoveWeights(i));
            theClone.setMoveBias(i, getMoveBias(i));
        }

        //Adding creature, looping over nearby cells
        for (int b = -1; b < 2; b++) {
            //newposition[1] for the new vclone is the max of 0 and the current position + b
            
            newPosition[1] = Math.min(Math.max(0,position[1] + b), worldSize[1] - 1);

            for (int a = -1; a < 2; a++) {

                newPosition[0] = Math.min(Math.max(0,position[0] + a), worldSize[0] - 1);
                // System.out.println("Creature " + getId() + " trying to add " + setId + " at position " + newPosition[0] + ", " + newPosition[1]);
                if (world.addCreature(theClone, newPosition)) {
                    // System.out.println("YES - WE MADE a clone " + setId);
                    if(!rescueCycle) {
                        energy /= 2;
                    }
                    // energy /= 2 ;
                    return true;

                }
            }
        }
        return false;

    }

    public String getMoveDirection() {

        double threshold = 0.1;
        int indexMax = 0;
        double maxValue = 0;
        String direction = "";

        SimpleMatrix visionVector = scanSurrounding();

        SimpleMatrix inputVector = visionVector;

        for (int l = 0; l < numberOfLayers; l++) {

            SimpleMatrix Wl = getMoveWeights(l);
            SimpleMatrix bl = getMoveBias(l);

            //System.out.println(Wl.numCols() + "x"+  Wl.numRows());
            //System.out.println(inputVector.numCols() + "x"+  inputVector.numRows());
            SimpleMatrix Wlout = Wl.mult(inputVector);
            Wlout = Wlout.plus(bl);
            inputVector = Functions.sigmoidFunction(Wlout);
            //System.out.println(inputVector.numCols() + "x"+  inputVector.numRows());

        }

        indexMax = Functions.findVectorIndexMax(inputVector);
        maxValue = inputVector.get(indexMax, 0);

        //System.out.println("IndexMax: " + indexMax);
        if (!(maxValue >= threshold)) {
            //Noth high enough activation
            direction = "";
        } else {

            switch (indexMax) {
                case (0): {
                    direction = "N";
                    break;

                }
                case (1): {
                    direction = "S";
                    //System.out.println(action);
                    break;

                }
                case (2): {
                    direction = "E";
                    //System.out.println(action);
                    break;

                }
                case (3): {
                    direction = "W";
                    //System.out.println(action);
                    break;
                }
                case (4): {
                    direction = "NE";
                    break;

                }
                case (5): {
                    direction = "NW";
                    //System.out.println(action);
                    break;

                }
                case (6): {
                    direction = "SE";
                    //System.out.println(action);
                    break;

                }
                case (7): {
                    direction = "SW";
                    //System.out.println(action);
                    break;
                }
                default:
                //What now
            }

        }
        //System.out.println("Creature " + getId() + ": " + direction);
        //System.out.println("Action: " + action);

        return direction;

    }

    public boolean moveNN() {

        String direction = "";
        direction = getMoveDirection();

        if (direction == "") {
            return false;
        } else {
            return move(direction);
        }

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
        if (tmpPosition[0] >= worldSize[0] - 1) {
            tmpPosition[0] = worldSize[0] - 1;
            //System.out.println("Trying to move outside x-range > max");
        }
        if (tmpPosition[0] < 0) {
            tmpPosition[0] = 0;
            //System.out.println("Trying to move outside x-range < 0");
        }

        if (tmpPosition[1] >= worldSize[1] - 1) {
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
        int numberOfObjects = 4;

        /*
        *Scanning area of "vision" around the creature for other creatures and food. 
         */
        SimpleMatrix surrounding = new SimpleMatrix((2 * visionLength + 1) * (2 * visionLength + 1), 1);
        SimpleMatrix surrounding2 = new SimpleMatrix(numberOfObjects * (2 * visionLength + 1) * (2 * visionLength + 1), 1);

        //Temp vector and counters for keeping one grid state triplet represents {wall, creature, food, empty}
        SimpleMatrix triplet = new SimpleMatrix(numberOfObjects, 1);
        int index1 = 0;
        int index2 = index1 + numberOfObjects - 1;

        Equation eq = new Equation();
        eq.alias(index1, "index1", index2, "index2", surrounding2, "surrounding2", triplet, "triplet");

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
                    //Checking that we are inside the world {wall, creature, food, empty}
                    //surrounding.set(matrixCounter, 3);

                    triplet.set(0, 1);

                    //System.out.println("Outside range at position " + scanPosition[0] + "," + scanPosition[1]);
                } else if (world.positionHasCreature(scanPosition) && (!(scanPosition[0] == position[0] && scanPosition[1] == position[1]))) {
                    //Checking for creatures
                    //System.out.println("Creature found at position " + scanPosition[0] + "," + scanPosition[1]);
                    //surrounding.set(matrixCounter, 1);

                    triplet.set(1, 1);

                } else if (world.getFoodReserve(scanPosition) > 0) {
                    //Checking for food
                    //System.out.println("Food found at position " + scanPosition[0] + "," + scanPosition[1]);
                    //surrounding.set(matrixCounter, 2);
                    triplet.set(2, 1);

                } else {
                    //Empty grid
                    triplet.set(3, 1);

                }
                eq.process("surrounding2(index1:index2) = triplet(:)");
                index1 += numberOfObjects;
                index2 = index1 + numberOfObjects - 1;
                eq.alias(index1, "index1", index2, "index2", surrounding2, "surrounding2", triplet, "triplet");

                matrixCounter++;
            }

        }
        //surrounding2.print();
        return surrounding2;
    }

    /*
    * Looks "actionReach" steps in either direction and returns a column vector with the contents of the cells
     */
    public SimpleMatrix scanActionArea() {


        /*
        *Scanning area of "actionReach" around the creature for other creatures and food. 
         */
        int numberOfObjects = 4;
        //SimpleMatrix surrounding = new SimpleMatrix((2 * actionReach + 1) * (2 * actionReach + 1), 1);
        SimpleMatrix surrounding2 = new SimpleMatrix(numberOfObjects * (2 * actionReach + 1) * (2 * actionReach + 1), 1);

        //Temp vector and counters for keeping one grid state triplet represents {wall, creature, food, empty}
        SimpleMatrix triplet = new SimpleMatrix(numberOfObjects, 1);
        int index1 = 0;
        int index2 = index1 + numberOfObjects - 1;

        Equation eq = new Equation();
        eq.alias(index1, "index1", index2, "index2", surrounding2, "surrounding2", triplet, "triplet");

        int matrixCounter = 0;

        int[] scanPosition = {0, 0};

        //Checking world boundaries
        int startX = position[0] - actionReach;
        int stopX = position[0] + actionReach;

        int startY = position[1] - actionReach;
        int stopY = position[1] + actionReach;

        /*Scanning surrounding for food and creatures */
        for (int j = startY; j <= stopY; j++) {
            for (int i = startX; i <= stopX; i++) {

                scanPosition[0] = i;
                scanPosition[1] = j;

                if (!(world.positionExists(scanPosition))) {
                    //Checking that we are inside the world {wall, creature, food, empty}
                    //surrounding.set(matrixCounter, 3);

                    triplet.set(0, 1);

                    //System.out.println("Outside range at position " + scanPosition[0] + "," + scanPosition[1]);
                } else if (world.positionHasCreature(scanPosition) && (!(scanPosition[0] == position[0] && scanPosition[1] == position[1]))) {
                    //Checking for creatures
                    //System.out.println("Creature found at position " + scanPosition[0] + "," + scanPosition[1]);
                    //surrounding.set(matrixCounter, 1);

                    triplet.set(1, 1);

                } else if (world.getFoodReserve(scanPosition) > 0) {
                    //Checking for food
                    //System.out.println("Food found at position " + scanPosition[0] + "," + scanPosition[1]);
                    //surrounding.set(matrixCounter, 2);
                    triplet.set(2, 1);

                } else {
                    //Empty grid
                    triplet.set(3, 1);

                }
                eq.process("surrounding2(index1:index2) = triplet(:)");
                index1 += numberOfObjects;
                index2 = index1 + numberOfObjects - 1;
                eq.alias(index1, "index1", index2, "index2", surrounding2, "surrounding2", triplet, "triplet");

                matrixCounter++;
            }

        }
        //surrounding2.print();
        return surrounding2;
    }

    public String getType() {
        //System.out.println(type);
        return type;
    }
}
