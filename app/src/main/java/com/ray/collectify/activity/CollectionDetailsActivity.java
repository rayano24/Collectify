package com.ray.collectify.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ray.collectify.CollectionAdapter;
import com.ray.collectify.CollectionDetailsAdapter;
import com.ray.collectify.R;
import com.ray.collectify.RecyclerTouchListener;
import com.ray.collectify.model.Collection;
import com.ray.collectify.model.CollectionDetails;
import com.ray.collectify.utils.HttpUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.msebera.android.httpclient.Header;


/**
 * Displays details of a specific trip and gives the user options to message a driver, leave a trip, or rate it.
 */
public class CollectionDetailsActivity extends AppCompatActivity {


    private List<CollectionDetails> collectionDetailsList = new ArrayList<>();
    private CollectionDetailsAdapter collectionDetailsAdapter;
    private RecyclerView collectionDetailsRecycler;

    private TextView collectionTitleView, collectionDescriptionView;
    private ImageView collectionImageView;


    private final String KEY_COLLECTION_NAME = "colName";
    private final String KEY_COLLECTION_ID = "colID";
    private final String KEY_COLLECTION_IMAGE_URL = "colImageUrl";
    private final String KEY_COLLECTION_DESCRIPTION = "colDescription";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_details);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String collectionName = prefs.getString(KEY_COLLECTION_NAME, null);
        long collectionID = prefs.getLong(KEY_COLLECTION_ID, -1);
        String collectionDescription = prefs.getString(KEY_COLLECTION_DESCRIPTION, null);;
        String collectionImageUrl = prefs.getString(KEY_COLLECTION_IMAGE_URL, null);;


        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(collectionName);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorControl));
        setSupportActionBar(toolbar);

        collectionDetailsRecycler = findViewById(R.id.collectionDetailsRecycler);
        collectionTitleView = findViewById(R.id.collectionTitle);
        collectionDescriptionView = findViewById(R.id.collectionDescription);
        collectionImageView = findViewById(R.id.collectionImage);


        collectionTitleView.setText(collectionName);
        collectionDescriptionView.setText(collectionDescription);
        Picasso.get().load(collectionImageUrl).into(collectionImageView);







        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        collectionDetailsAdapter = new CollectionDetailsAdapter(collectionDetailsList);


        RecyclerView.LayoutManager collectionDetailsLayoutManager = new LinearLayoutManager(CollectionDetailsActivity.this);
        collectionDetailsRecycler.setLayoutManager(collectionDetailsLayoutManager);
        collectionDetailsRecycler.setItemAnimator(new DefaultItemAnimator());
        collectionDetailsRecycler.setAdapter(collectionDetailsAdapter);


        loadProductList(collectionID, collectionName);


    }


    public void loadProductList(long id, final String collectionName) {


        HttpUtils.get("admin/collects.json?collection_id=" + Long.toString(id) + "&page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6", new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {

                    JSONArray array = response.getJSONArray("collects");

                    // parse all the data needed and put each trip one by one into the recycler view, if it is empty, display the error message
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        long productID = obj.getLong("product_id");

                        // TODO, please improve this in a future iteration, it may be bad practice

                        HttpUtils.get("admin/products.json?ids=" + Long.toString(productID) + "&page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6", new RequestParams(), new JsonHttpResponseHandler() {
                            @Override
                            public void onFinish() {
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                try {
                                    //   collectionList.clear();

                                    JSONArray productsArray = response.getJSONArray("products");
                                    JSONObject imageObject = null;





                                    // parse all the data needed and put each trip one by one into the recycler view, if it is empty, display the error message
                                    for (int i = 0; i < productsArray.length(); i++) {
                                        JSONObject product = productsArray.getJSONObject(i);
                                        String productTitle = product.getString("title");

                                        JSONArray variantsArray = product.getJSONArray("variants");

                                        imageObject = product.getJSONObject("image");

                                        int inventory = 0;

                                        for(int j = 0; j < variantsArray.length(); j++) {

                                            inventory += variantsArray.getJSONObject(i).getInt("inventory_quantity");

                                        }



                                        if(imageObject != null) {

                                            String imageUrl = imageObject.getString("src");
                                            String formattedUrl = imageUrl.replaceAll("(?<!https:)//", "/");


                                            collectionDetailsList.add(new CollectionDetails(collectionName, productTitle, inventory, formattedUrl));

                                        }
                                        else {
                                            collectionDetailsList.add(new CollectionDetails(collectionName, productTitle, inventory, "https://upload.wikimedia.org/wikipedia/commons/a/ac/No_image_available.svg"));


                                        }



                                    }

                                    collectionDetailsAdapter.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                Toast.makeText(CollectionDetailsActivity.this, "There was a network error, try again later.", Toast.LENGTH_LONG).show(); // generic network error


                            }

                        });

                    }

                    collectionDetailsAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(CollectionDetailsActivity.this, "There was a network error, try again later.", Toast.LENGTH_LONG).show(); // generic network error


            }

        });
    }


    public void getProduct(long productID, final String collectionName) {



    }

}




