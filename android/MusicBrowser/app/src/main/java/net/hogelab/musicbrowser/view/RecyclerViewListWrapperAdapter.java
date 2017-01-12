package net.hogelab.musicbrowser.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.model.entity.wrapper.ListWrapper;

/**
 * Created by kobayasi on 2017/01/12.
 */

public abstract class RecyclerViewListWrapperAdapter extends RecyclerView.Adapter<RecyclerViewListWrapperAdapter.BindingHolder> {

    protected Context mContext;
    protected LayoutInflater mInflater;

    protected ListWrapper mListWrapper;


    public RecyclerViewListWrapperAdapter(Context context, ListWrapper listWrapper) {
        mContext = context;
        mInflater = LayoutInflater.from(context);

        mListWrapper = listWrapper;
    }

    public ListWrapper getListWrapper() {
        return mListWrapper;
    }

    public ListWrapper swapListWrapper(ListWrapper listWrapper) {
        if (listWrapper == mListWrapper) {
            return null;
        }

        ListWrapper oldListWrapper = mListWrapper;
        mListWrapper = listWrapper;

        notifyDataSetChanged();

        return oldListWrapper;
    }


    public abstract void setupDataBinding(ViewDataBinding dataBinding, Object listItem, int position);


    @Override
    public int getItemViewType(int position) {
        return R.layout.list_item_artist;
    }

    @Override
    public RecyclerViewListWrapperAdapter.BindingHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        ViewDataBinding dataBinding = DataBindingUtil.inflate(mInflater, viewType, viewGroup, false);
        return new RecyclerViewListWrapperAdapter.BindingHolder(dataBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerViewListWrapperAdapter.BindingHolder viewHolder, int position) {
        if (mListWrapper != null) {
            Object listItem = mListWrapper.get(position);
            setupDataBinding(viewHolder.dataBinding, listItem, position);
        }
    }

    @Override
    public int getItemCount() {
        if (mListWrapper != null) {
            return mListWrapper.size();
        } else {
            return 0;
        }
    }


    public static class BindingHolder extends RecyclerView.ViewHolder {
        protected ViewDataBinding dataBinding;

        public BindingHolder(ViewDataBinding dataBinding) {
            super(dataBinding.getRoot());

            this.dataBinding = dataBinding;
        }
    }
}
