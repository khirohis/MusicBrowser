package net.hogelab.musicbrowser.view;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by kobayasi on 2016/04/12.
 */
public abstract class RecyclerViewCursorAdapter extends RecyclerView.Adapter<RecyclerViewCursorAdapter.BindingHolder> {


    protected Context mContext;
    protected Cursor mCursor;

    protected LayoutInflater mInflater;


    public RecyclerViewCursorAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;

        mInflater = LayoutInflater.from(context);
    }


    public Cursor getCursor() {
        return mCursor;
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }

        Cursor oldCursor = mCursor;
        mCursor = newCursor;
        notifyDataSetChanged();

        return oldCursor;
    }


    protected abstract void setupDataBinding(ViewDataBinding dataBinding, Cursor cursor, int position);


    @Override
    public BindingHolder onCreateViewHolder(ViewGroup viewGroup, int layoutId) {
        ViewDataBinding dataBinding = DataBindingUtil.inflate(mInflater, layoutId, viewGroup, false);
        return new BindingHolder(dataBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder viewHolder, int position) {
        if (mCursor != null) {
            mCursor.moveToPosition(position);
            setupDataBinding(viewHolder.dataBinding, mCursor, position);
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }


    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding dataBinding;

        public BindingHolder(ViewDataBinding dataBinding) {
            super(dataBinding.getRoot());

            this.dataBinding = dataBinding;
        }
    }
}
