package com.example.semiproject_guru2.database;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.semiproject_guru2.bean.MemberBean;
import com.example.semiproject_guru2.model.MyItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileDB {
    private static final String FILE_DB = "FileDB";
    private static Gson mGson = new Gson();

    private static SharedPreferences getSP(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_DB, Context.MODE_PRIVATE);
        return sp;
    }

    /** 새로운 멤버추가 */
    public static void addMember(Context context, MemberBean memberBean){
        //1.기존의 멤버 리스트를 불러온다.
        List<MemberBean> memberList = getMemberList(context);
        //2.기존의 멤버 리스트에 추가한다.
        memberList.add(memberBean);
        //3.멤버 리스트를 저장한다.
        String listStr = mGson.toJson(memberList);
        //4.저장한다.
        SharedPreferences.Editor editor = getSP(context).edit();
        editor.putString("memberList", listStr);
        editor.commit();
    }

    public static List<MemberBean> getMemberList(Context context) {
        String listStr = getSP(context).getString("memberList", null);
        //저장된 리스트가 없을 경우에 새로운 리스트를 리턴한다.
        if(listStr == null) {
            return new ArrayList<MemberBean>();
        }
        //Gson 으로 변환한다.
        List<MemberBean> memberList =
                mGson.fromJson(listStr, new TypeToken<List<MemberBean>>(){}.getType() );
        return memberList;
    }

    public static MemberBean getFindMember(Context context, String memId) {
        //1.멤버리스트를 가져온다
        List<MemberBean> memberList = getMemberList(context);
        //2.for 문 돌면서 해당 아이디를 찾는다.
        for(MemberBean bean : memberList) {
            if(TextUtils.equals(bean.memId, memId)) { //아이디가 같다.
                //3.찾았을 경우는 해당 MemberBean 을 리턴한다.
                return bean;
            }
        }
        //3-2.못찾았을 경우는??? null 리턴
        return null;
    }

    //로그인한 MemberBean 을 저장한다.
    public static void setLoginMember(Context context, MemberBean bean) {
        if(bean != null) {
            String str = mGson.toJson(bean);
            SharedPreferences.Editor editor = getSP(context).edit();
            editor.putString("loginMemberBean", str);
            editor.commit();
        }
    }
    //로그인한 MemberBean 을 취득한다.
    public static MemberBean getLoginMember(Context context) {
        String str = getSP(context).getString("loginMemberBean", null);
        if(str == null) return null;
        MemberBean memberBean = mGson.fromJson(str, MemberBean.class);
        return memberBean;
    }



    /****************************** 메모 관련 DB *********************************/

    public final static String TBL_ITEM = "ITEM";

    private static FileDB inst;
    private static SharedPreferences sf = null;  //저장 객체

    private static List<MyItem> items = null;  // 원본데이터

    private FileDB(){}

    public static FileDB getInstance(Context context){

        if (items == null){
            items = new ArrayList<>();
        }
        if(sf == null)
            sf = context.getSharedPreferences("MEMO", Activity.MODE_PRIVATE);

        if(inst == null)
            inst = new FileDB();
        return inst;
    }

    // Item 선두에 추가
    public void addItem(MyItem item){
        items.add(0,item);
    }
    // Item 획득
    public MyItem getItem(int index){
        return items.get(index);
    }
    // Item 변경
    public void setItems(int index, MyItem item){
        items.set(index, item);
    }
    // Item 삭제
    public void removeItem(int index){
        items.remove(index);
    }
    // Items를 SharedPreferences에 저장
    public void saveItem(){
        // 객체를 문자열(Json)로 변경
        String itemString = new GsonBuilder().serializeNulls().create().toJson(items);
        //저장
        SharedPreferences.Editor editor = sf.edit();
        editor.putString(TBL_ITEM, itemString);  //key, value 형식으로 저장
        editor.commit();
    }

    // items 획득
    public List<MyItem> loadItems(){
        // SharedPreferences의 Items정보를 문자열로 획득
        String itemsString = sf.getString(TBL_ITEM, "");
        if(!itemsString.isEmpty()){
            // 문자열을 MyItem 배열형태로 변환
            MyItem[] itemArray = new Gson().fromJson(itemsString, MyItem[].class);
            // 배열을 ArrayList형태로 변환
            items = new ArrayList<>(Arrays.asList(itemArray));
        }
        return items;
    }

}
