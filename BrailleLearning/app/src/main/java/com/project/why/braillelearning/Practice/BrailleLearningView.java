package com.project.why.braillelearning.Practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.project.why.braillelearning.BrailleInformationFactory.BrailleData;
import com.project.why.braillelearning.Global;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-09-14.
 */

public class BrailleLearningView extends View {
    private final int BIG_CIRCLE = 1; // 돌출 점자
    private final int MINI_CIRCLE = 0; // 비 돌출 점자

    private ArrayList<BrailleData> BrailleDataArray = new ArrayList<>();
    private Context context;
    private TextView textName;
    private Paint paint;
    private int BrailleDataIndex = 0;
    private float BigCircleRadiusRatio;
    private float MiniCircleRadiusRatio;
    private float Space;
    private Coordinate Coordinate_XY[][];



    public BrailleLearningView(Context context, ArrayList<BrailleData> BrailleDataArray){
        super(context);
        this.context = context;
        this.BrailleDataArray.addAll(BrailleDataArray);
        settingPaint();
        initBrailleRatio();
    }

    public void settingPaint(){
        paint = new Paint();
        paint.setColor(Color.WHITE);
    }

    public void initBrailleRatio(){
        BigCircleRadiusRatio = (float)0.1;
        MiniCircleRadiusRatio = (float)0.02;
        Space = (float)0.1;
    }

    @Override
    protected void onDraw(Canvas canvas){
        Log.d("ondraw","ondraw");
        if(!BrailleDataArray.isEmpty()) {
            if (BrailleDataIndex <= BrailleDataArray.size()) {
                BrailleData Data = BrailleDataArray.get(BrailleDataIndex);
                String LetterName = Data.getLetterName();
                int BrailleMatrix[][] = Data.getBrailleMatrix();

                setTextName(LetterName);
                setBraille(canvas, BrailleMatrix);
            }
        }
    }

    public void setTextName(String LetterName){
        if(textName==null) {
            textName = new TextView(context);

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER_HORIZONTAL
            );
            textName.setTextColor(Color.WHITE);
            textName.setPadding(0, 50, 0, 0);
            textName.setGravity(Gravity.CENTER);
            textName.setTextSize(80);
            textName.setLayoutParams(lp);
            ((FrameLayout) this.getParent()).addView(textName);
        }

        textName.setText(LetterName);
    }

    public void setBraille(Canvas canvas, int[][] BrailleMatrix) {
        int Row = 3; // 점자는 3행으로 이루어짐
        int Col = BrailleMatrix[0].length;
        initCoordinateXY(Row, Col); // 점자의 수만큼 행렬 초기화

        for (int i = 0; i < Row; i++) {
            float coordinate_Y = getCoordinate_Y((Row-1) - i); // Target 점자의 Y 좌표값 setting
            for (int j = 0; j < Col; j++) {
                float coordinate_X = getCoordinate_X(j); // Target 점자의 X 좌표값 setting
                int TargetBraille = BrailleMatrix[i][j]; // Target 점자 돌출 유무  1 or 0
                float radius = getRadius(TargetBraille); // 점자 돌출 유무에 따른 원의 크기 설정
                canvas.drawCircle(coordinate_X, coordinate_Y, radius, paint); // 점자 그리기
                CopyCoordinate(i, j, coordinate_X, coordinate_Y, TargetBraille); // 그린 점자 좌표값 행렬에 저장
            }
        }
    }

    public void CopyCoordinate(int Row, int Col, float X, float Y, int TargetBraille){
        Coordinate_XY[Row][Col] = new Coordinate();
        Coordinate_XY[Row][Col].setX(X);
        Coordinate_XY[Row][Col].setY(Y);

        if(TargetBraille==BIG_CIRCLE)
            Coordinate_XY[Row][Col].setTarget(true);
    }

    public void initCoordinateXY(int Row, int Col){
        Coordinate_XY = new Coordinate[Row][Col];
    }

    public float getRadius(int Braille){
        if(Braille == 0)
            return Global.DisplayY*MiniCircleRadiusRatio;
        else
            return Global.DisplayY*BigCircleRadiusRatio;
    }

    public float getCoordinate_Y(int Row){
        // 1 - (점자 원의 크기 * 행(점자 갯수) + 점자의 반지름) = 점자의 좌표
        return Global.DisplayY * (1 - ((2 * BigCircleRadiusRatio * Row) + BigCircleRadiusRatio));
    }

    public float getCoordinate_X(int Col){
        // 점자의 띄어쓰기 + 점자 원의 크기 * 열(점자 갯수) + 점자의 반지름 = 점자의 좌표
        return Global.DisplayY*Space*(Col/2) + Global.DisplayY * ((2 * BigCircleRadiusRatio * Col) + BigCircleRadiusRatio);
    }
}


