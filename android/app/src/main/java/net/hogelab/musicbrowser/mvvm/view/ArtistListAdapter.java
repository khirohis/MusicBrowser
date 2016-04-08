package net.hogelab.musicbrowser.mvvm.view;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.hogelab.musicbrowser.databinding.FragmentMvvmArtistListBinding;
import net.hogelab.musicbrowser.databinding.ListItemMvvmArtistBinding;
import net.hogelab.musicbrowser.mvvm.viewmodel.ArtistListItemViewModel;
import net.hogelab.musicbrowser.mvvm.viewmodel.ArtistListViewModel;

/**
 * Created by kobayasi on 2016/04/01.
 */
public class ArtistListAdapter extends CursorAdapter {

    private LayoutInflater mInflater;


    public ArtistListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ListItemMvvmArtistBinding binding = ListItemMvvmArtistBinding.inflate(mInflater);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ListItemMvvmArtistBinding binding = DataBindingUtil.getBinding(view);
        if (binding != null) {
            // clear immediate
            binding.thumbnail.setImageDrawable(null);

            binding.setViewModel(new ArtistListItemViewModel(view.getContext().getApplicationContext(), cursor));
        }
    }
}
