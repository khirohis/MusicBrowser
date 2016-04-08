package net.hogelab.musicbrowser.mvvm.view;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.hogelab.musicbrowser.databinding.FragmentMvvmArtistListBinding;
import net.hogelab.musicbrowser.mvvm.viewmodel.ArtistListViewModel;

/**
 * Created by kobayasi on 2016/04/01.
 */
public class ArtistListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>  {
    private static final String TAG = ArtistListFragment.class.getSimpleName();


    private static final int ARTIST_LIST_LOADER_ID = 1;

    public static final String[] ARTISTS_FIELDS_PROJECTION = {
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
    };

    private FragmentMvvmArtistListBinding mBinding;
    private ArtistListAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentMvvmArtistListBinding.inflate(inflater, container, false);
        mBinding.setViewModel(new ArtistListViewModel());

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new ArtistListAdapter(getActivity(), null, false);
        mBinding.artistList.setAdapter(mAdapter);

        getLoaderManager().initLoader(ARTIST_LIST_LOADER_ID, null, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getLoaderManager().destroyLoader(ARTIST_LIST_LOADER_ID);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                ARTISTS_FIELDS_PROJECTION,
                null,
                null,
                MediaStore.Audio.Artists.ARTIST + " COLLATE LOCALIZED ASC");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);

        mAdapter.notifyDataSetChanged();
    }
}
