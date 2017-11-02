package com.example.teerasaksathu.customers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyprofileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvName;
    private  TextView tvSurname;
    private  TextView tvPhonenumber;
    private Button btnRepair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        initInstances();

        btnRepair.setOnClickListener(this);

    }
    private void initInstances(){
            tvName = (TextView) findViewById(R.id.tvName);
            tvSurname = (TextView) findViewById(R.id.tvLastname);
            tvPhonenumber = (TextView) findViewById(R.id.tvTel);
            btnRepair = (Button) findViewById(R.id.btnRepair);
    }

    //TODO Edit userInformation
    @Override
    public void onClick(View view) {
        if (view == btnRepair){
//            Intent intent = new Intent(MyprofileActivity.this,);
//            startActivity(intent);
//
        }
    }
}
