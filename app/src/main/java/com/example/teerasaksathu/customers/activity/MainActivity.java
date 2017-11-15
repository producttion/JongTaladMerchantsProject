package com.example.teerasaksathu.customers.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.teerasaksathu.customers.R;
import com.example.teerasaksathu.customers.fragment.MarketListFragment;
import com.example.teerasaksathu.customers.fragment.MyProfileFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    public static Intent intentUsername;
    private Button btnMarketList;
    private Button btnProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, MarketListFragment.newInstance())
                    .commit();
        }

        initInstances();


    }


    public void initInstances() {
        intentUsername = getIntent();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,
                drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setTitle("จองตลาด");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnMarketList = findViewById(R.id.btnMarketList);
        btnProfile = findViewById(R.id.btnProfile);

        btnMarketList.setOnClickListener(this);
        btnProfile.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contentContainer);

        if (view == btnMarketList) {
            if (!(fragment instanceof MarketListFragment)) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.from_right, R.anim.to_left,
                                R.anim.from_left, R.anim.to_right
                        )
                        .replace(R.id.contentContainer, MarketListFragment.newInstance())
                        .commit();
                drawerLayout.closeDrawers();
            }
        } else if (view == btnProfile) {
            if (!(fragment instanceof MyProfileFragment)) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.from_right, R.anim.to_left,
                                R.anim.from_left, R.anim.to_right
                        )
                        .replace(R.id.contentContainer, MyProfileFragment.newInstance())
                        .commit();
                drawerLayout.closeDrawers();
            }
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
