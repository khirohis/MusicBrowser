package net.hogelab.musicbrowser.view;

import android.content.Context;
import android.databinding.ViewDataBinding;

import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.databinding.ListItemArtistBinding;
import net.hogelab.musicbrowser.model.entity.Artist;
import net.hogelab.musicbrowser.model.entity.wrapper.ArtistListWrapper;
import net.hogelab.musicbrowser.viewmodel.ArtistListItemViewModel;

/**
 * Created by kobayasi on 2016/04/01.
 */
public class ArtistListAdapter extends RecyclerViewListWrapperAdapter {

    public ArtistListAdapter(Context context, ArtistListWrapper listObject) {
        super(context, listObject);
    }


    @Override
    public int getItemViewType(int position) {
        return R.layout.list_item_artist;
    }


    @Override
    public void setupDataBinding(ViewDataBinding dataBinding, Object listItem, int position) {
        ListItemArtistBinding binding = (ListItemArtistBinding) dataBinding;

        // clear immediate
        binding.thumbnail.setImageDrawable(null);
        binding.setViewModel(new ArtistListItemViewModel(mContext, (Artist) listItem));
    }
}
