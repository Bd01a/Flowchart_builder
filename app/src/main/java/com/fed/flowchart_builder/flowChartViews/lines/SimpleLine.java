package com.fed.flowchart_builder.flowChartViews.lines;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fed.flowchart_builder.R;
import com.fed.flowchart_builder.flowChartViews.FlowChartViewGroup;
import com.fed.flowchart_builder.flowChartViews.blocks.SimpleBlockView;

import java.util.ArrayList;
import java.util.List;


public class SimpleLine extends View {

    private static final String TAG = "SimpleLine";
    private static final int DIRECTION_LEFT = 1;
    private static final int DIRECTION_RIGHT = 2;
    private static final int DIRECTION_TOP = 3;
    private static final int DIRECTION_BOTTOM = 4;

    private Paint mPaint;
    private List<PointF> mPoints;
    private float mStrokeWidth;
    private float mCurStrokeWidth;
    private int mStrokeColor;
    private FlowChartViewGroup mViewGroup;

    private float mWidth;
    private float mHeight;
    private float mX;
    private float mY;

    private float mDistanceBlockLine;

    private SimpleBlockView mBlock1;
    private BlockSide mSide1;
    private SimpleBlockView mBlock2;
    private BlockSide mSide2;

    public SimpleLine(Context context) {
        super(context);
        init(context, null);
    }

    public SimpleLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SimpleLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public float getLineWidth() {
        return mWidth;
    }

    public float getLineHeight() {
        return mHeight;
    }

    public float getLineX() {
        return mX;
    }

