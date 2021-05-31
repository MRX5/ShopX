package com.example.shopx.SearchFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopx.Model.ProductInfo;
import com.example.shopx.Model.myResponse;
import com.example.shopx.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.viewHolder> {
    private List<ProductInfo> items;
     private onItemClickListener listener;
    public SearchAdapter(onItemClickListener listener)
    {
        this.listener=listener;
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    public void setItems(List<ProductInfo> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    class viewHolder extends RecyclerView.ViewHolder {
        private TextView productName;
        private TextView productCategory;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productCategory = itemView.findViewById(R.id.product_category);
            itemView.setOnClickListener(v-> {
                ProductInfo currItem=items.get(getAdapterPosition());
                listener.onItemClick(currItem);
            });
        }
        private void bind(ProductInfo currItem)
        {
            productName.setText(currItem.getName());
            productCategory.setText(currItem.getCategory());
        }
    }
    interface onItemClickListener{
        void onItemClick(ProductInfo product);
    }
}
