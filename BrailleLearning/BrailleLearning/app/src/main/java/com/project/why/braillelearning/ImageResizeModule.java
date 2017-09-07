package com.project.why.braillelearning;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by hyuck on 2017-08-22.
 */

public class ImageResizeModule {

    ImageResizeModule(){
    }

    // Drawable 형태의 이미지를 얻는 메소드
    public Drawable getDrawableImage(Resources res, int raw_id, int width, int height){
        return new BitmapDrawable(res,getBitmapImage(res, raw_id, width, height));
    }

    // Bitmap형태의 이미지를 resize하여 반환받는 메소드
    public Bitmap getBitmapImage(Resources res, int res_id, int width, int height){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 실제로 이미지를 로드하지 않음
        BitmapFactory.decodeResource(res, res_id, options);
        options.inSampleSize = getInSampleSize(options, width, height);
        options.inJustDecodeBounds = false; // 이미지를 실제로 로드

        return BitmapFactory.decodeResource(res, res_id, options);
    }

    // 이미지 용량을 몇배로 줄일건지 결정하는 함수
    public int getInSampleSize(BitmapFactory.Options options, int realWidth, int realHeight){
        int inSampleSize = 1; // 따로 지정하지 않는다면 기본적으로 1
        int height = options.outHeight;
        int width = options.outWidth;

        if(height > realHeight || width > realWidth){
            while((height/inSampleSize)>realHeight && (width/inSampleSize)>realWidth){
                inSampleSize *= 2; //2의 지수만큼 해상도를 줄일 때 속도가 빠름
            }
        }

        return inSampleSize;
    }
}
