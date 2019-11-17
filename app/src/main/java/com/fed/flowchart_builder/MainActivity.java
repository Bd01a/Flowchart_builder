package com.fed.flowchart_builder;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PointF;
import android.os.Bundle;

import com.fed.flowchart_builder.blocks.OperationBlockView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OperationBlockView operationBlockView = findViewById(R.id.operation_block);
        OperationBlockView operationBlockView1 = findViewById(R.id.operation_block1);
    }
}
