//Refernces:- https://developer.android.com/reference/android/widget/Button.html
// https://developer.android.com/guide/topics/ui/controls/button.html

package com.example.carefree.carefree;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

public class SelectITExpertType extends AppCompatActivity {

    //Create Variables
    protected Button services,security,technician,developer,support,recommditexp;
    SharedPreferences preferences;
    public static final String PREFS_NAME= "loginusersdetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectprofessionaltype);

        services = (Button) findViewById(R.id.services);
        security = (Button) findViewById(R.id.security);
        technician= (Button) findViewById(R.id.technician);
        developer= (Button) findViewById(R.id.developer);
        support= (Button) findViewById(R.id.support);
        recommditexp= (Button) findViewById(R.id.recommditexp);


        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        final String customername=settings.getString("name", "").toString();


        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectITExpertType.this, CustomerMap.class);
                intent.putExtra("profession", "IT Services");
                insertToDatabase(customername, "Services");
                startActivity(intent);
            }
        });
        security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectITExpertType.this, CustomerMap.class);
                intent.putExtra("profession","IT Security");
                insertToDatabase(customername, "Security");
                startActivity(intent);
            }
        });
        technician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectITExpertType.this, CustomerMap.class);
                intent.putExtra("profession","IT Technician");
                insertToDatabase(customername, "Technician");
                startActivity(intent);
            }
        });
        developer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectITExpertType.this, CustomerMap.class);
                intent.putExtra("profession","IT Developer");
                insertToDatabase(customername, "Developer");
                startActivity(intent);
            }
        });
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectITExpertType.this, CustomerMap.class);
                intent.putExtra("profession","IT Support");
                insertToDatabase(customername, "Support");
                startActivity(intent);
            }
        });

        recommditexp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectITExpertType.this, RecomCustomerMap.class);
                intent.putExtra("profession","recom");
                startActivity(intent);
            }
        });

    }

    private void insertToDatabase(final String cname, final String itcat) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("customername", cname));
                nameValuePairs.add(new BasicNameValuePair("itexpertcat", itcat));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(GlobalDomain.domainadd+"addvisited.php");
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
        sendPostReqAsyncTask.execute(cname,itcat,"","","","","");
    }

}