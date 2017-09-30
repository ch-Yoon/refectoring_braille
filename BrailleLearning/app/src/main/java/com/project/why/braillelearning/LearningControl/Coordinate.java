package com.project.why.braillelearning.LearningControl;

/**
 * Created by hyuck on 2017-09-15.
 */

public class Coordinate {
    private float x;
    private float y;
    private boolean target=false;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean getTarget() {
        return target;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setTarget(boolean target) {
        this.target = target;
    }
}
