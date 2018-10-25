package com.example.ergil.testing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ergil.testing.Common.Common;
import com.example.ergil.testing.Model.APIResponse;
import com.example.ergil.testing.Remote.IMyAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassword extends AppCompatActivity {

    private EditText code, password, passwordAgain;
    private Button confirm;
    private String getToken;
    private String getEmail;

    IMyAPI mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mService = Common.getAPI();

        getEmail = getIntent().getStringExtra("email");
        getToken = getIntent().getStringExtra("token");

        code = (EditText) findViewById(R.id.token);
        code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });

        password = (EditText) findViewById(R.id.password_reset);
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });

        passwordAgain = (EditText) findViewById(R.id.password_reset_again);
        passwordAgain.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });

        confirm = (Button) findViewById(R.id.resetPass);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!code.getText().toString().matches("") && !password.getText().toString().matches("") && !passwordAgain.getText().toString().matches("")) {
                    if (getToken.matches(code.getText().toString().trim())) {
                        if(isValidPass()) {
                            if (password.getText().toString().matches(passwordAgain.getText().toString())) {
                                updatePassword(code.getText().toString().trim(), getEmail, password.getText().toString());
                                gotoLogInActivity();
                            } else {
                                Toast.makeText(ResetPassword.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ResetPassword.this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(ResetPassword.this, "Invalid token", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(ResetPassword.this, "Please fill in all the details", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(ResetPassword.this.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        Intent BackpressedIntent = new Intent();
        BackpressedIntent.setClass(getApplicationContext(), ForgotPassword.class);
        BackpressedIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(BackpressedIntent);
        finish();
    }

    private void updatePassword(String token, String email, String password) {
        mService.resetPass(token, email, password).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse result = response.body();

                if (result.isError()) {

                } else {
                    Toast.makeText(ResetPassword.this, "Password is updated successfully.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(ResetPassword.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void gotoLogInActivity() {
        Intent intent = new Intent(ResetPassword.this, LogIn.class);
        startActivity(intent);
    }

    public boolean isValidPass() {
        if (password.getText().toString().length() < 6) {
            return false;
        }
        else return true;
    }
}