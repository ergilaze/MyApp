package com.example.ergil.testing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ergil.testing.Common.Common;
import com.example.ergil.testing.Model.APIResponse;
import com.example.ergil.testing.Remote.IMyAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GettingStarted extends AppCompatActivity {

    private Button getStarted;
    private TextView logIn;

    SharedPreferences sharedPreferences;
    int isLogged;
    IMyAPI mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_started);

        mService = Common.getAPI();
        getInfo();


        getStarted = (Button) findViewById(R.id.getStarted);
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignUpActivity();
            }
        });

        logIn = (TextView) findViewById(R.id.tv);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogInActivity();
            }
        });
    }

        public void openSignUpActivity() {
            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
        }


    public void openLogInActivity() {
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void getInfo() {
        mService.getInfo().enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse result = response.body();
                if(result.isError()){
                    Toast.makeText(GettingStarted.this, result.getError_msg(), Toast.LENGTH_SHORT).show();
                }else{
                    isLogged = result.getUser().getLoggedInfo();
                    if(isLogged==1){
                        Intent intent = new Intent(GettingStarted.this, Home.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(GettingStarted.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}