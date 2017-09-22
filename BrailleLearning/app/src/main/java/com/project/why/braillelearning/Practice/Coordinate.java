package com.project.why.braillelearning.Practice;

/**
 * Created by hyuck on 2017-09-15.
 */

public class Coordinate {
    private float X;
    private float Y;
    private boolean Target=false;

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }

    public boolean isTarget() {
        return Target;
    }

    public void setTarget(boolean target) {
        Target = target;
    }
}
