package com.ray.collectify;


//Recycler view adapter for workout history

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import com.ray.collectify.model.Collection;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyViewHolder> {

    private List<Collection> collectionList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView collectionName; // while there are more variables, these are the only ones displayed in the recycler view

        public MyViewHolder(View view) {
            super(view);
            collectionName = view.findViewById(R.id.detail_product_name);





        }
    }


    public CollectionAdapter(List<Collection> collectionList) { this.collectionList = collectionList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_collections, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Collection collection = collectionList.get(position);
        holder.collectionName.setText(collection.getCollectionName());



    }



    @Override
    public int getItemCount() {
        return collectionList.size();
    }


}