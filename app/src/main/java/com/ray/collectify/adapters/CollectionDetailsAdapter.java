package com.ray.collectify.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ray.collectify.R;
import com.ray.collectify.model.CollectionDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The following adapter is responsible for handling the collection details recycler, which displays a specific product's name, inventory and image as well as it's collection.
 */
public class CollectionDetailsAdapter extends RecyclerView.Adapter<CollectionDetailsAdapter.MyViewHolder> {

    private List<CollectionDetails> collectionDetailsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView collectionName, productName, productInventory;
        protected ImageView productImage;


        public MyViewHolder(View view) {
            super(view);
            collectionName = view.findViewById(R.id.detailCollectionName);
            productName = view.findViewById(R.id.detailProductName);
            productInventory = view.findViewById(R.id.detailProductInventory);
            productImage = view.findViewById(R.id.detailProductImage);


        }
    }


    public CollectionDetailsAdapter(List<CollectionDetails> collectionDetailsList) {
        this.collectionDetailsList = collectionDetailsList;
    }

    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_collection_details, parent, false);
        // disabling the ability to focus on/click on elements of the recycler view as it does not open any other pages
        itemView.setClickable(false);
        itemView.setFocusable(false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CollectionDetails collection = collectionDetailsList.get(position);
        holder.collectionName.setText("From the " + collection.getCollectionName());
        holder.productName.setText(collection.getProductName());
        holder.productInventory.setText(Integer.toString(collection.getProductInventory()) + " units");
        Picasso.get().load(collection.getProductImageUrl()).into(holder.productImage);


    }


    @Override
    public int getItemCount() {
        return collectionDetailsList.size();
    }


}