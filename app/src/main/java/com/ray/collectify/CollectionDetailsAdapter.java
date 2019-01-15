package com.ray.collectify;


//Recycler view adapter for workout history

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ray.collectify.model.Collection;
import com.ray.collectify.model.CollectionDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CollectionDetailsAdapter extends RecyclerView.Adapter<CollectionDetailsAdapter.MyViewHolder> {

    private List<CollectionDetails> collectionDetailsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView collectionName, productName, productInventory; // while there are more variables, these are the only ones displayed in the recycler view
        protected ImageView productImage;


        public MyViewHolder(View view) {
            super(view);
            collectionName = view.findViewById(R.id.detail_collection_name);
            productName = view.findViewById(R.id.detail_product_name);
            productInventory = view.findViewById(R.id.detail_product_inventory);
            productImage = view.findViewById(R.id.detail_product_image);









        }
    }


    public CollectionDetailsAdapter(List<CollectionDetails> collectionDetailsList) { this.collectionDetailsList = collectionDetailsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_collection_details, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CollectionDetails collection = collectionDetailsList.get(position);
        holder.collectionName.setText("From the " + collection.getCollectionName());
        holder.productName.setText(collection.getProductName());
        holder.productInventory.setText(Integer.toString(collection.getProductInventory()) + " units");
        Picasso.get().load(collection.getProductImage()).into(holder.productImage);




    }



    @Override
    public int getItemCount() {
        return collectionDetailsList.size();
    }


}