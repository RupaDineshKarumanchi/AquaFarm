package com.example.aquafarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FeedStockAdapter extends RecyclerView.Adapter<FeedStockAdapter.ViewHolder> {

    Context context;
    List<FeedStockModel> feedStockList;

    public FeedStockAdapter(Context context, List<FeedStockModel> feedStockList) {
        this.context = context;
        this.feedStockList = feedStockList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.table_row_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedStockAdapter.ViewHolder holder, int position) {
        if (feedStockList != null && feedStockList.size() >0){
            FeedStockModel model = feedStockList.get(position);
            holder.brand.setText(model.getBrand());
            holder.feedNo.setText(model.getFeedNo());
            holder.avlQty.setText(model.getAvlQty());
        } else {
            return;
        }
    }

    @Override
    public int getItemCount() {
        return feedStockList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView brand, feedNo, avlQty;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            brand = itemView.findViewById(R.id.table_item_brand);
            feedNo = itemView.findViewById(R.id.table_item_feed_size);
            avlQty = itemView.findViewById(R.id.table_item_avl_qty);



        }

    }

}
