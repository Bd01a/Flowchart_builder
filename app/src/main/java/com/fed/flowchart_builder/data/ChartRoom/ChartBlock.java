package com.fed.flowchart_builder.data.ChartRoom;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "ChartBlocks")
//        ,
//        foreignKeys = @ForeignKey(entity = Note.class,
//                parentColumns = "id", childColumns = "note_id",
//                onDelete = ForeignKey.CASCADE))
public class ChartBlock {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "chart_name", index = true)
    private String mChartName;

    @ColumnInfo(name = "width")
    private float mWidth;

    @ColumnInfo(name = "height")
    private float mHeight;

    @ColumnInfo(name = "stroke_width")
    private float mStrokeWidth;

    @ColumnInfo(name = "text_size")
    private float mTextSize;

    @ColumnInfo(name = "color_stroke")
    private int mColorStroke;

    @ColumnInfo(name = "text_color")
    private int mTextColor;

    @ColumnInfo(name = "position_x")
    private int mPositionX;

    @ColumnInfo(name = "position_y")
    private int mPositionY;

    @ColumnInfo(name = "block_type")
    private int mBlockType;

    @ColumnInfo(name = "text")
    private String mText;

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getChartName() {
        return mChartName;
    }

    public void setChartName(String mChartName) {
        this.mChartName = mChartName;
    }

    public float getWidth() {
        return mWidth;
    }

    public void setWidth(float mWidth) {
        this.mWidth = mWidth;
    }

    public float getHeight() {
        return mHeight;
    }

    public void setHeight(float mHeight) {
        this.mHeight = mHeight;
    }

    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(float mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }

    public int getColorStroke() {
        return mColorStroke;
    }

    public void setColorStroke(int mColorStroke) {
        this.mColorStroke = mColorStroke;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public int getPositionX() {
        return mPositionX;
    }

    public void setPositionX(int positionX) {
        mPositionX = positionX;
    }

    public int getPositionY() {
        return mPositionY;
    }

    public void setPositionY(int positionY) {
        mPositionY = positionY;
    }

    public int getBlockType() {
        return mBlockType;
    }

    public void setBlockType(int blockType) {
        mBlockType = blockType;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }
}
