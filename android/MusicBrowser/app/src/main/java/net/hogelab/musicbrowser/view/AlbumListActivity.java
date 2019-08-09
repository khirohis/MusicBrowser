package net.hogelab.musicbrowser.view;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.databinding.ActivityAlbumListBinding;
import net.hogelab.musicbrowser.event.OpenAlbumEvent;
import net.hogelab.musicbrowser.model.AudioMediaStoreCursorFactory;
import net.hogelab.musicbrowser.viewmodel.AlbumListRootViewModel;

/**
 * Created by kobayasi on 2016/04/11.
 */
public class AlbumListActivity extends AppCompatActivity {

    private static final String TAG = AlbumListActivity.class.getSimpleName();

    private static final String BUNDLE_ARTIST_ID_KEY = "artistId";
    private static final int ARTIST_LOADER_ID = 1;


    private ActivityAlbumListBinding mBinding;
    private AlbumListRootViewModel mViewModel;
    private String mArtistId;


    public static Intent newIntent(Context context) {
        return new Intent(context, AlbumListActivity.class);
    }

    public static Intent newIntent(Context context, String artistId) {
        Intent intent =  new Intent(context, AlbumListActivity.class);
        intent.putExtra(BUNDLE_ARTIST_ID_KEY, artistId);

        return intent;
    }


    private final LoaderManager.LoaderCallbacks artistLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            final String artistId = args.getString(BUNDLE_ARTIST_ID_KEY);
            if (artistId != null) {
                return AudioMediaStoreCursorFactory.createArtistCursorLoader(AlbumListActivity.this, artistId);
            }

            // TODO: null を return できない
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

        mViewModel = new AlbumListRootViewModel(this, null);

        mBinding = ActivityAlbumListBinding.inflate(getLayoutInflater());
        mBinding.setViewModel(mViewModel);

        setContentView(mBinding.getRoot());

        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mBinding.toolbar.setNavigationOnClickListener((view) -> {
            finish();
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mArtistId = extras.getString(BUNDLE_ARTIST_ID_KEY);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_container, AlbumListFragment.newInstance(mArtistId))
                    .commit();
        }

        if (mArtistId != null) {
            Bundle args = new Bundle();
            args.putString(BUNDLE_ARTIST_ID_KEY, mArtistId);
            getSupportLoaderManager().initLoader(ARTIST_LOADER_ID, args, artistLoaderCallback);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

//        EventBus.getBus().register(this);
    }

    @Override
    public void onPause() {
//        EventBus.getBus().unregister(this);

        super.onPause();
    }


//    @Subscribe
    public void openAlbum(OpenAlbumEvent event) {
        startActivity(TrackListActivity.newIntent(this, event.albumId));
    }
}
