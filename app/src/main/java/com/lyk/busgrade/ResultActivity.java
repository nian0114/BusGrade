package com.lyk.busgrade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nian on 17/6/18.
 */

public class ResultActivity extends AppCompatActivity {
    @Bind(R.id.main_recyclerview)
    RecyclerView recyclerView;

    @Bind(R.id.resultLayout)
    CoordinatorLayout coordinatorLayout;

    public static boolean direction = true;
    private RequestQueue requestQueue;
    private StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(intent.getStringExtra("linenumber"));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);//提高性能
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new Adapter(this));

        requestQueue = Volley.newRequestQueue(this);
    }


    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        private LayoutInflater mInflater;

        public Adapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            @Bind(R.id.main_item_tv)
            TextView textView;
        }

        @Override
        public int getItemCount() {
            return direction ? MainActivity.busBean.getlineResults1().getStops().size() : MainActivity.busBean.getlineResults0().getStops().size();
        }

        /**
         * 创建ViewHolder
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.recycler_item, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        /**
         * 设置值
         */
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int i) {
            if (direction)
                holder.textView.setText(MainActivity.busBean.getlineResults1().getStops().get(i).getZdmc());
            else
                holder.textView.setText(MainActivity.busBean.getlineResults0().getStops().get(i).getZdmc());

            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    String line_id = "";
                    JSONObject jsonObject = null;

                    try {
                        jsonObject = new JSONObject(MainActivity.s);
                        line_id = jsonObject.getString("line_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent intent = getIntent();
                    request = new StringRequest(Request.Method.GET, "http://218.242.144.40/weixinpage/HandlerBus.ashx?action=Three&name=" + intent.getStringExtra("linenumber") + "%E8%B7%AF&lineid=" + line_id + "&stopid=" + (direction?MainActivity.busBean.getlineResults1().getStops().get(i).getDesc():MainActivity.busBean.getlineResults0().getStops().get(i).getDesc()) + "&direction=" + (direction ? "1" : "0"), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            BusNowBean busNowBean = gson.fromJson(response, BusNowBean.class);

                            showSnackbar(Snackbar.LENGTH_SHORT, busNowBean.getCars().get(0).getTerminal() + "还有" + busNowBean.getCars().get(0).getStopdis() + "站，约" + busNowBean.getCars().get(0).getDistance() + "米").show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showSnackbar(Snackbar.LENGTH_SHORT, "暂无车辆信息，请耐心等待").show();
                        }
                    });

                    requestQueue.add(request);

                    // 点击事件
                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.action_changese) {
            direction = !direction;
        }
        return true;
    }

    private Snackbar showSnackbar(int time, int strId) {
        return Snackbar.make(coordinatorLayout, getString(strId), time);
    }

    private Snackbar showSnackbar(int time, String str) {
        return Snackbar.make(coordinatorLayout, str, time);
    }
}
