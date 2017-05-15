package com.example.neha.appsdontlie_capstonestage2.data;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by neha on 4/30/17.
 */

@IgnoreExtraProperties
public class MyProfileData implements Serializable {
    private String name;
    private String gender;
    private String oldurl;
    private String newurl;
    private String weight;
    private String height;
    private String steps;
    private String calories;

    public MyProfileData(){}


    public String getName(){

        return name;
    }

    public void setName(String name){

        this.name = name;
    }


    public String getGender(){

        return gender;
    }

    public void setGender(String gender){

        this.gender = gender;
    }

    public String getOldUrl(){

        return oldurl;
    }

    public void setOldUrl(String oldurl){

        this.oldurl = oldurl;
    }

    public String getNewUrl(){

        return newurl;
    }

    public void setNewUrl(String newurl){

        this.newurl = newurl;
    }

    public String getWeight(){

        return weight;
    }

    public void setWeight(String weight){

        this.weight = weight;
    }

    public String getHeight(){

        return height;
    }

    public void setHeight(String height){

        this.height = height;
    }

    public String getSteps(){

        return steps;
    }

    public void setSteps(String step_counts){

        this.steps = step_counts;
    }
    public String getCalories(){

        return calories;
    }

    public void setCalories(String calories){

        this.calories = calories;
    }






}
