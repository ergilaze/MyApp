package com.example.ergil.testing;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ergil.testing.Common.Common;
import com.example.ergil.testing.Model.APIResponse;
import com.example.ergil.testing.Remote.IMyAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {

    private Button logOut;
    private Button deleteAcc;
    private boolean getEmail;

    IMyAPI mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mService = Common.getAPI();

        logOut = (Button) findViewById(R.id.logOut);
        deleteAcc = (Button) findViewById(R.id.deleteAcc);


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        deleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void logout(){
        mService.logout().enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse result = response.body();
                if(result.isError()){
                    Toast.makeText(Home.this, result.getError_msg(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(Home.this, GettingStarted.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
    }

    private void delete() {
        mService.delete().enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse result = response.body();
                if(result.isError()){
                    Toast.makeText(Home.this, result.getError_msg(), Toast.LENGTH_SHORT).show();
                } else{
                    Intent intent = new Intent(Home.this, GettingStarted.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
    }

    private void createDialog(){
        AlertDialog.Builder alrtDig = new AlertDialog.Builder(this);
        alrtDig.setMessage("Are you sure you want to DELETE your account?");
        alrtDig.setCancelable(false);

        alrtDig.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete();
            }
        });

        alrtDig.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alrtDig.create().show();
    }
}
