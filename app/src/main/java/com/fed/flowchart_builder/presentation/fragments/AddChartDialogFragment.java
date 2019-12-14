package com.fed.flowchart_builder.presentation.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fed.flowchart_builder.R;
import com.fed.flowchart_builder.presentation.view.ChartActivity;
import com.fed.flowchart_builder.presentation.view.MainActivity;

public class AddChartDialogFragment extends DialogFragment {
    private static final String ARG_CUTTON_CLICK = "button_click";


    public static AddChartDialogFragment newInstance(OnDialogButtonClick onDialogButtonClick) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_CUTTON_CLICK, onDialogButtonClick);

        AddChartDialogFragment fragment = new AddChartDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.title_dialog_add_chart));
        final EditText editText = new EditText(getContext());
        builder.setView(editText);
        final OnDialogButtonClick onDialogButtonClick = getArguments().getParcelable(ARG_CUTTON_CLICK);
        builder.setPositiveButton(R.string.text_positive_button_dialog_add_chart, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), ChartActivity.class);
                intent.putExtra(MainActivity.CHART_NAME, editText.getText().toString());
                startActivity(intent);
                onDialogButtonClick.onPositiveClick();

            }
        });
        builder.setNegativeButton(R.string.text_negative_button_dialog_add_chart, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        OnDialogButtonClick onDialogButtonClick = getArguments().getParcelable(ARG_CUTTON_CLICK);
        onDialogButtonClick.onNegativeClick();
    }

    public interface OnDialogButtonClick extends Parcelable {
        void onPositiveClick();

        void onNegativeClick();
    }


}
