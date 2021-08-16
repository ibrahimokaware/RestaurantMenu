package com.example.restaurantmenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    private Animation anim;
    private ImageView imageView;
    private TextView textView;

    MyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        // Create the animation.
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // show activity after splash end
                database = new MyDatabase(getApplicationContext());
                ArrayList<User> users = database.getLoggedUsers();
                if (users.size() != 0)   //  check logged users
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));   //  user logged
                else startActivity(new Intent(getApplicationContext(), SignInActivity.class));  //   user not logged
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        imageView.startAnimation(anim);
        textView.startAnimation(anim);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        database = new MyDatabase(getApplicationContext());
        ArrayList<User> users = database.getLoggedUsers();
        if (users.size() != 0)   //  check users logged
            startActivity(new Intent(getApplicationContext(), MainActivity.class));   //  move to main
        else startActivity(new Intent(getApplicationContext(), SignInActivity.class));   //  move to sign in
    }
}