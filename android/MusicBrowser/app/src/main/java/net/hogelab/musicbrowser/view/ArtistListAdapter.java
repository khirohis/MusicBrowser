package net.hogelab.musicbrowser.view;

import android.content.Context;
import android.databinding.ViewDataBinding;

import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.databinding.ListItemArtistBinding;
import net.hogelab.musicbrowser.model.entity.ArtistEntity;
import net.hogelab.musicbrowser.model.entity.EntityList;
import net.hogelab.musicbrowser.viewmodel.ArtistListItemViewModel;

/**
 * Created by kobayasi on 2016/04/01.
 */
public class ArtistListAdapter extends RecyclerViewEntityListAdapter {

    private static final String TAG = ArtistListAdapter.class.getSimpleName();


    public ArtistListAdapter(Context context, EntityList list) {
        super(context, list);
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
        binding.setViewModel(new ArtistListItemViewModel(mContext, (ArtistEntity) listItem));
    }
}
