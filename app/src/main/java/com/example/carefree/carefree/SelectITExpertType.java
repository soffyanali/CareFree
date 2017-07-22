package com.example.carefree.carefree;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class SelectITExpertType extends AppCompatActivity {

    //Create Variables
    protected Button services,security,technician,developer,support;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectprofessionaltype);

        services = (Button) findViewById(R.id.services);
        security = (Button) findViewById(R.id.security);
        technician= (Button) findViewById(R.id.technician);
        developer= (Button) findViewById(R.id.developer);
        support= (Button) findViewById(R.id.support);

        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectITExpertType.this, CustomerMap.class);
                intent.putExtra("profession", "IT Services");
                startActivity(intent);
            }
        });
        security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectITExpertType.this, CustomerMap.class);
                intent.putExtra("profession","IT Security");
                startActivity(intent);
            }
        });
        technician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectITExpertType.this, CustomerMap.class);
                intent.putExtra("profession","IT Technician");
                startActivity(intent);
            }
        });
        developer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectITExpertType.this, CustomerMap.class);
                intent.putExtra("profession","IT Developer");
                startActivity(intent);
            }
        });
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectITExpertType.this, CustomerMap.class);
                intent.putExtra("profession","IT Support");
                startActivity(intent);
            }
        });
    }
}