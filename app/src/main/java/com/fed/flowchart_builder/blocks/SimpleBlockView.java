package com.fed.flowchart_builder.blocks;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fed.flowchart_builder.FlowChartViewGroup;
import com.fed.flowchart_builder.R;

public abstract class SimpleBlockView extends View {
    private static final String TAG = "SimpleBlockViewTAG";

    private static final float RECT_SIZE_COEF = 0.8f;

    private boolean mIsSelected;
    float mParentScale;

    private RectF mRect;
    private RectF mFrameRect;
    private Paint mPaint;
    private Paint mFramePaint;
    private Paint mFrameFillPaint;

    private int mColorStroke;
    private float mWidth;
    private float mHeight;
    private float mStrokeWidth;
    private float mCurStrokeWidth;
    private float mIconSize;
    private float mDistanceBetweenIconAndRound;
    private PointF mTranslation;

    private int mColorStrokeFrame;
    private float mStrokeWidthFrame;
    private Point mPosition = new Point();

    private GestureDetector mGestureDetector;

    private Drawable mDeleteIcon;
    private RectF mDeleteIconRect;
    private Drawable mResizeIcon;
    private RectF mResizeIconRect;
    private BlockMode mBlockMode = BlockMode.FREE;
//    PointF mLastTouch = new PointF();

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
        if (mBlockMode == BlockMode.READY_TO_MOVING) {
            mBlockMode = BlockMode.MOVING;
            MotionEvent cancel = MotionEvent.obtain(event);
            cancel.setAction(MotionEvent.ACTION_CANCEL);
            mGestureDetector.onTouchEvent(cancel);

        }
        if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
            if (mBlockMode != BlockMode.FREE) {
                mBlockMode = BlockMode.FREE;
                ((FlowChartViewGroup) getParent()).setMode(FlowChartViewGroup.ViewGroupMode.FREE, SimpleBlockView.this);
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
        initPaint();
        initFramePaints();
        initGestureDetector();

        mDeleteIcon = getResources().getDrawable(R.drawable.ic_delete_blue_24dp);
        mResizeIcon = getResources().getDrawable(R.drawable.ic_resize_blue_24dp);

        mIconSize = getResources().getDimension(R.dimen.icon_block_size);
        mDistanceBetweenIconAndRound = getResources().getDimension(R.dimen.distance_between_icon_and_round);
        mTranslation = new PointF();
    }


    public void reSize(float w, float h) {
        initGeom(w, h);
        initPaint();
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

    }


    private void initGestureDetector() {

        GestureDetector.SimpleOnGestureListener simpleOnGestureListener =
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        if (mIsSelected) {
                            if (isInRect(mDeleteIconRect, e.getX(), e.getY())) {
                                deleteSelf();
                            }

                        }
                        setIsSelected(!mIsSelected);

                        invalidate();
                        return true;
                    }

                    @Override
                    public boolean onDown(MotionEvent e) {
                        if (mIsSelected) {
                            if (!isInRect(mDeleteIconRect, e.getX(), e.getY())) {
                                mBlockMode = BlockMode.MOVING;
                            }
                            ((FlowChartViewGroup) getParent()).setMode(FlowChartViewGroup.ViewGroupMode.CHILD_IN_ACTION, SimpleBlockView.this);
//                            Toast.makeText(getContext(), "onDown", Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        mBlockMode = BlockMode.READY_TO_MOVING;
                        ((FlowChartViewGroup) getParent()).setMode(FlowChartViewGroup.ViewGroupMode.CHILD_IN_ACTION, SimpleBlockView.this);
//                        Toast.makeText(getContext(), "onLongPress", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        float scale = ((FlowChartViewGroup) getParent()).getCurrentScale();
                        translate((e1.getX() - e2.getX()) / scale, (e1.getY() - e2.getY()) / scale);
                        getParent().requestLayout();
                        return true;
                    }
                };
        mGestureDetector = new GestureDetector(getContext(), simpleOnGestureListener);
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


    public void drawSelected(Canvas canvas) {
        canvas.drawRect(mFrameRect, mFramePaint);
        drawIcon(canvas, mDeleteIcon, mDeleteIconRect);
        drawIcon(canvas, mResizeIcon, mResizeIconRect);
    }
    public abstract void drawUnSelected(Canvas canvas);

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
        }
    }
    public boolean isSelected(){
        return mIsSelected;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    enum BlockMode {
        FREE,
        READY_TO_MOVING,
        MOVING;
    }
}
