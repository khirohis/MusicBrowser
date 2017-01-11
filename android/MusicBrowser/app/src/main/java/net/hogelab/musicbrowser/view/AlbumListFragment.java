package net.hogelab.musicbrowser.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
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


    private static final String BUNDLE_ARTIST_ID_KEY = "artistId";

    private static final int ALBUM_LIST_LOADER_ID = 1;


    private FragmentAlbumListBinding mBinding;
    private AlbumListAdapter mAdapter;
    private long mArtistId;

    private final LoaderManager.LoaderCallbacks albumListLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            final long artistId = args.getLong(BUNDLE_ARTIST_ID_KEY);
            if (artistId != 0L) {
                return AudioMediaStoreCursorLoaderFactory.createAlbumListCursorLoader(getActivity(), artistId);
            } else {
                return AudioMediaStoreCursorLoaderFactory.createAlbumListCursorLoader(getActivity());
            }
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


    public static AlbumListFragment newInstance(long artistId) {
        AlbumListFragment fragment = new AlbumListFragment();

        Bundle args = new Bundle();
        if (artistId != 0) {
            args.putLong(BUNDLE_ARTIST_ID_KEY, artistId);
        }
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mArtistId = args.getLong(BUNDLE_ARTIST_ID_KEY, 0L);
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
        if (mArtistId != 0L) {
            args.putLong(BUNDLE_ARTIST_ID_KEY, mArtistId);
        }
        getLoaderManager().initLoader(ALBUM_LIST_LOADER_ID, args, albumListLoaderCallback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getLoaderManager().destroyLoader(ALBUM_LIST_LOADER_ID);
    }
}
