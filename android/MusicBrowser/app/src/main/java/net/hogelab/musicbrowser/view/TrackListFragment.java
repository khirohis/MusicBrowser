package net.hogelab.musicbrowser.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.databinding.FragmentTrackListBinding;
import net.hogelab.musicbrowser.model.TrackListLoader;
import net.hogelab.musicbrowser.model.entity.TrackList;
import net.hogelab.musicbrowser.model.entity.wrapper.TrackListWrapper;
import net.hogelab.musicbrowser.viewmodel.TrackListViewModel;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * Created by kobayasi on 2016/04/18.
 */
public class TrackListFragment extends Fragment {
    private static final String TAG = TrackListFragment.class.getSimpleName();


    private static final String BUNDLE_ALBUM_ID_KEY = "albumId";

    private static final int TRACK_LIST_LOADER_ID = 1;


    private Realm mRealm;
    private FragmentTrackListBinding mBinding;
    private TrackListAdapter mAdapter;
    private String mAlbumId;

    private TrackList mTrackList;
    private final RealmChangeListener<TrackList> mChangedListener = (element) -> {
        if (element.isValid() && element.isLoaded()) {
            mAdapter.swapListWrapper(new TrackListWrapper(element));
            mBinding.swipeRefreshProgress.setRefreshing(false);
        }
    };


    private final LoaderManager.LoaderCallbacks trackListLoaderCallback = new LoaderManager.LoaderCallbacks<String>() {

        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            final String albumId = args.getString(BUNDLE_ALBUM_ID_KEY);
            return new TrackListLoader(getActivity(), albumId);
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {
            mAdapter.swapListWrapper(null);
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            if (data != null) {
                addChangedListener(data);
            }
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
        mBinding.swipeRefreshProgress.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        mBinding.swipeRefreshProgress.setRefreshing(true);

        Bundle args = new Bundle();
        if (mAlbumId != null) {
            args.putString(BUNDLE_ALBUM_ID_KEY, mAlbumId);
        }
        getLoaderManager().initLoader(TRACK_LIST_LOADER_ID, args, trackListLoaderCallback);
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            TrackListActivity activity = (TrackListActivity) getActivity();
            mRealm = activity.getRealm();

            // Loader のロードを待たずに保存済のリスト表示をする場合ココで
            // addChangedListener();
        } catch (ClassCastException e) {
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        removeChangedListener();
        mRealm = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getLoaderManager().destroyLoader(TRACK_LIST_LOADER_ID);
    }


    private void addChangedListener(String id) {
        removeChangedListener();

        if (mRealm != null) {
            mTrackList = mRealm.where(TrackList.class).equalTo("id", id).findFirstAsync();
            mTrackList.addChangeListener(mChangedListener);
        }
    }

    private void removeChangedListener() {
        if (mTrackList != null) {
            mTrackList.removeChangeListener(mChangedListener);
            mTrackList = null;
        }
    }
}
