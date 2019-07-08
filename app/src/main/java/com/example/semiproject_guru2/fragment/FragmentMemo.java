
package com.example.semiproject_guru2.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.semiproject_guru2.activity.NewMemoActivity;
import com.example.semiproject_guru2.R;

public class FragmentMemo extends Fragment {

    public ListView mLstMemo;


    public FragmentMemo(){}
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Fragment UI 생성
        View view = inflater.inflate(R.layout.fragment_memo, container, false);

        Button btnNewMemo = view.findViewById(R.id.btnNewMemo);
        btnNewMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //새메모 화면으로 이동
                Intent intent = new Intent(view.getContext(), NewMemoActivity.class);
                startActivity(intent);
            }
        });

        mLstMemo = view.findViewById(R.id.lstMemo);


        return view;
    }
}