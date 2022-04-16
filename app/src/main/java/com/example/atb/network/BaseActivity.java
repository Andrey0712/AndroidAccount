package com.example.atb.network;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.atb.MainActivity;
import com.example.atb.R;
import com.example.atb.account.LoginActivity;
import com.example.atb.account.RegisterActivity;
import com.example.atb.account.UsersActivity;
import com.google.android.material.navigation.NavigationView;


public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    @Override
    public void setContentView(View view) {
         drawerLayout=(DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout container =drawerLayout.findViewById(R.id.activity_container);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar= drawerLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView=drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,
                drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    protected void setActivityTitle(String title){
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(title);
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(Gravity.START);
        switch (item.getItemId()){
            case R.id.m_login:
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(0,0);
                break;
            case R.id.m_register:
                startActivity(new Intent(this, RegisterActivity.class));
                overridePendingTransition(0,0);
                break;
            case R.id.m_main:
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0,0);
                break;
            case R.id.m_users:
                startActivity(new Intent(this, UsersActivity.class));
                overridePendingTransition(0,0);
                break;
        }

        return false;
    }
}