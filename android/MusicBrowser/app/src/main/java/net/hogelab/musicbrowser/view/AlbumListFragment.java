package net.hogelab.musicbrowser.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.databinding.FragmentAlbumListBinding;
import net.hogelab.musicbrowser.model.AlbumListLoader;
import net.hogelab.musicbrowser.model.entity.AlbumListOwner;
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

    private AlbumListOwner mListOwner;
    private final RealmChangeListener<AlbumListOwner> mChangedListener = (element) -> {
        if (element.isValid() && element.isLoaded()) {
            mAdapter.swapList(element.getFirstAlbumList());
            mBinding.swipeRefreshProgress.setRefreshing(false);
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
            mAdapter.swapList(null);
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            // ロードを待って表示したい場合はココで
            addChangeListener(mArtistId);
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

        mBinding.swipeRefreshProgress.setEnabled(false);
        mBinding.swipeRefreshProgress.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        mBinding.swipeRefreshProgress.setRefreshing(true);

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

            // Loader のロードを待たずに保存済のリスト表示をする場合ココで
//            addChangeListener(mArtistId);
        } catch (ClassCastException e) {
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        removeChangeListener();
        mRealm = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getLoaderManager().destroyLoader(ALBUM_LIST_LOADER_ID);
    }


    private void addChangeListener(String id) {
        removeChangeListener();

        if (mRealm != null) {
            mListOwner = AlbumListOwner.queryById(mRealm, id).findFirstAsync();
            mListOwner.addChangeListener(mChangedListener);
        }
    }

    private void removeChangeListener() {
        if (mListOwner != null) {
            mListOwner.removeChangeListener(mChangedListener);
            mListOwner = null;
        }
    }
}
