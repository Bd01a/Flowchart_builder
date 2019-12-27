package com.fed.flowchart_builder.data;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.fed.flowchart_builder.data.ChartRoom.ChartBlock;
import com.fed.flowchart_builder.data.ChartRoom.ChartDatabase;
import com.fed.flowchart_builder.data.ChartRoom.ChartLine;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChartRepository {

    static ChartRepository mRepository;
    private ChartDatabase mDb;

    private ExecutorService mExecutorService = Executors.newSingleThreadScheduledExecutor();


    private ChartRepository(Context context) {
        context = context.getApplicationContext();
        RoomDatabase.Builder<ChartDatabase> builder = Room.databaseBuilder(context,
                ChartDatabase.class, "chartElements");
        mDb = builder.build();
    }

    public static ChartRepository getChartRepository(Context context) {
        if (mRepository == null) {
            mRepository = new ChartRepository(context);
            return mRepository;
        } else {
            return mRepository;
        }
    }



    public void addBlocks(List<ChartBlock> blocks) {
//        AddBlocksAsync addBlocksAsync = new AddBlocksAsync();
//        addBlocksAsync.execute(blocks);
        mExecutorService.submit(new AddBlocksAsync(blocks));
    }

    public void addLines(List<ChartLine> lines) {
//        AddLinesAsync addLinesAsync = new AddLinesAsync();
//        addLinesAsync.execute(lines);
        mExecutorService.submit(new AddLinesAsync(lines));
    }

    public void deleteAllByChartName(String chartName) {
//        DeleteAllByChartNameAsync deleteAllByChartNameAsync = new DeleteAllByChartNameAsync();
//        deleteAllByChartNameAsync.execute(chartName);
        mExecutorService.submit(new DeleteAllByChartNameAsync(chartName));
    }

    public void getBlocksByChartName(ChartLiveData<List<ChartBlock>> liveData, String chartName) {
//        GetBlocksByChartNameAsync getBlocksByChartNameAsync = new GetBlocksByChartNameAsync();
//        getBlocksByChartNameAsync.execute(chartName);
        mExecutorService.submit(new GetBlocksByChartName(liveData, chartName));

    }


//    public void getLinesByChartName(String chartName) {
//        GetLinesByChartNameAsync getLinesByChartNameAsync = new GetLinesByChartNameAsync();
//        getLinesByChartNameAsync.execute(chartName);
//    }

    public void getLinesByChartName(ChartLiveData<List<ChartLine>> liveData, String chartName) {
        mExecutorService.submit(new GetLinesByChartName(liveData, chartName));
    }


    public void getChartNames(ChartLiveData<List<String>> liveData) {
//        GetChartNamesAsync getChartNamesAsync = new GetChartNamesAsync();
//        getChartNamesAsync.execute();
        mExecutorService.submit(new GetChartNamesAsync(liveData));
    }


    class GetLinesByChartName implements Runnable {

        private ChartLiveData<List<ChartLine>> mLinesLiveData;
        private String mChartName;

        GetLinesByChartName(ChartLiveData<List<ChartLine>> linesLiveData, String chartName) {
            mLinesLiveData = linesLiveData;
            mChartName = chartName;
        }

        @Override
        public void run() {
            List<ChartLine> lines = mDb.getLineDao().getLinesByChartName(mChartName);
            mLinesLiveData.postValue(lines);
        }
    }


//
//    class GetLinesByChartNameAsync extends AsyncTask<String, Void, List<ChartLine>> {
//
//        @Override
//        protected List<ChartLine> doInBackground(String... strings) {
//            return mDb.getLineDao().getLinesByChartName(strings[0]);
//        }
//
//        @Override
//        protected void onPostExecute(List<ChartLine> lines) {
//            super.onPostExecute(lines);
//            mChartContract.getLinesByChartNameIsCompleted(lines);
//        }
//    }

//    class GetBlocksByChartNameAsync extends AsyncTask<String, Void, List<ChartBlock>> {
//
//        @Override
//        protected List<ChartBlock> doInBackground(String... strings) {
//            return mDb.getBlockDao().getBlocksByChartName(strings[0]);
//        }
//
//        @Override
//        protected void onPostExecute(List<ChartBlock> blocks) {
//            super.onPostExecute(blocks);
//            mChartContract.getBlocksByChartNameIsCompleted(blocks);
//        }
//    }

    class GetBlocksByChartName implements Runnable {

        private ChartLiveData<List<ChartBlock>> mBlocksLiveData;
        private String mChartName;

        GetBlocksByChartName(ChartLiveData<List<ChartBlock>> blocksLiveData, String chartName) {
            mBlocksLiveData = blocksLiveData;
            mChartName = chartName;
        }

        @Override
        public void run() {
            List<ChartBlock> blocks = mDb.getBlockDao().getBlocksByChartName(mChartName);
            mBlocksLiveData.postValue(blocks);
        }
    }


//    class GetChartNamesAsync extends AsyncTask<Void, Void, List<String>> {
//
//        @Override
//        protected List<String> doInBackground(Void... voids) {
//            return mDb.getBlockDao().getChartNames();
//        }
//
//        @Override
//        protected void onPostExecute(List<String> strings) {
//            super.onPostExecute(strings);
//            mMainContract.getChartNamesIsCompleted(strings);
//        }
//    }

    class GetChartNamesAsync implements Runnable {

        private ChartLiveData<List<String>> mNamesLiveData;

        GetChartNamesAsync(ChartLiveData<List<String>> blocksLiveData) {
            mNamesLiveData = blocksLiveData;
        }

        @Override
        public void run() {
            List<String> blocks = mDb.getBlockDao().getChartNames();
            mNamesLiveData.postValue(blocks);

        }
    }

//    class AddBlocksAsync extends AsyncTask<List<ChartBlock>, Void, Void> {
//        @Override
//        protected Void doInBackground(List<ChartBlock>... lists) {
//            mDb.getBlockDao().addChartBlocks(lists[0]);
//            return null;
//        }
//    }

    class AddBlocksAsync implements Runnable {

        private List<ChartBlock> mBlocks;

        AddBlocksAsync(List<ChartBlock> blocks) {
            mBlocks = blocks;
        }

        @Override
        public void run() {
            mDb.getBlockDao().addChartBlocks(mBlocks);

        }
    }

//    class AddLinesAsync extends AsyncTask<List<ChartLine>, Void, Void> {
//
//        @Override
//        protected Void doInBackground(List<ChartLine>... lists) {
//            mDb.getLineDao().addChartLine(lists[0]);
//            return null;
//        }
//    }

    class AddLinesAsync implements Runnable {

        private List<ChartLine> mBlocks;

        AddLinesAsync(List<ChartLine> blocks) {
            mBlocks = blocks;
        }

        @Override
        public void run() {
            mDb.getLineDao().addChartLine(mBlocks);
        }
    }

//    class DeleteAllByChartNameAsync extends AsyncTask<String, Void, Void> {
//        @Override
//        protected Void doInBackground(String... strings) {
//            mDb.getLineDao().deleteByChartName(strings[0]);
//            mDb.getBlockDao().deleteByChartName(strings[0]);
//            return null;
//        }
//    }

    class DeleteAllByChartNameAsync implements Runnable {

        private String mName;

        DeleteAllByChartNameAsync(String name) {
            mName = name;
        }

        @Override
        public void run() {
            mDb.getLineDao().deleteByChartName(mName);
            mDb.getBlockDao().deleteByChartName(mName);
        }
    }

}
