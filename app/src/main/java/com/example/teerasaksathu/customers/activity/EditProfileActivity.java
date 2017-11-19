package com.example.teerasaksathu.customers.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import com.example.teerasaksathu.customers.fragment.EditProfileFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static Intent intentUsername;
    private EditText etName;
    private EditText etSurname;
    private EditText etPhonenumber;
    private Button btnEditConfirm;
    private Button btnUploadImage;
    private String username;
    private String name;
    private String surname;
    private String phonenumber;
    private String pictureUrl;
    private String filePath;
    private ImageView ivImg;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

//        if(savedInstanceState == null){
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.contentContainer, EditProfileFragment.newInstance())
//                    .commit();
//        }
        initInstances();


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

    private void initInstances() {
        mStorageRef = FirebaseStorage.getInstance().getReference();
        intentUsername = getIntent();
        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etPhonenumber = findViewById(R.id.etPhonenumber);
        btnEditConfirm = findViewById(R.id.btnEditConfirm);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        ivImg = findViewById(R.id.imageView);
        username = EditProfileActivity.intentUsername.getStringExtra("username");
        loadUserData loadUserData = new loadUserData();
        loadUserData.execute(username);

        btnEditConfirm.setOnClickListener(this);
        btnUploadImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnEditConfirm) {
            String name = etName.getText().toString().trim();
            String surname = etSurname.getText().toString().trim();
            String phonenumber = etPhonenumber.getText().toString().trim();

            if (filePath != null) {
                Log.d("UploadImage", "Image has a path");
                mStorageRef = FirebaseStorage.getInstance().getReference();
                Uri file = Uri.fromFile(new File(filePath));
                StorageReference riversRef = mStorageRef.child("Merchants/" + username);

                riversRef.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                Uri pictureUrl = taskSnapshot.getDownloadUrl();
                                Log.d("UploadImage", "Image uploaded : " + pictureUrl);

                                UploadProfileImage uploadProfileImage = new UploadProfileImage();
                                uploadProfileImage.execute(username, pictureUrl.toString());
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
            EditUserData editUserData = new EditUserData();
            editUserData.execute(username, name, surname, phonenumber);

        }
        if (view == btnUploadImage) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "โปรดเลือกรูป"), 1);

        }

    }


    private class loadUserData extends AsyncTask<String, Void, String> {
        public static final String URL = "http://www.jongtalad.com/doc/load_user_data.php";


        @Override
        protected String doInBackground(String... values) {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("username", values[0])
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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    name = jsonObject.getString("name");
                    surname = jsonObject.getString("surname");
                    phonenumber = jsonObject.getString("phonenumber");
                    pictureUrl = jsonObject.getString("picture_url");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            etName.setText(name);
            etSurname.setText(surname);
            etPhonenumber.setText(phonenumber);
            try {

                Picasso.with(EditProfileActivity.this)
                        .load(pictureUrl)
                        .placeholder(R.drawable.placeholder)
                        .into(ivImg);

            } catch (IllegalArgumentException e) {

                Picasso.with(EditProfileActivity.this)
                        .load(R.drawable.pc)
                        .into(ivImg);
            }

        }
    }

    private class EditUserData extends AsyncTask<String, Void, String> {
        public static final String URL = "http://www.jongtalad.com/doc/edit_user_data.php";


        @Override
        protected String doInBackground(String... values) {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("username", values[0])
                    .add("name", values[1])
                    .add("surname", values[2])
                    .add("phonenumber", values[3])
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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.trim().equals("1")) {
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            } else {
                Toast.makeText(EditProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.trim().equals("1")) {
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            } else {
                Toast.makeText(EditProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
