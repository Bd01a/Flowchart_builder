package com.fed.flowchart_builder.flowChartViews.blocks;

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
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.fed.flowchart_builder.R;
import com.fed.flowchart_builder.adapters.ColorArrayAdapter;
import com.fed.flowchart_builder.flowChartViews.FlowChartViewGroup;
import com.fed.flowchart_builder.flowChartViews.SavedStateChild;
import com.fed.flowchart_builder.flowChartViews.lines.SimpleLine;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleBlockView extends View implements SavedStateChild {
    private static final String TAG = "SimpleBlockViewTAG";

    private static final float RECT_SIZE_COEF = 1f;
    private static final float MIN_SIZE = 50f;
    private static final float MAX_SIZE = 800f;

    private boolean mIsSelected;


    private RectF mRect = new RectF();
    private RectF mFrameRect = new RectF();
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

    private float mStrokeWidthFrame;
    private Point mPosition = new Point();
    private float mParentScale;

    private GestureDetector mGestureDetector;

    private Drawable mDeleteIcon;
    private RectF mDeleteIconRect = new RectF();
    private Drawable mResizeIcon;
    private RectF mResizeIconRect = new RectF();
    private Drawable mAddLineIcon;
    private boolean mIsShowAddIcons;
    private RectF mAddLineIconRectLeft = new RectF();
    private RectF mAddLineIconRectTop = new RectF();
    private RectF mAddLineIconRectRight = new RectF();
    private RectF mAddLineIconRectBottom = new RectF();
    private BlockMode mBlockMode = BlockMode.FREE;

    private PointF geomOptions = new PointF(0, 0);


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
        if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
            if (mBlockMode != BlockMode.FREE) {
                mBlockMode = BlockMode.FREE;
                ((FlowChartViewGroup) getParent()).setMode(FlowChartViewGroup.ViewGroupMode.FREE);
            }
        }
        return retVal ;
    }

    public float getStrokeWidth() {
        return mStrokeWidth;
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
        mAddLineIcon = getResources().getDrawable(R.drawable.ic_add_circle_outline_blue_24dp);

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

    public float getOriginalWidth() {
        return mWidth;
    }

    public float getOriginalHeight() {
        return mHeight;
    }

    public void updateGeomOptions() {
        mParentScale = ((FlowChartViewGroup) getParent()).getCurrentScale();

        mRect.left = 0;
        mRect.top = 0;
        mRect.right = mWidth * mParentScale;
        mRect.bottom = mHeight * mParentScale;

        float widthBeetwenFrameAndBlock = getResources().getDimension(R.dimen.width_between_frame_and_block);
        mCurStrokeWidth = mStrokeWidth * mParentScale;

        mFrameRect.left = mRect.left - widthBeetwenFrameAndBlock;
        mFrameRect.top = mRect.top - widthBeetwenFrameAndBlock;
        mFrameRect.right = mRect.right + widthBeetwenFrameAndBlock;
        mFrameRect.bottom = mRect.bottom + widthBeetwenFrameAndBlock;

        mDeleteIconRect.left = mFrameRect.left - mIconSize / 2;
        mDeleteIconRect.top = mFrameRect.top - mIconSize / 2;
        mDeleteIconRect.right = mFrameRect.left + mIconSize / 2;
        mDeleteIconRect.bottom = mFrameRect.top + mIconSize / 2;

        mResizeIconRect.left = mFrameRect.right - mIconSize / 2;
        mResizeIconRect.top = mFrameRect.bottom - mIconSize / 2;
        mResizeIconRect.right = mFrameRect.right + mIconSize / 2;
        mResizeIconRect.bottom = mFrameRect.bottom + mIconSize / 2;

        mAddLineIconRectBottom.left = mFrameRect.left + mFrameRect.width() / 2 - mIconSize / 2;
        mAddLineIconRectBottom.top = mFrameRect.bottom - mIconSize / 2;
        mAddLineIconRectBottom.right = mFrameRect.left + mFrameRect.width() / 2 + mIconSize / 2;
        mAddLineIconRectBottom.bottom = mFrameRect.bottom + mIconSize / 2;

        mAddLineIconRectTop.left = mFrameRect.left + mFrameRect.width() / 2 - mIconSize / 2;
        mAddLineIconRectTop.top = mFrameRect.top - mIconSize / 2;
        mAddLineIconRectTop.right = mFrameRect.left + mFrameRect.width() / 2 + mIconSize / 2;
        mAddLineIconRectTop.bottom = mFrameRect.top + mIconSize / 2;

        mAddLineIconRectLeft.left = mFrameRect.left - mIconSize / 2;
        mAddLineIconRectLeft.top = mFrameRect.top + mFrameRect.height() / 2 - mIconSize / 2;
        mAddLineIconRectLeft.right = mFrameRect.left + mIconSize / 2;
        mAddLineIconRectLeft.bottom = mFrameRect.top + mFrameRect.height() / 2 + mIconSize / 2;

        mAddLineIconRectRight.left = mFrameRect.right - mIconSize / 2;
        mAddLineIconRectRight.top = mFrameRect.top + mFrameRect.height() / 2 - mIconSize / 2;
        mAddLineIconRectRight.right = mFrameRect.right + mIconSize / 2;
        mAddLineIconRectRight.bottom = mFrameRect.top + mFrameRect.height() / 2 + mIconSize / 2;

        mTranslation.x = widthBeetwenFrameAndBlock + mIconSize / 2 + mStrokeWidthFrame;
        mTranslation.y = widthBeetwenFrameAndBlock + mIconSize / 2 + mStrokeWidthFrame;
        mCurTextSize = mTextSize * mParentScale;
        initPaint();
        initTextPaint();

        geomOptions.x = mRect.width() + 2 * mTranslation.x;
        geomOptions.y = mRect.height() + 2 * mTranslation.y;
    }

    public PointF getGeomOptions() {
        return geomOptions;
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
                        if (mIsSelected || mIsShowAddIcons) {
                            if (isInRect(mAddLineIconRectBottom, e.getX(), e.getY())) {
                                ((FlowChartViewGroup) getParent()).getLineManager().addBlock(SimpleBlockView.this,
                                        SimpleLine.BlockSide.BOTTOM);
                                return true;
                            } else if (isInRect(mAddLineIconRectTop, e.getX(), e.getY())) {
                                ((FlowChartViewGroup) getParent()).getLineManager().addBlock(SimpleBlockView.this,
                                        SimpleLine.BlockSide.TOP);
                                return true;
                            } else if (isInRect(mAddLineIconRectRight, e.getX(), e.getY())) {
                                ((FlowChartViewGroup) getParent()).getLineManager().addBlock(SimpleBlockView.this,
                                        SimpleLine.BlockSide.RIGHT);
                                return true;
                            } else if (isInRect(mAddLineIconRectLeft, e.getX(), e.getY())) {
                                ((FlowChartViewGroup) getParent()).getLineManager().addBlock(SimpleBlockView.this,
                                        SimpleLine.BlockSide.LEFT);
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
                            } else if (!isInRect(mDeleteIconRect, e.getX(), e.getY()) &&
                                    !isInRect(mAddLineIconRectBottom, e.getX(), e.getY()) &&
                                    !isInRect(mAddLineIconRectTop, e.getX(), e.getY()) &&
                                    !isInRect(mAddLineIconRectLeft, e.getX(), e.getY()) &&
                                    !isInRect(mAddLineIconRectRight, e.getX(), e.getY())) {
                                mBlockMode = BlockMode.MOVING;
                                ((FlowChartViewGroup) getParent()).setMode(FlowChartViewGroup.ViewGroupMode.CHILD_IN_ACTION);
                            }
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
                            resizeRect(distanceX / scale, distanceY / scale);
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

        final Spinner colorSpinner = view.findViewById(R.id.spinner_block_settings_color);
        final Spinner textColorSpinner = view.findViewById(R.id.spinner_block_settings_text_color);

        int[] objects = getResources().getIntArray(R.array.available_colors);
        List<Integer> colors = new ArrayList<>();
        int positionTextColor = 0;
        int positionColorStroke = 0;
        for (int object : objects) {
            colors.add(object);
            if (mTextColor == object) {
                positionTextColor = colors.size() - 1;
            }
            if (mColorStroke == object) {
                positionColorStroke = colors.size() - 1;
            }
        }
        ColorArrayAdapter colorArrayAdapter = new ColorArrayAdapter(getContext(), android.R.layout.simple_expandable_list_item_1,
                colors);
        colorSpinner.setAdapter(colorArrayAdapter);
        colorSpinner.setSelection(positionColorStroke);
        textColorSpinner.setAdapter(colorArrayAdapter);
        textColorSpinner.setSelection(positionTextColor);

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
                        mColorStroke = (int) (colorSpinner.getSelectedItem());
                        mTextColor = (int) (textColorSpinner.getSelectedItem());
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
        requestLayout();
    }

    private void resizeRect(float x, float y) {
        mWidth -= x / RECT_SIZE_COEF;
        mHeight -= y / RECT_SIZE_COEF;
        specifyBorders();

    }


    private void deleteSelf() {
        FlowChartViewGroup viewGroup = (FlowChartViewGroup) getParent();
        viewGroup.removeView(this);
        viewGroup.checkLines();
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
        int mColorStrokeFrame = getResources().getColor(R.color.color_stroke_frame);

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
        } else if (mIsShowAddIcons) {
            drawAddLineIcons(canvas);
        }
    }

    public void isAddLineIconsShow(boolean isShow) {
        mIsShowAddIcons = isShow;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMeasured = (int) (mWidth * RECT_SIZE_COEF + mStrokeWidth + mStrokeWidthFrame + mIconSize);
        int heightMeasured = (int) (mHeight * RECT_SIZE_COEF + mStrokeWidth + mStrokeWidthFrame + mIconSize);
        setMeasuredDimension(resolveSize(widthMeasured, widthMeasureSpec),
                resolveSize(heightMeasured, heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        reSize(w, h);
        super.onSizeChanged(w, h, oldw, oldh);
    }


    private void drawSelected(Canvas canvas) {
        canvas.drawRect(mFrameRect, mFramePaint);
        drawIcon(canvas, mDeleteIcon, mDeleteIconRect);
        drawIcon(canvas, mResizeIcon, mResizeIconRect);
        drawAddLineIcons(canvas);
    }

    private void drawAddLineIcons(Canvas canvas) {
        drawIcon(canvas, mAddLineIcon, mAddLineIconRectLeft);
        drawIcon(canvas, mAddLineIcon, mAddLineIconRectTop);
        drawIcon(canvas, mAddLineIcon, mAddLineIconRectRight);
        drawIcon(canvas, mAddLineIcon, mAddLineIconRectBottom);
    }

    public void drawUnSelected(Canvas canvas) {
        mCurText = new StringBuilder(mText);
        float textWidth = mTextPaint.measureText(mCurText.toString());
        while (textWidth >= mRect.width() && mCurText.length() > 1) {
            mCurText.delete(0, 1);
            mCurText.delete(mCurText.length() - 1, mCurText.length());
            textWidth = mTextPaint.measureText(mCurText.toString());
        }
        if (mCurText.length() == 1 && mTextPaint.measureText(mCurText.toString()) < mRect.width()) {
            return;
        }
        if ((mTextPaint.getTextSize()) > mRect.height()) {
            return;
        }
        float textX = mRect.centerX() - textWidth / 2;
        float textY = mRect.centerY() - (mTextPaint.ascent() + mTextPaint.descent()) / 2;
        canvas.drawText(mCurText.toString(), textX, textY, mTextPaint);
    }


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


    public void setIsSelected(boolean isSelected) {
        mIsSelected = isSelected;
        if (!isSelected) {
            mBlockMode = BlockMode.FREE;
            invalidate();
        } else {
            getParent().bringChildToFront(this);
        }
    }

    public boolean isSelected() {
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
        } else {
            return true;
        }
    }

    public void saveState(FlowChartViewGroup.FlowChartSavedState ss) {

        ss.mColorStroke.add(mColorStroke);
        ss.mWidth.add(mWidth);
        ss.mHeight.add(mHeight);
        ss.mStrokeWidth.add(mStrokeWidth);
        ss.mTextColor.add(mTextColor);
        ss.mPositionX.add(mPosition.x);
        ss.mPositionY.add(mPosition.y);
        if (this instanceof OperationBlockView) {
            ss.mBlockType.add(FlowChartViewGroup.FlowChartSavedState.OPERATION_BLOCK);
        } else if (this instanceof ConditionBlockView) {
            ss.mBlockType.add(FlowChartViewGroup.FlowChartSavedState.CONDITION_BLOCK);
        }
        ss.mText.add(mText);
        ss.mTextSize.add(mTextSize);
    }

    public void restoreState(FlowChartViewGroup.FlowChartSavedState ss, int index) {
        mColorStroke = ss.mColorStroke.get(index);
        mWidth = ss.mWidth.get(index);
        mHeight = ss.mHeight.get(index);
        mStrokeWidth = ss.mStrokeWidth.get(index);
        mTextColor = ss.mTextColor.get(index);
        mPosition.x = ss.mPositionX.get(index);
        mPosition.y = ss.mPositionY.get(index);
        mText = ss.mText.get(index);
        mTextSize = ss.mTextSize.get(index);
    }


    enum BlockMode {
        FREE,
        MOVING,
        RESIZE
    }


}
