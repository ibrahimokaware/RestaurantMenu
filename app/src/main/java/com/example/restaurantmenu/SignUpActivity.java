package com.example.restaurantmenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameET, emailET, passwordET;
    private Button signUpBT;
    private TextView signInTV, emailTV, passwordTV;
    private ImageView passwordIV;

    private boolean isShow = false, isNameOkay = false, isEmailOkay = false, isPasswordOkay = false;

    MyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameET = findViewById(R.id.et_name);
        emailET = findViewById(R.id.et_email);
        passwordET = findViewById(R.id.et_password);
        signUpBT = findViewById(R.id.bt_sign_up);
        signInTV = findViewById(R.id.tv_sign_in);
        passwordIV = findViewById(R.id.iv_password);
        emailTV = findViewById(R.id.tv_email);
        passwordTV = findViewById(R.id.tv_password);

        database = new MyDatabase(getApplicationContext());

        // set watcher for edit text
        nameET.addTextChangedListener(textWatcher);
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

        signUpBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameET.getText().toString();
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();

                User user = new User(name, email, password, 1);   //  insert new user
                boolean result = database.insertUser(user);   //  insert result
                if (result) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));   //  success insertion
                }
                else Toast.makeText(getApplicationContext(), getString(R.string.email_exist), Toast.LENGTH_SHORT).show();
            }
        });

        signInTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
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
        if (isEmpty(nameET)) {
            isNameOkay = false;
        } else {
            isNameOkay = true;
        }

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

        if (isNameOkay && isEmailOkay && isPasswordOkay) {
            signUpBT.setEnabled(true);
            signUpBT.setTextColor(getResources().getColor(android.R.color.white));
        } else {
            signUpBT.setEnabled(false);
            signUpBT.setTextColor(getResources().getColor(R.color.colorDisable));
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
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
    }
}