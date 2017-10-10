package com.project.why.braillelearning.LearningModel;

/**
 * Created by hyuck on 2017-09-15.
 */

public class BasicLearningCoordinate {
    private float areaRidus;
    private float x;
    private float serviceArea_X[];
    private float y;
    private float serviceArea_Y[];
    private boolean target = false;
    private int dotType;

    BasicLearningCoordinate(float areaRidus) {
        this.areaRidus = areaRidus;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean getTarget() {
        return target;
    }

    public int getDotType() {
        return dotType;
    }

    public void setX(float x) {
        this.x = x;
        setServiceArea_X();
    }

    public void setServiceArea_X(){
        serviceArea_X = new float[2];
        serviceArea_X[0] = x - areaRidus;
        serviceArea_X[1] = x + areaRidus;
    }

    public void setY(float y) {
        this.y = y;
        setServiceArea_Y();
    }

    public void setServiceArea_Y() {
        serviceArea_Y = new float[2];
        serviceArea_Y[0] = y - areaRidus;
        serviceArea_Y[1] = y + areaRidus;
    }

    public void setTarget(boolean target) {
        this.target = target;
    }

    public void setDotType(int dotType){
        this.dotType = dotType;
    }


    public boolean checkInsideArea_Y(float y){
        if(serviceArea_Y[0] <= y)
            return true;
        else
            return false;
    }

    public boolean checkInsideArea_X(float x){
        if(x <= serviceArea_X[1])
            return true;
        else
            return false;
    }

    public boolean checkSatisfyArea_Y(float y){
        if(serviceArea_Y[0] <= y && y <= serviceArea_Y[1])
            return true;
        else
            return false;
    }

    public boolean checkSatisfyArea_X(float x){
        if(serviceArea_X[0] <= x && x <= serviceArea_X[1])
            return true;
        else
            return false;
    }
}
