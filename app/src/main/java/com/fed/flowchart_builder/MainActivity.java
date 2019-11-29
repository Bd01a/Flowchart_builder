package com.fed.flowchart_builder;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.fed.flowchart_builder.blocks.OperationBlockView;
import com.fed.flowchart_builder.fragments.BlockCreateFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private FlowChartViewGroup mFlowChartViewGroup;
    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFlowChartViewGroup = findViewById(R.id.flowchart_view_group);
        OperationBlockView operationBlockView = findViewById(R.id.operation_block);
        OperationBlockView operationBlockView1 = findViewById(R.id.operation_block1);

        final BlockCreateFragment blockCreateFragment = new BlockCreateFragment(new BlockCreateFragment.OnActivityRequest() {
            @Override
            public FlowChartViewGroup getFlowChartViewGroup() {
                return mFlowChartViewGroup;
            }
        });

        mFloatingActionButton = findViewById(R.id.btn_block_create);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .add(R.id.additional_functionality_fragment, blockCreateFragment)
                        .commit();
                mFloatingActionButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mFloatingActionButton.setVisibility(View.VISIBLE);
    }
}
