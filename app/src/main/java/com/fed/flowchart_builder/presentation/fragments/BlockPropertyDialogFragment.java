package com.fed.flowchart_builder.presentation.fragments;

import androidx.fragment.app.DialogFragment;

public class BlockPropertyDialogFragment extends DialogFragment {

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

//            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//            View view = LayoutInflater.from(getContext())
//                    .inflate(R.layout.dialog_block_settings_layout, null, false);
//            final EditText textEditText = view.findViewById(R.id.edittext_block_settings_text);
//            final EditText widthEditText = view.findViewById(R.id.edittext_block_settings_width);
//            final EditText heightEditText = view.findViewById(R.id.edittext_block_settings_height);
//            final EditText strokeWidthEditText = view.findViewById(R.id.edittext_block_settings_stroke_width);
//            final EditText textSizeEditText = view.findViewById(R.id.edittext_block_settings_text_size);
//
//            final Spinner colorSpinner = view.findViewById(R.id.spinner_block_settings_color);
//            final Spinner textColorSpinner = view.findViewById(R.id.spinner_block_settings_text_color);
//
//            int[] objects = getResources().getIntArray(R.array.available_colors);
//            List<Integer> colors = new ArrayList<>();
//            int positionTextColor = 0;
//            int positionColorStroke = 0;
//            for (int object : objects) {
//                colors.add(object);
//                if (mTextColor == object) {
//                    positionTextColor = colors.size() - 1;
//                }
//                if (mColorStroke == object) {
//                    positionColorStroke = colors.size() - 1;
//                }
//            }
//            ColorArrayAdapter colorArrayAdapter = new ColorArrayAdapter(getContext(), android.R.layout.simple_expandable_list_item_1,
//                    colors);
//            colorSpinner.setAdapter(colorArrayAdapter);
//            colorSpinner.setSelection(positionColorStroke);
//            textColorSpinner.setAdapter(colorArrayAdapter);
//            textColorSpinner.setSelection(positionTextColor);
//
//            textEditText.setText(mText);
//            widthEditText.setText(String.valueOf((int) mWidth));
//            heightEditText.setText(String.valueOf((int) mHeight));
//            strokeWidthEditText.setText(String.valueOf((int) mStrokeWidth));
//            textSizeEditText.setText(String.valueOf((int) mTextSize));
//
//            builder.setTitle(getResources().getString(R.string.dialog_block_settings_title));
//            builder.setView(view);
//            builder.setPositiveButton(R.string.text_positive_button_dialog_block_settings,
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            mText = textEditText.getText().toString();
//                            mWidth = Float.valueOf(widthEditText.getText().toString());
//                            mHeight = Float.valueOf(heightEditText.getText().toString());
//                            mStrokeWidth = Float.valueOf(strokeWidthEditText.getText().toString());
//                            mTextSize = Float.valueOf(textSizeEditText.getText().toString());
//                            mColorStroke = (int) (colorSpinner.getSelectedItem());
//                            mTextColor = (int) (textColorSpinner.getSelectedItem());
//                            reInit();
//                        }
//                    });
//            builder.setNegativeButton(R.string.text_negative_button_dialog_block_settings,
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//        return builder.create();
//    }


}
