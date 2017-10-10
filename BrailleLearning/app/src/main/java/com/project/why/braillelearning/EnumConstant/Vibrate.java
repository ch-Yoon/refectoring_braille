package com.project.why.braillelearning.EnumConstant;
/**
 * Created by User on 2017-10-09.
 */

public enum Vibrate {
    STRONG(250), WEAKLY(50);
    int strength;

    Vibrate(int strength){
        this.strength = strength;
    }

    public int getStrength(){
        return strength;
    }


}
