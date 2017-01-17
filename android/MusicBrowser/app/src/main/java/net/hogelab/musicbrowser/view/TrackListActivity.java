package net.hogelab.musicbrowser.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import net.hogelab.musicbrowser.model.AlbumLoader;
import net.hogelab.musicbrowser.model.entity.AlbumEntity;
import net.hogelab.musicbrowser.viewmodel.TrackListRootViewModel;

import io.realm.Realm;

/**
 * Created by kobayasi on 2016/04/11.
 */
public class TrackListActivity extends AppCompatActivity {
    private static final String TAG = TrackListActivity.class.getSimpleName();


    private static final String BUNDLE_ALBUM_ID_KEY = "albumId";

    private static final int ALBUM_LOADER_ID = 1;


    private Realm mRealm;
    private ActivityTrackListBinding mBinding;
    private TrackListRootViewModel mViewModel;
    private String mAlbumId;
    private AlbumEntity mAlbum;
    private final RealmChangeListener<AlbumEntity> mChangeListener = new RealmChangeListener<AlbumEntity>() {

        @Override
        public void onChange(AlbumEntity element) {
            if (element.isValid() && element.isLoaded()) {
                mViewModel.setupFromAlbum(element);
            }
        }
    };


    public static Intent newIntent(Context context) {
        return new Intent(context, TrackListActivity.class);
    }

    public static Intent newIntent(Context context, String albumId) {
        Intent intent =  new Intent(context, TrackListActivity.class);
        intent.putExtra(BUNDLE_ALBUM_ID_KEY, albumId);

        return intent;
    }


    public Realm getRealm() {
        return mRealm;
    }


    private final LoaderManager.LoaderCallbacks albumLoaderCallback = new LoaderManager.LoaderCallbacks<String>() {

        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            final String albumId = args.getString(BUNDLE_ALBUM_ID_KEY);
            if (albumId != null) {
                return new AlbumLoader(TrackListActivity.this, albumId);
            }

            return null;
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            if (data != null) {
                Album album = mRealm.where(Album.class).equalTo("id", data).findFirst();
                mViewModel.setupFromAlbum(album);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRealm = Realm.getDefaultInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

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

    @Override
    public void onDestroy() {
        if (mRealm != null) {
            mRealm.close();
            mRealm = null;
        }

        super.onDestroy();
    }


    @Subscribe
    public void openTrack(OpenTrackEvent event) {
        Log.d(TAG, "Track ID: " + event.trackId);
    }


    private void addChangeListener(String id) {
        removeChangeListener();

        if (mRealm != null && mAlbumId != null) {
            mAlbum = AlbumEntity.queryById(mRealm, id).findFirstAsync();
            mAlbum.addChangeListener(mChangeListener);
        }
    }

    private void removeChangeListener() {
        if (mAlbum != null) {
            mAlbum.removeChangeListener(mChangeListener);
            mAlbum = null;
        }
    }
}
