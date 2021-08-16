package com.example.restaurantmenu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDatabase extends SQLiteOpenHelper {

    public static final String DB_NAME = "restaurant_db";
    public static final int DB_VERSION = 1;

    public static final String USER_TB_NAME = "user";
    public static final String USER_CLN_ID = "id";
    public static final String USER_CLN_NAME = "name";
    public static final String USER_CLN_EMAIL = "email";
    public static final String USER_CLN_PASSWORD = "password";
    public static final String USER_CLN_LOGIN = "login";

    public static final String MEAL_TB_NAME = "meal";
    public static final String MEAL_CLN_ID = "id";
    public static final String MEAL_CLN_USER_ID = "user_id";
    public static final String MEAL_CLN_IMAGE = "image";
    public static final String MEAL_CLN_NAME = "name";
    public static final String MEAL_CLN_INGREDIENTS = "ingredients";
    public static final String MEAL_CLN_PRICE = "price";
    public static final String MEAL_CLN_TYPE = "type";
    public static final String MEAL_CLN_CART = "cart";
    public static final String MEAL_CLN_CART_ICON = "cart_icon";


    public MyDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+USER_TB_NAME+" ("+USER_CLN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                USER_CLN_NAME +" TEXT NOT NULL," + USER_CLN_EMAIL +" TEXT UNIQUE NOT NULL," + USER_CLN_PASSWORD+" TEXT NOT NULL," +
                USER_CLN_LOGIN+" INTEGER NOT NULL)");
        sqLiteDatabase.execSQL("CREATE TABLE "+MEAL_TB_NAME+" ("+MEAL_CLN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                MEAL_CLN_USER_ID+" INTEGER NOT NULL,"+ MEAL_CLN_IMAGE+" INTEGER NOT NULL,"+ MEAL_CLN_NAME +" TEXT NOT NULL," +
                MEAL_CLN_INGREDIENTS +" TEXT NOT NULL," + MEAL_CLN_PRICE+" TEXT NOT NULL," + MEAL_CLN_TYPE+" TEXT NOT NULL," +
                MEAL_CLN_CART+" INTEGER NOT NULL,"+ MEAL_CLN_CART_ICON+" INTEGER NOT NULL,"+
                " FOREIGN KEY("+ MEAL_CLN_USER_ID+") REFERENCES " + USER_TB_NAME +"("+ USER_CLN_ID+"))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ USER_TB_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ MEAL_TB_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_CLN_NAME, user.getName());
        contentValues.put(USER_CLN_EMAIL, user.getEmail());
        contentValues.put(USER_CLN_PASSWORD, user.getPassword());
        contentValues.put(USER_CLN_LOGIN, user.getLogin());
        long result= db.insert(USER_TB_NAME, null, contentValues);
        return result != -1;
    }

    public boolean insertMeal(Meal meal) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MEAL_CLN_USER_ID, meal.getUserID());
        contentValues.put(MEAL_CLN_IMAGE, meal.getImage());
        contentValues.put(MEAL_CLN_NAME, meal.getName());
        contentValues.put(MEAL_CLN_INGREDIENTS, meal.getIngredients());
        contentValues.put(MEAL_CLN_PRICE, meal.getPrice());
        contentValues.put(MEAL_CLN_TYPE, meal.getType());
        contentValues.put(MEAL_CLN_CART, meal.getCart());
        contentValues.put(MEAL_CLN_CART_ICON, meal.getCartIcon());
        long result= db.insert(MEAL_TB_NAME, null, contentValues);
        return result != -1;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_CLN_NAME, user.getName());
        contentValues.put(USER_CLN_EMAIL, user.getEmail());
        contentValues.put(USER_CLN_PASSWORD, user.getPassword());
        contentValues.put(USER_CLN_LOGIN, user.getLogin());
        String args [] = {String.valueOf(user.getId()), user.getName()}; // or myUniversityInDB.getId()+""
        int result= db.update(USER_TB_NAME, contentValues, "id=? AND name=?", args);
        return result != 0; // >0
    }

    public long getUsersCount() {
        SQLiteDatabase db = getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, USER_TB_NAME); // (db,table_university_name,"city=?",args)يمكن استخدام args معها لو كان هناك شرط معين - نريد عدد الجانعات التي موقعها غزة مثلا
    }

    public long getMealsCountForUser(int userID) {
        SQLiteDatabase db = getReadableDatabase();
        String args [] = {String.valueOf(userID)};
        return DatabaseUtils.queryNumEntries(db, MEAL_TB_NAME, MEAL_CLN_USER_ID+"=?", args);
    }

    public boolean deleteUser(User user) {
        SQLiteDatabase db = getReadableDatabase();
        String args [] = {String.valueOf(user.getId())};
        int result= db.delete(USER_TB_NAME, "id=?", args);
        return result > 0; // != 0
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+USER_TB_NAME, null); // يمكن استخدام ال args
        if (cursor != null && cursor.moveToFirst()) {
            do {
                //int id = cursor.getInt(0);
                // String name = cursor.getString(1);
                int id = cursor.getInt(cursor.getColumnIndex(USER_CLN_ID));
                String name = cursor.getString(cursor.getColumnIndex(USER_CLN_NAME));
                String email = cursor.getString(cursor.getColumnIndex(USER_CLN_EMAIL));
                String password = cursor.getString(cursor.getColumnIndex(USER_CLN_PASSWORD));
                int login = cursor.getInt(cursor.getColumnIndex(USER_CLN_LOGIN));
                User user = new User(id, name, email, password, login);
                users.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return users;
    }

    public ArrayList<User> getUsers(String emailSearch) {
        ArrayList<User> users = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+USER_TB_NAME+" WHERE " + USER_CLN_EMAIL+"=?", new String[] {emailSearch});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                //int id = cursor.getInt(0);
                // String name = cursor.getString(1);
                int id = cursor.getInt(cursor.getColumnIndex(USER_CLN_ID));
                String name = cursor.getString(cursor.getColumnIndex(USER_CLN_NAME));
                String email = cursor.getString(cursor.getColumnIndex(USER_CLN_EMAIL));
                String password = cursor.getString(cursor.getColumnIndex(USER_CLN_PASSWORD));
                int login = cursor.getInt(cursor.getColumnIndex(USER_CLN_LOGIN));
                User user = new User(id, name, email, password, login);
                users.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return users;
    }

    public boolean resetPassword(String email, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_CLN_PASSWORD, password);
        String args [] = {String.valueOf(email)};
        int result= db.update(USER_TB_NAME, contentValues, "email=?", args);
        return result != 0; // >0
    }

    public boolean updateLogin(String email, int login) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_CLN_LOGIN, login);
        String args [] = {String.valueOf(email)};
        int result= db.update(USER_TB_NAME, contentValues, "email=?", args);
        return result != 0; // >0
    }

    public ArrayList<User> getLoggedUsers() {
        ArrayList<User> users = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+USER_TB_NAME+" WHERE " + USER_CLN_LOGIN+"=?", new String[] {1+""});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(USER_CLN_ID));
                String name = cursor.getString(cursor.getColumnIndex(USER_CLN_NAME));
                String email = cursor.getString(cursor.getColumnIndex(USER_CLN_EMAIL));
                String password = cursor.getString(cursor.getColumnIndex(USER_CLN_PASSWORD));
                int login = cursor.getInt(cursor.getColumnIndex(USER_CLN_LOGIN));
                User user = new User(id, name, email, password, login);
                users.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return users;
    }

    public boolean updateCart(int id, int userID, int cartValue) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MEAL_CLN_CART, cartValue);
        if (cartValue == 0)
            contentValues.put(MEAL_CLN_CART_ICON, R.drawable.ic_add_cart);
        else contentValues.put(MEAL_CLN_CART_ICON, R.drawable.ic_remove_cart);
        String args [] = {String.valueOf(id), String.valueOf(userID)};
        int result= db.update(MEAL_TB_NAME, contentValues, MEAL_CLN_ID+"=? AND "+MEAL_CLN_USER_ID+"=?", args);
        return result != 0; // >0
    }

    public ArrayList<Meal> getMeals(String typeSearch, int userIDSearch) {
        ArrayList<Meal> meals = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+MEAL_TB_NAME+" WHERE " + MEAL_CLN_TYPE+"=? AND "+MEAL_CLN_USER_ID+"=?",
                new String[] {typeSearch, String.valueOf(userIDSearch)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(MEAL_CLN_ID));
                int userID = cursor.getInt(cursor.getColumnIndex(MEAL_CLN_USER_ID));
                int image = cursor.getInt(cursor.getColumnIndex(MEAL_CLN_IMAGE));
                String name = cursor.getString(cursor.getColumnIndex(MEAL_CLN_NAME));
                String ingredients = cursor.getString(cursor.getColumnIndex(MEAL_CLN_INGREDIENTS));
                String price = cursor.getString(cursor.getColumnIndex(MEAL_CLN_PRICE));
                String type = cursor.getString(cursor.getColumnIndex(MEAL_CLN_TYPE));
                int cart = cursor.getInt(cursor.getColumnIndex(MEAL_CLN_CART));
                int cartIcon = cursor.getInt(cursor.getColumnIndex(MEAL_CLN_CART_ICON));
                Meal meal = new Meal(id, userID, image, name, ingredients, price, type, cart, cartIcon);
                meals.add(meal);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return meals;
    }

    public ArrayList<Meal> getMealsInCart(int userIDSearch) {
        ArrayList<Meal> meals = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+MEAL_TB_NAME+" WHERE " + MEAL_CLN_CART+"=? AND "+MEAL_CLN_USER_ID+"=?",
                new String[] {1+"", String.valueOf(userIDSearch)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(MEAL_CLN_ID));
                int userID = cursor.getInt(cursor.getColumnIndex(MEAL_CLN_USER_ID));
                int image = cursor.getInt(cursor.getColumnIndex(MEAL_CLN_IMAGE));
                String name = cursor.getString(cursor.getColumnIndex(MEAL_CLN_NAME));
                String ingredients = cursor.getString(cursor.getColumnIndex(MEAL_CLN_INGREDIENTS));
                String price = cursor.getString(cursor.getColumnIndex(MEAL_CLN_PRICE));
                String type = cursor.getString(cursor.getColumnIndex(MEAL_CLN_TYPE));
                int cart = cursor.getInt(cursor.getColumnIndex(MEAL_CLN_CART));
                int cartIcon = cursor.getInt(cursor.getColumnIndex(MEAL_CLN_CART_ICON));
                Meal meal = new Meal(id, userID, image, name, ingredients, price, type, cart, cartIcon);
                meals.add(meal);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return meals;
    }
}
