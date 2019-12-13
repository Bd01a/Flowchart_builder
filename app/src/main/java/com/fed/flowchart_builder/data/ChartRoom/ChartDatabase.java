package com.fed.flowchart_builder.data.ChartRoom;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ChartBlock.class, ChartLine.class}, version = 1, exportSchema = false)
public abstract class ChartDatabase extends RoomDatabase {
    public abstract ChartBlockDao getBlockDao();

    public abstract ChartLineDao getLineDao();
}
