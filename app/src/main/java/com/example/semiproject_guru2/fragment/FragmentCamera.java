package com.example.semiproject_guru2.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.semiproject_guru2.R;

public class FragmentCamera extends Fragment {

    //사진이 저장된 단말기상의 실제 경로
    public String mPhotoPath = "/sdcard/hello/world.jpg";  //dummy data

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Fragment UI 생성
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        Button btnMemoCam = view.findViewById(R.id.btnMemoCam);
        ImageView mimgMemo = view.findViewById(R.id.imgMemo);



        return view;


}}