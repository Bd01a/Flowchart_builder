package com.fed.flowchart_builder.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import androidx.annotation.StringRes;

import com.fed.flowchart_builder.R;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.ConditionBlockView;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.CycleBeginBlockView;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.CycleEndBlockView;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.FunctionBlockView;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.InletBlockView;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.OperationBlockView;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.SimpleBlockView;

public enum BlockDescription {
    OPERATION_BLOCK(R.string.block_operation, 1, new ObjectCreator() {
        @Override
        public SimpleBlockView getBlock(Context context) {
            return new OperationBlockView(context);
        }

        @Override
        public Bitmap getIcon(Context context) {
            Bitmap bitmap = Bitmap.createBitmap(BITMAP_SIZE, BITMAP_SIZE, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = initPaint(context);

            RectF rect = new RectF(0, 0, RECT_SIZE, RECT_SIZE * 0.8f);
            canvas.translate(STROKE_WIDTH / 2, STROKE_WIDTH / 2 + (BITMAP_SIZE - rect.height()) / 2);

            canvas.drawRect(rect, paint);

            return bitmap;
        }
    }),
    CONDITION_BLOCK(R.string.block_condition, 2, new ObjectCreator() {
        @Override
        public SimpleBlockView getBlock(Context context) {
            return new ConditionBlockView(context);
        }

        @Override
        public Bitmap getIcon(Context context) {
            Bitmap bitmap = Bitmap.createBitmap(BITMAP_SIZE, BITMAP_SIZE, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = initPaint(context);

            RectF rect = new RectF(0, 0, RECT_SIZE, RECT_SIZE);
            canvas.translate(STROKE_WIDTH / 2, STROKE_WIDTH / 2 + (BITMAP_SIZE - rect.height()) / 2);

            float middleX = (rect.right - rect.left) / 2;
            float middleY = (rect.bottom - rect.top) / 2;

            Path path = new Path();

            float x1 = rect.left + middleX;
            float y1 = rect.top;
            float x2 = rect.right;
            float y2 = rect.top + middleY;
            float x3 = rect.left + middleX;
            float y3 = rect.bottom;
            float x4 = rect.left;
            float y4 = rect.top + middleY;

            path.moveTo((x1 + x2) / 2, (y1 + y2) / 2);
            path.lineTo(x2, y2);
            path.lineTo(x3, y3);
            path.lineTo(x4, y4);
            path.lineTo(x1, y1);
            path.lineTo((x1 + x2) / 2, (y1 + y2) / 2);
            canvas.drawPath(path, paint);
            return bitmap;
        }
    }),
    CYCLE_BEGIN_BLOCK(R.string.block_begin_cycle, 3, new ObjectCreator() {
        @Override
        public SimpleBlockView getBlock(Context context) {
            return new CycleBeginBlockView(context);
        }

        @Override
        public Bitmap getIcon(Context context) {
            Bitmap bitmap = Bitmap.createBitmap(BITMAP_SIZE, BITMAP_SIZE, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = initPaint(context);

            RectF rect = new RectF(0, 0, RECT_SIZE, RECT_SIZE * 0.8f);
            canvas.translate(STROKE_WIDTH / 2, STROKE_WIDTH / 2 + (BITMAP_SIZE - rect.height()) / 2);

            Path path = new Path();

            float xl = rect.left + rect.width() / 6;
            float xr = rect.left + rect.width() / 6 * 5;

            float y1 = rect.top + rect.height() / 6;

            path.moveTo(rect.right, rect.bottom);
            path.lineTo(rect.left, rect.bottom);
            path.lineTo(rect.left, y1);
            path.lineTo(xl, rect.top);
            path.lineTo(xr, rect.top);
            path.lineTo(rect.right, y1);
            path.lineTo(rect.right, rect.bottom + paint.getStrokeWidth() / 2);
            canvas.drawPath(path, paint);
            return bitmap;
        }
    }),
    CYCLE_END_BLOCK(R.string.block_end_cycle, 4, new ObjectCreator() {
        @Override
        public SimpleBlockView getBlock(Context context) {
            return new CycleEndBlockView(context);
        }

        @Override
        public Bitmap getIcon(Context context) {
            Bitmap bitmap = Bitmap.createBitmap(BITMAP_SIZE, BITMAP_SIZE, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = initPaint(context);

            RectF rect = new RectF(0, 0, RECT_SIZE, RECT_SIZE * 0.8f);
            canvas.translate(STROKE_WIDTH / 2, STROKE_WIDTH / 2 + (BITMAP_SIZE - rect.height()) / 2);

            Path path = new Path();

            float xl = rect.left + rect.width() / 6;
            float xr = rect.left + rect.width() / 6 * 5;

            float y1 = rect.bottom - rect.height() / 6;

            path.moveTo(rect.right, rect.top);
            path.lineTo(rect.left, rect.top);
            path.lineTo(rect.left, y1);
            path.lineTo(xl, rect.bottom);
            path.lineTo(xr, rect.bottom);
            path.lineTo(rect.right, y1);
            path.lineTo(rect.right, rect.top - paint.getStrokeWidth() / 2);
            canvas.drawPath(path, paint);
            return bitmap;
        }
    }),
    FUNCTION_BLOCK(R.string.block_function, 5, new ObjectCreator() {
        @Override
        public SimpleBlockView getBlock(Context context) {
            return new FunctionBlockView(context);
        }

        @Override
        public Bitmap getIcon(Context context) {
            Bitmap bitmap = Bitmap.createBitmap(BITMAP_SIZE, BITMAP_SIZE, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = initPaint(context);

            RectF rect = new RectF(0, 0, RECT_SIZE, RECT_SIZE * 0.8f);
            canvas.translate(STROKE_WIDTH / 2, STROKE_WIDTH / 2 + (BITMAP_SIZE - rect.height()) / 2);

            canvas.drawRect(rect, paint);
            float x1 = rect.left + rect.width() / 8;
            float x2 = rect.left + rect.width() / 8 * 7;
            canvas.drawLine(x1, rect.top, x1, rect.bottom, paint);
            canvas.drawLine(x2, rect.top, x2, rect.bottom, paint);
            return bitmap;
        }
    }),
    INLET_BLOCK(R.string.block_inlet, 6, new ObjectCreator() {
        @Override
        public SimpleBlockView getBlock(Context context) {
            return new InletBlockView(context);
        }

        @Override
        public Bitmap getIcon(Context context) {
            Bitmap bitmap = Bitmap.createBitmap(BITMAP_SIZE, BITMAP_SIZE, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = initPaint(context);

            RectF rect = new RectF(0, 0, RECT_SIZE, RECT_SIZE * 0.8f);
            canvas.translate(STROKE_WIDTH / 2, STROKE_WIDTH / 2 + (BITMAP_SIZE - rect.height()) / 2);

            Path path = new Path();
            float middleX = (rect.right - rect.left) / 2;

            float xr = middleX + rect.width() / 4;
            float xl = middleX - rect.width() / 4;
            float yt = rect.top;
            float xrr = rect.right;
            float xll = rect.left;
            float yb = rect.bottom;

            path.moveTo(xr, yt);
            path.arcTo(middleX, yt, xrr, yb, -90, 180, true);
            path.lineTo(xl, yb);
            path.arcTo(xll, yt, middleX, yb, 90, 180, true);
            path.lineTo(xr, yt);

            canvas.drawPath(path, paint);
            return bitmap;
        }
    });

    private static final int BITMAP_SIZE = 150;
    private static final int RECT_SIZE = 126;
    private static final int STROKE_WIDTH = 12;

    BlockDescription(@StringRes int name, int id, ObjectCreator objectCreator) {
        mName = name;
        mObjectCreator = objectCreator;
        mId = id;
    }

    private int mName;
    private ObjectCreator mObjectCreator;
    private int mId;

    public static Paint initPaint(Context context) {
        Paint paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.colorPrimary));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setAntiAlias(true);
        return paint;
    }

    public int getName() {
        return mName;
    }


    public int getId() {
        return mId;
    }

    public SimpleBlockView getBlock(Context context) {
        return mObjectCreator.getBlock(context);
    }

    public Bitmap getIcon(Context context) {
        return mObjectCreator.getIcon(context);
    }

    interface ObjectCreator {
        SimpleBlockView getBlock(Context context);

        Bitmap getIcon(Context context);
    }

}
