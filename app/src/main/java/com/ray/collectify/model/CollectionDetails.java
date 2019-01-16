package com.ray.collectify.model;

/**
 * Model for the collection details class which is used in the recycler view to display a specific product's title, inventory and image. It also lists the collection title.
 */
public class CollectionDetails {

    private String collectionName, productName, productImageUrl;
    private int productInventory;

    public CollectionDetails() {
    }

    public CollectionDetails(String collectionName, String productName, int productInventory, String productImageUrl) {
        this.collectionName = collectionName;
        this.productName = productName;
        this.productInventory = productInventory;
        this.productImageUrl = productImageUrl;


    }

    public String getCollectionName() {
        return collectionName;
    }


    public int getProductInventory() {
        return productInventory;
    }


    public String getProductName() {
        return productName;
    }


    public String getProductImageUrl() {
        return productImageUrl;
    }


}


