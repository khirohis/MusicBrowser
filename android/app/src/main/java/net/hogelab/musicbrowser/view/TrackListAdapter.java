package net.hogelab.musicbrowser.view;

import android.content.Context;
import android.database.Cursor;
import android.databinding.ViewDataBinding;

import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.databinding.ListItemTrackBinding;
import net.hogelab.musicbrowser.viewmodel.TrackListItemViewModel;

/**
 * Created by kobayasi on 2016/04/18.
 */
public class TrackListAdapter extends RecyclerViewCursorAdapter {


    public TrackListAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }


    @Override
    public int getItemViewType(int position) {
        return R.layout.list_item_track;
    }

    @Override
    protected void setupDataBinding(ViewDataBinding dataBinding, Cursor cursor, int position) {
        ListItemTrackBinding binding = (ListItemTrackBinding) dataBinding;

        // clear immediate
        binding.thumbnail.setImageDrawable(null);
        binding.setViewModel(new TrackListItemViewModel(mContext, cursor));
    }
}
