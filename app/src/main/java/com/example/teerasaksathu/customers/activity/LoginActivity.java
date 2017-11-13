package com.example.teerasaksathu.customers.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.teerasaksathu.customers.R;
import com.example.teerasaksathu.customers.fragment.LoginFragment;


public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, LoginFragment.newInstance())
                    .commit();
        }
    }
}
