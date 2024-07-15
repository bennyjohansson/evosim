/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.benny.evosim;

/**
 *
 * @author bennyjohansson
 */
public class Constants {
    
    int CREATURE_VISION = 1;
    int MAX_AGE = 30;
    int INITIAL_ENERGY = 80;
    int MAX_ENERGY = 100;
    int ENERGY_TO_REPRODUCE = 50;
    int MAX_FOOD = 200;
    int MAX_STRENGTH = 10;
    int MAX_CREATURES = 1; // MAx 1 creature per grid
           
    public int getMaxAge() {
        return MAX_AGE;
    }

    public int getInitialEnergy() {
        return INITIAL_ENERGY;
    }

    public int getCreatureVision() {
        return CREATURE_VISION;
    }

    public int getMaxEnergy() {
        return MAX_ENERGY;
    }

    public int getEnergyToReproduce() {
        return ENERGY_TO_REPRODUCE;
    }

    public int getMaxFood() {
        return MAX_FOOD;
    }

    public int getMaxStrength() {
        return MAX_STRENGTH;
    }

    public int getMaxCreatures() {
        return MAX_CREATURES;
    }
    
}
