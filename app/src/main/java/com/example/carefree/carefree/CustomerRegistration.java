package com.example.carefree.carefree;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CustomerRegistration extends AppCompatActivity
{
    protected EditText standerdemail;
    protected EditText standerdpassword;
    protected Button signup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registercustomer);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        standerdemail = (EditText) findViewById(R.id.standerdemail);
        standerdpassword = (EditText) findViewById(R.id.standerdpassword);
        signup = (Button) findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=standerdemail.getText().toString();
                String password=standerdpassword.getText().toString();
                String account="standard".toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(StandardAccountSignup.this);
                builder.setMessage(R.string.sign_up_button_label)
                        .setTitle(R.string.signup_success)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();

                Intent intent = new Intent(StandardAccountSignup.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
