package net.hogelab.musicbrowser.view;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.databinding.FragmentTrackListBinding;
import net.hogelab.musicbrowser.model.AudioMediaStoreCursorFactory;
import net.hogelab.musicbrowser.viewmodel.TrackListViewModel;

/**
 * Created by kobayasi on 2016/04/18.
 */
public class TrackListFragment extends Fragment {

    private static final String TAG = TrackListFragment.class.getSimpleName();

    private static final String BUNDLE_ALBUM_ID_KEY = "albumId";
    private static final int TRACK_LIST_LOADER_ID = 1;


    private FragmentTrackListBinding mBinding;
    private TrackListAdapter mAdapter;
    private String mAlbumId;

    private final LoaderManager.LoaderCallbacks trackListLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            final String albumId = args.getString(BUNDLE_ALBUM_ID_KEY);
            if (albumId != null) {
                return AudioMediaStoreCursorFactory.createAlbumTrackListCursorLoader(getActivity(), albumId);
            } else {
                return AudioMediaStoreCursorFactory.createAllTrackListCursorLoader(getActivity());
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mAdapter.swapCursor(null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mAdapter.swapCursor(data);
            mBinding.swipeRefreshProgress.setRefreshing(false);
        }
    };


    public static TrackListFragment newInstance(String albumId) {
        TrackListFragment fragment = new TrackListFragment();

        Bundle args = new Bundle();
        if (albumId != null) {
            args.putString(BUNDLE_ALBUM_ID_KEY, albumId);
        }
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mAlbumId = args.getString(BUNDLE_ALBUM_ID_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentTrackListBinding.inflate(inflater, container, false);
        mBinding.setViewModel(new TrackListViewModel());

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new TrackListAdapter(getActivity(), null);
        mBinding.trackList.setAdapter(mAdapter);

        mBinding.swipeRefreshProgress.setEnabled(false);
        mBinding.swipeRefreshProgress.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        mBinding.swipeRefreshProgress.setRefreshing(true);

        Bundle args = new Bundle();
        if (mAlbumId != null) {
            args.putString(BUNDLE_ALBUM_ID_KEY, mAlbumId);
        }
        getLoaderManager().initLoader(TRACK_LIST_LOADER_ID, args, trackListLoaderCallback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getLoaderManager().destroyLoader(TRACK_LIST_LOADER_ID);
    }
}
