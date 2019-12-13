package com.fed.flowchart_builder.presentation.flowChartViews;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.fed.flowchart_builder.data.BlockDescription;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.ConditionBlockView;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.CycleBlockView;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.InletBlockView;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.OperationBlockView;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.SimpleBlockView;
import com.fed.flowchart_builder.presentation.flowChartViews.lines.LineManager;
import com.fed.flowchart_builder.presentation.flowChartViews.lines.SimpleLine;

import java.util.ArrayList;


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

    public void showAllAddLineIcons(boolean isShow) {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof SimpleBlockView) {
                ((SimpleBlockView) getChildAt(i)).isAddLineIconsShow(isShow);
                getChildAt(i).invalidate();
            }
        }
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
                    public void onLongPress(MotionEvent e) {
                        super.onLongPress(e);

                        mLineManager.showDeleteIcons(!mLineManager.isDrawDeleteIcons());
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

    public void checkLines() {
        mLineManager.checkBlocks();
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
                float width = ((SimpleLine) child).getLineWidth();
                float height = ((SimpleLine) child).getLineHeight();
                float positionX = ((SimpleLine) child).getLineX();
                float positionY = ((SimpleLine) child).getLineY();
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


    public int getNumberBlockChild(SimpleBlockView view) {
        int number = 0;
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof SimpleBlockView) {
                if (getChildAt(i).equals(view)) {
                    return number;
                }
                number++;
            }
        }
        return -1;
    }


    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        FlowChartSavedState ss = new FlowChartSavedState(superState);

        ss.mScrollX = getScrollX();
        ss.mScrollY = getScrollY();
        ss.mCurrentScale = mCurrentScale;

        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof SavedStateChild) {
                ((SavedStateChild) getChildAt(i)).saveState(ss);
            }

        }

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        FlowChartSavedState ss = (FlowChartSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        scrollTo(ss.mScrollX, ss.mScrollY);
        mCurrentScale = ss.mCurrentScale;

        for (int i = 0; i < ss.mBlockType.size(); i++) {
            if (ss.mBlockType.get(i) == BlockDescription.OPERATION_BLOCK.getId()) {
                OperationBlockView blockView = new OperationBlockView(getContext());
                addView(blockView);
                blockView.restoreState(ss, i);
            } else if (ss.mBlockType.get(i) == BlockDescription.CONDITION_BLOCK.getId()) {
                ConditionBlockView blockView = new ConditionBlockView(getContext());
                addView(blockView);
                blockView.restoreState(ss, i);
            } else if (ss.mBlockType.get(i) == BlockDescription.CYCLE_BLOCK.getId()) {
                CycleBlockView blockView = new CycleBlockView(getContext());
                addView(blockView);
                blockView.restoreState(ss, i);
            } else if (ss.mBlockType.get(i) == BlockDescription.INLET_BLOCK.getId()) {
                InletBlockView blockView = new InletBlockView(getContext());
                addView(blockView);
                blockView.restoreState(ss, i);
            }
        }
        mLineManager = new LineManager(getContext(), this);
        for (int i = 0; i < ss.mSide1.size(); i++) {
            SimpleBlockView blockView1 = (SimpleBlockView) getChildAt(i + ss.mNumBlock1.get(i));
            SimpleBlockView blockView2 = (SimpleBlockView) getChildAt(i + ss.mNumBlock2.get(i));

            SimpleLine.BlockSide side1 = ss.mSide1.get(i);
            SimpleLine.BlockSide side2 = ss.mSide2.get(i);

            mLineManager.addBlock(blockView1, side1);
            mLineManager.addBlock(blockView2, side2);

        }

    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    public enum ViewGroupMode {
        FREE,
        CHILD_IN_ACTION
    }

    public static class FlowChartSavedState extends BaseSavedState {


        public ArrayList<Float> mWidth = new ArrayList<>();
        public ArrayList<Float> mHeight = new ArrayList<>();
        public ArrayList<Float> mStrokeWidth = new ArrayList<>();
        public ArrayList<Float> mTextSize = new ArrayList<>();
        public ArrayList<Integer> mColorStroke = new ArrayList<>();
        public ArrayList<Integer> mTextColor = new ArrayList<>();
        public ArrayList<Integer> mPositionX = new ArrayList<>();
        public ArrayList<Integer> mPositionY = new ArrayList<>();
        public ArrayList<Integer> mBlockType = new ArrayList<>();
        public ArrayList<String> mText = new ArrayList<>();

        public ArrayList<Integer> mNumBlock1 = new ArrayList<>();
        public ArrayList<Integer> mNumBlock2 = new ArrayList<>();
        public ArrayList<SimpleLine.BlockSide> mSide1 = new ArrayList<>();
        public ArrayList<SimpleLine.BlockSide> mSide2 = new ArrayList<>();

        int mScrollX;
        int mScrollY;
        float mCurrentScale;


        FlowChartSavedState(Parcelable superState) {
            super(superState);
        }

        @SuppressWarnings("unchecked")
        private FlowChartSavedState(Parcel in, ClassLoader classLoader) {
            super(in);
            mScrollX = in.readInt();
            mScrollY = in.readInt();
            mCurrentScale = in.readFloat();

            mWidth = in.readArrayList(getClass().getClassLoader());
            mHeight = in.readArrayList(getClass().getClassLoader());
            mStrokeWidth = in.readArrayList(getClass().getClassLoader());
            mTextSize = in.readArrayList(getClass().getClassLoader());
            mColorStroke = in.readArrayList(getClass().getClassLoader());
            mTextColor = in.readArrayList(getClass().getClassLoader());
            mPositionX = in.readArrayList(getClass().getClassLoader());
            mPositionY = in.readArrayList(getClass().getClassLoader());
            mBlockType = in.readArrayList(getClass().getClassLoader());
            mText = in.readArrayList(getClass().getClassLoader());

            mNumBlock1 = in.readArrayList(getClass().getClassLoader());
            mNumBlock2 = in.readArrayList(getClass().getClassLoader());
            mSide1 = in.readArrayList(getClass().getClassLoader());
            mSide2 = in.readArrayList(getClass().getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);

            out.writeInt(mScrollX);
            out.writeInt(mScrollY);
            out.writeFloat(mCurrentScale);

            out.writeList(mWidth);
            out.writeList(mHeight);
            out.writeList(mStrokeWidth);
            out.writeList(mTextSize);
            out.writeList(mColorStroke);
            out.writeList(mTextColor);
            out.writeList(mPositionX);
            out.writeList(mPositionY);
            out.writeList(mBlockType);
            out.writeList(mText);

            out.writeList(mNumBlock1);
            out.writeList(mNumBlock2);
            out.writeList(mSide1);
            out.writeList(mSide2);

        }

        public static final ClassLoaderCreator<FlowChartSavedState> CREATOR
                = new ClassLoaderCreator<FlowChartSavedState>() {
            @Override
            public FlowChartSavedState createFromParcel(Parcel source, ClassLoader loader) {
                return new FlowChartSavedState(source, loader);
            }

            @Override
            public FlowChartSavedState createFromParcel(Parcel source) {
                return createFromParcel(source, null);
            }

            public FlowChartSavedState[] newArray(int size) {
                return new FlowChartSavedState[size];
            }
        };
    }


}