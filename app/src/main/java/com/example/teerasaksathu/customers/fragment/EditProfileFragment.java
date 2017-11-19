package com.example.teerasaksathu.customers.fragment;

import android.content.ContentProvider;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.teerasaksathu.customers.R;
import com.example.teerasaksathu.customers.activity.EditProfileActivity;
import com.example.teerasaksathu.customers.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;


public class EditProfileFragment extends Fragment implements View.OnClickListener {
    private EditText etName;
    private EditText etSurname;
    private EditText etPhonenumber;
    private Button btnEditConfirm;
    private String username;
    private String name;
    private String surname;
    private String phonenumber;
    private ImageView imageView;

    public EditProfileFragment() {
        super();
    }

    public static EditProfileFragment newInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        etName = rootView.findViewById(R.id.etName);
        etSurname = rootView.findViewById(R.id.etSurname);
        etPhonenumber = rootView.findViewById(R.id.etPhonenumber);
        btnEditConfirm = rootView.findViewById(R.id.btnEditConfirm);
        imageView = rootView.findViewById(R.id.imageView);
        username = EditProfileActivity.intentUsername.getStringExtra("username");
        loadUserData loadUserData = new loadUserData();
        loadUserData.execute(username);

        btnEditConfirm.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore Instance State here
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnEditConfirm) {
            String name = etName.getText().toString().trim();
            String surname = etSurname.getText().toString().trim();
            String phonenumber = etPhonenumber.getText().toString().trim();

            EditUserData editUserData = new EditUserData();
            editUserData.execute(username, name, surname, phonenumber);
        }
        if (view == imageView) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "โปรดเลือกรูป"), 1);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1) && (resultCode == RESULT_OK)) {
            Log.d("MyFrienfV1 ", "Result ==>OK");

            //หา path รูป
            Uri uri = data.getData();
            Log.d("data", String.valueOf(uri));
//            String imagePathString = myFinndPathImage(uri);
//            Log.d("MyFrienfV1", "imagePathString ==>" + imagePathString);
            //result Complete

            //Setup Image to ImageView
            try {
//                Bitmap bitmap = BitmapFactory.decodeStream();
//                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }//try


        }//if
    }

//    private String myFinndPathImage(Uri uri) {
//
//        String strResult = null;
//        String[] strings = {MediaStore.Images.Media.DATA};
//        try (Cursor cursor = getContentResolver().query(uri, strings, null, null, null)) {
//            if (cursor != null) {
//
//                cursor.moveToFirst();
//                int intIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                strResult = cursor.getString(intIndex);
//
//            } else {
//
//                strResult = uri.getPath();
//
//            }
//        }
//
//        return null;
//    }


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
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            etName.setText(name);
            etSurname.setText(surname);
            etPhonenumber.setText(phonenumber);

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
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
