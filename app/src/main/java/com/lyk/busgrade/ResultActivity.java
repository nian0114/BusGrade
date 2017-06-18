package com.lyk.busgrade;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Nian on 17/6/18.
 */

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.user_login);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpGet get = new HttpGet("http://218.242.144.40/weixinpage/HandlerBus.ashx?action=Two&name=1096%E8%B7%AF&lineid=001096");
                HttpClient httpClient = new DefaultHttpClient();
                try {
                    HttpResponse httpResponse = httpClient.execute(get);
                    JSONObject jsonObject;

                    String s = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                    s = s.replace("\n", "\\n");
                    try {

                        Gson gson = new Gson();
                        BusBean busBean = gson.fromJson(s, BusBean.class);

                        for(int i=0;i<busBean.getlineResults1().getStops().size();i++){
                            Log.d("TAG",busBean.getlineResults1().getStops().get(i).getZdmc());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
