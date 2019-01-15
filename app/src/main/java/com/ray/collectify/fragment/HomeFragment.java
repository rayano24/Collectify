package com.ray.collectify.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ray.collectify.CollectionAdapter;
import com.ray.collectify.R;
import com.ray.collectify.RecyclerTouchListener;
import com.ray.collectify.activity.CollectionDetailsActivity;
import com.ray.collectify.model.Collection;
import com.ray.collectify.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.Header;

/**
 * Displays the user's trips and allows them to open an individual trip as an instance of TripActivity
 */
public class HomeFragment extends Fragment {


    // recycler views

    private List<Collection> collectionList = new ArrayList<>();
    private CollectionAdapter collectionAdapter;

    private RecyclerView collectionRecycler;


    // preference keys

    private final String KEY_COLLECTION_NAME = "colName";
    private final String KEY_COLLECTION_ID = "colID";
    private final String KEY_COLLECTION_IMAGE_URL = "colImageUrl";
    private final String KEY_COLLECTION_DESCRIPTION = "colDescription";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());


        collectionRecycler = rootView.findViewById(R.id.collectionRecycler);


        collectionAdapter = new CollectionAdapter(collectionList);


        RecyclerView.LayoutManager pastLayoutManager = new LinearLayoutManager(getActivity());
        collectionRecycler.setLayoutManager(pastLayoutManager);
        collectionRecycler.setItemAnimator(new DefaultItemAnimator());
        collectionRecycler.setAdapter(collectionAdapter);


        // Click listener for trip selection
        collectionRecycler.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), collectionRecycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Collection collection = collectionList.get(position);
                prefs.edit().putString(KEY_COLLECTION_NAME, collection.getCollectionName()).apply();
                prefs.edit().putLong(KEY_COLLECTION_ID, collection.getCollectionID()).apply();
                prefs.edit().putString(KEY_COLLECTION_DESCRIPTION, collection.getCollectionDescription()).apply();
                prefs.edit().putString(KEY_COLLECTION_IMAGE_URL, collection.getImageUrl()).apply();

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
     * An async method that retrieves each collection title and ID and stores it in the collectionList
     */
    public void displayCollectionList() {

        HttpUtils.get("admin/custom_collections.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6", new RequestParams(), new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    collectionList.clear();

                    JSONArray array = response.getJSONArray("custom_collections");

                    // parse all the data needed and put each trip one by one into the recycler view, if it is empty, display the error message
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        long id = obj.getLong("id");
                        String title = obj.getString("title");
                        String description = obj.getString("body_html");
                        String imageUrl = obj.getJSONObject("image").getString("src").replaceAll("(?<!https:)//", "/");



                        collectionList.add(new Collection(title, id, description, imageUrl));


                    }

                    collectionAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                    errorResponse) {
                Toast.makeText(getActivity(), "There was a network error, try again later.", Toast.LENGTH_LONG).show(); // generic network error
            }
        });

    }


}