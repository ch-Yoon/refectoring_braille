package com.project.why.braillelearning.Menu;

import java.util.ArrayList;

/**
 * Created by User on 2017-09-02.
 */

public class TreeNode {
    private int ImageId; // image data id
    private int SoundId; // sound data id
    private TreeNode ParentTreeNode; // 부모노드
    private ArrayList<TreeNode> ChildTreeNodeArray; // 자식노드 Array

    TreeNode(){
        ParentTreeNode = null;
        ChildTreeNodeArray = null;
    }

    public TreeNode addChildTreeNode(int ImageId, int SoundId){ // 자식노드 등록 함수
        TreeNode ChildTreeNode = new TreeNode(); // 자식노드 생성
        ChildTreeNode.setImageId(ImageId); // 자식노드에 Image data 설정
        ChildTreeNode.setSoundId(SoundId); // 자식노드에 Sound data 설정

        ChildTreeNode.setParentTreeNode(this); // 자식노드의 부모노드를 현재노드로 설정

        if(ChildTreeNodeArray == null)
            ChildTreeNodeArray = new ArrayList<>();

        ChildTreeNodeArray.add(ChildTreeNode); // 현재 노드의 자식노드 Array에 자식노드 등록

        return ChildTreeNode; // 등록한 자식노드 반환
    }

    public void setImageId(int ImageId){
        this.ImageId = ImageId;
    }

    public void setSoundId(int SoundId){
        this.SoundId = SoundId;
    }

    public int getImageId(){
        return ImageId;
    }

    public int getSoundId(){
        return SoundId;
    }

    public void setParentTreeNode(TreeNode ParentTreeNode){
        this.ParentTreeNode = ParentTreeNode;
    }

    public TreeNode getParentTreeNode(){ // 부모 노드를 반환하는 함수
        return ParentTreeNode;
    }

    public TreeNode getChildTreeNode(int index){ // 하위 메뉴 리스트 중 특정 index의 TreeNode를 반환하는 함수
        if(ChildTreeNodeArray != null)
            return ChildTreeNodeArray.get(index);
        else
            return null;
    }

    public ArrayList<TreeNode> getChildTreeNodeArray(){
        return ChildTreeNodeArray;
    }

    public int getChildTreeNodeListSize(){ // 하위 메뉴 리스트의 사이즈를 반환하는 함수
        if(ChildTreeNodeArray == null)
            return 0;
        else
            return ChildTreeNodeArray.size();
    }


}
