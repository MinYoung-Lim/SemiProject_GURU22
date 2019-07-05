package com.example.semiproject_guru2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.semiproject_guru2.database.Database;
import com.example.semiproject_guru2.model.MemberModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();  //클래스명 획득
    public static final int VIEW_SAVE = 101;  // 저장화면 식별자

    private static final int MY_PERMISSION_CAMERA = 1111;
    private static final int REQ_TAKE_PHOTO = 2222;
    private static final int REQ_TAKE_ALBUM = 3333;
    private static final int REQ_TAKE_IMAGE_CROP = 4444;

    private ImageView imgSignUp;

    //멤버 변수
    static EditText editID;
    EditText editName;
    EditText editPwd;
    EditText editPwd2;

    Button btnCam;
    Button btnSignUpFinal;

    private String mCurrentImageFilePath = null;
    private Uri mProviderUri = null;
    private Uri mPhotoUri = null;
    private Uri mAlbumUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermission();

        setContentView(R.layout.activity_sign_up);

        imgSignUp = findViewById(R.id.imgSignUp);
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
                if (!editPwd.getText().toString().equals(editPwd2.getText().toString()))
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

        // 사진 찍기 버튼 이벤트
        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureCamera();
            }
        });
    }

    //카메라 작업시작
    private void captureCamera() {
        String state = Environment.getExternalStorageState();

        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = createFileName(); // 저장할 파일
            if (photoFile != null) {
                Uri providerURI = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                mProviderUri = providerURI;
                intent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);

                startActivityForResult(intent, REQ_TAKE_PHOTO);
            }
        }
    }


    // 이미지 파일명 생성
    private File createFileName() {
        // 현재 "년월일 시분초"를 기준으로 파일명 생성
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = timeStamp + ".jpg";

        File myDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "item");
        if (!myDir.exists()) {
            myDir.mkdir();
        }

        File imageFile = new File(myDir, fileName);
        mCurrentImageFilePath = imageFile.getAbsolutePath();

        return imageFile;
    }

    private void checkPermission() {
        // Self 권한 체크
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            // 권한동의 체크
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
            ) {

            } else {
                // 권한동의 팝업 표시 요청
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA}, 1111);
            }
        }
    } // End checkPermission

    // 사용자 권한동의 결과 획득
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case 1111:
                // 0:권한 허용  -1:권한 거부
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] < 0) {
                        Toast.makeText(this, "해당 권한을 활성화하셔야 합니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
        } // End switch
    }

    // 카메라, 앨범등의 처리결과
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode) {

            case REQ_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    imgSignUp.setImageURI(mProviderUri); // 사진촬영한 이미지 설정

                } else {
                    Toast.makeText(this, "사진촬영을 취소하였습니다.", Toast.LENGTH_LONG).show();
                }
                break;
            case REQ_TAKE_ALBUM:
                if (resultCode == Activity.RESULT_OK) {
                    File albumFile = createFileName();
                    mPhotoUri = data.getData();
                    mAlbumUri = Uri.fromFile(albumFile);

                    imgSignUp.setImageURI(mPhotoUri);   // 앨범에서 선택한 이미지 설정
                }
                break;
        } // End switch
    }

}
