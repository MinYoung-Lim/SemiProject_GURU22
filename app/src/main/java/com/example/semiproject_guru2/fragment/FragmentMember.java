package com.example.semiproject_guru2.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.semiproject_guru2.R;
import com.example.semiproject_guru2.bean.MemberBean;
import com.example.semiproject_guru2.database.FileDB;

import java.io.File;

public class FragmentMember extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member, container, false);

        ImageView imgProfile = view.findViewById(R.id.imgProfile);
        TextView txtMemId = view.findViewById(R.id.edtIdInfo);
        TextView txtMemName = view.findViewById(R.id.edtNameInfo);
        TextView txtMemPw = view.findViewById(R.id.edtPwdInfo);
        TextView txtMemDate = view.findViewById(R.id.txtMemDate);

        //파일DB 에서 가져온다.
        MemberBean memberBean = FileDB.getLoginMember( getActivity() );

        imgProfile.setImageURI( Uri.fromFile(new File(memberBean.photoPath)) );
        txtMemId.setText( "ID : "+ memberBean.memId );
        txtMemName.setText( "이름 : " + memberBean.memName );
        txtMemPw.setText( "비밀번호 : " + memberBean.memPw );
        txtMemDate.setText( "가입날짜 : " + memberBean.memRegDate );

        return view;
    }

}
