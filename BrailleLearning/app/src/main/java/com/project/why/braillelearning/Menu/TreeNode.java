package com.project.why.braillelearning.Menu;

import com.project.why.braillelearning.EnumConstant.Menu;
import java.util.ArrayList;

/**
 * Created by User on 2017-09-02.
 */

/**
 * 각각의 메뉴정보를 저장하고 있는 treeNode
 * image id, soundid, menu name을 갖고 있으며, 다수의 자식노드를 갖음.
 */
public class TreeNode {
    private int imageId; // image data id
    private int soundId; // sound data id
    private TreeNode parentTreeNode; // 부모노드
    private ArrayList<TreeNode> childTreeNodeArray; // 자식노드 Array
    private Menu menuName;

    TreeNode(){
        parentTreeNode = null;
        childTreeNodeArray = null;
        menuName = null;
    }

    /**
     * 자식노드 등록 함수
     * 등록된 자식노드를 반환함
     * @param menuName : 자식노드에 저장할 메뉴 이름
     * @param ImageId : 자식노드에 저장할 이미지 file id
     * @param SoundId : 자식 노드에 저장할 음성 file id
     * @return 등록한 자식노드 반환
     */
    public TreeNode addChildTreeNode(Menu menuName, int ImageId, int SoundId){ // 자식노드 등록 함수
        TreeNode ChildTreeNode = new TreeNode(); // 자식노드 생성
        ChildTreeNode.setImageId(ImageId); // 자식노드에 Image data 설정
        ChildTreeNode.setSoundId(SoundId); // 자식노드에 Sound data 설정
        ChildTreeNode.setTreeName(menuName);

        ChildTreeNode.setParentTreeNode(this); // 자식노드의 부모노드를 현재노드로 설정

        if(childTreeNodeArray == null)
            childTreeNodeArray = new ArrayList<>();

        childTreeNodeArray.add(ChildTreeNode); // 현재 노드의 자식노드 Array에 자식노드 등록

        return ChildTreeNode; // 등록한 자식노드 반환
    }

    public void setTreeName(Menu menuName){
        this.menuName = menuName;
    }

    public void setImageId(int imageId){
        this.imageId = imageId;
    }

    public void setSoundId(int soundId){
        this.soundId = soundId;
    }

    public int getImageId(){
        return imageId;
    }

    public int getSoundId(){
        return soundId;
    }

    public void setParentTreeNode(TreeNode ParentTreeNode){
        this.parentTreeNode = ParentTreeNode;
    }

    /**
     * 하위 메뉴 리스트 중 특정 index의 TreeNode를 반환하는 함수
     * @param index
     * @return
     */
    public TreeNode getChildTreeNode(int index){
        if(childTreeNodeArray != null)
            return childTreeNodeArray.get(index);
        else
            return null;
    }

    public Menu getTreeName(){
        return menuName;
    }

    /**
     * 하위 메뉴 리스트의 사이즈를 반환하는 함수
     * @return
     */
    public int getChildTreeNodeListSize(){
        if(childTreeNodeArray == null)
            return 0;
        else
            return childTreeNodeArray.size();
    }


}
