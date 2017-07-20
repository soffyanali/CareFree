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
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class ITExpertRegistration extends AppCompatActivity
{
    protected EditText ituname,itpassword,itphoneno,itemail;
    Spinner specialization;
    MultiAutoCompleteTextView Specialskills;
    protected Button itsignup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registeritaccount);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ituname = (EditText) findViewById(R.id.ituname);
        itpassword = (EditText) findViewById(R.id.itpassword);
        itphoneno = (EditText) findViewById(R.id.itphoneno);
        itemail = (EditText) findViewById(R.id.itemail);
        Specialskills=(MultiAutoCompleteTextView)findViewById(R.id.Specialskills) ;
        specialization= (Spinner) findViewById(R.id.specialization);

        itsignup = (Button) findViewById(R.id.itsignup);

        itsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    String name = ituname.getText().toString();
                    String password = itpassword.getText().toString();
                    String account = "professional".toString();
                    String professional = (String) specialization.getSelectedItem();
                    String phoneno = itphoneno.getText().toString();
                    String emailid = itemail.getText().toString();

                    insertToDatabase(name, password, account, professional, phoneno, emailid);
                    AlertDialog.Builder builder = new AlertDialog.Builder(ITExpertRegistration.this);
                    builder.setMessage("Registered")
                            .setTitle("User is registered")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    Intent intent = new Intent(ITExpertRegistration.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                   Toast.makeText(getApplicationContext(),name+" user is registered",Toast.LENGTH_LONG).show();

            }
        });

    }

    private void insertToDatabase(final String name, final String password,final String account_type,final String profession,final String phoneno,final String emailid ) {


        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String pname =name;
                String ppassword = password;
                String paccount = account_type;
                String userlatitute = "53.349805";
                String userlongitute = "-6.260310";

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
                    HttpPost httpPost = new HttpPost(GlobalDomain.domainadd+"inserdatabaseregistration.php");
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
