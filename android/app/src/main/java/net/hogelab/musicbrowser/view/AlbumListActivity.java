package net.hogelab.musicbrowser.view;

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
import net.hogelab.musicbrowser.databinding.ActivityAlbumListBinding;
import net.hogelab.musicbrowser.event.EventBus;
import net.hogelab.musicbrowser.event.OpenAlbumEvent;
import net.hogelab.musicbrowser.model.AudioMediaStoreCursorLoaderFactory;
import net.hogelab.musicbrowser.viewmodel.AlbumListRootViewModel;

/**
 * Created by kobayasi on 2016/04/11.
 */
public class AlbumListActivity extends AppCompatActivity {
    private static final String TAG = AlbumListActivity.class.getSimpleName();


    private static final int ARTIST_LOADER_ID = 1;


    private ActivityAlbumListBinding mBinding;
    private AlbumListRootViewModel mViewModel;


    private final LoaderManager.LoaderCallbacks artistLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            final long artistId = args.getLong("artistId");

            return AudioMediaStoreCursorLoaderFactory.createArtistCursorLoader(AlbumListActivity.this, artistId);
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

        mViewModel = new AlbumListRootViewModel(this, null);

        mBinding = ActivityAlbumListBinding.inflate(getLayoutInflater());
        mBinding.setViewModel(mViewModel);

        setContentView(mBinding.getRoot());

        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        long artistId = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            artistId = extras.getLong("artistId");
        }

        Bundle args = new Bundle();
        args.putLong("artistId", artistId);

        if (savedInstanceState == null) {
            AlbumListFragment fragment =  new AlbumListFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_container, fragment)
                    .commit();
        }

        getSupportLoaderManager().initLoader(ARTIST_LOADER_ID, args, artistLoaderCallback);
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
    public void openAlbum(OpenAlbumEvent event) {
        Intent intent = new Intent(this, TrackListActivity.class);
        intent.putExtra("albumId", event.albumId);

        startActivity(intent);
    }
}
