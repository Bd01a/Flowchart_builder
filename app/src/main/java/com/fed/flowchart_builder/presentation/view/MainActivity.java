package com.fed.flowchart_builder.presentation.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fed.flowchart_builder.R;
import com.fed.flowchart_builder.presentation.adapters.ChartsAdapter;
import com.fed.flowchart_builder.presentation.fragments.AddChartDialogFragment;
import com.fed.flowchart_builder.presentation.presenters.MainContracts;
import com.fed.flowchart_builder.presentation.presenters.MainPresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContracts.View {
    public static final String TAG = "ChartActivity";
    public static final String CHART_NAME = "chart_name";

    RecyclerView mRecyclerView;
    FloatingActionButton mFloatingButton;
    MainPresenter mPresenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new MainPresenter(this);
        mRecyclerView = findViewById(R.id.recyclerview_charts);
        mFloatingButton = findViewById(R.id.floating_button_add_chart);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatingButton.hide();
                AddChartDialogFragment addChartDialogFragment = new AddChartDialogFragment();
                addChartDialogFragment.show(getSupportFragmentManager(), null);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadChartNames();
        mFloatingButton.show();
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
                mFloatingButton.hide();
                Intent intent = new Intent(MainActivity.this, ChartActivity.class);
                intent.putExtra(CHART_NAME, chartName);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }


}
