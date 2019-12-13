package com.fed.flowchart_builder.data.ChartRoom;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChartBlockDao {

    @Insert
    long[] addChartBlocks(List<ChartBlock> blocks);

    @Query("SELECT * FROM chartblocks WHERE chart_name = :chartName")
    List<ChartBlock> getBlocksByChartName(String chartName);

    @Query("SELECT DISTINCT chart_name FROM chartblocks")
    List<String> getChartNames();

    @Query("DELETE FROM chartblocks WHERE chart_name = :chartName")
    void deleteByChartName(String chartName);
}
