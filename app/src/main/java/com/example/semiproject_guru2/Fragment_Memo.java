package com.example.semiproject_guru2;

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

public class Fragment_Memo extends Fragment {
public Fragment_Memo(){}
    @Override

        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            // Fragment UI 생성
            View view = inflater.inflate(R.layout.activity_fragment__memo, container, false);

            Button btnNewMemo = view.findViewById(R.id.btn_NewMemo);
            btnNewMemo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), NewMemo.class);
                    startActivity(intent);
                }
            });
            return view;
    }
}
