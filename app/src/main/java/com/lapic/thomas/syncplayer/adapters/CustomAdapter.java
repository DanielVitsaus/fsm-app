package com.lapic.thomas.syncplayer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.lapic.thomas.syncplayer.R;
import com.lapic.thomas.syncplayer.data.model.App;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thomasmarquesbrandaoreis on 28/09/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private Context mContext;
    private List<App> mList;

    public CustomAdapter(Context context, List<App> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public CustomAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_view, parent, false);
        return new CustomAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomAdapter.CustomViewHolder holder, int position) {
        App item = mList.get(position);
        holder.nome.setText(item.getId());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setList(List<App> list) {
        this.mList = list;
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        protected TextView nome;

        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
