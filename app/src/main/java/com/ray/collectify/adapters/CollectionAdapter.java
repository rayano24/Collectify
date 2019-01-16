package com.ray.collectify.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ray.collectify.R;
import com.ray.collectify.model.Collection;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The following adapter is responsible for handling the collection recycler, which displays the collection name, description and image.
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyViewHolder> {

    private List<Collection> collectionList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView collectionName, collectionDescription;
        protected ImageView collectionImage; // while there are more variables, these are the only ones displayed in the recycler view

        public MyViewHolder(View view) {
            super(view);
            collectionName = view.findViewById(R.id.collectionTitle);
            collectionDescription = view.findViewById(R.id.collectionDescription);
            collectionImage = view.findViewById(R.id.collectionImage);


        }
    }


    public CollectionAdapter(List<Collection> collectionList) {
        this.collectionList = collectionList;
    }

    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_collections, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Collection collection = collectionList.get(position);
        holder.collectionName.setText(collection.getCollectionName());
        holder.collectionDescription.setText(collection.getCollectionDescription());
        Picasso.get().load(collection.getCollectionImageUrl()).into(holder.collectionImage); // picasso call to load image through the URL fetched through JSON


    }


    @Override
    public int getItemCount() {
        return collectionList.size();
    }


}