package com.example.carefree.carefree;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.attr.name;

public class LoginActivity extends AppCompatActivity {

    // Creating JSON Parser object
    public static final String PREFS_NAME= "loginusersdetails";
    protected EditText emailEditText;
    protected EditText passwordEditText;
    protected Button loginButton;
    protected TextView signUpTextView;
    InputStream is;
    String result;
    JSONParser jParser = new JSONParser();
    Boolean flag=false;
    SharedPreferences sharedpreferences;
    ArrayList<HashMap<String, String>> productsList;

    // products JSONArray
    JSONArray users = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("logged", "").toString().equals("logged")) {
            Intent intent2 = new Intent(LoginActivity.this, LogOutActivity.class);
            startActivity(intent2);
            finish();

        }
        signUpTextView = (TextView) findViewById(R.id.signUpText);
        emailEditText = (EditText) findViewById(R.id.unametext);
        passwordEditText = (EditText) findViewById(R.id.passwordtxt);
        loginButton = (Button) findViewById(R.id.email_sign_in_button);

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SelectAccountType.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                email = email.trim();
                password = password.trim();

                if (email.isEmpty() || password.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Empty username or password...!!!")
                            .setTitle("Invalid inputs")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    final String emailAddress = email;

                    readJson();
                    // use this to start and trigger a service
                    //Login with an email/password combination
                }
            }
        });
    }

    protected void readJson()
    {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(GlobalDomain.domainadd+"userdetails.php", "GET", params);

            // Check your log cat for JSON reponse

            try {

                    Log.d("All Users: ", json.toString());

                sharedpreferences = getSharedPreferences("loginusersdetails", Context.MODE_PRIVATE);
                    users = json.optJSONArray("result");
                    // looping through All Products
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject c = users.getJSONObject(i);

                        String name=c.getString("name");
                        String password=c.getString("password");
                        String accountype=c.getString("account_type");
                        String profession=c.getString("profession");
                        String phoneno=c.getString("phoneno");
                        String emailid=c.getString("emailid");
                        String userlatitute=c.getString("userlatitute");
                        String userlongitute=c.getString("userlongitute");

                        if(emailEditText.getText().toString().equals(name)&&passwordEditText.getText().toString().equals(password))
                        {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("name", name);
                            editor.putString("password", password);
                            editor.putString("account_type", accountype);
                            editor.putString("profession",profession);
                            editor.putString("phoneno",phoneno);
                            editor.putString("emailid", emailid);
                            editor.putString("userlatitute", userlatitute);
                            editor.putString("userlongitute", userlongitute);
                            editor.putString("logged", "logged");
                            editor.commit();

                            //if standard then ask for what they looking for or professional then show their current location
                            if(accountype.equals("standard")) {
                                //       Intent intent = new Intent(LoginActivity.this, ShowMapStandard.class);
                                //     startActivity(intent);

                                Intent intent2 = new Intent(LoginActivity.this, SelectITExpertType.class);
                                startActivity(intent2);

                            }else {

                                //Intent intent1 = new Intent(LoginActivity.this, ITExpertMap.class); //change your current location
                                //startActivity(intent1);
                                // Toast.makeText(getApplicationContext(),"Profession= "+profession,Toast.LENGTH_LONG).show();
                            }
                            flag=true;
                            break;
                        }
                        else
                        {
                            flag=false;
                        }

                    }
                if(flag==false)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Wrong username or password...!!!")
                            .setTitle("Invalid inputs")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    emailEditText.setText("");
                    passwordEditText.setText("");
                }
                }catch (Exception e)
            {

            }
    }

}