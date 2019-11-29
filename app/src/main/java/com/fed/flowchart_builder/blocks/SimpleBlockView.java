package com.fed.flowchart_builder.blocks;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.fed.flowchart_builder.FlowChartViewGroup;
import com.fed.flowchart_builder.R;
import com.fed.flowchart_builder.adapters.ColorArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleBlockView extends View {
    private static final String TAG = "SimpleBlockViewTAG";

    private static final float RECT_SIZE_COEF = 0.8f;
    private static final float MIN_SIZE = 50f;
    private static final float MAX_SIZE = 800f;

    private boolean mIsSelected;


    private RectF mRect;
    private RectF mFrameRect;
    private Paint mPaint;
    private Paint mFramePaint;
    private Paint mFrameFillPaint;
    private Paint mTextPaint;

    private int mColorStroke;
    private float mWidth;
    private float mHeight;
    private float mStrokeWidth;
    private float mCurStrokeWidth;
    //    private float mTextStrokeWidth;
    private int mTextColor;
    private float mIconSize;
    private float mDistanceBetweenIconAndRound;
    private PointF mTranslation;
    private String mText;
    private StringBuilder mCurText;
    private float mTextSize;
    private float mCurTextSize;

    private int mColorStrokeFrame;
    private float mStrokeWidthFrame;
    private Point mPosition = new Point();
    private float mParentScale;

    private GestureDetector mGestureDetector;

    private Drawable mDeleteIcon;
    private RectF mDeleteIconRect;
    private Drawable mResizeIcon;
    private RectF mResizeIconRect;
    private BlockMode mBlockMode = BlockMode.FREE;

    public SimpleBlockView(Context context) {
        super(context);
        init(context,null);
    }

    public SimpleBlockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public SimpleBlockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean retVal = mGestureDetector.onTouchEvent(event);
        Log.d("resize", "touch event " + mBlockMode.toString());
        if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
            if (mBlockMode != BlockMode.FREE) {
                mBlockMode = BlockMode.FREE;
                ((FlowChartViewGroup) getParent()).setMode(FlowChartViewGroup.ViewGroupMode.FREE);
            }
        }
        return retVal ;
    }

    public Point getPosition(){
        return mPosition;
    }

    public RectF getRect(){
        return mRect;
    }

    public Paint getPaint(){
        return mPaint;
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs){
        extractAttributes(context,attrs);
        mCurTextSize = mTextSize;
        initPaint();
        initTextPaint();
        initFramePaints();
        initGestureDetector();

        mDeleteIcon = getResources().getDrawable(R.drawable.ic_delete_blue_24dp);
        mResizeIcon = getResources().getDrawable(R.drawable.ic_resize_blue_24dp);

        mIconSize = getResources().getDimension(R.dimen.icon_block_size);
        mDistanceBetweenIconAndRound = getResources().getDimension(R.dimen.distance_between_icon_and_round);
        mTranslation = new PointF();
        specifyBorders();
    }


    private void specifyBorders() {
        mWidth = mWidth < MIN_SIZE ? MIN_SIZE : mWidth;
        mHeight = mHeight < MIN_SIZE ? MIN_SIZE : mHeight;

        mWidth = mWidth > MAX_SIZE ? MAX_SIZE : mWidth;
        mHeight = mHeight > MAX_SIZE ? MAX_SIZE : mHeight;
    }


    public void reSize(float w, float h) {
        initGeom(w, h);
        initPaint();
        initTextPaint();
    }

    private void initGeom(float w, float h) {
        mParentScale = ((FlowChartViewGroup) getParent()).getCurrentScale();
        mRect = new RectF(0, 0,
                mWidth * RECT_SIZE_COEF * mParentScale, mHeight * RECT_SIZE_COEF * mParentScale);
        mCurStrokeWidth = mStrokeWidth * mParentScale;
        mTranslation = new PointF((w - mRect.right) / 2, (h - mRect.bottom) / 2);
        PointF widthBetweenIconAndBlock = new PointF(mTranslation.x / 2 - mStrokeWidthFrame / 2,
                mTranslation.y / 2 - mStrokeWidthFrame / 2);
        mFrameRect = new RectF(mRect.left - widthBetweenIconAndBlock.x, mRect.top - widthBetweenIconAndBlock.y,
                mRect.right + widthBetweenIconAndBlock.x, mRect.bottom + widthBetweenIconAndBlock.y);
        mDeleteIconRect = new RectF(-mTranslation.x + mStrokeWidthFrame / 2, -mTranslation.y + mStrokeWidthFrame / 2,
                -mTranslation.x + mStrokeWidthFrame / 2 + mIconSize, -mTranslation.y + mStrokeWidthFrame / 2 + mIconSize);
        mResizeIconRect = new RectF(w - mTranslation.x - mStrokeWidthFrame / 2 - mIconSize, h - mTranslation.y - mStrokeWidthFrame / 2 - mIconSize,
                w - mTranslation.x - mStrokeWidthFrame / 2, h - mTranslation.y - mStrokeWidthFrame / 2);

        mCurTextSize = mTextSize * mParentScale;
    }


    private void initGestureDetector() {

        GestureDetector.SimpleOnGestureListener simpleOnGestureListener =
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        if (mIsSelected) {
                            if (isInRect(mDeleteIconRect, e.getX(), e.getY())) {
                                deleteSelf();
                                return true;
                            }


                        }
                        setIsSelected(!mIsSelected);
                        if (mIsSelected) {
                            ((FlowChartViewGroup) getParent()).selectChild(SimpleBlockView.this);
                        } else {
                            ((FlowChartViewGroup) getParent()).setMode(FlowChartViewGroup.ViewGroupMode.FREE);
                        }

                        invalidate();
                        return true;
                    }

                    @Override
                    public boolean onDown(MotionEvent e) {
                        if (mIsSelected) {
                            if (isInRect(mResizeIconRect, e.getX(), e.getY())) {
                                mBlockMode = BlockMode.RESIZE;
                                ((FlowChartViewGroup) getParent()).setMode(FlowChartViewGroup.ViewGroupMode.CHILD_IN_ACTION);
                            } else if (!isInRect(mDeleteIconRect, e.getX(), e.getY())) {
                                mBlockMode = BlockMode.MOVING;
                                ((FlowChartViewGroup) getParent()).setMode(FlowChartViewGroup.ViewGroupMode.CHILD_IN_ACTION);
                            }
//                            ((FlowChartViewGroup) getParent()).setMode(FlowChartViewGroup.ViewGroupMode.CHILD_IN_ACTION, SimpleBlockView.this);
//                            Toast.makeText(getContext(), "onDown", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        dialogSettingsCreate();
                    }

                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        float scale = ((FlowChartViewGroup) getParent()).getCurrentScale();
                        if (mBlockMode == BlockMode.MOVING) {
                            translate((e1.getX() - e2.getX()) / scale, (e1.getY() - e2.getY()) / scale);
                            requestLayout();
                        } else if (mBlockMode == BlockMode.RESIZE) {
                            resizeRect(distanceX, distanceY);
                            requestLayout();
                        }
                        return true;
                    }
                };
        mGestureDetector = new GestureDetector(getContext(), simpleOnGestureListener);
    }

    private void dialogSettingsCreate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_block_settings_layout, null, false);
        final EditText textEditText = view.findViewById(R.id.edittext_block_settings_text);
        final EditText widthEditText = view.findViewById(R.id.edittext_block_settings_width);
        final EditText heightEditText = view.findViewById(R.id.edittext_block_settings_height);
        final EditText strokeWidthEditText = view.findViewById(R.id.edittext_block_settings_stroke_width);
        final EditText textSizeEditText = view.findViewById(R.id.edittext_block_settings_text_size);

        Spinner colorSpinner = view.findViewById(R.id.spinner_block_settings_color);
        Spinner textColorSpinner = view.findViewById(R.id.spinner_block_settings_text_color);

        int[] objects = getResources().getIntArray(R.array.available_colors);
        List<Integer> colors = new ArrayList<>();
        for (int object :
                objects) {
            colors.add(new Integer(object));
        }
        ColorArrayAdapter colorArrayAdapter = new ColorArrayAdapter(getContext(), android.R.layout.simple_expandable_list_item_1,
                colors);
        colorSpinner.setAdapter(colorArrayAdapter);
        textColorSpinner.setAdapter(colorArrayAdapter);

        textEditText.setText(mText);
        widthEditText.setText(String.valueOf((int) mWidth));
        heightEditText.setText(String.valueOf((int) mHeight));
        strokeWidthEditText.setText(String.valueOf((int) mStrokeWidth));
        textSizeEditText.setText(String.valueOf((int) mTextSize));

        builder.setTitle(getResources().getString(R.string.dialog_block_settings_title));
        builder.setView(view);
        builder.setPositiveButton(R.string.text_positive_button_dialog_block_settings,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mText = textEditText.getText().toString();
                        mWidth = Float.valueOf(widthEditText.getText().toString());
                        mHeight = Float.valueOf(heightEditText.getText().toString());
                        mStrokeWidth = Float.valueOf(strokeWidthEditText.getText().toString());
                        mTextSize = Float.valueOf(textSizeEditText.getText().toString());
                        reInit();
                    }
                });
        builder.setNegativeButton(R.string.text_negative_button_dialog_block_settings,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private void reInit() {
        mCurTextSize = mTextSize * mParentScale;
        mCurStrokeWidth = mStrokeWidth * mParentScale;
        initPaint();
        initTextPaint();
        initFramePaints();
        specifyBorders();
        invalidate();
    }

    private void resizeRect(float x, float y) {
        mWidth -= x / RECT_SIZE_COEF;
        mHeight -= y / RECT_SIZE_COEF;
        specifyBorders();

    }


    private void deleteSelf() {
        ((FlowChartViewGroup) getParent()).removeView(this);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mCurStrokeWidth);
        mPaint.setColor(mColorStroke);
    }

    private void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mCurTextSize);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setColor(mTextColor);
    }

    private void initFramePaints() {
        mStrokeWidthFrame = getResources().getDimension(R.dimen.stroke_width_frame_block);
        mColorStrokeFrame = getResources().getColor(R.color.color_stroke_frame);

        mFramePaint = new Paint();
        mFramePaint.setStyle(Paint.Style.STROKE);
        mFramePaint.setAntiAlias(true);
        mFramePaint.setStrokeWidth(mStrokeWidthFrame);
        mFramePaint.setColor(mColorStrokeFrame);

        mFrameFillPaint = new Paint();
        mFrameFillPaint.setStyle(Paint.Style.FILL);
        mFrameFillPaint.setAntiAlias(true);
        int colorBackground = getResources().getColor(R.color.color_background);
        mFrameFillPaint.setColor(colorBackground);
    }

    private void extractAttributes(@NonNull Context context, @Nullable AttributeSet attrs) {
        final Resources.Theme theme = context.getTheme();
        final TypedArray typedArray = theme.obtainStyledAttributes(attrs, R.styleable.SimpleBlockView,
                0, R.style.SimpleBlockDefaultStyle);

        try {
            mColorStroke = typedArray.getInteger(R.styleable.SimpleBlockView_color_stroke, 0);
            mWidth = typedArray.getDimension(R.styleable.SimpleBlockView_width, 0);
            mHeight = typedArray.getDimension(R.styleable.SimpleBlockView_height, 0);
            mStrokeWidth = typedArray.getDimension(R.styleable.SimpleBlockView_stroke_width, 0);
            mPosition.x = typedArray.getInteger(R.styleable.SimpleBlockView_x, 0);
            mPosition.y = typedArray.getInteger(R.styleable.SimpleBlockView_y, 0);
            mText = typedArray.getString(R.styleable.SimpleBlockView_text);
            mTextSize = typedArray.getDimensionPixelSize(R.styleable.SimpleBlockView_text_size, 0);
            mTextColor = typedArray.getColor(R.styleable.SimpleBlockView_text_color, Color.BLACK);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mTranslation.x, mTranslation.y);
        drawUnSelected(canvas);
        if (mIsSelected) {
            drawSelected(canvas);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMeasured = (int) (mWidth * RECT_SIZE_COEF + mStrokeWidthFrame + mIconSize);
        int heightMeasured = (int) (mHeight * RECT_SIZE_COEF + mStrokeWidthFrame + mIconSize);

        setMeasuredDimension(resolveSize(widthMeasured, widthMeasureSpec),
                resolveSize(heightMeasured, heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        reSize(w, h);
        super.onSizeChanged(w, h, oldw, oldh);
    }


    private void drawSelected(Canvas canvas) {
        canvas.drawRect(mFrameRect, mFramePaint);
        drawIcon(canvas, mDeleteIcon, mDeleteIconRect);
        drawIcon(canvas, mResizeIcon, mResizeIconRect);
    }

    public void drawUnSelected(Canvas canvas) {
        mCurText = new StringBuilder(mText);
        float textWidth = mTextPaint.measureText(mCurText.toString());
        while (textWidth >= mRect.width() && mCurText.length() > 1) {
            mCurText.delete(0, 1);
            mCurText.delete(mCurText.length() - 1, mCurText.length());
            textWidth = mTextPaint.measureText(mCurText.toString());
        }
        if ((mTextPaint.getTextSize()) > mRect.height()) {
            return;
        }
        float textX = mRect.centerX() - textWidth / 2;
        float textY = mRect.centerY() - (mTextPaint.ascent() + mTextPaint.descent()) / 2;
        canvas.drawText(mCurText.toString(), textX, textY, mTextPaint);
    }

    ;

    private void drawIcon(Canvas canvas, Drawable icon, RectF bounds) {
        icon.setBounds((int) (bounds.left + mDistanceBetweenIconAndRound),
                (int) (bounds.top + mDistanceBetweenIconAndRound),
                (int) (bounds.right - mDistanceBetweenIconAndRound),
                (int) (bounds.bottom - mDistanceBetweenIconAndRound));
        canvas.drawOval(bounds, mFrameFillPaint);
        canvas.drawOval(bounds, mFramePaint);
        icon.draw(canvas);
    }

    public void translate(float x, float y){
        mPosition.x -= x;
        mPosition.y -= y;
    }

    private boolean isInRect(RectF rect, float x, float y) {
        float r = rect.right + mTranslation.x;
        float l = rect.left + mTranslation.x;
        float b = rect.bottom + mTranslation.y;
        float t = rect.top + mTranslation.y;
        return x > l && x < r && y < b && y > t;
    }



//    public abstract boolean isInBlock(float x, float y);
    public void setIsSelected(boolean isSelected){
        mIsSelected = isSelected;
        if (!isSelected) {
            mBlockMode = BlockMode.FREE;
            invalidate();
        }
    }
    public boolean isSelected(){
        return mIsSelected;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        SimpleBlockView view = (SimpleBlockView) obj;
        if (!(getPosition().equals(view.getPosition()))) {
            return false;
        } else if (mWidth != view.mWidth || mHeight != view.mHeight) {
            return false;
        } else if (mStrokeWidth != view.mStrokeWidth || mColorStroke != view.mColorStroke) {
            return false;
        }

        return true;
    }

    enum BlockMode {
        FREE,
        MOVING,
        RESIZE;
    }
}
