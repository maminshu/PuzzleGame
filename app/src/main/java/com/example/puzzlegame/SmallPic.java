package com.example.puzzlegame;

// 声明一个类，用于存放每一个小图片对应的信息
public class SmallPic {
    private int rowNo;
    private int colNo;
    private String resourceName;

    // 构造函数，利用属性变量进行初始化
    public SmallPic(int rowNo, int colNo, String resourceName){
        this.rowNo = rowNo;
        this.colNo = colNo;
        this.resourceName = resourceName;
    }

    // 构建函数，利用一个已有的对象进行初始化
    public SmallPic(SmallPic smallPic){
        this.rowNo = smallPic.rowNo;
        this.colNo = smallPic.colNo;
        this.resourceName = smallPic.resourceName;
    }

    // 单独设置属性值的函数
    public void setRowNo(int rowNo){
        this.rowNo = rowNo;
    }

    public void setColNo(int colNo){
        this.colNo = colNo;
    }

    public void setResourceName(String resourceName){
        this.resourceName = resourceName;
    }

    // 获取属性值的函数
    public int getRowNo(){
        return rowNo;
    }

    public int getColNo(){
        return colNo;
    }

    public String getResourceName(){
        return resourceName;
    }
}
