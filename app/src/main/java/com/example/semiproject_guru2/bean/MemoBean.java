package com.example.semiproject_guru2.bean;

public class MemoBean {

    public static int memoID;  //고유 번호
    public static String memoPicPath;
    public String memo;
    public String memoDate;


    public static int getMemoID() {
        return memoID;
    }

    public static void setMemoID(int memoID) {
        MemoBean.memoID = memoID;
    }

    public static String getMemoPicPath() {
        return memoPicPath;
    }

    public void setMemoPicPath(String memoPicPath) {
        this.memoPicPath = memoPicPath;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMemoDate() {
        return memoDate;
    }

    public void setMemoDate(String memoDate) {
        this.memoDate = memoDate;
    }
}
