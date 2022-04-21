/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.benny.evosim;

/**
 *
 * @author bennyjohansson
 */
public class GridContainer {

    public GridContainer() {
    }

    /*
        * Parameters
     */
    DigitalCreature theCreature;
    int foodReserve;

    /*
        * Functions
     */
    public void addCreature(DigitalCreature myCreature) {

        theCreature = myCreature;
    }

    public boolean removeCreature() {

        if (theCreature == null) {
            return false;
        } else {
            theCreature = null;
            return true;
        }
    }
    
    public DigitalCreature getCreature() {
        return theCreature;
    }

    public boolean hasCreature() {
        if(theCreature == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isEmpty() {

        if (theCreature == null) {
            return true;
        } else {
            return false;
        }

    }

    public int getFoodReserve() {
        return foodReserve;
    }

    public void addFoodReserve(int myFood) {
        foodReserve += myFood;
    }
    
    public void removeFoodReserve() {
        foodReserve = 0;
    }
}
