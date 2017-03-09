package net.hogelab.musicbrowser.view;

import android.content.Context;
import android.databinding.ViewDataBinding;

import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.databinding.ListItemTrackBinding;
import net.hogelab.musicbrowser.databinding.ListItemTrackHeaderBinding;
import net.hogelab.musicbrowser.model.entity.EntityList;
import net.hogelab.musicbrowser.model.entity.TrackEntity;
import net.hogelab.musicbrowser.viewmodel.TrackListHeaderViewModel;
import net.hogelab.musicbrowser.viewmodel.TrackListItemViewModel;

/**
 * Created by kobayasi on 2016/04/18.
 */
public class TrackListAdapter extends RecyclerViewEntityListAdapter {

    private static final String TAG = TrackListAdapter.class.getSimpleName();


    public TrackListAdapter(Context context, EntityList listObject) {
        super(context, listObject);
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return R.layout.list_item_track_header;
        }

        return R.layout.list_item_track;
    }

    @Override
    public void onBindViewHolder(BindingHolder viewHolder, int position) {
        if (viewHolder.getItemViewType() == R.layout.list_item_track_header) {
            setupHeaderDataBinding(viewHolder.dataBinding);
        } else {
            super.onBindViewHolder(viewHolder, position - 1);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    protected void setupDataBinding(ViewDataBinding dataBinding, Object listItem, int position) {
        ListItemTrackBinding binding = (ListItemTrackBinding) dataBinding;

        // clear immediate
        binding.thumbnail.setImageDrawable(null);
        binding.setViewModel(new TrackListItemViewModel(mContext, (TrackEntity) listItem));
    }

    protected void setupHeaderDataBinding(ViewDataBinding dataBinding) {
        ListItemTrackHeaderBinding binding = (ListItemTrackHeaderBinding) dataBinding;
        binding.setViewModel(new TrackListHeaderViewModel());
    }
}
