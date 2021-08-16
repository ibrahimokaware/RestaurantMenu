package com.example.restaurantmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MyDatabase database;

    private Toolbar toolbar;
    private CardView mealsCV, sandwichesCV, appetizersCV, drinksCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new MyDatabase(this);

        mealsCV = findViewById(R.id.cv_meals);
        sandwichesCV = findViewById(R.id.cv_sandwiches);
        appetizersCV = findViewById(R.id.cv_appetizers);
        drinksCV = findViewById(R.id.cv_drinks);
        toolbar = findViewById(R.id.tb_main);

        setSupportActionBar(toolbar);

        mealsCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MealActivity.class).putExtra("type", getString(R.string.meals)));
            }
        });

        sandwichesCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MealActivity.class).putExtra("type", getString(R.string.sandwiches)));
            }
        });

        appetizersCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MealActivity.class).putExtra("type", getString(R.string.appetizers)));
            }
        });

        drinksCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MealActivity.class).putExtra("type", getString(R.string.drinks)));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                startActivity(new Intent(getApplicationContext(), MealActivity.class).putExtra("type", getString(R.string.cart)));
                break;
            case R.id.item2:
                ArrayList<User> users = database.getLoggedUsers();   // get users from database
                User user = users.get(0);   // get logged user
                database.updateLogin(user.getEmail(), 0);   // update user login information
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}