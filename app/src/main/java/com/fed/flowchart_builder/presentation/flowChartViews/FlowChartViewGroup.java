package com.fed.flowchart_builder.presentation.flowChartViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import com.fed.flowchart_builder.data.BlockDescription;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.SimpleBlockView;
import com.fed.flowchart_builder.presentation.flowChartViews.lines.LineManager;
import com.fed.flowchart_builder.presentation.flowChartViews.lines.SimpleLineView;
import com.fed.flowchart_builder.presentation.fragments.BlockPropertyDialogFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * ViewGroup that holds {@link SimpleBlockView} and {@link SimpleLineView}
 * @author Fedorov Sergey
 */
public class FlowChartViewGroup extends ViewGroup {
    private static final String TAG = "ViewGroupTag";

    private static final float MIN_SCALE = 0.1f;
    private static final float MAX_SCALE = 6f;
    private ViewGroupMode mMode = ViewGroupMode.FREE;
    private SimpleBlockView mCurrentSelectedView;
    private static final int MAX_BORDER = 1000;

    /**
     * radius in which the event is not considered ACTION_MOVE
     */
    private static final float TOUCH_SLOP = 0.5f;

    private GestureDetector mGestureDetector;
    private Scroller mScroller;
    private ScaleGestureDetector mScaleGestureDetector;

    private float mCurrentScale = 1;

    /**
     * Borders for drawing and scrolling
     */
    private Rect mBorderViewGroup = new Rect(-MAX_BORDER, -MAX_BORDER, MAX_BORDER, MAX_BORDER);

    private boolean mIsScrolling;
    /**
     * true until the first onScroll call in one event
     */
    private boolean mIsFirstScroll;

    /**
     * saves coordinates of ACTION_DOWN
     */
    private PointF mDownPosition;

    /**
     * controls all {@link SimpleLineView}
     */
    private LineManager mLineManager;

    /**
     * contract for dialog of for transmission from {@link SimpleBlockView} to Activity
     */
    private StartDialogContract mStartDialogContract;

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

    public StartDialogContract getStartDialogContract() {
        return mStartDialogContract;
    }

    public void setStartDialogContract(StartDialogContract startDialogContract) {
        mStartDialogContract = startDialogContract;
    }

    /**
     * initialization of fields
     */
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


    public void setSelectedBlockView(@Nullable SimpleBlockView view) {
        if ((view == null && mCurrentSelectedView != null) ||
                (view != null && !view.equals(mCurrentSelectedView) && mCurrentSelectedView != null)) {
            mCurrentSelectedView.setIsSelected(false);
            invalidate();
        }
        if (view != null) {
            mLineManager.setSelectedLineView(null);
        }
        mCurrentSelectedView = view;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        boolean retVal = mScaleGestureDetector.onTouchEvent(event);
        retVal = mGestureDetector.onTouchEvent(event) || retVal;
        return retVal || super.onTouchEvent(event);
    }


    /**
     * need to create bitmap
     */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
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
                if (xDiff > TOUCH_SLOP) {
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

//                    @Override
//                    public void onLongPress(MotionEvent e) {
//                        super.onLongPress(e);
//
//                        mLineManager.showDeleteIcons(!mLineManager.isDrawDeleteIcons());
//                    }

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
                child.invalidate();
                child.layout(childL, childT, childR, childB);
            } else if (child instanceof SimpleLineView) {
                float width = ((SimpleLineView) child).getLineWidth();
                float height = ((SimpleLineView) child).getLineHeight();
                float positionX = ((SimpleLineView) child).getLineX();
                float positionY = ((SimpleLineView) child).getLineY();
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
            if (getChildAt(i) instanceof SimpleBlockView) {
                Parcelable parcelable = ((SimpleBlockView) getChildAt(i)).onSaveInstanceState();
                ss.mBlocks.add(parcelable);
            } else if (getChildAt(i) instanceof SimpleLineView) {
                Parcelable parcelable = ((SimpleLineView) getChildAt(i)).onSaveInstanceState();
                ss.mLines.add(parcelable);
            }

        }

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        FlowChartSavedState ss = (FlowChartSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);

        scrollTo(ss.mScrollX - (displaySize.x - displaySize.y) / 2, ss.mScrollY - (displaySize.y - displaySize.x) / 2);
        mCurrentScale = ss.mCurrentScale;

        for (int i = 0; i < ss.mBlocks.size(); i++) {
            BlockDescription[] blockDescriptions = BlockDescription.values();
            for (BlockDescription description : blockDescriptions) {
                SimpleBlockView.BlockSavedState savedState = (SimpleBlockView.BlockSavedState) ss.mBlocks.get(i);
                if (savedState.getBlockType() == description.getId()) {
                    SimpleBlockView blockView = description.getBlock(getContext());
                    addView(blockView);
                    blockView.onRestoreInstanceState(savedState);
                }
            }
        }
        mLineManager = new LineManager(getContext(), this);
        for (int i = 0; i < ss.mLines.size(); i++) {
            SimpleLineView.LineSavedState savedState = (SimpleLineView.LineSavedState) ss.mLines.get(i);
            mLineManager.restoreLine(i, savedState);
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


        int mScrollX;
        int mScrollY;
        float mCurrentScale;

        List<Parcelable> mBlocks = new ArrayList<>();
        List<Parcelable> mLines = new ArrayList<>();


        FlowChartSavedState(Parcelable superState) {
            super(superState);
        }

        @SuppressWarnings("unchecked")
        private FlowChartSavedState(Parcel in, ClassLoader classLoader) {
            super(in);
            mScrollX = in.readInt();
            mScrollY = in.readInt();
            mCurrentScale = in.readFloat();

            mBlocks = in.readArrayList(getClass().getClassLoader());
            mLines = in.readArrayList(getClass().getClassLoader());


        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);

            out.writeInt(mScrollX);
            out.writeInt(mScrollY);
            out.writeFloat(mCurrentScale);

            out.writeList(mBlocks);
            out.writeList(mLines);

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


    public interface StartDialogContract {
        void onStart(String text, float width, float height, float strokeWidth,
                     float textSize, int textColor, int strokeColor,
                     BlockPropertyDialogFragment.OnPositiveClick onPositiveClick);
    }


}
