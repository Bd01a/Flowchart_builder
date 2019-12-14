package com.fed.flowchart_builder.presentation.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fed.flowchart_builder.R;
import com.fed.flowchart_builder.data.ChartRepository;
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
        mPresenter = new MainPresenter(this, ChartRepository.getChartRepository(getContext()));
        mRecyclerView = findViewById(R.id.recyclerview_charts);
        mFloatingButton = findViewById(R.id.floating_button_add_chart);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatingButton.hide();
                AddChartDialogFragment addChartDialogFragment = AddChartDialogFragment.
                        newInstance(new AddChartDialogFragment.OnDialogButtonClick() {
                            @Override
                            public void onPositiveClick() {
                                mFloatingButton.show();
                            }

                            @Override
                            public void onNegativeClick() {
                                mFloatingButton.show();
                            }

                            @Override
                            public int describeContents() {
                                return 0;
                            }

                            @Override
                            public void writeToParcel(Parcel dest, int flags) {

                            }
                        });
                addChartDialogFragment.show(getSupportFragmentManager(), null);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
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

            @Override
            public void onLongClick(String chartName, View view) {
                showPopupMenu(chartName, view);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mFloatingButton.show();
    }

    private void showPopupMenu(final String chartName, View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.popup_menu_chart_item);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.item_delete) {

                    mPresenter.deleteAllByChartName(chartName);
                    mPresenter.loadChartNames();
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

}
