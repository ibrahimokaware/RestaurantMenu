package com.example.restaurantmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class SignInActivity extends AppCompatActivity {

    private EditText emailET, passwordET;
    private Button signInBT, signUpBT;
    private ImageView passwordIV;
    private TextView emailTV, passwordTV;

    private boolean isShow = false, isEmailOkay = false, isPasswordOkay = false;

    MyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailET = findViewById(R.id.et_email);
        passwordET = findViewById(R.id.et_password);
        signInBT = findViewById(R.id.bt_sign_in);
        signUpBT = findViewById(R.id.bt_sign_up);
        passwordIV = findViewById(R.id.iv_password);
        emailTV = findViewById(R.id.tv_email);
        passwordTV = findViewById(R.id.tv_password);

        database = new MyDatabase(getApplicationContext());

        // set watcher for edit text
        emailET.addTextChangedListener(textWatcher);
        passwordET.addTextChangedListener(textWatcher);

        checkValidation();

        // set password toggle
        passwordIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShow) {
                    passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordET.setSelection(passwordET.length());
                    passwordIV.setImageResource(R.drawable.ic_visibility_off);
                    isShow = true;
                } else {
                    passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordET.setSelection(passwordET.length());
                    passwordIV.setImageResource(R.drawable.ic_visibility);
                    isShow = false;
                }
            }
        });

        signInBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                ArrayList<User> users = database.getUsers(email);   //  get users by email
                if (users.size() == 0)   //  no users have this email
                    Toast.makeText(getApplicationContext(), "Email Error!", Toast.LENGTH_SHORT).show();
                else {
                    //for (User u : users)   -->> multiple items
                    User user = users.get(0);   //  get logged user
                    if (user.getPassword().equals(password)) {   //  check user password
                        database.updateLogin(email, 1);   //  change login info.
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    else Toast.makeText(getApplicationContext(), "Password Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUpBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkValidation();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    void checkValidation() {
        if (isEmpty(emailET)) {
            isEmailOkay = false;
        } else {
            // check email pattern
            if (!Patterns.EMAIL_ADDRESS.matcher(emailET.getText().toString()).matches()) {
                emailTV.setText(getString(R.string.email_check));
                isEmailOkay = false;
            } else {
                emailTV.setText("");
                isEmailOkay = true;
            }
        }

        if (isEmpty(passwordET)) {
            isPasswordOkay = false;
        } else {
            // check password length
            if (passwordET.getText().toString().length() < 6 || passwordET.getText().toString().length() > 20) {
                passwordTV.setText(getString(R.string.password_check));
                isPasswordOkay = false;
            } else {
                passwordTV.setText("");
                isPasswordOkay = true;
            }
        }

        if (isEmailOkay && isPasswordOkay) {
            signInBT.setEnabled(true);
            signInBT.setTextColor(getResources().getColor(android.R.color.white));
        } else {
            signInBT.setEnabled(false);
            signInBT.setTextColor(getResources().getColor(R.color.colorDisable));
        }
    }

    private boolean isEmpty(EditText editText) {
        if (TextUtils.isEmpty(editText.getText().toString()))
            return true;
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}