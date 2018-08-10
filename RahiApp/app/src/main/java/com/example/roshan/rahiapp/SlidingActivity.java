package com.example.roshan.rahiapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.roshan.rahiapp.maps.MapActivity;

public class SlidingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding);
        setSupportActionBar((Toolbar) findViewById(R.id.main_toolbar));

    }

    public void ml(View view){
        Intent i=new Intent(getApplicationContext(),MapActivity.class);
        startActivity(i);
    }
}
