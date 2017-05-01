package com.example.neha.appsdontlie_capstonestage2.data;

/**
 * Created by neha on 4/30/17.
 */

public class MyProfileData {
    private String name;
    private String userID;
    private String gender;
    private String old_photo_url;
    private String new_photo_url;
    private String weight;
    private String height;
    private String password;
    private String step_counts;
    private String calories;

    public MyProfileData(){}

    public MyProfileData(String name,String userID,String gender,String old_photo_url,String new_photo_url,String weight,String height,String password,String step_counts,String calories){

        this.name = name;
        this.userID = userID;
        this.gender = gender;
        this.old_photo_url = old_photo_url;
        this.new_photo_url = new_photo_url;
        this.weight = weight;
        this.height = height;
        this.password = password;
        this.step_counts = step_counts;
        this.calories = calories;

    }

    public String getName(){

        return name;
    }

    public void setName(String name){

        this.name = name;
    }


    public String getUserID(){

        return userID;
    }

    public void setUserID(String userID){

        this.userID = userID;
    }

    public String getGender(){

        return gender;
    }

    public void setGender(String gender){

        this.gender = gender;
    }

    public String getOldPhotoUrl(){

        return old_photo_url;
    }

    public void setOldPhotoUrl(String old_photo_url){

        this.old_photo_url = old_photo_url;
    }

    public String getNewPhotoUrl(){

        return new_photo_url;
    }

    public void setNewPhotoUrl(String new_photo_url){

        this.new_photo_url = new_photo_url;
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
    public String getPassword(){

        return password;
    }

    public void setPassword(String password){

        this.password = password;
    }
    public String getSteps(){

        return step_counts;
    }

    public void setSteps(String step_counts){

        this.step_counts = step_counts;
    }
    public String getCalories(){

        return calories;
    }

    public void setCalories(String calories){

        this.calories = calories;
    }






}
