package net.hogelab.musicbrowser.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import net.hogelab.musicbrowser.databinding.FragmentAlbumListBinding;
import net.hogelab.musicbrowser.event.EventBus;
import net.hogelab.musicbrowser.event.OpenAlbumEvent;
import net.hogelab.musicbrowser.model.AudioMediaStoreCursorLoaderFactory;
import net.hogelab.musicbrowser.viewmodel.AlbumListViewModel;

/**
 * Created by kobayasi on 2016/04/11.
 */
public class AlbumListFragment extends Fragment {
    private static final String TAG = AlbumListFragment.class.getSimpleName();


    private static final int ALBUM_LIST_LOADER_ID = 1;


    private FragmentAlbumListBinding mBinding;
    private AlbumListAdapter mAdapter;
    private long mArtistId;


    private final LoaderManager.LoaderCallbacks albumsLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            final long artistId = args.getLong("artistId");

            return AudioMediaStoreCursorLoaderFactory.createAlbumsCursorLoader(getActivity(), artistId);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mAdapter.swapCursor(null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mAdapter.swapCursor(data);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mArtistId = args.getLong("artistId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAlbumListBinding.inflate(inflater, container, false);
        mBinding.setViewModel(new AlbumListViewModel());

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new AlbumListAdapter(getActivity(), null);
        mBinding.albumList.setAdapter(mAdapter);

        Bundle args = new Bundle();
        args.putLong("artistId", mArtistId);
        getLoaderManager().initLoader(ALBUM_LIST_LOADER_ID, args, albumsLoaderCallback);
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
    public void onDestroyView() {
        super.onDestroyView();

        getLoaderManager().destroyLoader(ALBUM_LIST_LOADER_ID);
    }


    @Subscribe
    public void openAlbum(OpenAlbumEvent event) {
        Intent intent = new Intent(getActivity(), TrackListActivity.class);
        intent.putExtra("albumId", event.albumId);

        startActivity(intent);
    }
}
