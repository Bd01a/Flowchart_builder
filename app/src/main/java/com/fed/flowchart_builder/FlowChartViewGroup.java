package com.fed.flowchart_builder;

import android.content.Context;
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

    private static final float MIN_SCALE = 0.2f;
    private static final float MAX_SCALE = 3f;
    private ViewGroupMode mMode = ViewGroupMode.FREE;
    private SimpleBlockView mCurrentSelectedView;
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
                        mCurrentScale = Math.max(MIN_SCALE, Math.min(MAX_SCALE, scaleFactor * mCurrentScale));
                        if (mCurrentScale < MAX_SCALE && mCurrentScale > MIN_SCALE) {
                            scrollTo((int) ((getScrollX() + detector.getFocusX()) * scaleFactor - detector.getFocusX()),
                                    (int) ((getScrollY() + detector.getFocusY()) * scaleFactor - detector.getFocusY()));
                        }
                        requestLayout();
                        return true;
                    }
                };
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), simpleOnScaleGestureListener);
    }

    public void setMode(ViewGroupMode mode) {
        mMode = mode;
    }

    public void selectChild(SimpleBlockView view) {
        if (!view.equals(mCurrentSelectedView) && mCurrentSelectedView != null) {
            mCurrentSelectedView.setIsSelected(false);
            invalidate();
        }
        mCurrentSelectedView = view;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            final float xDiff = calculateDistance(event);
            if (xDiff < mTouchSlop) {
//               mCurrentSelectedView.set
            }
        }

        boolean retVal = mScaleGestureDetector.onTouchEvent(event);
        retVal = mGestureDetector.onTouchEvent(event) || retVal;
        return retVal || super.onTouchEvent(event);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mMode != ViewGroupMode.FREE) {
            return false;
        }
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
                int poxitionX = (int) (mCurrentScale * (simpleBlockView.getPosition().x));
                int poxitionY = (int) (mCurrentScale * (simpleBlockView.getPosition().y));
//                int width = simpleBlockView.getMeasuredWidth();
//                int height = simpleBlockView.getMeasuredHeight();
//
//                float iconSize = getResources().getDimension(R.dimen.icon_block_size);
//                float widthStroke = getResources().getDimension(R.dimen.stroke_width_frame_block);
//                int iconSizeRequire = (int)(widthStroke+iconSize);
//
//                if (iconSizeRequire*2<width*mCurrentScale || iconSizeRequire*2<height*mCurrentScale) {
//                    width = (int) (mCurrentScale * width);
//                    height = (int) (mCurrentScale * height);
//
//                } else {
//                    if(iconSizeRequire*2<width*mCurrentScale) {
//                        width = 2 * iconSizeRequire;
//                    }
//                    if(iconSizeRequire*2<height*mCurrentScale) {
//                        height = 2 * iconSizeRequire;
//                    }
//                    simpleBlockView.reSize(width, height);
//                    simpleBlockView.invalidate();
//                }


//                if (mCurrentScale >= 1) {
//                    width = (int) (mCurrentScale * width);
//                    height = (int) (mCurrentScale * height);
//
//                }
//                else {
//                    simpleBlockView.reSize(width, height);
//                    simpleBlockView.invalidate();
//                }
                int width = (int) ((SimpleBlockView) child).getParams().x;
                int height = (int) ((SimpleBlockView) child).getParams().y;
                final int childL = poxitionX - width / 2;
                final int childT = poxitionY - height / 2;
                final int childR = poxitionX + width / 2;
                final int childB = poxitionY + height / 2;
                child.layout(childL, childT, childR, childB);

            }

        }
    }

    public float getCurrentScale() {
        return mCurrentScale;
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

    public enum ViewGroupMode {
        FREE,
        CHILD_IN_ACTION;
    }

}
