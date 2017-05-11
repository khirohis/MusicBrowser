package net.hogelab.musicbrowser.view;

import android.content.Context;
import android.database.Cursor;
import android.databinding.ViewDataBinding;

import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.databinding.ListItemArtistBinding;
import net.hogelab.musicbrowser.viewmodel.ArtistListItemViewModel;

/**
 * Created by kobayasi on 2016/04/01.
 */
public class ArtistListAdapter extends RecyclerViewCursorAdapter {

    private static final String TAG = ArtistListAdapter.class.getSimpleName();


    public ArtistListAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }


    @Override
    public int getItemViewType(int position) {
        return R.layout.list_item_artist;
    }

    @Override
    protected void setupDataBinding(ViewDataBinding dataBinding, Cursor cursor, int position) {
        ListItemArtistBinding binding = (ListItemArtistBinding) dataBinding;

        // clear immediate
        binding.thumbnail.setImageDrawable(null);
        binding.setViewModel(new ArtistListItemViewModel(mContext, cursor));
    }
}
