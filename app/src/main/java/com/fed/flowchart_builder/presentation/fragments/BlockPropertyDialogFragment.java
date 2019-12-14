package com.fed.flowchart_builder.presentation.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fed.flowchart_builder.R;
import com.fed.flowchart_builder.presentation.adapters.ColorArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class BlockPropertyDialogFragment extends DialogFragment {

    public static final String ARG_POSITIVE_CLICK = "positeve_click";
    public static final String ARG_TEXT = "text";
    public static final String ARG_WIDTH = "width";
    public static final String ARG_HEIGHT = "height";
    public static final String ARG_STROKE_WIDTH = "stroke_width";
    public static final String ARG_TEXT_SIZE = "text_size";
    public static final String ARG_TEXT_COLOR = "tet_color";
    public static final String ARG_STROKE_COLOR = "stroke_color";


    public static BlockPropertyDialogFragment newInstance(String text, float width, float height, float strokeWidth,
                                                          float textSize, int textColor, int strokeColor,
                                                          OnPositiveClick onPositiveClick) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_POSITIVE_CLICK, onPositiveClick);
        args.putString(ARG_TEXT, text);
        args.putFloat(ARG_WIDTH, width);
        args.putFloat(ARG_HEIGHT, height);
        args.putFloat(ARG_TEXT_SIZE, textSize);
        args.putFloat(ARG_STROKE_WIDTH, strokeWidth);
        args.putInt(ARG_TEXT_COLOR, textColor);
        args.putInt(ARG_STROKE_COLOR, strokeColor);

        BlockPropertyDialogFragment fragment = new BlockPropertyDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_block_settings_layout, null, false);
        final EditText textEditText = view.findViewById(R.id.edittext_block_settings_text);
        final EditText widthEditText = view.findViewById(R.id.edittext_block_settings_width);
        final EditText heightEditText = view.findViewById(R.id.edittext_block_settings_height);
        final EditText strokeWidthEditText = view.findViewById(R.id.edittext_block_settings_stroke_width);
        final EditText textSizeEditText = view.findViewById(R.id.edittext_block_settings_text_size);

        final Spinner colorSpinner = view.findViewById(R.id.spinner_block_settings_color);
        final Spinner textColorSpinner = view.findViewById(R.id.spinner_block_settings_text_color);


        int[] objects = getResources().getIntArray(R.array.available_colors);
        List<Integer> colors = new ArrayList<>();
        int positionTextColor = 0;
        int positionColorStroke = 0;
        for (int object : objects) {
            colors.add(object);
            if (getArguments().getInt(ARG_TEXT_COLOR) == object) {
                positionTextColor = colors.size() - 1;
            }
            if (getArguments().getInt(ARG_STROKE_COLOR) == object) {
                positionColorStroke = colors.size() - 1;
            }
        }
        ColorArrayAdapter colorArrayAdapter = new ColorArrayAdapter(getContext(), android.R.layout.simple_expandable_list_item_1,
                colors);
        colorSpinner.setAdapter(colorArrayAdapter);
        colorSpinner.setSelection(positionColorStroke);
        textColorSpinner.setAdapter(colorArrayAdapter);
        textColorSpinner.setSelection(positionTextColor);

        textEditText.setText(getArguments().getString(ARG_TEXT));
        widthEditText.setText(String.valueOf((int) getArguments().getFloat(ARG_WIDTH)));
        heightEditText.setText(String.valueOf((int) getArguments().getFloat(ARG_HEIGHT)));
        strokeWidthEditText.setText(String.valueOf((int) getArguments().getFloat(ARG_STROKE_WIDTH)));
        textSizeEditText.setText(String.valueOf((int) getArguments().getFloat(ARG_TEXT_SIZE)));

        builder.setTitle(getResources().getString(R.string.dialog_block_settings_title));
        builder.setView(view);
        builder.setPositiveButton(R.string.text_positive_button_dialog_block_settings,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (getArguments() != null) {
                            OnPositiveClick onPositiveClick = getArguments().getParcelable(ARG_POSITIVE_CLICK);
                            if (onPositiveClick != null) {
                                onPositiveClick.onClick(textEditText, widthEditText, heightEditText,
                                        strokeWidthEditText, textSizeEditText, colorSpinner, textColorSpinner);
                            }
                        }
                    }
                });
        builder.setNegativeButton(R.string.text_negative_button_dialog_block_settings,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    public interface OnPositiveClick extends Parcelable {
        void onClick(EditText textEditText, EditText widthEditText, EditText heightEditText,
                     EditText strokeWidthEditText, EditText textSizeEditText, Spinner colorSpinner, Spinner textColorSpinner);
    }

}
