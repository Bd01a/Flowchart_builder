package com.fed.flowchart_builder.presentation.flowChartViews.blocks;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fed.flowchart_builder.R;
import com.fed.flowchart_builder.data.ChartRoom.ChartBlock;
import com.fed.flowchart_builder.presentation.flowChartViews.FlowChartViewGroup;
import com.fed.flowchart_builder.presentation.flowChartViews.lines.SimpleLineView;
import com.fed.flowchart_builder.presentation.fragments.BlockPropertyDialogFragment;

/**
 * View that displays a block, the basic structural unit of a block diagram.
 *
 * @author Sergey Fedorov
 * @attr ref R.styleable.SimpleBlockView_width
 * @attr ref R.styleable.SimpleBlockView_height
 * @attr ref R.styleable.SimpleBlockView_color_stroke
 * @attr ref R.styleable.SimpleBlockView_stroke_width
 * @attr ref R.styleable.SimpleBlockView_x
 * @attr ref R.styleable.SimpleBlockView_y
 * @attr ref R.styleable.SimpleBlockView_text_size
 * @attr ref R.styleable.SimpleBlockView_text_color
 * @attr ref R.styleable.SimpleBlockView_text
 */
public abstract class SimpleBlockView extends View {
    private static final String TAG = "SimpleBlockViewTAG";


    /**
     * minimum possible block width
     */
    private static final float MIN_SIZE = 50f;
    /**
     * minimum possible block height
     */
    private static final float MAX_SIZE = 800f;

    /**
     * if true, auxiliary marking is drawn and the buttons are clickable
     */
    private boolean mIsSelected;


    /**
     * Rectangle designating block boundaries
     */
    private RectF mRect = new RectF();
    /**
     * Rectangle denoting the boundaries of the auxiliary frame of the block
     */
    private RectF mFrameRect = new RectF();
    /**
     * Paint for drawing block
     */
    private Paint mPaint;
    /**
     * Paint for drawing auxiliary lines
     */
    private Paint mFramePaint;
    /**
     * Paint for fill icons
     */
    private Paint mFrameFillPaint;
    /**
     * Paint for drawing text
     */
    private Paint mTextPaint;

    /**
     * Color for {@link SimpleBlockView#mPaint}
     *
     * @attr ref R.styleable.SimpleBlockView_color_stroke
     */
    private int mColorStroke;

    /**
     * absolute width of {@link SimpleBlockView#mRect}
     *
     * @attr ref R.styleable.SimpleBlockView_width
     */
    private float mWidth;

    /**
     * absolute height of {@link SimpleBlockView#mRect}
     *
     * @attr ref R.styleable.SimpleBlockView_height
     */
    private float mHeight;

    /**
     * Stroke width for {@link SimpleBlockView#mPaint}
     *
     * @attr ref R.styleable.SimpleBlockView_color_stroke
     */
    private float mStrokeWidth;

    /**
     * {@link SimpleBlockView#mStrokeWidth} with considering scaling
     */
    private float mCurStrokeWidth;

    /**
     * Color for {@link SimpleBlockView#mPaint}
     * @attr ref R.styleable.SimpleBlockView_text_color
     *
     */
    private int mTextColor;

    private float mIconSize;
    /**
     * distance beetween icon and stroke around icon
     */
    private float mDistanceBetweenIconAndRound;

    /**
     * translation of canvas
     */
    private PointF mTranslation;


    /**
     * text that is displayed in the middle of the block
     * @attr ref R.styleable.SimpleBlockView_text
     */
    private String mText;

    /**
     * text size for {@link SimpleBlockView#mTextPaint}
     * @attr ref R.styleable.SimpleBlockView_text_size
     */
    private float mTextSize;

    /**
     * used for transformations of {@link SimpleBlockView#mText} with lack of space
     */
    private StringBuilder mCurText = new StringBuilder();


    /**
     * {@link SimpleBlockView#mTextSize}  with considering scaling
     */
    private float mCurTextSize;

    /**
     * stroke width for {@link SimpleBlockView#mFramePaint}
     */
    private float mStrokeWidthFrame;


    /**
     * Absolute coordinates of this block
     * @attr ref R.styleable.SimpleBlockView_x
     * @attr ref R.styleable.SimpleBlockView_y
     */
    private Point mPosition = new Point();
    private float mParentScale;