    public float getLineY() {
        return mY;
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        extractAttributes(context, attrs);
        mPoints = new ArrayList<>();
        mCurStrokeWidth = mStrokeWidth;
        mDistanceBlockLine = getResources().getDimension(R.dimen.distance_block_line);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mStrokeColor);
        mPaint.setStrokeWidth(mCurStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    public void addBlocks(SimpleBlockView block1, BlockSide side1, SimpleBlockView block2, BlockSide side2) {
        mBlock1 = block1;
        mBlock2 = block2;
        mSide1 = side1;
        mSide2 = side2;
        createLine();
    }

    private void createLine() {
        PointF firstPoint = determineStartPoint(mBlock1, mSide1);
        PointF lastPoint = determineStartPoint(mBlock2, mSide2);

        PointF start = createSecondPointNearBlock(firstPoint, mSide1);
        PointF end = createSecondPointNearBlock(lastPoint, mSide2);
        PointF nextStart = new PointF();
        PointF nextEnd = new PointF();


        mPoints.add(firstPoint);
        mPoints.add(start);
        float leftX2 = mBlock2.getPosition().x - mBlock2.getOriginalWidth() / 2 - mDistanceBlockLine;
        float rightX2 = mBlock2.getPosition().x + mBlock2.getOriginalWidth() / 2 + mDistanceBlockLine;

        float leftX1 = mBlock1.getPosition().x - mBlock1.getOriginalWidth() / 2 - mDistanceBlockLine;
        float rightX1 = mBlock1.getPosition().x + mBlock1.getOriginalWidth() / 2 + mDistanceBlockLine;

        float topY2 = mBlock2.getPosition().y - mBlock2.getOriginalHeight() / 2 - mDistanceBlockLine;
        float bottomY2 = mBlock2.getPosition().y + mBlock2.getOriginalHeight() / 2 + mDistanceBlockLine;

        float topY1 = mBlock1.getPosition().y - mBlock1.getOriginalHeight() / 2 - mDistanceBlockLine;
        float bottomY1 = mBlock1.getPosition().y + mBlock1.getOriginalHeight() / 2 + mDistanceBlockLine;
        nextStart.x = start.x;
        nextStart.y = start.y;
        nextEnd.x = end.x;
        nextEnd.y = end.y;

        switch (mSide1) {
            case LEFT:
                switch (mSide2) {
                    case BOTTOM:
                        leftToBottom(start, end, nextStart, nextEnd, leftX2, rightX2, leftX1, rightX1, topY2, bottomY2, topY1, bottomY1);
                        break;
                    case RIGHT:
                        leftToRight(start, end, nextStart, nextEnd, leftX2, rightX2, leftX1, rightX1, topY2, bottomY2, topY1, bottomY1);
                        break;
                    case TOP:
                        leftToTop(start, end, nextStart, nextEnd, leftX2, rightX2, leftX1, rightX1, topY2, bottomY2, topY1, bottomY1);
                        break;
                    case LEFT:
                        leftToLeft(start, end, nextStart, nextEnd, leftX2, rightX2, leftX1, rightX1, topY2, bottomY2, topY1, bottomY1);
                        break;
                }
                break;
            case TOP:
                switch (mSide2) {
                    case BOTTOM:
                        topToBottom(start, end, nextStart, nextEnd, leftX2, rightX2, leftX1, rightX1, topY2, bottomY2, topY1, bottomY1);
                        break;
                    case RIGHT:
                        topToRight(start, end, nextStart, nextEnd, leftX1, rightX1, rightX2, topY2, bottomY2, topY1);
                        break;
                    case TOP:
                        topToTop(start, end, nextStart, nextEnd, leftX2, rightX2, leftX1, rightX1, topY2, bottomY2, topY1);
                        break;
                    case LEFT:
                        topToLeft(start, end, nextStart, nextEnd, leftX2, leftX1, rightX1, topY2, bottomY2, topY1);
                        break;
                }
                break;
            case RIGHT:
                switch (mSide2) {
                    case BOTTOM:
                        rightToBottom(start, end, nextStart, nextEnd, leftX2, rightX2, leftX1, rightX1, topY2, bottomY2, topY1, bottomY1);
                        break;
                    case RIGHT:
                        rightToRight(start, end, nextStart, nextEnd, leftX2, rightX2, leftX1, rightX1, topY2, bottomY2, topY1, bottomY1);
                        break;
                    case TOP:
                        rightToTop(start, end, nextStart, nextEnd, leftX2, rightX2, leftX1, rightX1, topY2, bottomY2, topY1, bottomY1);
                        break;
                    case LEFT:
                        rightToLeft(start, end, nextStart, nextEnd, leftX2, rightX2, leftX1, rightX1, topY2, bottomY2, topY1, bottomY1);
                        break;
                }
                break;
            case BOTTOM:
                switch (mSide2) {
                    case BOTTOM:
                        bottomToBottom(start, end, nextStart, nextEnd, leftX2, rightX2, leftX1, rightX1, topY2, bottomY2, bottomY1, topY1);
                        break;
                    case RIGHT:
                        bottomToRight(start, end, nextStart, nextEnd, leftX1, rightX1, rightX2, topY2, bottomY2, bottomY1);
                        break;
                    case TOP:
                        bottomToTop(start, end, nextStart, nextEnd, leftX2, rightX2, leftX1, rightX1, topY2, bottomY1);
                        break;
                    case LEFT:
                        bottomToLeft(start, end, nextStart, nextEnd, leftX2, leftX1, rightX1, topY2, bottomY2, bottomY1);
                        break;
                }
                break;
        }

        mPoints.add(nextStart);
        mPoints.add(nextEnd);
        mPoints.add(end);
        mPoints.add(lastPoint);
        findMeasure();
    }

    private void bottomToBottom(PointF start, PointF end, PointF nextStart, PointF nextEnd, float leftX2, float rightX2, float leftX1, float rightX1, float topY2, float bottomY2, float bottomY1, float topY1) {
        if (bottomY1 <= bottomY2 - 2 * mDistanceBlockLine) {
            if (start.x < rightX2 && start.x > leftX2) {
                if (rightX2 - start.x > start.x - leftX2) {
                    nextStart.x = leftX2;
                    nextEnd.x = leftX2;
                } else {
                    nextStart.x = rightX2;
                    nextEnd.x = rightX2;
                }
            } else {
                nextEnd.x = start.x;
            }
        } else {
            if (rightX1 < end.x || leftX1 > end.x) {
                nextStart.x = end.x;
            } else {
                if (end.x > start.x) {
                    nextStart.x = rightX1;
                    nextEnd.x = rightX1;
                } else {
                    nextStart.x = leftX1;
                    nextEnd.x = leftX1;
                }
            }
        }
    }

    private void topToBottom(PointF start, PointF end, PointF nextStart, PointF nextEnd, float leftX2, float rightX2, float leftX1, float rightX1, float topY2, float bottomY2, float topY1, float bottomY1) {
        if (topY1 < bottomY2) {
            if (leftX1 > rightX2) {
                nextStart.x = rightX2 + (leftX1 - rightX2) / 2;
                nextEnd.x = nextStart.x;
            } else if (rightX1 < leftX2) {
                nextStart.x = rightX1 + (leftX2 - rightX1) / 2;
                nextEnd.x = nextStart.x;
            } else {
                if (start.x > end.x) {
                    nextStart.x = Math.max(rightX1, rightX2);
                    nextEnd.x = nextStart.x;
                } else {
                    nextStart.x = Math.min(leftX1, leftX2);
                    nextEnd.x = nextStart.x;
                }
            }
        } else {
            nextStart.y = bottomY2 + (topY1 - bottomY2) / 2;
            nextEnd.y = nextStart.y;
        }
    }

    private void rightToRight(PointF start, PointF end, PointF nextStart, PointF nextEnd, float leftX2, float rightX2, float leftX1, float rightX1, float topY2, float bottomY2, float topY1, float bottomY1) {
        if (rightX2 > rightX1) {
            if (start.y > bottomY2 || start.y < topY2) {
                nextStart.x = end.x;
            } else if (start.y < end.y) {
                nextStart.y = topY2;
                nextEnd.y = nextStart.y;
            } else {
                nextStart.y = bottomY2;
                nextEnd.y = nextStart.y;
            }
        } else {
            if (bottomY1 < end.y || topY1 > end.y) {
                nextStart.y = end.y;
            } else {
                if (end.y > start.y) {
                    nextStart.y = bottomY1;
                    nextEnd.y = nextStart.y;
                } else {
                    nextStart.y = topY1;
                    nextEnd.y = nextStart.y;
                }
            }
        }
    }

    private void leftToLeft(PointF start, PointF end, PointF nextStart, PointF nextEnd, float leftX2, float rightX2, float leftX1, float rightX1, float topY2, float bottomY2, float topY1, float bottomY1) {
        if (leftX2 < leftX1) {
            if (start.y > bottomY2 || start.y < topY2) {
                nextStart.x = end.x;
            } else if (start.y < end.y) {
                nextStart.y = topY2;
                nextEnd.y = nextStart.y;
            } else {
                nextStart.y = bottomY2;
                nextEnd.y = nextStart.y;
            }
        } else {
            if (bottomY1 < end.y || topY1 > end.y) {
                nextStart.y = end.y;
            } else {
                if (end.y > start.y) {
                    nextStart.y = bottomY1;
                    nextEnd.y = nextStart.y;
                } else {
                    nextStart.y = topY1;
                    nextEnd.y = nextStart.y;
                }
            }
        }
    }

    private void rightToLeft(PointF start, PointF end, PointF nextStart, PointF nextEnd, float leftX2, float rightX2, float leftX1, float rightX1, float topY2, float bottomY2, float topY1, float bottomY1) {
        if (rightX1 < leftX2 + 2 * mDistanceBlockLine && rightX1 > leftX2) {
            nextStart.x = rightX2 + (leftX1 - rightX2) / 2;
            nextEnd.x = nextStart.x;
            end.x = nextEnd.x;
            start.x = nextStart.x;
        } else if (rightX1 < leftX2) {
            nextStart.x = rightX1 + (leftX2 - rightX1) / 2;
            nextEnd.x = nextStart.x;
        } else if (topY1 > bottomY2) {
            nextStart.y = bottomY2 + (topY1 - bottomY2) / 2;
            nextEnd.y = nextStart.y;
        } else if (bottomY1 < topY2) {
            nextStart.y = bottomY1 + (topY2 - bottomY1) / 2;
            nextEnd.y = nextStart.y;
        } else {
            if (start.y < end.y) {
                nextStart.y = Math.min(topY1, topY2);
                nextEnd.y = nextStart.y;
            } else {
                nextStart.y = Math.max(bottomY1, bottomY2);
                nextEnd.y = nextStart.y;
            }
        }
    }

    private void leftToRight(PointF start, PointF end, PointF nextStart, PointF nextEnd, float leftX2, float rightX2, float leftX1, float rightX1, float topY2, float bottomY2, float topY1, float bottomY1) {
        if (leftX1 > rightX2 - 2 * mDistanceBlockLine && leftX1 < rightX2) {
            nextStart.x = rightX2 + (leftX1 - rightX2) / 2;
            nextEnd.x = nextStart.x;
            end.x = nextEnd.x;
            start.x = nextStart.x;
        } else if (leftX1 > rightX2) {
            nextStart.x = rightX2 + (leftX1 - rightX2) / 2;
            nextEnd.x = nextStart.x;
        } else if (topY1 > bottomY2) {
            nextStart.y = bottomY2 + (topY1 - bottomY2) / 2;
            nextEnd.y = nextStart.y;
        } else if (bottomY1 < topY2) {
            nextStart.y = bottomY1 + (topY2 - bottomY1) / 2;
            nextEnd.y = nextStart.y;
        } else {
            if (start.y < end.y) {
                nextStart.y = Math.min(topY1, topY2);
                nextEnd.y = nextStart.y;
            } else {
                nextStart.y = Math.max(bottomY1, bottomY2);
                nextEnd.y = nextStart.y;
            }
        }
    }

    private void rightToBottom(PointF start, PointF end, PointF nextStart, PointF nextEnd, float leftX2, float rightX2, float leftX1, float rightX1, float topY2, float bottomY2, float topY1, float bottomY1) {
        if (rightX1 < leftX2 + 2 * mDistanceBlockLine && rightX1 > leftX2) {
            if (start.y > end.y) {
                nextEnd.y = start.y;
            } else {
                nextStart.x = rightX2 + (leftX1 - rightX2) / 2;
                nextEnd.x = nextStart.x;
                start.x = nextStart.x;
            }
        } else if (rightX1 < leftX2) {
            if (start.y > end.y) {
                nextEnd.y = start.y;
            } else {
                nextStart.x = rightX2 + (leftX1 - rightX2) / 2;
                nextEnd.x = nextStart.x;
            }
        } else if (end.x >= start.x) {
            if (start.y > end.y - mDistanceBlockLine) {
                nextEnd.y = start.y;
            } else {
                nextStart.x = rightX2;
                nextEnd.x = nextStart.x;
            }
        } else {
            if (topY1 > bottomY2) {
                nextStart.y = bottomY2 + (topY1 - bottomY2) / 2;
                nextEnd.y = nextStart.y;
            } else if (bottomY1 < bottomY2) {
                nextStart.x = Math.max(rightX2, start.x);
                nextEnd.x = nextStart.x;
            } else {
                nextStart.y = bottomY1;
                nextEnd.y = nextStart.y;
            }
        }
    }

    private void rightToTop(PointF start, PointF end, PointF nextStart, PointF nextEnd, float leftX2, float rightX2, float leftX1, float rightX1, float topY2, float bottomY2, float topY1, float bottomY1) {
        if (rightX1 < leftX2 + 2 * mDistanceBlockLine && rightX1 > leftX2) {
            if (start.y < end.y) {
                nextEnd.y = start.y;
            } else {
                nextStart.x = rightX2 + (leftX1 - rightX2) / 2;
                nextEnd.x = nextStart.x;
                start.x = nextStart.x;
            }
        } else if (rightX1 < leftX2) {
            if (start.y < end.y) {
                nextEnd.y = start.y;
            } else {
                nextStart.x = rightX2 + (leftX1 - rightX2) / 2;
                nextEnd.x = nextStart.x;
            }
        } else if (end.x >= start.x) {
            if (start.y < end.y - mDistanceBlockLine) {
                nextEnd.y = start.y;
            } else {
                nextStart.x = rightX2;
                nextEnd.x = nextStart.x;
            }
        } else {
            if (bottomY1 < topY2) {
                nextStart.y = bottomY1 + (topY2 - bottomY1) / 2;
                nextEnd.y = nextStart.y;
            } else if (topY1 > topY2) {
                nextStart.x = Math.max(rightX2, start.x);
                nextEnd.x = nextStart.x;
            } else {
                nextStart.y = topY1;
                nextEnd.y = nextStart.y;
            }
        }
    }

    private void leftToTop(PointF start, PointF end, PointF nextStart, PointF nextEnd, float leftX2, float rightX2, float leftX1, float rightX1, float topY2, float bottomY2, float topY1, float bottomY1) {
        if (leftX1 > rightX2 - 2 * mDistanceBlockLine && leftX1 < rightX2) {
            if (start.y < end.y) {
                nextEnd.y = start.y;
            } else {
                nextStart.x = rightX2 + (leftX1 - rightX2) / 2;
                nextEnd.x = nextStart.x;
                start.x = nextStart.x;
            }
        } else if (leftX1 > rightX2) {
            if (start.y < end.y - mDistanceBlockLine) {
                nextEnd.y = start.y;
            } else {
                nextStart.x = rightX2 + (leftX1 - rightX2) / 2;
                nextEnd.x = nextStart.x;
            }
        } else if (end.x <= start.x) {
            if (start.y < end.y) {
                nextEnd.y = start.y;
            } else {
                nextStart.x = leftX2;
                nextEnd.x = nextStart.x;
            }
        } else {
            if (bottomY1 < topY2) {
                nextStart.y = bottomY1 + (topY2 - bottomY1) / 2;
                nextEnd.y = nextStart.y;
            } else if (topY1 > topY2) {
                nextStart.x = Math.min(leftX2, start.x);
                nextEnd.x = nextStart.x;
            } else {
                nextStart.y = topY1;
                nextEnd.y = nextStart.y;
            }
        }
    }

    private void leftToBottom(PointF start, PointF end, PointF nextStart, PointF nextEnd, float leftX2, float rightX2, float leftX1, float rightX1, float topY2, float bottomY2, float topY1, float bottomY1) {
        if (leftX1 > rightX2 - 2 * mDistanceBlockLine && leftX1 < rightX2) {
            if (start.y > end.y) {
                nextEnd.y = start.y;
            } else {
                nextStart.x = rightX2 + (leftX1 - rightX2) / 2;
                nextEnd.x = nextStart.x;
                start.x = nextStart.x;
            }
        } else if (leftX1 > rightX2) {
            if (start.y > end.y - mDistanceBlockLine) {
                nextEnd.y = start.y;
            } else {
                nextStart.x = rightX2 + (leftX1 - rightX2) / 2;
                nextEnd.x = nextStart.x;
            }
        } else if (end.x <= start.x) {
            if (start.y > end.y) {
                nextEnd.y = start.y;
            } else {
                nextStart.x = leftX2;
                nextEnd.x = nextStart.x;
            }
        } else {
            if (topY1 > bottomY2) {
                nextStart.y = bottomY2 + (topY1 - bottomY2) / 2;
                nextEnd.y = nextStart.y;
            } else if (bottomY1 < bottomY2) {
                nextStart.x = Math.min(leftX2, start.x);
                nextEnd.x = nextStart.x;
            } else {
                nextStart.y = bottomY1;
                nextEnd.y = nextStart.y;
            }
        }
    }

    private void bottomToLeft(PointF start, PointF end, PointF nextStart, PointF nextEnd, float leftX2, float leftX1, float rightX1, float topY2, float bottomY2, float bottomY1) {
        if (bottomY1 > topY2 && bottomY1 < topY2 + 2 * mDistanceBlockLine) {
            if (start.x > end.x) {

                nextStart.y = bottomY1 + (topY2 - bottomY1) / 2;
                nextEnd.y = nextStart.y;
                start.y = nextStart.y;
            } else {

                nextStart.y = end.y;
            }
        } else if (bottomY1 < topY2) {
            if (start.x > end.x) {
                nextStart.y = bottomY1 + (topY2 - bottomY1) / 2;
                nextEnd.y = nextStart.y;
            } else {
                nextStart.y = end.y;
            }
        } else if (bottomY1 > topY2 && bottomY1 < bottomY2) {
            if (leftX1 > leftX2) {
                nextStart.y = bottomY2;
                nextEnd.y = nextStart.y;
            } else if (rightX1 < leftX2) {
                if (start.y < end.y) {
                    nextStart.y = end.y;
                } else {
                    nextStart.x = rightX1 + (leftX2 - rightX1) / 2;
                    nextEnd.x = nextStart.x;
                }
            } else {
                if (start.y > end.y) {
                    nextStart.x = leftX1;
                    nextEnd.x = nextStart.x;
                } else {
                    nextStart.y = end.y;
                }
            }
        } else {
            if (leftX1 > leftX2) {
                nextStart.x = leftX2;
            } else if (rightX1 < leftX2) {
                nextStart.x = rightX1 + (leftX2 - rightX1) / 2;
                nextEnd.x = nextStart.x;
            } else {
                nextStart.x = leftX1;
                nextEnd.x = nextStart.x;
            }
        }
    }

    private void topToLeft(PointF start, PointF end, PointF nextStart, PointF nextEnd, float leftX2, float leftX1, float rightX1, float topY2, float bottomY2, float topY1) {
        if (topY1 < bottomY2 && topY1 > bottomY2 - 2 * mDistanceBlockLine) {
            if (start.x > end.x) {
                nextStart.y = topY1 + (bottomY2 - topY1) / 2;
                nextEnd.y = nextStart.y;
                start.y = nextStart.y;
            } else {
                nextStart.y = end.y;
            }
        } else if (topY1 > bottomY2) {
            if (start.x > end.x) {
                nextStart.y = topY1 + (bottomY2 - topY1) / 2;
                nextEnd.y = nextStart.y;
            } else {
                nextStart.y = end.y;
            }
        } else if (topY1 < bottomY2 && topY1 > topY2) {
            if (leftX1 > leftX2) {
                nextStart.y = topY2;
                nextEnd.y = nextStart.y;
            } else if (rightX1 < leftX2) {
                if (start.y > end.y) {
                    nextStart.y = end.y;
                } else {
                    nextStart.x = rightX1 + (leftX2 - rightX1) / 2;
                    nextEnd.x = nextStart.x;
                }
            } else {
                if (start.y < end.y) {
                    nextStart.x = leftX1;
                    nextEnd.x = nextStart.x;
                } else {
                    nextStart.y = end.y;
                }
            }
        } else {
            if (leftX1 > leftX2) {
                nextStart.x = leftX2;
            } else if (rightX1 < leftX2) {
                nextStart.x = rightX1 + (leftX2 - rightX1) / 2;
                nextEnd.x = nextStart.x;
            } else {
                nextStart.x = leftX1;
                nextEnd.x = nextStart.x;
            }
        }
    }

    private void bottomToRight(PointF start, PointF end, PointF nextStart, PointF nextEnd, float leftX1, float rightX1, float rightX2, float topY2, float bottomY2, float bottomY1) {
        if (bottomY1 > topY2 && bottomY1 < topY2 + 2 * mDistanceBlockLine) {
            if (start.x < end.x) {

                nextStart.y = bottomY1 + (topY2 - bottomY1) / 2;
                nextEnd.y = nextStart.y;
                start.y = nextStart.y;
            } else {

                nextStart.y = end.y;
            }
        } else if (bottomY1 < topY2) {
            if (start.x < end.x) {
                nextStart.y = bottomY1 + (topY2 - bottomY1) / 2;
                nextEnd.y = nextStart.y;
            } else {
                nextStart.y = end.y;
            }
        } else if (bottomY1 > topY2 && bottomY1 < bottomY2) {
            if (rightX1 < rightX2) {
                nextStart.y = bottomY2;
                nextEnd.y = nextStart.y;
            } else if (leftX1 > rightX2) {
                if (start.y < end.y) {
                    nextStart.y = end.y;
                } else {
                    nextStart.x = rightX2 + (leftX1 - rightX2) / 2;
                    nextEnd.x = nextStart.x;
                }
            } else {
                if (start.y > end.y) {
                    nextStart.x = rightX1;
                    nextEnd.x = nextStart.x;
                } else {
                    nextStart.y = end.y;
                }
            }
        } else {
            if (rightX1 < rightX2) {
                nextStart.x = rightX2;
            } else if (leftX1 > rightX2) {
                nextStart.x = rightX2 + (leftX1 - rightX2) / 2;
                nextEnd.x = nextStart.x;
            } else {
                nextStart.x = rightX1;
                nextEnd.x = nextStart.x;
            }
        }
    }

    private void topToRight(PointF start, PointF end, PointF nextStart, PointF nextEnd, float leftX1, float rightX1, float rightX2, float topY2, float bottomY2, float topY1) {
        if (topY1 < bottomY2 && topY1 > bottomY2 - 2 * mDistanceBlockLine) {
            if (start.x < end.x) {
                nextStart.y = topY1 + (bottomY2 - topY1) / 2;
                nextEnd.y = nextStart.y;
                start.y = nextStart.y;
            } else {
                nextStart.y = end.y;
            }
        } else if (topY1 > bottomY2) {
            if (start.x < end.x) {
                nextStart.y = topY1 + (bottomY2 - topY1) / 2;
                nextEnd.y = nextStart.y;
            } else {
                nextStart.y = end.y;
            }
        } else if (topY1 < bottomY2 && topY1 > topY2) {
            if (rightX1 < rightX2) {
                nextStart.y = topY2;
                nextEnd.y = nextStart.y;
            } else if (leftX1 > rightX2) {
                if (start.y > end.y) {
                    nextStart.y = end.y;
                } else {
                    nextStart.x = rightX2 + (leftX1 - rightX2) / 2;
                    nextEnd.x = nextStart.x;
                }
            } else {
                if (start.y < end.y) {
                    nextStart.x = rightX1;
                    nextEnd.x = nextStart.x;
                } else {
                    nextStart.y = end.y;
                }
            }
        } else {
            if (rightX1 < rightX2) {
                nextStart.x = rightX2;
            } else if (leftX1 > rightX2) {
                nextStart.x = rightX2 + (leftX1 - rightX2) / 2;
                nextEnd.x = nextStart.x;
            } else {
                nextStart.x = rightX1;
                nextEnd.x = nextStart.x;
            }
        }
    }

    private void bottomToTop(PointF start, PointF end, PointF nextStart, PointF nextEnd, float leftX2, float rightX2, float leftX1, float rightX1, float topY2, float bottomY1) {
        if (bottomY1 < topY2 + 2 * mDistanceBlockLine && bottomY1 > topY2) {
            start.y = bottomY1 + (topY2 - bottomY1) / 2;
            nextStart.y = start.y;
            end.y = nextStart.y;
            nextEnd.y = end.y;
        } else if (bottomY1 > topY2 + 2 * mDistanceBlockLine) {
            if (rightX1 < leftX2) {
                nextStart.x = rightX1 + (leftX2 - rightX1) / 2;
                nextEnd.x = nextStart.x;
            } else if (leftX1 > rightX2) {
                nextStart.x = rightX2 + (leftX1 - rightX2) / 2;
                nextEnd.x = nextStart.x;
            } else if (rightX1 > rightX2) {
                nextStart.x = rightX1;
                nextEnd.x = nextStart.x;
            } else if (leftX1 < leftX2) {
                nextStart.x = leftX1;
                nextEnd.x = nextStart.x;
            }
        } else {
            nextStart.y = bottomY1 + (topY2 - bottomY1) / 2;
            nextEnd.y = nextStart.y;
        }
    }

    private void topToTop(PointF start, PointF end, PointF nextStart, PointF nextEnd, float leftX2, float rightX2, float leftX1, float rightX1, float topY2, float bottomY2, float topY1) {
        if (topY1 >= bottomY2 - 2 * mDistanceBlockLine) {
            if (start.x < rightX2 && start.x > leftX2) {
                if (rightX2 - start.x > start.x - leftX2) {
                    nextStart.x = leftX2;
                    nextEnd.x = leftX2;
                } else {
                    nextStart.x = rightX2;
                    nextEnd.x = rightX2;
                }
            } else {
                nextEnd.x = start.x;
            }
        } else if (topY1 < bottomY2 && topY1 > topY2) {
            nextStart.y = end.y;
        } else {
            if (rightX1 < end.x || leftX1 > end.x) {
                nextEnd.y = start.y;
            } else {
                if (end.x > start.x) {
                    nextStart.x = rightX1;
                    nextEnd.x = rightX1;
                } else {
                    nextStart.x = leftX1;
                    nextEnd.x = leftX1;
                }
            }
        }
    }

    private PointF createSecondPointNearBlock(PointF first, BlockSide side) {
        PointF point = new PointF();
        switch (side) {
            case TOP:
                point = new PointF(first.x, first.y - mDistanceBlockLine);
                break;
            case LEFT:
                point = new PointF(first.x - mDistanceBlockLine, first.y);
                break;
            case RIGHT:
                point = new PointF(first.x + mDistanceBlockLine, first.y);
                break;
            case BOTTOM:
                point = new PointF(first.x, first.y + mDistanceBlockLine);
                break;
        }
        return point;
    }

    private PointF determineStartPoint(SimpleBlockView blockView, BlockSide side) {
        float width = blockView.getRect().width();
        float height = blockView.getRect().height();

        PointF point = new PointF();

        switch (side) {
            case TOP:
                point.x = blockView.getPosition().x;
                point.y = blockView.getPosition().y - height / 2;
                break;
            case LEFT:
                point.x = blockView.getPosition().x - width / 2;
                point.y = blockView.getPosition().y;
                break;
            case RIGHT:
                point.x = blockView.getPosition().x + width / 2;
                point.y = blockView.getPosition().y;
                break;
            case BOTTOM:
                point.x = blockView.getPosition().x;
                point.y = blockView.getPosition().y + height / 2;
                break;
        }
        return point;
    }

    public void findViewGroup() {
        mViewGroup = (FlowChartViewGroup) getParent();
    }

    private void extractAttributes(@NonNull Context context, @Nullable AttributeSet attrs) {
        final Resources.Theme theme = context.getTheme();
        final TypedArray typedArray = theme.obtainStyledAttributes(attrs, R.styleable.SimpleLine,
                0, R.style.SimpleLineDefault);

        try {
            mStrokeColor = typedArray.getColor(R.styleable.SimpleLine_color_stroke_line, 0);
            mStrokeWidth = typedArray.getDimension(R.styleable.SimpleLine_stroke_width_line, 0);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float scale = mViewGroup.getCurrentScale();
        canvas.translate((-mX + mStrokeWidth / 2) * scale, (-mY + mStrokeWidth / 2) * scale);
        for (int i = 1; i < mPoints.size(); i++) {
            canvas.drawLine((mPoints.get(i - 1).x) * scale, (mPoints.get(i - 1).y) * scale,
                    (mPoints.get(i).x) * scale, (mPoints.get(i).y) * scale, mPaint);
        }

    }

    public void findMeasure() {

        float lX = mPoints.get(0).x;
        float bY = mPoints.get(0).y;
        float rX = mPoints.get(0).x;
        float tY = mPoints.get(0).y;
        for (int i = 1; i < mPoints.size(); i++) {

            if (mPoints.get(i).x < lX) {
                lX = mPoints.get(i).x;
            } else if (mPoints.get(i).x > rX) {
                rX = mPoints.get(i).x;
            }

            if (mPoints.get(i).y > bY) {
                bY = mPoints.get(i).y;
            } else if (mPoints.get(i).y < tY) {
                tY = mPoints.get(i).y;
            }
        }
        mWidth = rX - lX + mStrokeWidth;
        mHeight = bY - tY + mStrokeWidth;
        mX = lX;
        mY = tY;
    }

    public void update() {
        mPoints.clear();
        createLine();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewGroup != null) {
            mCurStrokeWidth = mViewGroup.getCurrentScale() * mStrokeWidth;
        }
        initPaint();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        findMeasure();
        int widthMeasured = (int) (mWidth);
        int heightMeasured = (int) (mHeight);

        setMeasuredDimension(resolveSize(widthMeasured, widthMeasureSpec),
                resolveSize(heightMeasured, heightMeasureSpec));
    }

    public enum BlockSide {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM
    }
}