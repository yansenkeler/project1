package com.fruit.client.activity;

import com.fruit.client.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.VolleyError;
import com.fruit.core.api.VolleyResponse;

public class MainActivity extends AppCompatActivity implements VolleyResponse{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onResponse(Object response, int taskid) {

    }

    @Override
    public void onErrorResponse(VolleyError error, int taskid) {

    }
}
