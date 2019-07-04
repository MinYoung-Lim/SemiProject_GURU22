package com.example.semiproject_guru2.database;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.semiproject_guru2.model.MemberModel;
import com.example.semiproject_guru2.model.MyItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
public class Database {

    public final static String TBL_ITEM = "ITEM";

    private static Database inst;
    private static SharedPreferences sf = null;  //저장 객체

    private static List<MyItem> items = null;  // 원본데이터

    private Database(){}

    public static Database getInstance(Context context){

        if (items == null){
            items = new ArrayList<>();
        }
        if(sf == null)
            sf = context.getSharedPreferences("MEMO", Activity.MODE_PRIVATE);

        if(inst == null)
            inst = new Database();
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

    //회원 저장
    public void setMember(MemberModel memberModel){
        //com.example.project_03.model2.MemberModel 객체를 Json형태의 문자열로 변환
        String memberString = new GsonBuilder().serializeNulls().create().toJson(memberModel);
        Log.d("Database", "memberString=" + memberString);

        //저장
        SharedPreferences.Editor editor = sf.edit();
        editor.putString(memberModel.getId(), memberString);  //key, value 형식으로 저장
        editor.commit();
    }
    //회원 조회
    public MemberModel getMember(String id){
        MemberModel member;
        member = null;
        //id를 이용해 회원정보 획득
        String memberString = sf.getString(id, "");
        if(memberString != null) {
            member = new Gson().fromJson(memberString, MemberModel.class);
        } else
            Log.e("Database", "member null");
        return member;
    }

    //회원의 비밀번호 체크
    public boolean checkMember(String id, String pwd){
        boolean isTrue = false;

        if(id.isEmpty() || pwd.isEmpty())
            return isTrue;
        //회원 획득
        MemberModel member = getMember(id);
        if(member == null)
            return isTrue;  //실패
        if(member.getPwd().equals(pwd))
            isTrue = true;  //성공
        return isTrue;
    }


}


