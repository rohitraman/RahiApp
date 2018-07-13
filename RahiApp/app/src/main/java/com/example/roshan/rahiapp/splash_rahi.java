package com.example.roshan.rahiapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import me.relex.circleindicator.CircleIndicator;


public class splash_rahi extends AppCompatActivity {

    ViewPager mPager;
    splash_adapter adapter;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES = {R.drawable.tenor, R.drawable.one, R.drawable.two, R.drawable.three};
    Button btnGoToApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_rahi);
        init();
        btnGoToApp = (Button) findViewById(R.id.btn_go_to_app);
        btnGoToApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(splash_rahi.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void init() {

        mPager = (ViewPager) findViewById(R.id.pager);
        adapter = new splash_adapter(getApplicationContext(), IMAGES);

        mPager.setAdapter(adapter);
        CircleIndicator circleIndicator = (CircleIndicator) findViewById(R.id.indicator);
        circleIndicator.setViewPager(mPager);


    }
}
