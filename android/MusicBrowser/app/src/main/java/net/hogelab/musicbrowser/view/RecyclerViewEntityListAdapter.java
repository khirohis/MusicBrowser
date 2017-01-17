package net.hogelab.musicbrowser.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import net.hogelab.musicbrowser.R;
import net.hogelab.musicbrowser.model.entity.EntityHolder;
import net.hogelab.musicbrowser.model.entity.EntityList;

/**
 * Created by kobayasi on 2017/01/16.
 */

public abstract class RecyclerViewEntityListAdapter extends RecyclerView.Adapter<RecyclerViewEntityListAdapter.BindingHolder> {

    protected Context mContext;
    protected LayoutInflater mInflater;

    protected EntityList mList;


    public RecyclerViewEntityListAdapter(Context context, EntityList list) {
        mContext = context;
        mInflater = LayoutInflater.from(context);

        mList = list;

        // 宿題：mList の変更を監視してリストをアップデートすること
    }

    public EntityList getList() {
        return mList;
    }

    public EntityList swapList(EntityList list) {
        if (list == mList) {
            return null;
        }

        EntityList oldList = mList;
        mList = list;

        notifyDataSetChanged();

        // oldList の解放タイミング

        return oldList;
    }


    @Override
    public int getItemViewType(int position) {
        return R.layout.list_item_artist;
    }

    @Override
    public RecyclerViewEntityListAdapter.BindingHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        ViewDataBinding dataBinding = DataBindingUtil.inflate(mInflater, viewType, viewGroup, false);
        return new RecyclerViewEntityListAdapter.BindingHolder(dataBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerViewEntityListAdapter.BindingHolder viewHolder, int position) {
        if (mList != null) {
            EntityHolder holder = mList.getHolder(position);
            Object listItem = holder.getEntity();
            setupDataBinding(viewHolder.dataBinding, listItem, position);
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        } else {
            return 0;
        }
    }


    protected abstract void setupDataBinding(ViewDataBinding dataBinding, Object listItem, int position);


    public static class BindingHolder extends RecyclerView.ViewHolder {
        protected ViewDataBinding dataBinding;

        public BindingHolder(ViewDataBinding dataBinding) {
            super(dataBinding.getRoot());

            this.dataBinding = dataBinding;
        }
    }
}
