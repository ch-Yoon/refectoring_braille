package com.project.why.braillelearning;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-08-29.
 */

public class TreeItem extends ImageResizeModule{
    /*
     * 각 메뉴 별 이미지 item class
     */

    private int ItemId; // 이미지 id
    private ArrayList<TreeItem> mSubMenuList = null; // 하위 메뉴 목록 array

    TreeItem(){
        super();
    }

    public void setImageId(int IamgeId){
        ItemId = IamgeId;
    }

    public int getSubMenuListSize(){ // 하위 메뉴리스트의 길이를 반환하는 함수
        if(mSubMenuList==null)
            return 0;
        else
            return mSubMenuList.size();
    }

    public TreeItem getSubMenuImageItem(int index){ // 하위 메뉴 리스트 중 특정 index의 MenuImageItem을 반환하는 함수
        if(mSubMenuList!=null)
            return mSubMenuList.get(index);
        else
            return null;
    }

    public void setSubMenuList(ArrayList<TreeItem> SubMenu){ // 하위 메뉴 목록 setting
        mSubMenuList = new ArrayList<>();
        mSubMenuList.addAll(SubMenu);
    }

    public Drawable getDrawableImage(Resources res, int width, int height){ // 리사이징된 이미지 리턴 함수
        return super.getDrawableImage(res, ItemId, width, height);
    }
}
