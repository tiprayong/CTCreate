package com.comtip.tip.ctcreate;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by user on 26/12/2559.
 */
public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.CustomViewHolder> {
    private Activity context;
    private String [] critique;

    private OnItem_ClickListener onItemClickListener;
    private OnItem_LongClickListener onItemLongClickListener;

    public CustomRecyclerViewAdapter(Activity context, String [] critique) {
        this.context = context;
        this.critique = critique;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_custom, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {

        holder.textViewCustom.setText(Html.fromHtml(critique[position]));
        //normal click
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        };
        holder.textViewCustom.setOnClickListener(listener);

        //long click
        View.OnLongClickListener longListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClickListener.onItemLongClickListener(position);
                return false;
            }
        };
        holder.textViewCustom.setOnLongClickListener(longListener);
    }

    @Override
    public int getItemCount() {
        return critique.length;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCustom;


        public CustomViewHolder(View itemView) {
            super(itemView);
            this.textViewCustom = (TextView) itemView.findViewById(R.id.textviewCustom);
        }
    }

    // Normal Click
    public interface OnItem_ClickListener {
        void onItemClick(int position);
    }

    public OnItem_ClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItem_ClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // Long Click
    public interface OnItem_LongClickListener {
        void onItemLongClickListener(int position);
    }

    public OnItem_LongClickListener getOnItemLongClickListener(){
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItem_LongClickListener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }


}
