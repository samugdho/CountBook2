package com.example.sadman.countbook;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Counter{
    private String name;
    private String comment;
    private Date date;
    private int initVal;
    private int currentVal;

    public void setInitVal(int initVal) {
        this.update();
        this.initVal = initVal;
    }

    public Date getLast() {
        return last;
    }



    private Date last;

    public Counter(String name, String comment, int initVal) {
        this.name = name;
        this.comment = comment;
        this.initVal = initVal;
        this.date = new Date();
        this.last = new Date();
        this.currentVal = this.initVal;
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


    public String getDateString() {
        return new SimpleDateFormat("dd/MM/yy, h:mm a").format(this.date);
    }
    public String getLastString() {
        return new SimpleDateFormat("dd/MM/yy, h:mm:ss a").format(this.last);
    }
    public void update(){
        this.last = new Date();
    }
    public int getInitVal() {
        return initVal;
    }


    @Override
    public String toString() {
        return new SimpleDateFormat("dd/MM/yy, h:mm a").format(this.date)+"\n\t"+this.name+" | "+this.currentVal;
    }
}
