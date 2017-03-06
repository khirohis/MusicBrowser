package net.hogelab.musicbrowser.model;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import io.realm.Realm;

/**
 * Created by kobayasi on 2017/01/11.
 */

public abstract class MediaStoreToRealmLoader extends AsyncTaskLoader<String> {

    private static final String TAG = MediaStoreToRealmLoader.class.getSimpleName();

    protected String mData;


    public MediaStoreToRealmLoader(Context context) {
        super(context);
    }


    protected abstract String loadData(Realm realm);


    @Override
    public String loadInBackground() {
        String result = null;

        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            result = loadData(realm);
            realm.commitTransaction();
        } finally {
            realm.close();
        }

        return result;
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
