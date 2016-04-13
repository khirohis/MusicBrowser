package net.hogelab.musicbrowser.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import net.hogelab.musicbrowser.databinding.FragmentArtistListBinding;
import net.hogelab.musicbrowser.event.EventBus;
import net.hogelab.musicbrowser.event.OpenArtistEvent;
import net.hogelab.musicbrowser.model.AudioMediaStoreCursorLoaderFactory;
import net.hogelab.musicbrowser.viewmodel.ArtistListViewModel;

/**
 * Created by kobayasi on 2016/04/01.
 */
public class ArtistListFragment extends Fragment {
    private static final String TAG = ArtistListFragment.class.getSimpleName();


    private static final int ARTIST_LIST_LOADER_ID = 1;


    private FragmentArtistListBinding mBinding;
    private ArtistListAdapter mAdapter;


    private final LoaderManager.LoaderCallbacks artistListLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return AudioMediaStoreCursorLoaderFactory.createArtistListCursorLoader(getActivity());
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
    public void onResume() {
        super.onResume();

        EventBus.getBus().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getBus().unregister(this);

        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getLoaderManager().destroyLoader(ARTIST_LIST_LOADER_ID);
    }


    @Subscribe
    public void openArtist(OpenArtistEvent event) {
        Intent intent = new Intent(getActivity(), AlbumListActivity.class);
        intent.putExtra("artistId", event.artistId);

        startActivity(intent);
    }
}
