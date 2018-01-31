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
    private int ImageId; // image data id
    private int SoundId; // sound data id
    private TreeNode ParentTreeNode; // 부모노드
    private ArrayList<TreeNode> ChildTreeNodeArray; // 자식노드 Array
    private Menu menuName;

    TreeNode(){
        ParentTreeNode = null;
        ChildTreeNodeArray = null;
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

        if(ChildTreeNodeArray == null)
            ChildTreeNodeArray = new ArrayList<>();

        ChildTreeNodeArray.add(ChildTreeNode); // 현재 노드의 자식노드 Array에 자식노드 등록

        return ChildTreeNode; // 등록한 자식노드 반환
    }

    public void setTreeName(Menu menuName){
        this.menuName = menuName;
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

    /**
     * 하위 메뉴 리스트 중 특정 index의 TreeNode를 반환하는 함수
     * @param index
     * @return
     */
    public TreeNode getChildTreeNode(int index){
        if(ChildTreeNodeArray != null)
            return ChildTreeNodeArray.get(index);
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
        if(ChildTreeNodeArray == null)
            return 0;
        else
            return ChildTreeNodeArray.size();
    }


}
