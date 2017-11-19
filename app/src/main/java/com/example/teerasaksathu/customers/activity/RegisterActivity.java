package com.example.teerasaksathu.customers.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.teerasaksathu.customers.R;
import com.example.teerasaksathu.customers.fragment.RegisterFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button conflButtonrm;
    EditText editTextID_card, editText_Name, editText_Surname, editText_Phone,
            editText_Username, editText_Password, editText_conflrmPassWord;
    String ID_CardString, nameString, surNameString,
            phoneString, usernameString, passwordString, conlrmPassWordString;
    private String filePath;
    private StorageReference mStorageRef;
    private Button btnUploadImage;
    private ImageView ivImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.contentContainer, RegisterFragment.newInstance())
//                    .commit();
//        }

        initInstances();
    }

    private void initInstances() {
        ivImg = findViewById(R.id.ivImg);
        conflButtonrm = findViewById(R.id.btncomflrmregister);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        editTextID_card = findViewById(R.id.edidcard);
        editText_Name = findViewById(R.id.edname);
        editText_Surname = findViewById(R.id.edsurname);
        editText_Phone = findViewById(R.id.edphone);
        editText_Username = findViewById(R.id.edusername);
        editText_Password = findViewById(R.id.edpassword);
        editText_conflrmPassWord = findViewById(R.id.edcomfirm);


        conflButtonrm.setOnClickListener(this);
        btnUploadImage.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 1) && (resultCode == RESULT_OK)) {
            Log.d("MyFrienfV1 ", "Result ==>OK");

            //หา path รูป
            Uri uri = data.getData();
            filePath = myFindPathImage(uri);
            Log.d("MyFrienfV1", "imagePathString ==>" + filePath);
            //result Complete

            //Setup Image to ImageView
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                ivImg.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }//try


        }//if
    }

    private String myFindPathImage(Uri uri) {


        String strResult = null;
        String[] strings = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, strings, null, null, null);
        if (cursor != null) {

            cursor.moveToFirst();
            int intIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            strResult = cursor.getString(intIndex);

        } else {

            strResult = uri.getPath();

        }
        return strResult;
    }//myFindPathImage


    @Override
    public void onClick(View view) {

        if (view == conflButtonrm) {
            checkvalue();
        } else if (view == btnUploadImage) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "โปรดเลือกรูป"), 1);

        }

    }

    private void checkvalue() {

        ID_CardString = editTextID_card.getText().toString().trim();
        nameString = editText_Name.getText().toString().trim();
        surNameString = editText_Surname.getText().toString().trim();
        phoneString = editText_Phone.getText().toString().trim();
        usernameString = editText_Username.getText().toString().trim();
        passwordString = editText_Password.getText().toString().trim();
        conlrmPassWordString = editText_conflrmPassWord.getText().toString().trim();


        if (ID_CardString.length() == 0 || nameString.length() == 0 || surNameString.length() == 0 || phoneString.length() == 0 || usernameString.length() == 0 || passwordString.length() == 0 || conlrmPassWordString.length() == 0) {
            Toast.makeText(RegisterActivity.this, "กรอกให้ครบทุกช่อง", Toast.LENGTH_SHORT).show();
        } else {
            if (ID_CardString.length() != 13) {
                Toast.makeText(RegisterActivity.this, "กรอกเลขบัตรประจำตัวประชาชนให้ครบ 13 หลัก", Toast.LENGTH_SHORT).show();
            } else {
                if (passwordString.length() < 8) {
                    Toast.makeText(RegisterActivity.this, "กรอก password อย่างน้อย 8 ตัว", Toast.LENGTH_SHORT).show();
                } else {
                    if (passwordString.equals(conlrmPassWordString)) {

                        if (phoneString.length() != 10) {
                            Toast.makeText(RegisterActivity.this, "กรอกหมายเลขโทรศัพท์ให้ครบ 10 หลัก", Toast.LENGTH_SHORT).show();
                        } else {


                            Register register = new Register();
                            register.execute(ID_CardString, nameString, surNameString, phoneString, usernameString, passwordString);

                            if (filePath != null) {
                                Log.d("UploadImage", "Image has a path");
                                mStorageRef = FirebaseStorage.getInstance().getReference();
                                Uri file = Uri.fromFile(new File(filePath));
                                StorageReference riversRef = mStorageRef.child("Merchants/" + usernameString);


                                riversRef.putFile(file)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                // Get a URL to the uploaded content
                                                Uri pictureUrl = taskSnapshot.getDownloadUrl();
                                                Log.d("UploadImage", "Image uploaded : " + pictureUrl);

                                                UploadProfileImage uploadProfileImage = new UploadProfileImage();
                                                uploadProfileImage.execute(usernameString, pictureUrl.toString());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Handle unsuccessful uploads
                                                // ...
                                                Log.d("UploadImage", "Fail to upload image");

                                            }
                                        });

                            }


                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "password ไม่ตรงกัน", Toast.LENGTH_SHORT).show();
                    }//password กับ comflempassword ต้องตรงกัน
                }//passwordต้องไม่น้อยกว่า 8
            }//บัตรประชาชนต้องมี 13 หลัก
        }//กรอกให้ครบ


    }

    private class Register extends AsyncTask<String, Void, String> {

        private static final String URLregister = "http://www.jongtalad.com/doc/register_merchants.php";

        @Override
        protected String doInBackground(String... values) {


            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("idCard", values[0])
                        .add("name", values[1])
                        .add("surname", values[2])
                        .add("phonenumber", values[3])
                        .add("username", values[4])
                        .add("password", values[5])
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(URLregister).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();


            } catch (Exception e) {

                Log.d("Register", e.getMessage());
                return e.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Register result", s);
            if (s.equals("1")) {
                Toast.makeText(RegisterActivity.this, "สมัครสมาชิก สำเร็จ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(RegisterActivity.this, "Username นี้มีอยู่ในระบบอยู่แล้ววกรุณาใช้ Username อื่น", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UploadProfileImage extends AsyncTask<String, Void, String> {
        public static final String URL = "http://www.jongtalad.com/doc/upload_profile_image.php";


        @Override
        protected String doInBackground(String... values) {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("username", values[0])
                    .add("pictureUrl", values[1])
                    .build();
            Request request = new Request.Builder()
                    .url(URL)
                    .post(requestBody)
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return "Not Success - code : " + response.code();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error - " + e.getMessage();
            }
        }
    }
}


