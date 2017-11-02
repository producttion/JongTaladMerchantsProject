package com.example.teerasaksathu.customers;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegisterPage;
    private Button btnLoginPage;
    private TextView tvForgotPassword;
    private EditText etUsername;
    private EditText etPassword;
    private String username;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("logined");
        FirebaseMessaging.getInstance().subscribeToTopic("lockReserved");
        initInstances();
        btnRegisterPage.setOnClickListener(this);
        btnLoginPage.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);


    }

    private void initInstances() {
        btnRegisterPage = (Button) findViewById(R.id.btnRegisterPage);
        btnLoginPage = (Button) findViewById(R.id.btnLoginPage);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

    }

    private class Login extends AsyncTask<String, Void, String> {

        private static final String URL = "http://www.jongtalad.com/doc/login_merchants.php";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Loading...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... values) {
            OkHttpClient okHttpClient = new OkHttpClient();

            RequestBody requestBody = new FormBody.Builder()
                    .add("username", values[0])
                    .add("password", values[1])
                    .build();

            Request.Builder builder = new Request.Builder();
            Request request = builder
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
            Log.d("Post", "==>" + s);
            if (s.equals("1")) {
                Toast.makeText(MainActivity.this, "Login สำเร็จ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, MarketList.class);
                intent.putExtra("username", username);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Username หรือ Password ผิด", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnLoginPage) {
            if (checkLogin()) {
                username = etUsername.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                Login login = new Login();
                login.execute(username, password);

            } else {
                Toast.makeText(MainActivity.this, "โปรดกรอก Username และ Password", Toast.LENGTH_SHORT).show();
            }

        } else if (view == btnRegisterPage) {
            Intent intent = new Intent(getApplicationContext(), RegisterFrom.class);
            startActivity(intent);
        }
    }

    private boolean checkLogin() {
        if (etUsername.length() == 0 || etPassword.length() == 0)
            return false;
        else
            return true;
    }

}
