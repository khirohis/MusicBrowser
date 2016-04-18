package net.hogelab.musicbrowser.view;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.squareup.otto.Subscribe;

import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.databinding.ActivityTrackListBinding;
import net.hogelab.musicbrowser.event.EventBus;
import net.hogelab.musicbrowser.event.OpenTrackEvent;
import net.hogelab.musicbrowser.model.AudioMediaStoreCursorLoaderFactory;
import net.hogelab.musicbrowser.viewmodel.TrackListRootViewModel;

/**
 * Created by kobayasi on 2016/04/11.
 */
public class TrackListActivity extends AppCompatActivity {
    private static final String TAG = TrackListActivity.class.getSimpleName();


    private static final String BUNDLE_ALBUM_ID_KEY = "albumId";

    private static final int ALBUM_LOADER_ID = 1;


    private ActivityTrackListBinding mBinding;
    private TrackListRootViewModel mViewModel;
    private long mAlbumId;


    public static Intent newIntent(Context context) {
        return new Intent(context, TrackListActivity.class);
    }

    public static Intent newIntent(Context context, long albumId) {
        Intent intent =  new Intent(context, TrackListActivity.class);
        intent.putExtra(BUNDLE_ALBUM_ID_KEY, albumId);

        return intent;
    }


    private final LoaderManager.LoaderCallbacks albumLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            final long albumId = args.getLong(BUNDLE_ALBUM_ID_KEY);

            return AudioMediaStoreCursorLoaderFactory.createAlbumCursorLoader(TrackListActivity.this, albumId);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null && data.moveToFirst()) {
                mViewModel.setupFromCursor(data);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new TrackListRootViewModel(this, null);

        mBinding = ActivityTrackListBinding.inflate(getLayoutInflater());
        mBinding.setViewModel(mViewModel);

        setContentView(mBinding.getRoot());

        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mAlbumId = extras.getLong(BUNDLE_ALBUM_ID_KEY);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_container, TrackListFragment.newInstance(mAlbumId))
                    .commit();
        }

        Bundle args = new Bundle();
        if (mAlbumId != 0L) {
            args.putLong(BUNDLE_ALBUM_ID_KEY, mAlbumId);
        }
        getSupportLoaderManager().initLoader(ALBUM_LOADER_ID, args, albumLoaderCallback);
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getBus().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getBus().unregister(this);

        super.onPause();
    }


    @Subscribe
    public void openTrack(OpenTrackEvent event) {
        Log.d(TAG, "Track ID: " + event.trackId);
    }
}
