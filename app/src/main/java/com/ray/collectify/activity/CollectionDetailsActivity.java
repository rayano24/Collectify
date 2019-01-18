package com.ray.collectify.activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ray.collectify.R;
import com.ray.collectify.adapters.CollectionAdapter;
import com.ray.collectify.adapters.CollectionDetailsAdapter;
import com.ray.collectify.model.Collection;
import com.ray.collectify.model.CollectionDetails;
import com.ray.collectify.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.Header;


/**
 * Displays specific products of a collection as well as a header with the collection image, title and description.
 */
public class CollectionDetailsActivity extends AppCompatActivity {


    // recycler related elements

    private List<CollectionDetails> collectionDetailsList = new ArrayList<>();
    private List<Collection> collectionDetailsHeaderList = new ArrayList<>();
    private CollectionDetailsAdapter collectionDetailsAdapter;
    private CollectionAdapter collectionDetailsHeaderAdapter;
    private RecyclerView collectionDetailsRecycler, collectionDetailsHeaderRecycler;

    // preference keys

    private static final String KEY_COLLECTION_NAME = "colName";
    private static final String KEY_COLLECTION_ID = "colID";
    private static final String KEY_COLLECTION_IMAGE_URL = "colImageUrl";
    private static final String KEY_COLLECTION_DESCRIPTION = "colDescription";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_details);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String collectionName = prefs.getString(KEY_COLLECTION_NAME, null);
        long collectionID = prefs.getLong(KEY_COLLECTION_ID, -1);
        String collectionDescription = prefs.getString(KEY_COLLECTION_DESCRIPTION, null);
        String collectionImageUrl = prefs.getString(KEY_COLLECTION_IMAGE_URL, null);


        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);

        // updating the toolbar title with the collection name
        toolbar.setTitle(collectionName);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorControl));
        setSupportActionBar(toolbar);

        // configuring the recycler views for the collection header and product list

        collectionDetailsRecycler = findViewById(R.id.collectionDetailsRecycler);
        collectionDetailsHeaderRecycler = findViewById(R.id.collectionDetailsHeaderRecycler);

        collectionDetailsHeaderAdapter = new CollectionAdapter(collectionDetailsHeaderList);
        collectionDetailsAdapter = new CollectionDetailsAdapter(collectionDetailsList);


        // setting up the layout managers

        RecyclerView.LayoutManager collectionDetailsRecyclerLm = new LinearLayoutManager(CollectionDetailsActivity.this);
        RecyclerView.LayoutManager collectionDetailsHeaderRecyclerLm = new LinearLayoutManager(CollectionDetailsActivity.this);

        collectionDetailsHeaderRecycler.setLayoutManager(collectionDetailsHeaderRecyclerLm);
        collectionDetailsRecycler.setLayoutManager(collectionDetailsRecyclerLm);

        collectionDetailsHeaderRecycler.setItemAnimator(new DefaultItemAnimator());
        collectionDetailsRecycler.setItemAnimator(new DefaultItemAnimator());

        collectionDetailsHeaderRecycler.setAdapter(collectionDetailsHeaderAdapter);
        collectionDetailsRecycler.setAdapter(collectionDetailsAdapter);

        // adding elements to the collection header
        collectionDetailsHeaderList.add(new Collection(collectionName, collectionID, collectionDescription, collectionImageUrl));


        // returns to the paused activity (main in this case) when pressing the back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        loadProductList(collectionID, collectionName, collectionImageUrl);


    }


    /**
     * An async call that is responsible for loading the list of products for a specific collection.
     *
     * @param id             a long representing the collection ID
     * @param collectionName a string representing the collection name
     */
    private void loadProductList(long id, final String collectionName, final String collectionImageUrl) {


        HttpUtils.get("admin/collects.json?collection_id=" + Long.toString(id) + "&page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6", new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                // this is not particularly necessary, as this is a singular activity, however done in case
                collectionDetailsList.clear();

                try {

                    JSONArray collectsList = response.getJSONArray("collects");

                    for (int collectsIndex = 0; collectsIndex < collectsList.length(); collectsIndex++) {

                        JSONObject obj = collectsList.getJSONObject(collectsIndex);
                        long productID = obj.getLong("product_id");

                        // TODO, potentially poor practice, please look into improving in the future

                        // this now looks into retrieving the specific products from the collection
                        HttpUtils.get("admin/products.json?ids=" + Long.toString(productID) + "&page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6", new RequestParams(), new JsonHttpResponseHandler() {
                            @Override
                            public void onFinish() {
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                try {

                                    JSONArray productsArray = response.getJSONArray("products");


                                    for (int productIndex = 0; productIndex < productsArray.length(); productIndex++) {
                                        JSONObject product = productsArray.getJSONObject(productIndex);
                                        String productTitle = product.getString("title");


                                        // in order to get the overall product inventory, we must check the inventory of each variant of the product
                                        JSONArray variantsArray = product.getJSONArray("variants");

                                        int inventory = 0;

                                        for (int variantIndex = 0; variantIndex < variantsArray.length(); variantIndex++) {

                                            inventory += variantsArray.getJSONObject(variantIndex).getInt("inventory_quantity");

                                        }


                                        collectionDetailsList.add(new CollectionDetails(collectionName, productTitle, inventory, collectionImageUrl));


                                    }

                                    collectionDetailsAdapter.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                Toast.makeText(CollectionDetailsActivity.this, getResources().getString(R.string.general_network_error), Toast.LENGTH_LONG).show(); // generic network error


                            }

                        });

                    }

                    collectionDetailsAdapter.notifyDataSetChanged();
                    // this was done to prevent the slow loading in. there is a load in delay due to the nature of the above request
                    collectionDetailsRecycler.setVisibility(View.VISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(CollectionDetailsActivity.this, getResources().getString(R.string.general_network_error), Toast.LENGTH_LONG).show(); // generic network error


            }

        });
    }


}




