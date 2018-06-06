package com.project.why.braillelearning.LearningModel;

/**
 * Created by hyuck on 2017-09-15.
 */

/**
 * 점자 행렬 중 한개의 점에 대한 정보가 저장되는 class
 * 점의 터치 좌푯값, drawing 좌푯값, x와 y좌푯값, 돌출유무, 점자 번호 정보를 갖고 있음
 */
public class Dot {
    private float touchAreaRadius;
    private float bigCircleRadius;
    private float smallCircleRadius;
    private float viewAreaRadius;
    private float x;
    private float y;
    private boolean target = false;
    private int dotType;

    public Dot(){}

    /**
     * Dot가 매개변수로 넘어왔을 때, 깊은복사를 위한 생성자
     * @param copyDot : 다른 곳에서 만들어진 dot class
     */
    public Dot(Dot copyDot){
        this.touchAreaRadius = copyDot.getTouchAreaRadius();
        this.viewAreaRadius = copyDot.getViewAreaRadius();
        this.x = copyDot.getX();
        this.y = copyDot.getY();
        this.target = copyDot.getTarget();
        this.dotType = copyDot.getDotType();
    }

    public void setTouchAreaRadius(float touchAreaRadius) {
        this.touchAreaRadius = touchAreaRadius;
    }

    private void setViewAreaRadius(float viewAreaRadius) {
        this.viewAreaRadius = viewAreaRadius;
    }

    public void setBigCircleRadius(float Radius){
        this.bigCircleRadius = Radius;
    }

    public void setSmallCircleRadius(float Radius){
        this.smallCircleRadius = Radius;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setTarget(boolean target) {
        this.target = target;
        if(target == true)
            setViewAreaRadius(bigCircleRadius);
        else
            setViewAreaRadius(smallCircleRadius);
    }

    public void changeTarget(){
        if(target == true)
            setTarget(false);
        else
            setTarget(true);
    }

    public void setDotType(int dotType){
        this.dotType = dotType;
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

    public float getViewAreaRadius() {
        return viewAreaRadius;
    }

    public float getTouchAreaRadius() {
        return touchAreaRadius;
    }


    /**
     * 터치영역 중 y좌표의 허용영역에 해당되는지 check하는 함수
     * @param y : 터치된 y좌표
     * @return true(해당됨), false(해당되지 않음)
     */
    public boolean checkSatisfyArea_Y(float y){
        if(this.y - touchAreaRadius <= y && y <= this.y + touchAreaRadius)
            return true;
        else
            return false;
    }


    /**
     * 터치영역 중 x좌표의 허용영역에 해당되는지 check하는 함수
     * @param x : 터치된 x좌표
     * @return true(해당됨), false(해당되지 않음)
     */
    public boolean checkSatisfyArea_X(float x){
        if(this.x - touchAreaRadius <= x && x <= this.x + touchAreaRadius)
            return true;
        else
            return false;
    }
}
