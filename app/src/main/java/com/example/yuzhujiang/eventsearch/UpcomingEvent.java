package com.example.yuzhujiang.eventsearch;

public class UpcomingEvent {

    private String display;
    private String artist;
    private String time;
    private String type;
    private String uri;


    public UpcomingEvent() {
    }

    public UpcomingEvent(String display, String artist, String time, String type, String uri) {
        this.display = display;
        this.artist = artist;
        this.time = time;
        this.type = type;
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public String getDisplay() {
        return display;
    }

    public String getArtist() {
        return artist;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }



    public void setDisplay(String display) {
        this.display = display;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
