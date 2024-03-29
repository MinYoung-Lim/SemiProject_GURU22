package com.example.semiproject_guru2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.semiproject_guru2.R;
import com.example.semiproject_guru2.bean.MemberBean;
import com.example.semiproject_guru2.database.FileDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.util.Collections.rotate;

public class CameraCaptureActivity extends AppCompatActivity {

    private ImageView mImgProfile;
    //사진이 저장도니 경로 - onActivityResult()로부터 받는 데이터
    private Uri mCaptureUri;
    //사진이 저장된 단말기상의 실제 경로
    public String mPhotoPath;
    //startActivityForResult() 에 넘겨주는 값, 이 값이 나중에 onActivityResult()로 돌아와서
    //내가 던진값인지를 구별할 때 사용하는 상수이다.
    public static final int REQUEST_IMAGE_CAPTURE = 200;

    //멤버변수
    private EditText mEdtId, mEdtName, mEdtPw1, mEdtPw2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

                    //카메라를 사용하기 위한 퍼미션을 요청한다.
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    }, 0);

                    mImgProfile = findViewById(R.id.imgSignUp);
                    Button btnCamera = findViewById(R.id.btn_cam);
                    //사진찍기 버튼
                    btnCamera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            takePicture();
                        }
                    });

                    //갤러리 연결
                    findViewById(R.id.btnGal).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            //intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, 1);
                        }
                    });

                    mEdtId = findViewById(R.id.editID);
                    mEdtName = findViewById(R.id.editName);
                    mEdtPw1 = findViewById(R.id.editPwd);
                    mEdtPw2 = findViewById(R.id.editPwd2);

                    //회원가입 버튼
                    findViewById(R.id.btn_SignUpFinal).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            joinProcess();
                        }
                    });

        //비밀번호 4자리 이상인지 확인
        mEdtPw1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String mPw = mEdtPw1.getText().toString();
                if(mPw.length() < 4){
                    mEdtPw1.setTextColor(Color.RED);
                }
                else{
                    mEdtPw1.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

                }//end onCreate()

                //회원가입 처리
                private void joinProcess() {

                    MemberBean memberBean = new MemberBean();
                    memberBean.memId = mEdtId.getText().toString();
                    memberBean.memName = mEdtName.getText().toString();
                    memberBean.photoPath = mPhotoPath;

                    String pw1 = mEdtPw1.getText().toString();
                    String pw2 = mEdtPw2.getText().toString();

                    //패스워드가 4자리 이상이어야함
                    if(pw1.length()<4){
                        //4자리 미만이면
                        Toast.makeText(this, "패스워드가 4자리 미만입니다.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    //서로 패스워드가 일치하는지 확인한다.
                    if( !TextUtils.equals(pw1, pw2) ) {
                        //일치하지 않을경우는
                        Toast.makeText(this, "패스워드가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    //아이디가 공백인지 체크한다.
                    if( TextUtils.isEmpty(memberBean.memId) ) {
                        Toast.makeText(this, "회원 아이디를 입력하세요", Toast.LENGTH_LONG).show();
                        return;
                    }
                    //이미 존재하는 회원 아이디를 찾는다.
                    MemberBean findMemBean = FileDB.getFindMember(this, memberBean.memId);
                    if( findMemBean != null ) {
                        //해당되는 아이디를 찾았다.
                        Toast.makeText(this, "입력하신 회원 아이디는 이미 존재 합니다.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    // 사진을 찍지 않았을 때
                    if(mPhotoPath==null){
                        Toast.makeText(this, "사진을 등록해주세요.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    memberBean.memPw = pw1;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    memberBean.memRegDate = sdf.format(new Date());

                    //memberBean 을 파일로 저장한다. ==> JSON 변환후
                    FileDB.addMember(this, memberBean);

                    //회원가입 완료
                    Toast.makeText(this, "회원가입이 완료 되었습니다.", Toast.LENGTH_LONG).show();
                    finish();
                }

                public void takePicture() {

                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        mCaptureUri = Uri.fromFile( getOutPutMediaFile() );
                    } else {
                        mCaptureUri = FileProvider.getUriForFile(this,
                                "com.example.semiproject_guru2", getOutPutMediaFile());
                    }

                    i.putExtra(MediaStore.EXTRA_OUTPUT, mCaptureUri);

                    //내가 원하는 액티비티로 이동하고, 그 액티비티가 종료되면 (finish되면)
                    //다시금 나의 액티비티의 onActivityResult() 메서드가 호출되는 구조이다.
                    //내가 어떤 데이터를 받고 싶을때 상대 액티비티를 호출해주고 그 액티비티에서
                    //호출한 나의 액티비티로 데이터를 넘겨주는 구조이다. 이때 호출되는 메서드가
                    //onActivityResult() 메서드 이다.
                    startActivityForResult(i, REQUEST_IMAGE_CAPTURE);

                }

                private File getOutPutMediaFile() {
                    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "cameraDemo");
                    if(!mediaStorageDir.exists()) {
                        if(!mediaStorageDir.mkdirs()) {
                            return null;
                        }
                    }

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    File file = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

                    mPhotoPath = file.getAbsolutePath();

                    return file;
                }

                private void sendPicture() {
                    Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath);
                    Bitmap resizedBmp = getResizedBitmap(bitmap, 4, 100, 100);

                    bitmap.recycle();

                    //사진이 캡쳐되서 들어오면 뒤집어져 있다. 이애를 다시 원상복구 시킨다.
                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(mPhotoPath);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    int exifOrientation;
                    int exifDegree;
                    if(exif != null) {
                        exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_NORMAL);
                        exifDegree = exifOrientToDegree(exifOrientation);
                    } else {
                        exifDegree = 0;
                    }


                    Bitmap rotatedBmp = roate(resizedBmp, exifDegree);

                    //줄어든 이미지를 다시 저장한다
                    saveBitmapToFileCache(rotatedBmp, mPhotoPath);

                    mImgProfile.setImageBitmap( rotatedBmp );

                    Toast.makeText(this, "사진경로: " + mPhotoPath, Toast.LENGTH_LONG).show();

                }

    private void saveBitmapToFileCache(Bitmap bitmap, String strFilePath) {

        File fileCacheItem = new File(strFilePath);
        OutputStream out = null;

        try
        {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    private int exifOrientToDegree(int exifOrientation) {
                    if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
                        return 90;
                    }
                    else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
                        return 180;
                    }
                    else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
                        return 270;
                    }
                    return 0;
                }

                private Bitmap roate(Bitmap bmp, float degree) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(degree);
                    return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(),
                            matrix, true);
                }

                //비트맵의 사이즈를 줄여준다.
                public static Bitmap getResizedBitmap(Bitmap srcBmp, int size, int width, int height){
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = size;
                    Bitmap resized = Bitmap.createScaledBitmap(srcBmp, width, height, true);
                    return resized;
                }

                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                    super.onActivityResult(requestCode, resultCode, data);

                    if(resultCode == RESULT_OK){
                        switch (requestCode){
                            case 1:
                                sendPicture(data.getData()); //갤러리에서 가져오기
                                break;
                            case REQUEST_IMAGE_CAPTURE:
                                sendPicture();
                                break;
                        }
                    }


//                    //카메라로부터 오는 데이터를 취득한다.
//                    if(resultCode == RESULT_OK) {
//                        if(requestCode == REQUEST_IMAGE_CAPTURE) {
//                            sendPicture();
//                        }
//                    }
                }

                private void sendPicture(Uri imgUri){

                    mPhotoPath = getRealPathFromURI(imgUri);
                    ExifInterface exif = null;
                    try{
                        exif = new ExifInterface(mPhotoPath);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int exifDegree = exifOrientToDegree(exifOrientation);

                    Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath);
                    mImgProfile.setImageBitmap(bitmap);

                }

                private String getRealPathFromURI(Uri contentUri){
                    int column_index = 0;
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
                    if(cursor.moveToFirst()){
                        column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    }
                    return cursor.getString(column_index);
                }




        }

