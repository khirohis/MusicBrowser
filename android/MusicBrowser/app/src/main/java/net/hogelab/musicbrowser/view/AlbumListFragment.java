package net.hogelab.musicbrowser.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.hogelab.musicbrowser.databinding.FragmentAlbumListBinding;
import net.hogelab.musicbrowser.model.AlbumListLoader;
import net.hogelab.musicbrowser.model.entity.AlbumList;
import net.hogelab.musicbrowser.model.entity.wrapper.AlbumListWrapper;
import net.hogelab.musicbrowser.viewmodel.AlbumListViewModel;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * Created by kobayasi on 2016/04/11.
 */
public class AlbumListFragment extends Fragment {
    private static final String TAG = AlbumListFragment.class.getSimpleName();


    private static final String BUNDLE_ARTIST_ID_KEY = "artistId";

    private static final int ALBUM_LIST_LOADER_ID = 1;


    private Realm mRealm;
    private FragmentAlbumListBinding mBinding;
    private AlbumListAdapter mAdapter;
    private String mArtistId;

    private AlbumList mAlbumList;
    private final RealmChangeListener<AlbumList> mChangedListener = new RealmChangeListener<AlbumList>() {

        @Override
        public void onChange(AlbumList element) {
            if (element.isValid() && element.isLoaded()) {
                mAdapter.swapListWrapper(new AlbumListWrapper(element));
            }
        }
    };


    private final LoaderManager.LoaderCallbacks albumListLoaderCallback = new LoaderManager.LoaderCallbacks<String>() {

        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            final String artistId = args.getString(BUNDLE_ARTIST_ID_KEY);
            return new AlbumListLoader(getContext(), artistId);
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


    public static AlbumListFragment newInstance(String artistId) {
        AlbumListFragment fragment = new AlbumListFragment();

        Bundle args = new Bundle();
        if (artistId != null) {
            args.putString(BUNDLE_ARTIST_ID_KEY, artistId);
        }
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mArtistId = args.getString(BUNDLE_ARTIST_ID_KEY);
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
        if (mArtistId != null) {
            args.putString(BUNDLE_ARTIST_ID_KEY, mArtistId);
        }
        getLoaderManager().initLoader(ALBUM_LIST_LOADER_ID, args, albumListLoaderCallback);
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            AlbumListActivity activity = (AlbumListActivity) getActivity();
            mRealm = activity.getRealm();
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

        getLoaderManager().destroyLoader(ALBUM_LIST_LOADER_ID);
    }


    private void addChangedListener(String id) {
        removeChangedListener();

        if (mRealm != null) {
            mAlbumList = mRealm.where(AlbumList.class).equalTo("id", id).findFirstAsync();
            mAlbumList.addChangeListener(mChangedListener);
        }
    }

    private void removeChangedListener() {
        if (mAlbumList != null) {
            mAlbumList.removeChangeListener(mChangedListener);
            mAlbumList = null;
        }
    }
}
