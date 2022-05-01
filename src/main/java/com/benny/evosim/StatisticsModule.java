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
    ArrayList<Integer> eatCount = new ArrayList<>();
    ArrayList<Integer> fightCount = new ArrayList<>();
    ArrayList<Integer> reproduceCount = new ArrayList<>();
    ArrayList<Integer> doNothingCount = new ArrayList<>();
    
//    int[] fightCount;
//    int[] reproduceCount;
//    int[] doNothingCount;
    
    
    
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
    
    public ArrayList<Integer> getActionCount(String type) {
        ArrayList<Integer> myReturn = new ArrayList<>();
        
        switch(type) {
            case("eat") :{
                myReturn =  eatCount;
                break;
            }
            case("fight") :{
                myReturn =   fightCount;
                break;
            }
            case("reproduce") :{
                myReturn =   reproduceCount;
                break;
            }
            case("doNothing") :{
                myReturn =   doNothingCount;
                break;
            }
        }
        
        //Wrong input
       
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
    }
    
}
