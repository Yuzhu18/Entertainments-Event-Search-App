package com.example.yuzhujiang.eventsearch;

import java.util.ArrayList;

public class FavItem {

    private String name;
    private String time;
    private String venue;
    private String id;
    private String imgUrl;
    private String category;
    private ArrayList<String> artList;
    private String lat;
    private String lng;

    public FavItem() {
    }

    public FavItem(String name, String time, String venue, String id, String imgUrl, String category, ArrayList<String> artList, String lat, String lng) {
        this.name = name;
        this.time = time;
        this.venue = venue;
        this.id = id;
        this.imgUrl = imgUrl;
        this.category = category;
        this.artList = artList;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getVenue() {
        return venue;
    }

    public String getId() {
        return id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<String> getArtList() {
        return artList;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setArtList(ArrayList<String> artList) {
        this.artList = artList;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
