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
import net.hogelab.musicbrowser.databinding.ActivityAlbumListBinding;
import net.hogelab.musicbrowser.event.EventBus;
import net.hogelab.musicbrowser.event.OpenAlbumEvent;
import net.hogelab.musicbrowser.model.ArtistLoader;
import net.hogelab.musicbrowser.model.AudioMediaStoreCursorLoaderFactory;
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


    private final LoaderManager.LoaderCallbacks artistLoaderCallback = new LoaderManager.LoaderCallbacks<String>() {

        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            final String artistId = args.getString(BUNDLE_ARTIST_ID_KEY);
            if (artistId != null) {
                return new ArtistLoader(AlbumListActivity.this, artistId);
            }

            return null;
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            if (data != null) {
                mViewModel.setupFromArtistId(data);
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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mArtistId = extras.getString(BUNDLE_ARTIST_ID_KEY);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_container, AlbumListFragment.newInstance(mArtistId))
                    .commit();
        }

        Bundle args = new Bundle();
        if (mArtistId != null) {
            args.putString(BUNDLE_ARTIST_ID_KEY, mArtistId);
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
        startActivity(TrackListActivity.newIntent(this, event.albumId));
    }
}
