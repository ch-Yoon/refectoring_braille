package com.project.why.braillelearning;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by hyuck on 2017-08-28.
 */

public class MenuImageManager extends ImageIdManager {
    /*
     * 메뉴의 image를 tree 형태로 관리하는 class
     * MenuImageIdManger class로부터 id를 가져와 tree로 구현
     */

    private TreeItem RootMenuItem = new TreeItem(); // Root Menu Item;

    // 메뉴 이미지를 얻어오기 위한 Deque
    MenuImageManager(){
        setMenu();
    }

    public void setMenu(){
        Deque<Integer> AdressDeque = new LinkedList<>();
        RootMenuItem.setSubMenuList(setMenu(super.getImageId(AdressDeque), AdressDeque));
    }

    public ArrayList<TreeItem> setMenu(int Menu[], Deque<Integer> deque){ // image 들을 트리 구조로 구성해주는 재귀함수
        ArrayList<TreeItem> MenuArray = new ArrayList<>();
        Deque<Integer> AdressDeque = DequeCopy(deque);

        for(int i=0 ; i<Menu.length ; i++){
            AdressDeque.addLast(i); // 현재 메뉴 index를 deque에 저장
            TreeItem item = new TreeItem();
            item.setImageId(Menu[i]); //item class에 image id 저장
            int SubMenuImageId[] = super.getImageId(AdressDeque); // super class로부터 image id 값 탐색
            if(SubMenuImageId != null)
                item.setSubMenuList(setMenu(SubMenuImageId, AdressDeque)); // 현재 메뉴의 하위메뉴를 set
            MenuArray.add(item);
            AdressDeque.removeLast(); // 직전에 저장한 index를 deque로부터 삭제
        }

        return MenuArray;
    }

    /*
     * 현재 내가 위치하고 있는 메뉴 리스트의 길이를 구하는 함수
     * RootMenuItem의 SubMenu가 대 메뉴
     */
    public int getMenuListSize(Deque<Integer> deque){
        Deque<Integer> MenuAdreeDeque = DequeCopy(deque);

        if(!MenuAdreeDeque.isEmpty())
            MenuAdreeDeque.removeLast();
        TreeItem TargetMenuItem = getMenuImageItem(MenuAdreeDeque);

        return TargetMenuItem.getSubMenuListSize();
    }

    /*
     * Deque안에 있는 메뉴 index 정보를 통해 MenuImageItem을 탐색하는 함수
     * RootMenuItem의 SubMenu가 대 메뉴
     */
    public TreeItem getMenuImageItem(Deque<Integer> deque){
        Deque<Integer> MenuAdressDeque = DequeCopy(deque);

        TreeItem TargetMenuItem = RootMenuItem;
        while(!MenuAdressDeque.isEmpty()){
            TargetMenuItem = TargetMenuItem.getSubMenuImageItem(MenuAdressDeque.pollFirst());
        }

        return TargetMenuItem;
    }

    public Deque<Integer> DequeCopy(Deque<Integer> Deque){ //Deque copy 함수
        Deque<Integer> CopyDeque = new LinkedList<>();
        CopyDeque.addAll(Deque);

        return CopyDeque;
    }


}




