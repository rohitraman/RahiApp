package com.example.roshan.rahiapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.roshan.rahiapp.model.TimeLineModel;
import com.example.roshan.rahiapp.model.TrainStatus;


import java.util.ArrayList;
import java.util.List;


public class TimeLineActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TimeLineAdapter mTimeLineAdapter;
    private List<TimeLineModel> mDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);

        initView();
    }

    private LinearLayoutManager getLinearLayoutManager() {

        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }


    private void initView() {
        setDataListItems();
        mTimeLineAdapter = new TimeLineAdapter(mDataList);
        mRecyclerView.setAdapter(mTimeLineAdapter);
    }

    private void setDataListItems() {
        mDataList.add(new TimeLineModel("I", "", TrainStatus.ARRIVING));
        mDataList.add(new TimeLineModel("H", "2018-07-12 08:00", TrainStatus.AVAILABLE));
        mDataList.add(new TimeLineModel("G", "2018-07-11 21:00", TrainStatus.DEPARTED));
        mDataList.add(new TimeLineModel("F", "2018-07-11 18:00", TrainStatus.DEPARTED));
        mDataList.add(new TimeLineModel("E", "2018-07-11 09:30", TrainStatus.DEPARTED));
        mDataList.add(new TimeLineModel("D", "2018-07-11 08:00", TrainStatus.DEPARTED));
        mDataList.add(new TimeLineModel("C", "2018-07-10 15:00", TrainStatus.DEPARTED));
        mDataList.add(new TimeLineModel("B", "2018-07-10 14:30", TrainStatus.DEPARTED));
        mDataList.add(new TimeLineModel("A", "2018-07-10 14:00", TrainStatus.DEPARTED));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Menu
        switch (item.getItemId()) {
            //When home is clicked
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
