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

import com.project.why.braillelearning.LearningModel.BrailleData;
import com.project.why.braillelearning.LearningControl.Coordinate;
import com.project.why.braillelearning.LearningControl.Data;

/**
 * Created by hyuck on 2017-09-14.
 */

public class BasicView extends View implements ViewObservers {
    private Context context;
    private Data data;
    private TextView textName;
    private float bigCircle;
    private float miniCircle;


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
        if(data != null) {
            String letterName = data.getLetterName();
            Coordinate coordinate_xy[][] = data.getCoordinate_XY();

            setTextName(letterName);
            setBraille(canvas, coordinate_xy);
        }
    }

    public void setTextName(String letterName){
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
            textName.setTextSize(80);
            textName.setLayoutParams(lp);
            ((FrameLayout) this.getParent()).addView(textName);
        }

        textName.setText(letterName);
    }

    public void setBraille(Canvas canvas, Coordinate brailleMatrix[][]) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);


        int row = brailleMatrix.length; // 점자는 3행으로 이루어짐
        int col = brailleMatrix[0].length;

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                float coordinate_X = brailleMatrix[i][j].getX();
                float coordinate_Y = brailleMatrix[i][j].getY();
                int dotType = brailleMatrix[i][j].getDotType();
                float radius = getRadius(dotType);
                int color = getColor(dotType);
                paint.setColor(color);
                canvas.drawCircle(coordinate_X, coordinate_Y, radius, paint); // 점자 그리기
            }
        }
    }

    public int getColor(int dotType){
        if(dotType == -1)
            return Color.CYAN;
        else if(dotType == -2)
            return Color.RED;
        else
            return Color.WHITE;
    }

    public float getRadius(int dotType){
        if(dotType == 0)
            return miniCircle;
        else if( dotType == 1)
            return bigCircle;
        else if(dotType == -1)
            return miniCircle/2;
        else
            return miniCircle/2;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void initCircle(float bigCircle, float miniCircle) {
        this.bigCircle = bigCircle;
        this.miniCircle = miniCircle;
    }

    @Override
    public void nodifyBraille(Data data) {
     //   copyBraille(brailleData);
        this.data = data;
        invalidate();
    }
}


