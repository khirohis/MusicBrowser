package net.hogelab.musicbrowser.model;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by kobayasi on 2017/01/11.
 */

public abstract class MediaStoreLoader extends AsyncTaskLoader<String> {
    protected String mData;


    public MediaStoreLoader(Context context) {
        super(context);
    }

    protected abstract String loadData();


    @Override
    public String loadInBackground() {
        return loadData();
    }

    @Override
    public void deliverResult(String data) {
        if (isReset()) {
            return;
        }

        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }

        if (takeContentChanged() || mData == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();

        if (mData != null) {
            mData = null;
        }
    }

    @Override
    public void onCanceled(String data) {
        super.onCanceled(data);
    }
}
