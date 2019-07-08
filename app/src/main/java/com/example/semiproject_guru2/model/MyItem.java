package com.example.semiproject_guru2.model;

import java.io.Serializable;

public class MyItem implements Serializable {
    private int imageId;  // 이미지
    private String contents;  //내용


    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public MyItem(){}

    @Override
    public String toString() {
        return "MyItem{" +
                "imageId=" + imageId +
                ", contents='" + contents + '\'' +
                '}';
    }

    public MyItem(int imageId, String contents) {
        this.imageId = imageId;
        this.contents = contents;
    }
}

