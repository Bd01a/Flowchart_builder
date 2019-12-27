package com.fed.flowchart_builder.data;

import androidx.lifecycle.LiveData;

public class ChartLiveData<T> extends LiveData<T> {
    @Override
    protected void postValue(T value) {
        super.postValue(value);
    }

    @Override
    protected void setValue(T value) {
        super.setValue(value);
    }
}
