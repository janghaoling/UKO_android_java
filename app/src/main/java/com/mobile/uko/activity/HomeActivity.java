package com.mobile.uko.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobile.uko.R;
import com.mobile.uko.fragment.ChatFragment;
import com.mobile.uko.fragment.HomeFragment;
import com.mobile.uko.fragment.NotificationFragment;
import com.mobile.uko.fragment.ProfileFragment;
import com.mobile.uko.utils.SharedHelper;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "HomeActivity";
    private BottomAppBar bottomAppBar;
    private RelativeLayout rl_home, rl_chat, rl_profile, rl_notification;
    private ImageView img_home, img_chat, img_profile, img_notification;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d(TAG, "Access Token in Home:::::" + SharedHelper.getKey(this, "access_token"));
        initUI();

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        fragment = new HomeFragment();
        transaction.add(R.id.main_container, fragment).commit();
    }


    private void initUI() {
        bottomAppBar = (BottomAppBar) findViewById(R.id.bottomAppBar);

        rl_home = (RelativeLayout) findViewById(R.id.homePage);
        rl_chat = (RelativeLayout) findViewById(R.id.chat);
        rl_profile = (RelativeLayout) findViewById(R.id.profile);
        rl_notification = (RelativeLayout) findViewById(R.id.notification);
        img_home = findViewById(R.id.img_home);
        img_chat = findViewById(R.id.img_chat);
        img_profile = findViewById(R.id.img_profile);
        img_notification = findViewById(R.id.img_notification);
        fab = findViewById(R.id.fab_button);

        rl_home.setOnClickListener(this);
        rl_chat.setOnClickListener(this);
        rl_profile.setOnClickListener(this);
        rl_notification.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homePage:
                img_home.setImageResource(R.drawable.ic_home_selected);
                img_chat.setImageResource(R.drawable.ic_chat_unselected);
                img_profile.setImageResource(R.drawable.ic_account_unselected);
                img_notification.setImageResource(R.drawable.ic_notification_unselected);
                fragment = new HomeFragment();
                break;
            case R.id.chat:
                img_home.setImageResource(R.drawable.ic_home_unselected);
                img_chat.setImageResource(R.drawable.ic_chat_selected);
                img_profile.setImageResource(R.drawable.ic_account_unselected);
                img_notification.setImageResource(R.drawable.ic_notification_unselected);
                fragment = new ChatFragment();
                break;
            case R.id.profile:
                img_home.setImageResource(R.drawable.ic_home_unselected);
                img_chat.setImageResource(R.drawable.ic_chat_unselected);
                img_profile.setImageResource(R.drawable.ic_account_selected);
                img_notification.setImageResource(R.drawable.ic_notification_unselected);
                fragment = new ProfileFragment();
                break;
            case R.id.notification:
                img_home.setImageResource(R.drawable.ic_home_unselected);
                img_chat.setImageResource(R.drawable.ic_chat_unselected);
                img_profile.setImageResource(R.drawable.ic_account_unselected);
                img_notification.setImageResource(R.drawable.ic_notification_selected);
                fragment = new NotificationFragment();
                break;
        }
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment).commit();
    }



}