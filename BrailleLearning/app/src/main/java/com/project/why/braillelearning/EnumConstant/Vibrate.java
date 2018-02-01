package com.project.why.braillelearning.EnumConstant;
/**
 * Created by User on 2017-10-09.
 */

/**
 * 점자 학습 진행에 따른 진동 강도 enum
 * STRING : 강한진동
 * WEAKLY : 약한진동
 */
public enum Vibrate {
    STRONG(250), MIDDLE(100), WEAKLY(50);
    int strength;

    Vibrate(int strength){
        this.strength = strength;
    }

    public int getStrength(){
        return strength;
    }


}
