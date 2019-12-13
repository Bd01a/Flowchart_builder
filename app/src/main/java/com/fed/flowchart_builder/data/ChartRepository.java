package com.fed.flowchart_builder.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.fed.flowchart_builder.data.ChartRoom.ChartBlock;
import com.fed.flowchart_builder.data.ChartRoom.ChartDatabase;
import com.fed.flowchart_builder.data.ChartRoom.ChartLine;

import java.util.List;

public class ChartRepository {

    static ChartRepository mRepository;
    private ChartDatabase mDb;
    private RepositoryMainPresenterContract mMainContract;
    private RepositoryChartPresenterContract mChartContract;

    private ChartRepository(Context context) {
        context = context.getApplicationContext();
        RoomDatabase.Builder<ChartDatabase> builder = Room.databaseBuilder(context,
                ChartDatabase.class, "chartElements");
        mDb = builder.build();
    }

    public static ChartRepository getChartRepository(Context context) {
        if (mRepository == null) {
            return new ChartRepository(context);
        } else {
            return mRepository;
        }
    }

    public void setChartContract(RepositoryChartPresenterContract chartContract) {
        mChartContract = chartContract;
    }

    public void setMainContract(RepositoryMainPresenterContract mainContract) {
        mMainContract = mainContract;
    }


    public void addBlocks(List<ChartBlock> blocks) {
        AddBlocksAsync addBlocksAsync = new AddBlocksAsync();
        addBlocksAsync.execute(blocks);
    }

    public void addLines(List<ChartLine> lines) {
        AddLinesAsync addLinesAsync = new AddLinesAsync();
        addLinesAsync.execute(lines);
    }

    public void deleteAllByChartName(String chartName) {
        DeleteAllByChartNameAsync deleteAllByChartNameAsync = new DeleteAllByChartNameAsync();
        deleteAllByChartNameAsync.execute(chartName);
    }

    public void getBlocksByChartName(String chartName) {
        GetBlocksByChartNameAsync getBlocksByChartNameAsync = new GetBlocksByChartNameAsync();
        getBlocksByChartNameAsync.execute(chartName);
    }


    public void getLinesByChartName(String chartName) {
        GetLinesByChartNameAsync getLinesByChartNameAsync = new GetLinesByChartNameAsync();
        getLinesByChartNameAsync.execute(chartName);
    }


    public void getChartNames() {
        GetChartNamesAsync getChartNamesAsync = new GetChartNamesAsync();
        getChartNamesAsync.execute();
    }


    public interface RepositoryMainPresenterContract {
        void getChartNamesIsCompleted(List<String> names);
    }

    public interface RepositoryChartPresenterContract {
        void getBlocksByChartNameIsCompleted(List<ChartBlock> blocks);

        void getLinesByChartNameIsCompleted(List<ChartLine> lines);
    }

    class GetLinesByChartNameAsync extends AsyncTask<String, Void, List<ChartLine>> {

        @Override
        protected List<ChartLine> doInBackground(String... strings) {
            return mDb.getLineDao().getLinesByChartName(strings[0]);
        }

        @Override
        protected void onPostExecute(List<ChartLine> lines) {
            super.onPostExecute(lines);
            mChartContract.getLinesByChartNameIsCompleted(lines);
        }
    }

    class GetBlocksByChartNameAsync extends AsyncTask<String, Void, List<ChartBlock>> {

        @Override
        protected List<ChartBlock> doInBackground(String... strings) {
            return mDb.getBlockDao().getBlocksByChartName(strings[0]);
        }

        @Override
        protected void onPostExecute(List<ChartBlock> blocks) {
            super.onPostExecute(blocks);
            mChartContract.getBlocksByChartNameIsCompleted(blocks);
        }
    }

    class GetChartNamesAsync extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            return mDb.getBlockDao().getChartNames();
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            mMainContract.getChartNamesIsCompleted(strings);
        }
    }

    class AddBlocksAsync extends AsyncTask<List<ChartBlock>, Void, Void> {
        @Override
        protected Void doInBackground(List<ChartBlock>... lists) {
            mDb.getBlockDao().addChartBlocks(lists[0]);
            return null;
        }
    }

    class AddLinesAsync extends AsyncTask<List<ChartLine>, Void, Void> {

        @Override
        protected Void doInBackground(List<ChartLine>... lists) {
            mDb.getLineDao().addChartLine(lists[0]);
            return null;
        }
    }

    class DeleteAllByChartNameAsync extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            mDb.getLineDao().deleteByChartName(strings[0]);
            mDb.getBlockDao().deleteByChartName(strings[0]);
            return null;
        }
    }

}
