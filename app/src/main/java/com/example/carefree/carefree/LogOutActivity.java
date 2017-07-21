package com.example.carefree.carefree;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class LogOutActivity extends AppCompatActivity {

    //Create Variables
    protected Button logoutbutton,continuebutton;
    public static final String PREFS_NAME= "loginusersdetails";
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logoutlayout);

        logoutbutton = (Button) findViewById(R.id.logoutbutton);
        continuebutton = (Button) findViewById(R.id.continuebutton);

        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("name");
                editor.remove("password");
                editor.commit();

                Intent intent = new Intent(LogOutActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                if (settings.getString("logged", null).toString().equals("logged"))
                {
                    if(settings.getString("account_type",null).toString().equals("standard"))
                    {
                        Intent intent2 = new Intent(LogOutActivity.this, SelectITExpertType.class);
                        startActivity(intent2);
                        finish();

                    }else {
                        //Intent intent1 = new Intent(LogOutActivity.this, ShowMapProfessional.class); //change your current location
                        //startActivity(intent1);
                        finish();

                    }
                }
            }
        });


    }
}