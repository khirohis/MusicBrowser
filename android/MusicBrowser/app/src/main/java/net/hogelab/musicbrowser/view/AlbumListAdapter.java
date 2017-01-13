package net.hogelab.musicbrowser.view;

import android.content.Context;
import android.databinding.ViewDataBinding;

import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.databinding.ListItemAlbumBinding;
import net.hogelab.musicbrowser.model.entity.Album;
import net.hogelab.musicbrowser.model.entity.wrapper.AlbumListWrapper;
import net.hogelab.musicbrowser.viewmodel.AlbumListItemViewModel;

/**
 * Created by kobayasi on 2016/04/11.
 */
public class AlbumListAdapter extends RecyclerViewListWrapperAdapter {


    public AlbumListAdapter(Context context, AlbumListWrapper listObject) {
        super(context, listObject);
    }


    @Override
    public int getItemViewType(int position) {
        return R.layout.list_item_album;
    }

    @Override
    public void setupDataBinding(ViewDataBinding dataBinding, Object listItem, int position) {
        ListItemAlbumBinding binding = (ListItemAlbumBinding) dataBinding;

        // clear immediate
        binding.thumbnail.setImageDrawable(null);
        binding.setViewModel(new AlbumListItemViewModel(mContext, (Album) listItem));
    }
}
