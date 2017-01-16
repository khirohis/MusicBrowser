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
import net.hogelab.musicbrowser.model.entity.ArtistListOwner;
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

    private ArtistListOwner mListOwner;
    private final RealmChangeListener<ArtistListOwner> mArtistListChangeListener = new RealmChangeListener<ArtistListOwner>() {

        @Override
        public void onChange(ArtistListOwner element) {
            if (element.isValid() && element.isLoaded()) {
                mAdapter.swapList(element.getFirstArtistList());
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
            mAdapter.swapList(null);
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            // ロードを待って表示したい場合はココで addChangeListener()
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

            // Loader のロードを待たずに保存済のリスト表示をする場合ココで
            addChangeListener();
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

        getLoaderManager().destroyLoader(ARTIST_LIST_LOADER_ID);
    }


    private void addChangeListener() {
        removeChangeListener();

        if (mRealm != null) {
            mListOwner = ArtistListOwner.query(mRealm).findFirstAsync();
            mListOwner.addChangeListener(mArtistListChangeListener);
        }
    }

    private void removeChangeListener() {
        if (mListOwner != null) {
            mListOwner.removeChangeListener(mArtistListChangeListener);
            mListOwner = null;
        }
    }
}
