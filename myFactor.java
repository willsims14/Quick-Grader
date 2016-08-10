package com.gmail.willsims.quick_grade;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Will Sims on 7/16/2016.
 *
 * Because you cannot set an int to NULL, to reset the scores of the myFactor,
 *   the score is reset to -1. When the score is equal to -1, the rest of the
 *   program knows that the user either clicked "clear scores" or they haven't
 *   entered a score for that factor yet.
 *
 * The Parcel in the constructor is used to pass the array of myFactors between activities
 *
 */
public class myFactor implements Parcelable{
    private String mName;
    private double mWeight;
    private double mScore;
    private boolean hasScore;


    myFactor(Parcel in){
        super();
        this.mScore = -1;
        this.hasScore = false;
        readFromParcel(in);
    }


    public double getFactorWeight(){
        return mWeight;
    }

    public String getFactorName(){
        return mName;
    }
    public double getFactorScore(){
        return mScore;
    }
    public boolean scoreHasBeenEntered(){
        if(this.mScore != -1 && this.mScore >= 0){
            this.hasScore = true;
        }else{
            this.hasScore = false;
        }
        return this.hasScore;
    }
    public void setFactorValues(String tempName, double tempWeight){
        this.mName = tempName;
        this.mWeight = tempWeight;
        this.mScore = -1;
    }
    @Override
    public int describeContents() {
        return 0;
    }


    public void clearScore(){
        this.mScore = -1;
        this.hasScore = false;
    }

    // Gives the myFactor a score
    public void setFactorScore(double score){
        this.mScore = score;
        this.hasScore = true;
    }

    // Parcelables
    public static final Parcelable.Creator<myFactor> CREATOR = new Parcelable.Creator<myFactor>() {
        public myFactor createFromParcel(Parcel in) {
            return new myFactor(in);
        }

        public myFactor[] newArray(int size) {

            return new myFactor[size];
        }

    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeDouble(this.mWeight);
        dest.writeDouble(this.mScore);
    }
    public void readFromParcel(Parcel in){
        this.mName = in.readString();
        this.mWeight = in.readDouble();
        this.mScore = in.readDouble();
    }
}
