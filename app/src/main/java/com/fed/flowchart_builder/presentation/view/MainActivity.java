package com.fed.flowchart_builder.presentation.view;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fed.flowchart_builder.ChartProvider;
import com.fed.flowchart_builder.R;
import com.fed.flowchart_builder.data.ChartRepository;
import com.fed.flowchart_builder.presentation.adapters.ChartsAdapter;
import com.fed.flowchart_builder.presentation.fragments.AddChartDialogFragment;
import com.fed.flowchart_builder.presentation.presenters.MainPresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "ChartActivity";
    public static final String CHART_NAME = "chart_name";
    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 4801;

    RecyclerView mRecyclerView;
    FloatingActionButton mFloatingButton;
    MainPresenter mPresenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new MainPresenter(this, ChartRepository.getChartRepository(getContext()));


        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .penaltyDialog()
                .build());


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


    public Context getContext() {
        return getApplicationContext();
    }


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
                switch (item.getItemId()) {
                    case R.id.item_delete:
                        mPresenter.deleteAllByChartName(chartName);
                        mPresenter.loadChartNames();
                        return true;
                    case R.id.item_share:
                        mPresenter.prepareChartToShare(chartName);
                        return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }


    public void shareChart(String chartName) {


        Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);
        canvas.drawLine(0, 0, 200, 200, paint);

        String fileName = "fileName.png";
        File imageFileToShare = new File(getFilesDir(), fileName);

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(imageFileToShare);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, out);
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Uri uri = ChartProvider.getUriForFile(this, "com.fed.chartprovider", imageFileToShare);

        Intent share = new Intent(Intent.ACTION_SEND);

        share.setDataAndType(uri, this.getContentResolver().getType(uri));
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setClipData(ClipData.newUri(getContentResolver(), getString(R.string.app_name), uri));
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(share);

    }


}
