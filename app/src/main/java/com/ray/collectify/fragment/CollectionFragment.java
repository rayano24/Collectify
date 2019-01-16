package com.ray.collectify.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ray.collectify.R;
import com.ray.collectify.activity.CollectionDetailsActivity;
import com.ray.collectify.adapters.CollectionAdapter;
import com.ray.collectify.model.Collection;
import com.ray.collectify.utils.HttpUtils;
import com.ray.collectify.utils.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.Header;

/**
 * Displays the user's trips and allows them to open an individual trip as an instance of TripActivity
 */
public class CollectionFragment extends Fragment {


    // recycler related elements

    private List<Collection> collectionList = new ArrayList<>();
    private CollectionAdapter collectionAdapter;
    private RecyclerView collectionRecycler;


    // preference keys

    private final String KEY_COLLECTION_NAME = "colName";
    private final String KEY_COLLECTION_ID = "colID";
    private final String KEY_COLLECTION_IMAGE_URL = "colImageUrl";
    private final String KEY_COLLECTION_DESCRIPTION = "colDescription";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_collection, container, false);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());


        // setting up the recycler view

        collectionRecycler = rootView.findViewById(R.id.collectionRecycler);

        collectionAdapter = new CollectionAdapter(collectionList);

        RecyclerView.LayoutManager pastLayoutManager = new LinearLayoutManager(getActivity());
        collectionRecycler.setLayoutManager(pastLayoutManager);
        collectionRecycler.setItemAnimator(new DefaultItemAnimator());
        collectionRecycler.setAdapter(collectionAdapter);


        // Click listener for collection selection
        collectionRecycler.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), collectionRecycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Collection collection = collectionList.get(position);
                // sending collection information to shared preferences so it can be used in the details activity
                prefs.edit().putString(KEY_COLLECTION_NAME, collection.getCollectionName()).apply();
                prefs.edit().putLong(KEY_COLLECTION_ID, collection.getCollectionID()).apply();
                prefs.edit().putString(KEY_COLLECTION_DESCRIPTION, collection.getCollectionDescription()).apply();
                prefs.edit().putString(KEY_COLLECTION_IMAGE_URL, collection.getCollectionImageUrl()).apply();

                Intent I = new Intent(getActivity(), CollectionDetailsActivity.class);
                startActivity(I);
            }

            @Override
            public void onLongClick(View view, int position) {
                // do nothing
            }
        }));

        displayCollectionList();


        return rootView;

    }


    /**
     * An async method that retrieves a collection's title, ID, image source and description through a get request. <br>
     * Loads it into a list to display in a recycler view.
     */
    public void displayCollectionList() {

        HttpUtils.get("admin/custom_collections.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6", new RequestParams(), new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    collectionList.clear();

                    JSONArray customCollectionsArray = response.getJSONArray("custom_collections");

                    // parse all the data needed and put each trip one by one into the recycler view, if it is empty, display the error message
                    for (int collectionIndex = 0; collectionIndex < customCollectionsArray.length(); collectionIndex++) {

                        JSONObject collection = customCollectionsArray.getJSONObject(collectionIndex);

                        long id = collection.getLong("id");
                        String title = collection.getString("title");
                        String description = collection.getString("body_html");

                        // removes escape characters from the URl so the image can properly be fetched
                        String imageUrl = collection.getJSONObject("image").getString("src").replaceAll("(?<!https:)//", "/");

                        if (description != null && !description.isEmpty()) {
                            collectionList.add(new Collection(title, id, description, imageUrl));
                        } else {
                            // if there is no description, set a generic statement to update it on the shopify admin panel
                            collectionList.add(new Collection(title, id, getResources().getString(R.string.collection_fragment_no_description), imageUrl));
                        }


                    }

                    collectionAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                    errorResponse) {
                Toast.makeText(getActivity(), getResources().getString(R.string.general_network_error), Toast.LENGTH_LONG).show(); // generic network error
            }
        });

    }


}