package com.project.why.braillelearning.LearningView;

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

import com.project.why.braillelearning.LearningModel.Dot;
import com.project.why.braillelearning.LearningModel.BrailleData;

/**
 * Created by hyuck on 2017-09-14.
 */

/**
 * 점자를 화면에 출력하는 view class
 * observer로 관찰하는 brailleData를 활용하여 화면에 점자를 그림
 */
public class BasicView extends View implements ViewObservers {
    private Context context;
    private TextView textName;
    private String letterName;
    private Dot[][] brailleMatrix;

    public BasicView(Context context){
        super(context);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas){
        Log.d("ondraw","ondraw");
        drawBraille(canvas);
    }

    public void drawBraille(Canvas canvas){
        if(brailleMatrix != null){
            setTextName();
            setBraille(canvas);
        }
    }

    public void setTextName(){
        if(textName == null) {
            textName = new TextView(context);

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER_HORIZONTAL
            );

            textName.setTextColor(Color.WHITE);
            textName.setPadding(0, 50, 0, 0);
            textName.setGravity(Gravity.CENTER);
            textName.setTextSize(60);
            textName.setLayoutParams(lp);
            ((FrameLayout) this.getParent()).addView(textName);
        }

        textName.setText(letterName);
    }

    public void setBraille(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);


        int col = brailleMatrix.length; // 점자는 4행으로 이루어짐
        int row = brailleMatrix[0].length;

        for (int i=0; i<col; i++) {
            for (int j=0; j<row; j++) {
                float coordinate_X = brailleMatrix[i][j].getX();
                float coordinate_Y = brailleMatrix[i][j].getY();
                int dotType = brailleMatrix[i][j].getDotType();
                float radius = brailleMatrix[i][j].getViewAreaRidus();
                int color = getColor(dotType);
                paint.setColor(color);
                canvas.drawCircle(coordinate_X, coordinate_Y, radius, paint); // 점자 그리기
            }
        }
    }

    /**
     * 점자 번호에 따라 점의 색을 구분
     * @param dotType : 점자 번호
     * @return
     */
    public int getColor(int dotType){
        if(dotType == 7)
            return Color.RED;
        else if(dotType == 8)
            return Color.CYAN;
        else
            return Color.WHITE;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPause() {
        if(textName != null)
            textName.setText("");
        letterName = "";
        brailleMatrix = null;
        textName = null;
    }

    /**
     * brailleData를 callback받는 함수
     * @param letterName : 점자를 의미하는 글자
     * @param brailleMatrix : 점자의 모든 좌표값
     */
    @Override
    public void nodifyBraille(String letterName, Dot[][] brailleMatrix) {
        this.letterName = letterName;
        this.brailleMatrix = brailleMatrix;
        invalidate();
    }
}


