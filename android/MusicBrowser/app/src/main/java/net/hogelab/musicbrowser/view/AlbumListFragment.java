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
            if (mRealm != null) {
                AlbumList list = mRealm.where(AlbumList.class).equalTo("id", data).findFirst();
                if (list != null) {
                    mAdapter.swapListWrapper(new AlbumListWrapper(list));
                }
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

        mRealm = Realm.getDefaultInstance();

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
    public void onDestroyView() {
        super.onDestroyView();

        getLoaderManager().destroyLoader(ALBUM_LIST_LOADER_ID);
    }

    @Override
    public void onDestroy() {
        if (mRealm != null) {
            mRealm.close();
        }

        super.onDestroy();
    }
}
