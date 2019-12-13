package com.fed.flowchart_builder.data.ChartRoom;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChartLineDao {

    @Insert
    long[] addChartLine(List<ChartLine> lines);

    @Query("SELECT * FROM chartline WHERE chart_name = :chartName")
    List<ChartLine> getLinesByChartName(String chartName);

    @Query("DELETE FROM chartline WHERE chart_name = :chartName")
    void deleteByChartName(String chartName);
}
