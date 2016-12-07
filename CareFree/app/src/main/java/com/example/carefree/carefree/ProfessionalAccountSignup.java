package com.example.carefree.carefree;

import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.os.StrictMode;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
        import android.widget.Toast;


        import java.io.InputStream;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;


public class ProfessionalAccountSignup extends AppCompatActivity
{
    protected EditText professionusername,professionpassword,tphoneno,emailidtext;
    Spinner spinner;
    protected Button professionalsignup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professionalaccount);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        professionusername = (EditText) findViewById(R.id.professionusername);
        professionpassword = (EditText) findViewById(R.id.professionpassword);
        tphoneno = (EditText) findViewById(R.id.phoneno);
        emailidtext = (EditText) findViewById(R.id.emailidtext);
        spinner= (Spinner) findViewById(R.id.spinner);

        professionalsignup = (Button) findViewById(R.id.professionalsignup);

        professionalsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=professionusername.getText().toString();
                String password=professionpassword.getText().toString();
                String account="professional".toString();
                String professional= (String) spinner.getSelectedItem();
                String phoneno=tphoneno.getText().toString();
                String emailid=emailidtext.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfessionalAccountSignup.this);
                builder.setMessage(R.string.sign_up_button_label)
                        .setTitle(R.string.signup_success)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();

                Intent intent = new Intent(ProfessionalAccountSignup.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
