package com.example.ergil.testing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ergil.testing.Common.Common;
import com.example.ergil.testing.Model.APIResponse;
import com.example.ergil.testing.Remote.IMyAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Timer;
import java.util.TimerTask;


public class LogIn extends AppCompatActivity {

    private ProgressBar loading;
    private Button logIn;
    private EditText email, password;
    private TextView forgotPass;
    IMyAPI mService;

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mService = Common.getAPI();

        email = (EditText) findViewById(R.id.email_log) ;
        password = (EditText) findViewById(R.id.password_log) ;
        logIn = (Button) findViewById(R.id.signUp);
        loading = (ProgressBar) findViewById(R.id.loading) ;

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);
                logIn.setVisibility(View.INVISIBLE);
                if(((!email.getText().toString().matches("")) && (!password.getText().toString().matches(""))) == true) {
                    authenticateUser(email.getText().toString(), password.getText().toString());
                }
                else {
                    Toast.makeText(LogIn.this, "Please fill in all the details", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.INVISIBLE);
                    logIn.setVisibility(View.VISIBLE);
                }

            }
        });

        forgotPass = (TextView) findViewById(R.id.forgotPass);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openForgotPasswordActivity();
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });

    }

    private void openForgotPasswordActivity() {
        Intent intent = new Intent(LogIn.this, ForgotPassword.class);
        startActivity(intent);

    }

    private void authenticateUser(String email, String password) {
        mService.loginUser(email, password).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse result = response.body();
                if(result.isError()) {
                    Toast.makeText(LogIn.this, result.getError_msg(), Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.INVISIBLE);
                    logIn.setVisibility(View.VISIBLE);

                }
                else {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                        Intent intent = new Intent(LogIn.this, Home.class);
                        startActivity(intent);
                        finish();
                        }
                    }, 4000);

                }

            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(LogIn.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.INVISIBLE);
                logIn.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(LogIn.this.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        Intent BackpressedIntent = new Intent();
        BackpressedIntent .setClass(getApplicationContext(),GettingStarted.class);
        BackpressedIntent .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(BackpressedIntent );
        finish();
    }


}
