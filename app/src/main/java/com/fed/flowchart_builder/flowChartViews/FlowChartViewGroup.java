package com.fed.flowchart_builder.flowChartViews;

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

import com.fed.flowchart_builder.flowChartViews.blocks.SimpleBlockView;
import com.fed.flowchart_builder.flowChartViews.lines.LineManager;
import com.fed.flowchart_builder.flowChartViews.lines.SimpleLine;


public class FlowChartViewGroup extends ViewGroup {
    private static final String TAG = "ViewGroupTag";

    private static final float MIN_SCALE = 0.1f;
    private static final float MAX_SCALE = 6f;
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

    private LineManager mLineManager;

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

    private void init() {
        setSaveEnabled(true);
        initGestureDetector();
        initScaleGestureDetector();

        mLineManager = new LineManager(getContext(), this);
    }


    public LineManager getLineManager() {
        return mLineManager;
    }

    private void initScaleGestureDetector() {
        ScaleGestureDetector.SimpleOnScaleGestureListener simpleOnScaleGestureListener =
                new ScaleGestureDetector.SimpleOnScaleGestureListener() {
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
                mIsFirstScroll = true;
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
        return (float) Math.sqrt(Math.pow(mDownPosition.x - ev.getX(), 2) + Math.pow(mDownPosition.y - ev.getY(), 2));
    }

    private void initGestureDetector() {

        mScroller = new Scroller(getContext());
        GestureDetector.SimpleOnGestureListener simpleOnGestureListener =
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        if (mIsFirstScroll) {
                            mIsFirstScroll = false;
                            distanceX = -e2.getX() + mDownPosition.x;
                            distanceY = -e2.getY() + mDownPosition.y;

                        }
                        scrollBy((int) distanceX, (int) distanceY);
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        mScroller.fling(getScrollX(), getScrollY(), -(int) velocityX, -(int) velocityY,
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
        if (mScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            scrollTo(x, y);
            if (oldX != getScrollX() || oldY != getScrollY()) {
                onScrollChanged(getScrollX(), getScrollY(), oldX, oldY);
            }
            postInvalidate();
        }

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLineManager.update();
        for (int i = 0; i < getChildCount(); i++) {

            final View child = getChildAt(i);
            if (child instanceof SimpleBlockView) {
                final SimpleBlockView simpleBlockView = (SimpleBlockView) child;
                simpleBlockView.updateGeomOptions();
                int positionX = (int) (mCurrentScale * (simpleBlockView.getPosition().x));
                int positionY = (int) (mCurrentScale * (simpleBlockView.getPosition().y));
                int width = (int) ((SimpleBlockView) child).getGeomOptions().x;
                int height = (int) ((SimpleBlockView) child).getGeomOptions().y;
                final int childL = positionX - width / 2;
                final int childT = positionY - height / 2;
                final int childR = positionX + width / 2;
                final int childB = positionY + height / 2;
                child.layout(childL, childT, childR, childB);
            } else if (child instanceof SimpleLine) {
                float width = mCurrentScale * ((SimpleLine) child).getLineWidth();
                float height = mCurrentScale * ((SimpleLine) child).getLineHeight();
                float positionX = mCurrentScale * ((SimpleLine) child).getLineX();
                float positionY = mCurrentScale * ((SimpleLine) child).getLineY();
                final int childL = (int) positionX;
                final int childT = (int) positionY;
                final int childR = (int) (positionX + width);
                final int childB = (int) (positionY + height);
                child.invalidate();
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
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }


    public enum ViewGroupMode {
        FREE,
        CHILD_IN_ACTION;
    }

//
//    @Override
//    public Parcelable onSaveInstanceState() {
//        Parcelable superState = super.onSaveInstanceState();
//        SavedState ss = new SavedState(superState);
//        ss.childrenStates = new SparseArray();
//        ss.widthes = new ArrayList<>();
//        ss.heightes = new ArrayList<>();
//
//        Log.d(TAG, "save " + getChildCount());
//        for (int i = 0; i < getChildCount(); i++) {
//            Log.d(TAG, "" + getChildAt(i).isSaveEnabled());
//            if (getChildAt(i) instanceof SimpleBlockView) {
//
//                ss.widthes.add(((SimpleBlockView) getChildAt(i)).getOriginalWidth());
//                ss.widthes.add(((SimpleBlockView) getChildAt(i)).getOriginalHeight());
//            }
//            getChildAt(i).saveHierarchyState(ss.childrenStates);
//        }
//        return ss;
//    }
//
//    @Override
//    public void onRestoreInstanceState(Parcelable state) {
//        SavedState ss = (SavedState) state;
//        super.onRestoreInstanceState(ss.getSuperState());
//        Log.d(TAG, "restore " + getChildCount());
//        for (int i = 0; i < getChildCount(); i++) {
//            getChildAt(i).restoreHierarchyState(ss.childrenStates);
//        }
//    }
//
//    @Override
//    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
//        dispatchFreezeSelfOnly(container);
//    }
//
//    @Override
//    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
//        dispatchThawSelfOnly(container);
//    }
//
//    static class SavedState extends BaseSavedState {
//        SparseArray childrenStates;
//
//        List<Float> widthes;
//        List<Float> heightes;
//
//        SavedState(Parcelable superState) {
//            super(superState);
//        }
//
//        private SavedState(Parcel in, ClassLoader classLoader) {
//            super(in);
//            widthes = in.readArrayList(getClass().getClassLoader());
//            heightes = in.readArrayList(getClass().getClassLoader());
////            childrenStates = in.readSparseArray(classLoader);
//        }
//
//        @Override
//        public void writeToParcel(Parcel out, int flags) {
//            super.writeToParcel(out, flags);
//            out.writeList(widthes);
//            out.writeList(heightes);
////            out.writeSparseArray(childrenStates);
//        }
//
//        public static final ClassLoaderCreator<SavedState> CREATOR
//                = new ClassLoaderCreator<SavedState>() {
//            @Override
//            public SavedState createFromParcel(Parcel source, ClassLoader loader) {
//                return new SavedState(source, loader);
//            }
//
//            @Override
//            public SavedState createFromParcel(Parcel source) {
//                return createFromParcel(source, null);
//            }
//
//            public SavedState[] newArray(int size) {
//                return new SavedState[size];
//            }
//        };
//    }


}