    private GestureDetector mGestureDetector;

    private Drawable mDeleteIcon;
    /**
     * define borders of {@link SimpleBlockView#mDeleteIcon}
     */
    private RectF mDeleteIconRect = new RectF();
    private Drawable mResizeIcon;
    /**
     * define borders of {@link SimpleBlockView#mResizeIcon}
     */
    private RectF mResizeIconRect = new RectF();
    private Drawable mAddLineIcon;

    /**
     * if true {@link SimpleBlockView#mAddLineIcon} must be painted
     */
    private boolean mIsShowAddIcons;
    /**
     * define borders of left {@link SimpleBlockView#mAddLineIcon}
     */
    private RectF mAddLineIconRectLeft = new RectF();
    /**
     * define borders of top {@link SimpleBlockView#mAddLineIcon}
     */
    private RectF mAddLineIconRectTop = new RectF();
    /**
     * define borders of right {@link SimpleBlockView#mAddLineIcon}
     */
    private RectF mAddLineIconRectRight = new RectF();
    /**
     * define borders of bottom {@link SimpleBlockView#mAddLineIcon}
     */
    private RectF mAddLineIconRectBottom = new RectF();
    /**
     * define what is currently view doing
     */
    private BlockMode mBlockMode = BlockMode.FREE;

    /**
     * defines sizes of view
     */
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


    /**
     * checks {@link SimpleBlockView#mWidth} and {@link SimpleBlockView#mHeight} for {@link SimpleBlockView#MAX_SIZE}
     * and {@link SimpleBlockView#MIN_SIZE}, and assign true values
     */
    private void specifyBorders() {
        mWidth = mWidth < MIN_SIZE ? MIN_SIZE : mWidth;
        mHeight = mHeight < MIN_SIZE ? MIN_SIZE : mHeight;

        mWidth = mWidth > MAX_SIZE ? MAX_SIZE : mWidth;
        mHeight = mHeight > MAX_SIZE ? MAX_SIZE : mHeight;
    }

    /**
     * @return absolute width of block
     */
    public float getOriginalWidth() {
        return mWidth;
    }

    /**
     * @return absolute height of block
     */
    public float getOriginalHeight() {
        return mHeight;
    }

