/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.benny.evosim;

import org.ejml.simple.SimpleMatrix;
import java.util.*;

/**
 *
 * @author bennyjohansson
 */
public class StatisticsModule {
    int initialNumberOfCreatures;
    int numberOfCreaturesKilledInCombat;
    int numberOfCreaturesKilledByStarvation;
    int numberOfCreaturesKilledByAge;
    int numberOfBabies;
    int creatureIdCount = 0;
    int oldestGenerationAlive = 0;
    int oldestGenerationEver = 0;
    String oldestGenerationAliveType = "";
    String oldestGenerationEverType = "";
    ArrayList<Integer> eatCount = new ArrayList<>();
    ArrayList<Integer> fightCount = new ArrayList<>();
    ArrayList<Integer> reproduceCount = new ArrayList<>();
    ArrayList<Integer> doNothingCount = new ArrayList<>();
    ArrayList<Integer> killedCount = new ArrayList<>();
    ArrayList<Integer> birthCount = new ArrayList<>();
    ArrayList<Integer> diedCount = new ArrayList<>();
    ArrayList<Integer> cloneCount = new ArrayList<>();
    ArrayList<Integer> clonedCount = new ArrayList<>();

    // Adding a list of array to stor the actionVector. Each scan is stored in a new
    // array
    // The action-vecor is a simple matrix with 5 rows and 1 column wiht double
    // values
    // adn stored in an arraylist
    // {food, creatures, strength, energy, age}

    ArrayList<SimpleMatrix> actionVectors = new ArrayList<>();

    public StatisticsModule() {
        initialNumberOfCreatures = 0;
        numberOfCreaturesKilledInCombat = 0;
        numberOfBabies = 0;

    }

    public int getInitialNumberOfCreatures() {
        return initialNumberOfCreatures;
    }

    public int getCreatureIdCount() {
        return creatureIdCount;
    }

    public int getOldestGenerationAlive() {
        return oldestGenerationAlive;
    }

    public int getOldestGenerationEver() {
        return oldestGenerationEver;
    }

    public String getOldestGenerationAliveType() {
        return oldestGenerationAliveType;
    }

    public String getOldestGenerationEverType() {
        return oldestGenerationEverType;
    }

    public void setOldestGeneration(int generation, String type) {

        this.oldestGenerationAlive = generation;
        this.oldestGenerationAliveType = type;
        // Checking if the generation is the oldest ever
        if (generation > oldestGenerationEver) {
            oldestGenerationEver = generation;
            oldestGenerationEverType = type;
        }
    }

    public ArrayList<Integer> getActionCount(String type) {
        ArrayList<Integer> myReturn = new ArrayList<>();

        switch (type) {
            case ("eat"): {
                myReturn = eatCount;
                break;
            }
            case ("fight"): {
                myReturn = fightCount;
                break;
            }
            case ("reproduce"): {
                myReturn = reproduceCount;
                break;
            }
            case ("doNothing"): {
                myReturn = doNothingCount;
                break;
            }
        }

        return myReturn;
    }

    public void addToEatCount(int myData) {
        eatCount.add(myData);
    }

    public void addToFightCount(int myData) {
        fightCount.add(myData);
    }

    public void addToReproduceCount(int myData) {
        reproduceCount.add(myData);
    }

    public void addToDoNothingCount(int myData) {
        doNothingCount.add(myData);
    }

    public void addToKilledCount(int myData) {
        killedCount.add(myData);
    }

    public void addToDiedCount(int myData) {
        diedCount.add(myData);
    }

    public void addToBirthCount(int myData) {
        birthCount.add(myData);
    }

    // Creatures trying to clone
    public void addToCloneCount(int myData) {
        cloneCount.add(myData);
    }

    // Creatures succeeding in cloning
    public void addToClonedCount(int myData) {
        clonedCount.add(myData);
    }

    // Adding an action vector to the array by appending it to the existing
    // arraylist
    public void addActionVector(SimpleMatrix myVector) {
        actionVectors.add(myVector);
    }

    // Saving the action vector to a file, separated by commas and new lines
    public void saveActionVectorsToFile(String fileName) {
        StringBuilder myString = new StringBuilder();
        for (int i = 0; i < actionVectors.size(); i++) {
            for (int j = 0; j < actionVectors.get(i).numRows(); j++) {
                myString.append(actionVectors.get(i).get(j, 0)).append(",");
            }
            myString.append("\n");
        }
        Functions.saveStringToFile(fileName, myString.toString());
    }

    public void setCreatureIdCount(int myId) {
        creatureIdCount = myId;
    }

    public void changeCreatureIdCount(int ch) {
        creatureIdCount += ch;
    }

    public void changeInitialNumberOfCreatures(int ch) {
        initialNumberOfCreatures += ch;
    }

    public int getNumberOfCreaturesKilled() {
        return numberOfCreaturesKilledInCombat + numberOfCreaturesKilledByStarvation + numberOfCreaturesKilledByAge;
    }

    public void changeNumberOfCreaturesKilledInCombat(int ch) {
        numberOfCreaturesKilledInCombat += ch;
    }

    public void changenumberOfCreaturesKilledByStarvation(int ch) {
        numberOfCreaturesKilledByStarvation += ch;
    }

    public void changenumberOfCreaturesKilledByAge(int ch) {
        numberOfCreaturesKilledByAge += ch;
    }

    public void changeNumberOfBabies(int ch) {
        numberOfBabies += ch;
    }

    public void printStats() {
        System.out.println("Killed Creatures: " + getNumberOfCreaturesKilled());
        System.out.println(" - Killed Combat: " + numberOfCreaturesKilledInCombat);
        System.out.println(" - Killed Starve/age: " + numberOfCreaturesKilledByStarvation);
        System.out.println("Born: " + numberOfBabies);
        System.out.println("");

        System.out.println("---------------------");
        System.out.printf("%-5s %-5s %-5s %-10s %-5s %-10s %-5s %-5s %-5s %-5s%n", "Y", "EAT", "FIGHT", "REPRODUCE",
                "CLONE", "DO NOTHING", "BIRTH", "CLONED", "KILLED", "DIED");

        for (int i = 0; i < eatCount.size(); i++) {
            System.out.printf("%-5d %-5d %-5d %-10d %-5d %-10d %-5d %-5d %-5d %-5d%n",
                    i,
                    eatCount.get(i),
                    fightCount.get(i),
                    reproduceCount.get(i),
                    cloneCount.get(i),
                    doNothingCount.get(i),
                    birthCount.get(i),
                    clonedCount.get(i),
                    killedCount.get(i),
                    diedCount.get(i));
        }

    }

}
