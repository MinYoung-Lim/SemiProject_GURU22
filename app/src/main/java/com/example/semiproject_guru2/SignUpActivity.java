package com.example.semiproject_guru2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.semiproject_guru2.database.Database;
import com.example.semiproject_guru2.model.MemberModel;

public class SignUpActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();  //클래스명 획득
    public static final int VIEW_SAVE = 101;  // 저장화면 식별자

    static EditText editID;
    EditText editName;
    EditText editPwd;
    EditText editPwd2;

    Button btnCam;
    Button btnSignUpFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //입력 필드
        editID = findViewById(R.id.editID);
        editName = findViewById(R.id.editName);
        editPwd = findViewById(R.id.editPwd);
        editPwd2 = findViewById(R.id.editPwd2);

        //버튼 객체 획득
        btnCam = findViewById(R.id.btn_cam);
        btnSignUpFinal = findViewById(R.id.btn_SignUpFinal);
        // 회원가입 버튼 이벤트
        btnSignUpFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //저장
                Log.d(TAG, "id : " + editID.getText());
                Log.d(TAG, "name : " + editName.getText());
                Log.d(TAG, "pwd : " + editPwd.getText());
                Log.d(TAG, "pwd2 : " + editPwd2.getText());

                // 비밀번호 확인 틀린경우, Toast 메시지
                if(!editPwd.getText().toString().equals(editPwd2.getText().toString()))
                    //Toast 뿌리기
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                else {
                    MemberModel member = new MemberModel();
                    member.setId(editID.getText().toString());
                    member.setName(editName.getText().toString());
                    member.setPwd(editPwd.getText().toString());

                    //저장
                    Database db = Database.getInstance(getApplicationContext());
                    db.setMember(member);
                    finish();
                    Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();


                }
            }
        });
    }
}
