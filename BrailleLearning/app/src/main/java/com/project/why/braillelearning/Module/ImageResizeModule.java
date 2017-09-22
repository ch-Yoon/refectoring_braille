package com.project.why.braillelearning.Module;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by hyuck on 2017-08-22.
 */

public class ImageResizeModule {
    /*
     * image Resize class
     *
     */

    private Resources Res;

    public ImageResizeModule(Resources res){
        this.Res = res;
    }

    // Drawable 형태의 이미지를 얻는 메소드
    public Drawable getDrawableImage(int raw_id, int width, int height){
        return new BitmapDrawable(Res, getBitmapImage(raw_id, width, height));
    }

    // Bitmap형태의 이미지를 resize하여 반환받는 메소드
    public Bitmap getBitmapImage(int res_id, int width, int height){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 실제로 이미지를 로드하지 않음
        BitmapFactory.decodeResource(Res, res_id, options);
        options.inSampleSize = getInSampleSize(options, width, height);
        options.inJustDecodeBounds = false; // 이미지를 실제로 로드

        return BitmapFactory.decodeResource(Res, res_id, options);
    }

    // 이미지 용량을 몇배로 줄일건지 결정하는 함수
    public int getInSampleSize(BitmapFactory.Options options, int real_width, int real_height){
        int inSampleSize = 2; // 따로 지정하지 않는다면 기본적으로 1
        int virtual_height = options.outHeight;
        int virtual_width = options.outWidth;

        if(virtual_height > real_height || virtual_width > real_width){
            while((virtual_height/inSampleSize)>real_height && (virtual_width/inSampleSize)>real_width){
                inSampleSize *= 2; //2의 지수만큼 해상도를 줄일 때 속도가 빠름
            }
        }

        return inSampleSize;
    }
}
