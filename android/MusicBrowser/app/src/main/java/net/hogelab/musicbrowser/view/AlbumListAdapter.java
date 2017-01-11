package net.hogelab.musicbrowser.view;

import android.content.Context;
import android.database.Cursor;
import android.databinding.ViewDataBinding;

import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.databinding.ListItemAlbumBinding;
import net.hogelab.musicbrowser.viewmodel.AlbumListItemViewModel;

/**
 * Created by kobayasi on 2016/04/11.
 */
public class AlbumListAdapter extends RecyclerViewCursorAdapter {


    public AlbumListAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }


    @Override
    public int getItemViewType(int position) {
        return R.layout.list_item_album;
    }

    @Override
    protected void setupDataBinding(ViewDataBinding dataBinding, Cursor cursor, int position) {
        ListItemAlbumBinding binding = (ListItemAlbumBinding) dataBinding;

        // clear immediate
        binding.thumbnail.setImageDrawable(null);
        binding.setViewModel(new AlbumListItemViewModel(mContext, cursor));
    }
}
