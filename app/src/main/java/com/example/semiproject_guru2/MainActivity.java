package com.example.semiproject_guru2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.semiproject_guru2.database.Database;
import com.example.semiproject_guru2.model.MemberModel;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();  //클래스명 획득

    EditText editID_Login;
    EditText editPwd_Login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btnSignup = findViewById(R.id.btn_signup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);

            }
        });

        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainTabActivity.class);

                //입력 필드
               EditText editID_Login = findViewById(R.id.editID_Login);
               EditText editPwd_Login = findViewById(R.id.editPwd_Login);

                MemberModel member = new MemberModel();
                Database db = Database.getInstance(getApplicationContext());
                db.setMember(member);
                MemberModel savedMember = db.getMember(editID_Login.getText().toString());
                Log.e(TAG, "savedMember=" + savedMember.toString());
                //ID, PWD 체크
                boolean check = db.checkMember(editID_Login.getText().toString(), editPwd_Login.getText().toString());
                if(check) {
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "로그인 되었습니다!", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "아이디나 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
