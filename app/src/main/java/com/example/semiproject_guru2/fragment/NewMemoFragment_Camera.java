package com.example.semiproject_guru2.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.semiproject_guru2.R;

public class NewMemoFragment_Camera extends Fragment {

    public NewMemoFragment_Camera() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Fragment UI 생성
        View view = inflater.inflate(R.layout.activity_new_memo_fragment__camera, container, false);

        return view;
    }
}