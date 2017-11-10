package com.example.teerasaksathu.customers;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyprofileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvName;
    private TextView tvSurname;
    private TextView tvPhonenumber;
    private Button btnEdit;
    private String name;
    private String surname;
    private String phonenumber;
    private String username;
    private String TAG = "UserProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        initInstances();

        btnEdit.setOnClickListener(this);

    }

    private void initInstances() {
        tvName = (TextView) findViewById(R.id.tvName);
        tvSurname = (TextView) findViewById(R.id.tvLastname);
        tvPhonenumber = (TextView) findViewById(R.id.tvTel);
        btnEdit = (Button) findViewById(R.id.btnRepair);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        loadUserData loadUserData = new loadUserData();
        loadUserData.execute(username);



    }

    @Override
    public void onClick(View view) {
        if (view == btnEdit) {
            //TODO Change username
            Intent intent = new Intent(MyprofileActivity.this, EditProfileActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);

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

            tvName.setText(name);
            tvSurname.setText(surname);
            tvPhonenumber.setText(phonenumber);

        }
    }
}
