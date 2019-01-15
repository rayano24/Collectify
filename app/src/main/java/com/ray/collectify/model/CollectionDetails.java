package com.ray.collectify.model;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Constructor for the collection class used in recyclerView.
 */
public class CollectionDetails {

    private String collectionName, productName, productImage;
    private int productInventory;

    public CollectionDetails() {
    }

    public CollectionDetails(String collectionName, String productName, int productInventory, String productImage) {
        this.collectionName = collectionName;
        this.productName = productName;
        this.productInventory = productInventory;
        this.productImage = productImage;



    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }


    public int getProductInventory() {
        return productInventory;
    }

    public void setProductInventory(int productInventory) {
        this.productInventory = productInventory;
    }



    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String imageLink) {
        this.productImage = imageLink;
    }




}


