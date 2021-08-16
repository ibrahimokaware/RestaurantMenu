package com.example.restaurantmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MealActivity extends AppCompatActivity {

    private MyDatabase database;
    
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView textView, totalPriceTV;

    private Intent intent;
    private String type;
    private double totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        recyclerView = findViewById(R.id.recyclerView);
        textView = findViewById(R.id.textView);
        totalPriceTV = findViewById(R.id.tv_total_price);

        toolbar = findViewById(R.id.tb_meal);
        setSupportActionBar(toolbar);

        intent = getIntent();
        type = intent.getStringExtra("type");   //  get meal type

        toolbar.setTitle(type);

        // set home icon in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // set icon color
        final Drawable drawable = toolbar.getNavigationIcon();
        drawable.setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.white), PorterDuff.Mode.SRC_ATOP);

        database = new MyDatabase(getApplicationContext());

        // get user id from db
        ArrayList<User> users = database.getLoggedUsers();
        User user = users.get(0);
        int userID = user.getId();

        int cartIcon = R.drawable.ic_add_cart;

        ArrayList<Meal> mealArrayList = addMeals(userID, cartIcon);

        // insert meals data only once
        if (database.getMealsCountForUser(userID) == 0) {   //  no data for this user
            for (Meal meal : mealArrayList)
                database.insertMeal(meal);
        }

        ArrayList<Meal> meals;

        // get data by meal type
        if (type.equals("Cart")) {
            meals = database.getMealsInCart(userID);   //  get cart data
            for (Meal meal : meals)
                totalPrice += Double.parseDouble(meal.getPrice());   //  calculate total price
            totalPriceTV.setText(getString(R.string.total_price)+ totalPrice);
            totalPriceTV.setVisibility(View.VISIBLE);
        } else meals = database.getMeals(type, userID);   //  get meals data

        final MealAdapter mealAdapter = new MealAdapter(meals, type, textView, new OnRVCartIVClickListener() {
            @Override
            public void OnClickListener(Meal meal) {
                if (meal.getCart() == 0) {   //  meal is not in cart
                    database.updateCart(meal.getId(), meal.getUserID(), 1);   //  add meal to cart
                    if (!type.equals("Cart"))   //  show message if you are not in cart page
                        Toast.makeText(getApplicationContext(), "Meal Added Successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    database.updateCart(meal.getId(), meal.getUserID(), 0);   // remove meal from cart
                    if (type.equals("Cart")) {   //  calculate total price if you are in cart page
                        totalPrice -= Double.parseDouble(meal.getPrice());
                        totalPriceTV.setText(getString(R.string.total_price)+ totalPrice);
                    } else Toast.makeText(getApplicationContext(), "Meal Removed Successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // show no items yet text
        if (meals.size() == 0)
            textView.setText(R.string.meals_appear);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mealAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    public ArrayList<Meal> addMeals(int userID, int cartIcon) {
        ArrayList<Meal> mealArrayList = new ArrayList<>();

        //Meals
        mealArrayList.add(new Meal(userID, R.drawable.m1, "Bourbon St Chicken and Shrimp", "Delicious chicken with mix of many ingredients",
                "25.00", getString(R.string.meals), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.m2, "BBQ chicken pizza", "Delicious chicken with mix of many ingredients",
                "16.00", getString(R.string.meals), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.m3, "Chicken Bryan", "Delicious chicken with mix of many ingredients",
                "20.00", getString(R.string.meals), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.m4, "Chicken Alfredo", "Delicious chicken with mix of many ingredients",
                "15.00", getString(R.string.meals), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.m5, "Momma's Pancake Breakfast", "Delicious chicken with mix of many ingredients",
                "8.00", getString(R.string.meals), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.m6, "Ultimate Feast", "Delicious chicken with mix of many ingredients",
                "27.00", getString(R.string.meals), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.m7, "Broccoli Cheddar Soup", "Delicious chicken with mix of many ingredients",
                "5.00", getString(R.string.meals), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.m8, "Rib-Eye Steak", "Delicious chicken with mix of many ingredients",
                "15.00", getString(R.string.meals), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.m9, "Chicken Madeira", "Delicious chicken with mix of many ingredients",
                "20.00", getString(R.string.meals), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.m10, "Mongolian Beef", "Delicious chicken with mix of many ingredients",
                "16.00", getString(R.string.meals), 0, cartIcon));

        // Sandwiches
        mealArrayList.add(new Meal(userID, R.drawable.s1, "Cheeseburger", "Delicious chicken with mix of many ingredients",
                "1.00", getString(R.string.sandwiches), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.s2, "Tuna Sandwich", "Delicious chicken with mix of many ingredients",
                "5.00", getString(R.string.sandwiches), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.s3, "Roast Beef", "Delicious chicken with mix of many ingredients",
                "3.00", getString(R.string.sandwiches), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.s4, "Ham Sandwich", "Delicious chicken with mix of many ingredients",
                "7.00", getString(R.string.sandwiches), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.s5, "Bacon Sandwich ", "Delicious chicken with mix of many ingredients",
                "4.00", getString(R.string.sandwiches), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.s6, "Pulled Pork", "Delicious chicken with mix of many ingredients",
                "5.00", getString(R.string.sandwiches), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.s7, "BLT", "Delicious chicken with mix of many ingredients",
                "7.00", getString(R.string.sandwiches), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.s8, "Egg Salad", "Delicious chicken with mix of many ingredients",
                "3.00", getString(R.string.sandwiches), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.s9, "Reuben", "Delicious chicken with mix of many ingredients",
                "5.00", getString(R.string.sandwiches), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.s10, "Meatball", "Delicious chicken with mix of many ingredients",
                "7.00", getString(R.string.sandwiches), 0, cartIcon));

        // Appetizers
        mealArrayList.add(new Meal(userID, R.drawable.a1, "Mozzarella Sticks", "Delicious chicken with mix of many ingredients",
                "2.00", getString(R.string.appetizers), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.a2, "Caesar Salad", "Delicious chicken with mix of many ingredients",
                "10.00", getString(R.string.appetizers), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.a3, "Guacamole", "Delicious chicken with mix of many ingredients",
                "2.00", getString(R.string.appetizers), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.a4, "Onion Rings", "Delicious chicken with mix of many ingredients",
                "2.00", getString(R.string.appetizers), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.a5, "Chalupa", "Delicious chicken with mix of many ingredients",
                "3.00", getString(R.string.appetizers), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.a6, "Waldorf Salad", "Delicious chicken with mix of many ingredients",
                "1.00", getString(R.string.appetizers), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.a7, "Avocado Toast", "Delicious chicken with mix of many ingredients",
                "7.00", getString(R.string.appetizers), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.a8, "Chile relleno", "Delicious chicken with mix of many ingredients",
                "9.00", getString(R.string.appetizers), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.a9, "Deviled Eggs", "Delicious chicken with mix of many ingredients",
                "2.00", getString(R.string.appetizers), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.a10, "Shrimp Cocktail", "Delicious chicken with mix of many ingredients",
                "1.00", getString(R.string.appetizers), 0, cartIcon));

        mealArrayList.add(new Meal(userID, R.drawable.d1, "Coca Cola", "Delicious chicken with mix of many ingredients",
                "1.00", getString(R.string.drinks), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.d2, "Coca Cola Light", "Delicious chicken with mix of many ingredients",
                "1.00", getString(R.string.drinks), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.d3, "Fanta", "Delicious chicken with mix of many ingredients",
                "1.00", getString(R.string.drinks), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.d4, "Sprite", "Delicious chicken with mix of many ingredients",
                "1.00", getString(R.string.drinks), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.d5, "Bitter Lemon", "Delicious chicken with mix of many ingredients",
                "1.00", getString(R.string.drinks), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.d6, "Ginger Ale", "Delicious chicken with mix of many ingredients",
                "1.00", getString(R.string.drinks), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.d7, "Tonic Water", "Delicious chicken with mix of many ingredients",
                "1.00", getString(R.string.drinks), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.d8, "Soda Water", "Delicious chicken with mix of many ingredients",
                "1.00", getString(R.string.drinks), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.d9, "Nesta Peach", "Delicious chicken with mix of many ingredients",
                "1.00", getString(R.string.drinks), 0, cartIcon));
        mealArrayList.add(new Meal(userID, R.drawable.d10, "Nesta Lemon", "Delicious chicken with mix of many ingredients",
                "1.00", getString(R.string.drinks), 0, cartIcon));
        return mealArrayList;
    }
}