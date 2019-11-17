package com.fed.flowchart_builder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.fed.flowchart_builder.blocks.SimpleBlockView;




public class FlowChartViewGroup extends ViewGroup {
    private static final String TAG = "ViewGroupTag";
    private static final int MAX_BORDER = 1000;

    private GestureDetector mGestureDetector;
    private Scroller mScroller;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mCurrentScale = 1;

    private Rect mBorderViewGroup = new Rect(-MAX_BORDER, -MAX_BORDER, MAX_BORDER, MAX_BORDER);

    private boolean mIsScrolling;
    private boolean mIsFirstScroll;
    private float mTouchSlop = 0.5f;
    private PointF mDownPosition;

    public FlowChartViewGroup(Context context) {
        super(context);
        init();
    }

    public FlowChartViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlowChartViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        initGestureDetector();
        initScaleGestureDetector();
    }

    private void initScaleGestureDetector() {
        ScaleGestureDetector.SimpleOnScaleGestureListener simpleOnScaleGestureListener =
                new ScaleGestureDetector.SimpleOnScaleGestureListener(){
                    @Override
                    public boolean onScale(ScaleGestureDetector detector) {
                        float scaleFactor = detector.getScaleFactor();
                        float newScale = (float)Math.max(0.2, Math.min(2,scaleFactor*mCurrentScale));
                        mCurrentScale = newScale;
                        invalidate();
                        return true;
                    }
                };
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), simpleOnScaleGestureListener);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.scale(mCurrentScale, mCurrentScale);
        super.dispatchDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean retVal = mScaleGestureDetector.onTouchEvent(event);
        retVal = mGestureDetector.onTouchEvent(event) || retVal;
        return retVal || super.onTouchEvent(event);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mIsScrolling = false;
            return false;
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) mScroller.abortAnimation();
                mDownPosition = new PointF(ev.getX(), ev.getY());
                mIsScrolling = false;
                mIsFirstScroll=true;
                break;
            case MotionEvent.ACTION_MOVE: {
                if (mIsScrolling) {
                    return true;
                }
                final float xDiff = calculateDistance(ev);
                if (xDiff > mTouchSlop) {
                    mIsScrolling = true;
                    return true;
                }
                break;
            }
        }

        return false;
    }

    private float calculateDistance(MotionEvent ev) {
        return (float)Math.sqrt(Math.pow(mDownPosition.x-ev.getX(), 2)+Math.pow(mDownPosition.y-ev.getY(),2));
    }

    private void initGestureDetector() {
        mScroller = new Scroller(getContext());
        GestureDetector.SimpleOnGestureListener simpleOnGestureListener =
                new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        if(mIsFirstScroll){
                            mIsFirstScroll = false;
                            distanceX = -e2.getX()+mDownPosition.x;
                            distanceY = -e2.getY()+mDownPosition.y;
                        }
                            scrollBy((int) distanceX, (int) distanceY);
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        mScroller.fling(getScrollX(), getScrollY(), -(int)velocityX, -(int)velocityY,
                                mBorderViewGroup.left, mBorderViewGroup.right,
                                mBorderViewGroup.top, mBorderViewGroup.bottom
                                );

                        return true;
                    }

                };
        mGestureDetector = new GestureDetector(getContext(), simpleOnGestureListener);
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            scrollTo(x, y);
            if (oldX != getScrollX() || oldY != getScrollY())
            {
                onScrollChanged(getScrollX(), getScrollY(), oldX, oldY);
            }
            postInvalidate();
        }

    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if(child instanceof SimpleBlockView) {
                final SimpleBlockView simpleBlockView = (SimpleBlockView)child;
                final int childL = simpleBlockView.getPosition().x-simpleBlockView.getMeasuredWidth()/2;
                final int childT = simpleBlockView.getPosition().y - simpleBlockView.getMeasuredHeight()/2;
                final int childR = child.getMeasuredWidth() + childL;
                final int childB = child.getMeasuredHeight() + childT;
                child.layout(childL, childT, childR, childB);

            }

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                measureChild(child, widthMeasureSpec,  heightMeasureSpec);
            }
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec));
    }


}
