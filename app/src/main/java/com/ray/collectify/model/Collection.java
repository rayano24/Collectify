package com.ray.collectify.model;

/**
 * Model for the collection class which is used in the recycler view to display the collection title, description and image. It also stores the collection ID.
 */
public class Collection {

    private String collectionName, collectionDescription, collectionImageUrl;
    private long collectionID;

    public Collection() {
    }

    public Collection(String collectionName, long collectionID, String collectionDescription, String collectionImageUrl) {
        this.collectionName = collectionName;
        this.collectionID = collectionID;
        this.collectionDescription = collectionDescription;
        this.collectionImageUrl = collectionImageUrl;


    }

    public String getCollectionName() {
        return collectionName;
    }


    public long getCollectionID() {
        return collectionID;
    }


    public String getCollectionDescription() {
        return collectionDescription;
    }

    public void setCollectionDescription(String description) {
        this.collectionDescription = description;
    }


    public String getCollectionImageUrl() {
        return collectionImageUrl;
    }


}


