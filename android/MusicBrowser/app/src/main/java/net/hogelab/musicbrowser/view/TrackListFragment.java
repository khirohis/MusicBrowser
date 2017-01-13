package net.hogelab.musicbrowser.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.hogelab.musicbrowser.databinding.FragmentTrackListBinding;
import net.hogelab.musicbrowser.model.TrackListLoader;
import net.hogelab.musicbrowser.model.entity.TrackList;
import net.hogelab.musicbrowser.model.entity.wrapper.TrackListWrapper;
import net.hogelab.musicbrowser.viewmodel.TrackListViewModel;

import io.realm.Realm;

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
            TrackList list = mRealm.where(TrackList.class).equalTo("id", data).findFirst();
            if (list != null) {
                mAdapter.swapListWrapper(new TrackListWrapper(list));
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

        mRealm = Realm.getDefaultInstance();

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

    @Override
    public void onDestroy() {
        if (mRealm != null) {
            mRealm.close();
        }

        super.onDestroy();
    }
}
