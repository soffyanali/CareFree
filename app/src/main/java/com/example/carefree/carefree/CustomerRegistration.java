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
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class CustomerRegistration extends AppCompatActivity
{
    protected EditText cstuname,cstpassword;
    protected EditText cstphoneno,cstemail,ccstpassword;
    protected Button signup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registercustomer);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        cstuname = (EditText) findViewById(R.id.cstuname);
        cstpassword = (EditText) findViewById(R.id.cstpassword);
        ccstpassword = (EditText) findViewById(R.id.ccstpassword);
        cstphoneno = (EditText) findViewById(R.id.cstphoneno);
        cstemail = (EditText) findViewById(R.id.cstemail);

        signup = (Button) findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=cstuname.getText().toString();
                String password=cstpassword.getText().toString();
                String confpassword=ccstpassword.getText().toString();
                String account="standard".toString();
                String phone=cstphoneno.getText().toString();
                String email=cstemail.getText().toString();

                if(confpassword.equals(password))
                {
                    insertToDatabase(name, password, account,email,phone);
                    AlertDialog.Builder builder = new AlertDialog.Builder(CustomerRegistration.this);
                    builder.setMessage("Registration")
                            .setTitle("Registration Successfully...!!!")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    Intent intent = new Intent(CustomerRegistration.this, LoginActivity.class);
                    startActivity(intent);
                }else
                {
                    Toast.makeText(getApplicationContext(),"Password donot match....!!!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void insertToDatabase(final String name, final String password,final String account_type,final String emailid1,final String phonenno) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String pname =name;
                String ppassword = password;
                String paccount = account_type;
                String profession = "";
                String emailid = emailid1;
                String phoneno = phonenno;
                String userlatitute = "";
                String userlongitute = "";

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("name", pname));
                nameValuePairs.add(new BasicNameValuePair("password", password));
                nameValuePairs.add(new BasicNameValuePair("account_type", account_type));
                nameValuePairs.add(new BasicNameValuePair("profession", profession));
                nameValuePairs.add(new BasicNameValuePair("phoneno", phoneno));
                nameValuePairs.add(new BasicNameValuePair("emailid", emailid));
                nameValuePairs.add(new BasicNameValuePair("userlatitute", userlatitute));
                nameValuePairs.add(new BasicNameValuePair("userlongitute", userlongitute));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(GlobalDomain.domainadd+"insertinfo.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                } catch (ClientProtocolException e) {

                } catch (Exception e) {

                }
                return "success";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(name, password,account_type,"","","","","");
    }
}
