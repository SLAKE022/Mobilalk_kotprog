package com.example.italdiszkont;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ShoppingItemAdapter extends RecyclerView.Adapter<ShoppingItemAdapter.ViewHolder> implements Filterable {
    private ArrayList<ShoppingItem> shoppingItemsData = new ArrayList<>();
    private ArrayList<ShoppingItem> shoppingItemsDataAll = new ArrayList<>();
    private Context context;
    private int lastPostition =-1;

    ShoppingItemAdapter(Context context, ArrayList<ShoppingItem> itemsData){
        this.shoppingItemsData = itemsData;
        this.shoppingItemsDataAll = itemsData;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder( ShoppingItemAdapter.ViewHolder holder, int position) {
        ShoppingItem currentItem = shoppingItemsData.get(position);

        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPostition){
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPostition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return shoppingItemsData.size();
    }

    @Override
    public Filter getFilter() {
        return shoppingFilter;
    }

    private Filter shoppingFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ShoppingItem> filterList = new ArrayList<>();
            FilterResults results = new FilterResults();
            if(constraint == null || constraint.length() == 0){
                results.count = shoppingItemsDataAll.size();
                results.values = shoppingItemsDataAll;
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(ShoppingItem item : shoppingItemsDataAll){
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filterList.add(item);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            shoppingItemsData = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titleText;
        private TextView infoText;
        private TextView priceText;
        private TextView alcoholText;
        private ImageView itemImage;
        private RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.itemTitle);
            infoText = itemView.findViewById(R.id.subTitle);
            priceText = itemView.findViewById(R.id.price);
            alcoholText = itemView.findViewById(R.id.alcoholContent);
            itemImage = itemView.findViewById(R.id.itemImage);
            ratingBar = itemView.findViewById(R.id.ratingBar);

        }

        public void bindTo(ShoppingItem currentItem) {
            titleText.setText(currentItem.getName());
            infoText.setText(currentItem.getInfo());
            priceText.setText(currentItem.getPrice());
            alcoholText.setText(currentItem.getAlcohol());
            ratingBar.setRating(currentItem.getRated());

            Glide.with(context).load(currentItem.getImageResource()).into(itemImage);
            itemView.findViewById(R.id.add_to_cart).setOnClickListener(v -> ((ShopActivity)context).updateAlertIcon(currentItem));
        }
    }
}

