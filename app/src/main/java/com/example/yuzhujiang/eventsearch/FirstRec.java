package com.example.yuzhujiang.eventsearch;

import java.util.ArrayList;

public class FirstRec {

    public FirstRec() {

    }

    private String name;
    private String time;
    private String venue;
    private String id;
    private String imgUrl;
    private String category;
    private ArrayList<String> artList;
    private Double lat;
    private Double lng;

    public FirstRec(String name, String time, String venue, String id, String imgUrl, String category, ArrayList<String> artList, Double lat, Double lng) {
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

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public ArrayList<String> getArtList() {
        return artList;
    }

    public FirstRec(String name, String time, String venue, String id, String imgUrl, String category) {
        this.name = name;
        this.time = time;
        this.venue = venue;
        this.id = id;
        this.imgUrl = imgUrl;
        this.category = category;
    }

    public String getCategory() {
        return category;
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

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
