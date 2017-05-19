package net.hogelab.musicbrowser.view;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;

import com.squareup.otto.Subscribe;

import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.databinding.ActivityTrackListBinding;
import net.hogelab.musicbrowser.event.EventBus;
import net.hogelab.musicbrowser.event.PlayTrackEvent;
import net.hogelab.musicbrowser.model.AudioMediaStoreCursorFactory;
import net.hogelab.musicbrowser.viewmodel.TrackListRootViewModel;

/**
 * Created by kobayasi on 2016/04/11.
 */
public class TrackListActivity extends BaseActivity {

    private static final String TAG = TrackListActivity.class.getSimpleName();

    private static final String BUNDLE_ALBUM_ID_KEY = "albumId";
    private static final int ALBUM_LOADER_ID = 1;


    private ActivityTrackListBinding mBinding;
    private TrackListRootViewModel mViewModel;
    private String mAlbumId;


    public static Intent newIntent(Context context) {
        return new Intent(context, TrackListActivity.class);
    }

    public static Intent newIntent(Context context, String albumId) {
        Intent intent =  new Intent(context, TrackListActivity.class);
        intent.putExtra(BUNDLE_ALBUM_ID_KEY, albumId);

        return intent;
    }


    private final LoaderManager.LoaderCallbacks albumLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            final String albumId = args.getString(BUNDLE_ALBUM_ID_KEY);
            if (albumId != null) {
                return AudioMediaStoreCursorFactory.createAlbumCursorLoader(TrackListActivity.this, albumId);
            }

            return null;
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null && data.moveToNext()) {
                mViewModel.setupFromCursor(data);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        mViewModel = new TrackListRootViewModel(this, null);

        mBinding = ActivityTrackListBinding.inflate(getLayoutInflater());
        mBinding.setViewModel(mViewModel);

        setContentView(mBinding.getRoot());

        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mBinding.toolbar.setNavigationOnClickListener((view) -> {
            finish();
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mAlbumId = extras.getString(BUNDLE_ALBUM_ID_KEY);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_container, TrackListFragment.newInstance(mAlbumId))
                    .commit();
        }

        Bundle args = new Bundle();
        if (mAlbumId != null) {
            args.putString(BUNDLE_ALBUM_ID_KEY, mAlbumId);
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
    public void playTrack(PlayTrackEvent event) {
        Log.d(TAG, "Track ID: " + event.trackId);

        startPlayTrack(event.trackId);
        startActivity(PlayerActivity.newIntent(this));
    }
}
