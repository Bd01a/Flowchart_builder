package com.fed.flowchart_builder.presentation.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fed.flowchart_builder.R;
import com.fed.flowchart_builder.presentation.adapters.ChartsAdapter;
import com.fed.flowchart_builder.presentation.presenters.MainContracts;
import com.fed.flowchart_builder.presentation.presenters.MainPresenter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContracts.View {
    public static final String TAG = "ChartActivity";
    public static final String CHART_NAME = "chart_name";

    RecyclerView mRecyclerView;
    Button mFloatingButton;
    MainPresenter mPresenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new MainPresenter(this);
        mRecyclerView = findViewById(R.id.recyclerview_charts);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPresenter.loadChartNames();


    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void loadingChartNamesIsCompleted(List<String> names) {
        ChartsAdapter adapter = new ChartsAdapter(names);
        adapter.setOnItemClickListener(new ChartsAdapter.OnItemClickListener() {
            @Override
            public void onClick(String chartName) {

                Intent intent = new Intent(MainActivity.this, ChartActivity.class);
                intent.putExtra(CHART_NAME, chartName);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }


}