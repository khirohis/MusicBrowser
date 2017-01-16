package net.hogelab.musicbrowser.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.hogelab.musicbrowser.databinding.FragmentArtistListBinding;
import net.hogelab.musicbrowser.model.ArtistListLoader;
import net.hogelab.musicbrowser.model.entity.ArtistList;
import net.hogelab.musicbrowser.model.entity.wrapper.ArtistListWrapper;
import net.hogelab.musicbrowser.viewmodel.ArtistListViewModel;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * Created by kobayasi on 2016/04/01.
 */
public class ArtistListFragment extends Fragment {
    private static final String TAG = ArtistListFragment.class.getSimpleName();


    private static final int ARTIST_LIST_LOADER_ID = 1;


    private Realm mRealm;
    private FragmentArtistListBinding mBinding;
    private ArtistListAdapter mAdapter;

    private ArtistList mArtistList;
    private final RealmChangeListener<ArtistList> mChangedListener = new RealmChangeListener<ArtistList>() {

        @Override
        public void onChange(ArtistList element) {
            if (element.isValid() && element.isLoaded()) {
                mAdapter.swapListWrapper(new ArtistListWrapper(element));
            }
        }
    };


    private final LoaderManager.LoaderCallbacks artistListLoaderCallback = new LoaderManager.LoaderCallbacks<String>() {

        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            return new ArtistListLoader(getContext());
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


    public static ArtistListFragment newInstance() {
        return new ArtistListFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentArtistListBinding.inflate(inflater, container, false);
        mBinding.setViewModel(new ArtistListViewModel());

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new ArtistListAdapter(getActivity(), null);
        mBinding.artistList.setAdapter(mAdapter);

        getLoaderManager().initLoader(ARTIST_LIST_LOADER_ID, null, artistListLoaderCallback);
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            ArtistListActivity activity = (ArtistListActivity) getActivity();
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

        getLoaderManager().destroyLoader(ARTIST_LIST_LOADER_ID);
    }


    private void addChangedListener(String id) {
        removeChangedListener();

        if (mRealm != null) {
            mArtistList = mRealm.where(ArtistList.class).equalTo("id", id).findFirstAsync();
            mArtistList.addChangeListener(mChangedListener);
        }
    }

    private void removeChangedListener() {
        if (mArtistList != null) {
            mArtistList.removeChangeListener(mChangedListener);
            mArtistList = null;
        }
    }
}