    /**
     * defines all geometric paremeters with considering scaling
     */
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
                                        SimpleLineView.BlockSide.BOTTOM);
                                return true;
                            } else if (isInRect(mAddLineIconRectTop, e.getX(), e.getY())) {
                                ((FlowChartViewGroup) getParent()).getLineManager().addBlock(SimpleBlockView.this,
                                        SimpleLineView.BlockSide.TOP);
                                return true;
                            } else if (isInRect(mAddLineIconRectRight, e.getX(), e.getY())) {
                                ((FlowChartViewGroup) getParent()).getLineManager().addBlock(SimpleBlockView.this,
                                        SimpleLineView.BlockSide.RIGHT);
                                return true;
                            } else if (isInRect(mAddLineIconRectLeft, e.getX(), e.getY())) {
                                ((FlowChartViewGroup) getParent()).getLineManager().addBlock(SimpleBlockView.this,
                                        SimpleLineView.BlockSide.LEFT);
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

    /**
     * creates {@link android.app.AlertDialog} that allows to change block parameters
     */
    private void dialogSettingsCreate() {
        ((FlowChartViewGroup) getParent()).getStartDialogContract().onStart(mText, mWidth, mHeight, mStrokeWidth, mTextSize,
                mTextColor, mColorStroke, new BlockPropertyDialogFragment.OnPositiveClick() {
                    @Override
                    public void onClick(EditText textEditText, EditText widthEditText, EditText heightEditText, EditText strokeWidthEditText, EditText textSizeEditText, Spinner colorSpinner, Spinner textColorSpinner) {
                        mText = textEditText.getText().toString();
                        mWidth = Float.valueOf(widthEditText.getText().toString());
                        mHeight = Float.valueOf(heightEditText.getText().toString());
                        mStrokeWidth = Float.valueOf(strokeWidthEditText.getText().toString());
                        mTextSize = Float.valueOf(textSizeEditText.getText().toString());
                        mColorStroke = (int) (colorSpinner.getSelectedItem());
                        mTextColor = (int) (textColorSpinner.getSelectedItem());
                        reInit();
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {

                    }
                });
    }

    /**
     * change current values due to scaling
     */
    private void reInit() {
        mCurTextSize = mTextSize * mParentScale;
        mCurStrokeWidth = mStrokeWidth * mParentScale;
        initPaint();
        initTextPaint();
        initFramePaints();
        specifyBorders();
        requestLayout();
    }

    /**
     * @param x delta x
     * @param y delta y
     */
    private void resizeRect(float x, float y) {
        mWidth -= x;
        mHeight -= y;
        specifyBorders();

    }


    /**
     * deletes self from childs of parents and call {@link FlowChartViewGroup#checkLines()}
     */
    private void deleteSelf() {
        FlowChartViewGroup viewGroup = (FlowChartViewGroup) getParent();
        viewGroup.removeView(this);
        viewGroup.checkLines();
    }

    /**
     * initializes {@link SimpleBlockView#mPaint}
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mCurStrokeWidth);
        mPaint.setColor(mColorStroke);
    }

    /**
     * initializes {@link SimpleBlockView#mTextPaint}
     */
    private void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mCurTextSize);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mTextColor);
    }

    /**
     * initializes {@link SimpleBlockView#mFramePaint} and {@link SimpleBlockView#mFrameFillPaint}
     */
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
    public void onDraw(Canvas canvas) {
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

        int widthMeasured = (int) (mWidth + mStrokeWidth + mStrokeWidthFrame + mIconSize);
        int heightMeasured = (int) (mHeight + mStrokeWidth + mStrokeWidthFrame + mIconSize);
        setMeasuredDimension(resolveSize(widthMeasured, widthMeasureSpec),
                resolveSize(heightMeasured, heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        reSize(w, h);
        super.onSizeChanged(w, h, oldw, oldh);
    }


    /**
     * drawing if {@link SimpleBlockView#mIsSelected} is true.
     * draws all icons and auxiliary frame
     */
    private void drawSelected(Canvas canvas) {
        canvas.drawRect(mFrameRect, mFramePaint);
        drawIcon(canvas, mDeleteIcon, mDeleteIconRect);
        drawIcon(canvas, mResizeIcon, mResizeIconRect);
        drawAddLineIcons(canvas);
    }

    /**
     * draws all icons
     */
    private void drawAddLineIcons(Canvas canvas) {
        drawIcon(canvas, mAddLineIcon, mAddLineIconRectLeft);
        drawIcon(canvas, mAddLineIcon, mAddLineIconRectTop);
        drawIcon(canvas, mAddLineIcon, mAddLineIconRectRight);
        drawIcon(canvas, mAddLineIcon, mAddLineIconRectBottom);
    }

    /**
     * draws {@link SimpleBlockView#mText}
     * needs to overrides for childs
     */
    public void drawUnSelected(Canvas canvas) {
        mCurText.append(mText);
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
        mCurText.delete(0, mCurText.length());
    }


    /**
     * draws icon
     * @param icon what will be painted
     * @param bounds boundaries of icon
     */
    private void drawIcon(Canvas canvas, Drawable icon, RectF bounds) {
        icon.setBounds((int) (bounds.left + mDistanceBetweenIconAndRound),
                (int) (bounds.top + mDistanceBetweenIconAndRound),
                (int) (bounds.right - mDistanceBetweenIconAndRound),
                (int) (bounds.bottom - mDistanceBetweenIconAndRound));
        canvas.drawOval(bounds, mFrameFillPaint);
        canvas.drawOval(bounds, mFramePaint);
        icon.draw(canvas);
    }

    /**
     * changes position
     * @param x delta x
     * @param y delta y
     */
    public void translate(float x, float y){
        mPosition.x -= x;
        mPosition.y -= y;
    }

    /**
     * determines if a point (x,y) lies in a rectangle with considering translation
     * @param rect rectangle
     * @param x x coordinate of point
     * @param y y coordinate of point
     * @return true if point in rectangle, false if not
     */
    private boolean isInRect(RectF rect, float x, float y) {
        return rect.contains(x - mTranslation.x, y - mTranslation.y);
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


    @Nullable
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        BlockSavedState ss = new BlockSavedState(parcelable);
        ss.mColorStroke = mColorStroke;
        ss.mWidth = mWidth;
        ss.mHeight = mHeight;
        ss.mStrokeWidth = mStrokeWidth;
        ss.mTextColor = mTextColor;
        ss.mPositionX = mPosition.x;
        ss.mPositionY = mPosition.y;

        ss.mBlockType = getSelfType();

        ss.mText = mText;
        ss.mTextSize = mTextSize;
        return ss;
    }

    /**
     * @return own parameters for saving in database
     */
    public ChartBlock save() {
        ChartBlock chartBlock = new ChartBlock();
        chartBlock.setColorStroke(mColorStroke);
        chartBlock.setWidth(mWidth);
        chartBlock.setHeight(mHeight);
        chartBlock.setStrokeWidth(mStrokeWidth);
        chartBlock.setTextColor(mTextColor);
        chartBlock.setPositionX(mPosition.x);
        chartBlock.setPositionY(mPosition.y);

        chartBlock.setBlockType(getSelfType());

        chartBlock.setText(mText);
        chartBlock.setTextSize(mTextSize);
        return chartBlock;
    }


    /**
     * @return type of this view with considering {@link com.fed.flowchart_builder.data.BlockDescription}
     */
    abstract int getSelfType();


    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        BlockSavedState ss = (BlockSavedState) state;
        mColorStroke = ss.mColorStroke;
        mWidth = ss.mWidth;
        mHeight = ss.mHeight;
        mStrokeWidth = ss.mStrokeWidth;
        mTextColor = ss.mTextColor;
        mPosition.x = ss.mPositionX;
        mPosition.y = ss.mPositionY;
        mText = ss.mText;
        mTextSize = ss.mTextSize;
    }

    /**
     * extracts attributes from {@link ChartBlock}
     * @param chartBlock objects that contains all attributes of this view
     */
    public void setAttr(ChartBlock chartBlock) {
        mColorStroke = chartBlock.getColorStroke();
        mWidth = chartBlock.getWidth();
        mHeight = chartBlock.getHeight();
        mStrokeWidth = chartBlock.getStrokeWidth();
        mTextColor = chartBlock.getTextColor();
        mPosition.x = chartBlock.getPositionX();
        mPosition.y = chartBlock.getPositionY();
        mText = chartBlock.getText();
        mTextSize = chartBlock.getTextSize();
    }


    enum BlockMode {
        FREE,
        MOVING,
        RESIZE
    }


    public static class BlockSavedState extends BaseSavedState {


        public static final ClassLoaderCreator<BlockSavedState> CREATOR
                = new ClassLoaderCreator<BlockSavedState>() {
            @Override
            public BlockSavedState createFromParcel(Parcel source, ClassLoader loader) {
                return new BlockSavedState(source, loader);
            }

            @Override
            public BlockSavedState createFromParcel(Parcel source) {
                return createFromParcel(source, null);
            }

            public BlockSavedState[] newArray(int size) {
                return new BlockSavedState[size];
            }
        };
        float mWidth;
        float mHeight;
        float mStrokeWidth;
        float mTextSize;
        int mColorStroke;
        int mTextColor;
        int mPositionX;
        int mPositionY;
        int mBlockType;
        String mText;

        BlockSavedState(Parcelable superState) {
            super(superState);
        }

        @SuppressWarnings("unchecked")
        private BlockSavedState(Parcel in, ClassLoader classLoader) {
            super(in);

            mWidth = in.readFloat();
            mHeight = in.readFloat();
            mStrokeWidth = in.readFloat();
            mTextSize = in.readFloat();
            mColorStroke = in.readInt();
            mTextColor = in.readInt();
            mPositionX = in.readInt();
            mPositionY = in.readInt();
            mBlockType = in.readInt();
            mText = in.readString();

        }

        public int getBlockType() {
            return mBlockType;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);

            out.writeFloat(mWidth);
            out.writeFloat(mHeight);
            out.writeFloat(mStrokeWidth);
            out.writeFloat(mTextSize);
            out.writeInt(mColorStroke);
            out.writeInt(mTextColor);
            out.writeInt(mPositionX);
            out.writeInt(mPositionY);
            out.writeInt(mBlockType);
            out.writeString(mText);

        }
    }

}
