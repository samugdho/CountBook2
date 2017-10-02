/*
 * Copyright (c) 2017. Sadman, CMPUT 301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and conditions of the
 * Code of Students Behaviour at University of Alberta
 */

package com.example.sadman.countbook;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Counter class to store information on counters;
 * name, comment -> String;
 * initVal, currentVal -> int;
 * creationDate, updateDate -> Date;
 * Most of these have getters and setters;
 * And some specialized getters for Dates;
 * All setters update updateDate
 * uses SimpleDateFormat for date and time Formatting
 * @see SimpleDateFormat
 * creationDate,
 */
public class Counter{
    private String name;
    private String comment;
    private Date creationDate;
    private Date updateDate;
    private int initVal;
    private int currentVal;

    /**
     * Create a counter, set initial value, name, and optional comment
     * @param name
     * @param comment
     * @param initVal
     */
    public Counter(String name, String comment, int initVal) {
        this.name = name;
        this.comment = comment;
        this.initVal = initVal;
        this.creationDate = new Date();
        this.updateDate = new Date();
        this.currentVal = this.initVal;
    }

    public int getInitVal() {
        return initVal;
    }

    public void setInitVal(int initVal) {
        this.update();
        this.initVal = initVal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.update();
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.update();
        this.comment = comment;
    }

    public int getCurrentVal() {
        return currentVal;
    }

    public void setCurrentVal(int currentVal) {
        this.update();
        this.currentVal = currentVal;
    }
    /**
     * Specialized getter for creationDate
     * @return formatted date String
     * @see SimpleDateFormat
     */
    public String getCreationString() {
        return new SimpleDateFormat("dd/MM/yy, h:mm a").format(this.creationDate);
    }

    /**
     * Specialized getter for updateDate
     * @return formatted date String
     * @see SimpleDateFormat
     */
    public String getUpdateString() {
        return new SimpleDateFormat("dd/MM/yy, h:mm:ss a").format(this.updateDate);
    }
    /**
     * Updates the updateDate to NOW
     */
    public void update(){
        this.updateDate = new Date();
    }

    /**
     * unused toString method
     * @return some string
     */
    @Override
    public String toString() {
        return new SimpleDateFormat("dd/MM/yy, h:mm a").format(this.creationDate)+"\n\t"+this.name+" | "+this.currentVal;
    }
}
