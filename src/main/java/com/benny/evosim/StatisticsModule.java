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
    ArrayList<Integer> killedCount = new ArrayList<>();
    ArrayList<Integer> birthCount = new ArrayList<>();
    ArrayList<Integer> diedCount = new ArrayList<>();
    ArrayList<Integer> cloneCount = new ArrayList<>();
    
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
    
     public void addToCloneCount(int myData) {
        cloneCount.add(myData);
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
        System.out.println("Y" + ", EAT, " + "FIGHT, "+ " REPRODUCE, "+ " DO NOTHING, " + "BIRTH, " + "CLONED, " + "KILLED, " + "DIED");
        for (int i = 0; i < eatCount.size(); i++) {
            
            System.out.println(i + ", " + eatCount.get(i) + ", "+ fightCount.get(i) + ", "+ reproduceCount.get(i) + ", "+ doNothingCount.get(i)+ ", "+ birthCount.get(i)+ ", " + cloneCount.get(i)+ ", "+ killedCount.get(i) + ", "+ diedCount.get(i));
            
        }
    
   
        
    }
    
}
