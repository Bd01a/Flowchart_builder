package com.fed.flowchart_builder.data.ChartRoom;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChartLine {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "chart_name", index = true)
    private String mChartName;

    @ColumnInfo(name = "number_block_1")
    private int mNumBlock1;

    @ColumnInfo(name = "number_block_2")
    private int mNumBlock2;

    @ColumnInfo(name = "side_1")
    private int mSide1;

    @ColumnInfo(name = "side_2")
    private int mSide2;

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

    public int getNumBlock1() {
        return mNumBlock1;
    }

    public void setNumBlock1(int mNumBlock1) {
        this.mNumBlock1 = mNumBlock1;
    }

    public int getNumBlock2() {
        return mNumBlock2;
    }

    public void setNumBlock2(int mNumBlock2) {
        this.mNumBlock2 = mNumBlock2;
    }

    public int getSide1() {
        return mSide1;
    }

    public void setSide1(int mSide1) {
        this.mSide1 = mSide1;
    }

    public int getSide2() {
        return mSide2;
    }

    public void setSide2(int mSide2) {
        this.mSide2 = mSide2;
    }
}
