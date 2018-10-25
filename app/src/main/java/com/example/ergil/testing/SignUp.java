package com.example.ergil.testing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ergil.testing.Common.Common;
import com.example.ergil.testing.Model.APIResponse;
import com.example.ergil.testing.Model.User;
import com.example.ergil.testing.Remote.IMyAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    private Button signUp;
    private ImageButton backButton;
    private ProgressBar loading;
    private EditText email, name, lastname, password, passwordAgain;

    IMyAPI mService;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mService = Common.getAPI();


        signUp = (Button) findViewById(R.id.signUp);
        loading = (ProgressBar) findViewById(R.id.loading);
        email = (EditText) findViewById(R.id.email_sign);
        name = (EditText) findViewById(R.id.name);
        lastname = (EditText) findViewById(R.id.lastname);
        password = (EditText) findViewById(R.id.password_sign);
        passwordAgain = (EditText) findViewById(R.id.password_again);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp.setVisibility(View.INVISIBLE);
                loading.setVisibility(View.VISIBLE);
                if(((!email.getText().toString().matches("")) && (!name.getText().toString().matches("")) && (!lastname.getText().toString().matches(""))
                        && (!password.getText().toString().matches("")) && (!passwordAgain.getText().toString().matches(""))) == true) {
                    if (isValidEmail()) {
                        if (isValidPass()) {
                            if (password.getText().toString().matches(passwordAgain.getText().toString())) {
                                createNewUser(name.getText().toString().trim(), lastname.getText().toString().trim(), email.getText().toString().trim(), password.getText().toString());
                                timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(SignUp.this, Home.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, 4000);
                            }
                            else {
                                Toast.makeText(SignUp.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.INVISIBLE);
                                signUp.setVisibility(View.VISIBLE);
                            }
                        }
                        else {
                            Toast.makeText(SignUp.this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.INVISIBLE);
                            signUp.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        Toast.makeText(SignUp.this, "Invalid email", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.INVISIBLE);
                        signUp.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    Toast.makeText(SignUp.this, "Please fill in all the datails", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.INVISIBLE);
                    signUp.setVisibility(View.VISIBLE);
                }

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

        lastname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });

        passwordAgain.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });
    }

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z.]+";

    public boolean isValidEmail() {
        if (email.getText().toString().trim().matches(emailPattern) && email.getText().toString().trim().length() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    public boolean isValidPass() {
        if (password.getText().toString().length() < 6) {
            return false;
        }
        else return true;
    }


    private void createNewUser(String name, String lastname, String email, final String password) {
        mService.registerUser(name, lastname, email, password).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse result = response.body();
                if(result.isError()) {
                    Toast.makeText(SignUp.this, result.getError_msg(), Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.INVISIBLE);
                    signUp.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(SignUp.this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(SignUp.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.INVISIBLE);
                signUp.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(SignUp.this.INPUT_METHOD_SERVICE);
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

