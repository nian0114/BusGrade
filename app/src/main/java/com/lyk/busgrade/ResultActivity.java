package com.lyk.busgrade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nian on 17/6/18.
 */

public class ResultActivity extends AppCompatActivity {
    @Bind(R.id.main_recyclerview)
    RecyclerView recyclerView;

    public static boolean direction=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent=getIntent();
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
            return direction?MainActivity.busBean.getlineResults1().getStops().size():MainActivity.busBean.getlineResults0().getStops().size();
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
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        if (item.getItemId()==R.id.action_changese){
            direction= !direction;
        }
        return true;
    }


}
