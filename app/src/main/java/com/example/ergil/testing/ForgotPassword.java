package com.example.ergil.testing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ergil.testing.Common.Common;
import com.example.ergil.testing.Model.APIResponse;
import com.example.ergil.testing.Model.User;
import com.example.ergil.testing.Remote.GMailSender;
import com.example.ergil.testing.Remote.IMyAPI;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {

    private Button resetPass;
    private EditText email;
    Session session = null;
    Context context = null;
    private String getToken = "";


    IMyAPI mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mService = Common.getAPI();

        resetPass = (Button) findViewById(R.id.resetPass);
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().matches("")) {
                    Properties props = new Properties();
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.socketFactory.port", "465");
                    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.port", "465");

                    session = Session.getDefaultInstance(props, new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication("designstudiopro01@gmail.com", "123ergi1");
                        }
                    });

                    sendToken(email.getText().toString().trim());
                } else
                    Toast.makeText(ForgotPassword.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            }
        });

        email = (EditText) findViewById(R.id.email_forgot_pass);
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });
    }

    private void sendToken(String email) {
        mService.forgotPass(email).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse result = response.body();
                if (result.isError()) {
                    Toast.makeText(ForgotPassword.this, result.getError_msg(), Toast.LENGTH_SHORT).show();
                } else {
                    String token = result.getUser().getToken();
                    getToken += token;
                    RetreiveFeedTask task = new RetreiveFeedTask();
                    task.execute();
                    openResetPasswordActivity();
                    Toast.makeText(ForgotPassword.this, "Successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(ForgotPassword.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(ForgotPassword.this.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        Intent BackpressedIntent = new Intent();
        BackpressedIntent.setClass(getApplicationContext(), LogIn.class);
        BackpressedIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(BackpressedIntent);
        finish();
    }

    public void openResetPasswordActivity() {
        Intent intent = new Intent(this, ResetPassword.class);
        intent.putExtra("email", email.getText().toString().trim());
        intent.putExtra("token", getToken);
        startActivity(intent);
    }



    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        private String subject = "Reset password";

        @Override
        protected String doInBackground(String... params) {
            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("designstudiopro01@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getText().toString().trim()));
                message.setSubject(subject);
                message.setContent("Your code is: " + getToken + ". You have 5 minutes to reset your password before the code expires.", "text/html; charset=utf-8");
                Transport.send(message);
            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_LONG).show();
        }
    }
}

