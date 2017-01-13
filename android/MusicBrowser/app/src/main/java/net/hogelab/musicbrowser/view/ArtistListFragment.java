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
import io.realm.RealmResults;

/**
 * Created by kobayasi on 2016/04/01.
 */
public class ArtistListFragment extends Fragment {
    private static final String TAG = ArtistListFragment.class.getSimpleName();


    private static final int ARTIST_LIST_LOADER_ID = 1;


    private Realm mRealm;
    private FragmentArtistListBinding mBinding;
    private ArtistListAdapter mAdapter;


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
            ArtistList list = mRealm.where(ArtistList.class).equalTo("id", data).findFirst();
            if (list != null) {
                mAdapter.swapListWrapper(new ArtistListWrapper(list));
            }
        }
    };


    public static ArtistListFragment newInstance() {
        return new ArtistListFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRealm = Realm.getDefaultInstance();
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
    public void onDestroyView() {
        super.onDestroyView();

        getLoaderManager().destroyLoader(ARTIST_LIST_LOADER_ID);
    }

    @Override
    public void onDestroy() {
        if (mRealm != null) {
            mRealm.close();
        }

        super.onDestroy();
    }
}
