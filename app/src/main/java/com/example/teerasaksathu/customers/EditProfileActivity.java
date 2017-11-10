package com.example.teerasaksathu.customers;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName;
    private EditText etSurname;
    private EditText etPhonenumber;
    private Button btnEditConfirm;
    private String username;
    private String name;
    private String surname;
    private String phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initInstances();

        btnEditConfirm.setOnClickListener(this);
    }

    private void initInstances() {
        etName = (EditText) findViewById(R.id.etName);
        etSurname = (EditText) findViewById(R.id.etSurname);
        etPhonenumber = (EditText) findViewById(R.id.etPhonenumber);
        btnEditConfirm = (Button) findViewById(R.id.btnEditConfirm);


        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        loadUserData loadUserData = new loadUserData();
        loadUserData.execute(username);
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
                Intent intent = new Intent(EditProfileActivity.this, MyprofileActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            } else {
                Toast.makeText(EditProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
