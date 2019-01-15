package com.ray.collectify.model;

/**
 * Constructor for the collection class used in recyclerView.
 */
public class Collection {

    private String collectionName, collectionDescription, imageUrl;
    private long collectionID;

    public Collection() {
    }

    public Collection(String collectionName, long collectionID, String collectionDescription, String imageUrl) {
        this.collectionName = collectionName;
        this.collectionID = collectionID;
        this.collectionDescription = collectionDescription;
        this.imageUrl = imageUrl;


    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }



    public long getCollectionID() {
        return collectionID;
    }

    public void setCollectionID(long id) {
        this.collectionID = collectionID;
    }



    public String getCollectionDescription() {
        return collectionDescription;
    }

    public void setCollectionDescription(String description) {
        this.collectionDescription = description;
    }



    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String url) {
        this.imageUrl = url;
    }












}


