package com.example.yuzhujiang.eventsearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class searchRst extends AppCompatActivity {

    String searchList;
    private List<FirstRec> listFrist;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView searchLoad;
    private TextView NoResults;
    private boolean noRst = false;

    @Override
    protected void onRestart() {
        super.onRestart();

        setContentView(R.layout.activity_search_rst);

        recyclerView = findViewById(R.id.recycleId);
        listFrist = new ArrayList<>();

        progressBar = findViewById(R.id.proBar);
        searchLoad = findViewById(R.id.load);

        progressBar.setVisibility(View.GONE);
        searchLoad.setVisibility(View.GONE);
//        recyclerView.setVisibility(View.GONE);

        NoResults = findViewById(R.id.NoResults);
        NoResults.setVisibility(View.GONE);

        // get back button
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

//        handleRst();

        //get data from search Fragment
        Intent intent = getIntent();

        searchList = intent.getStringExtra("searchList");

//        if(searchList.length() == 0){
//            NoResults.setVisibility(View.VISIBLE);
//        }else{

        try {
            JSONArray rstArray  = new JSONArray(searchList);
            Log.i("rst Json array", Integer.toString(rstArray.length()));////////////////////

            JSONObject jsonObject = null;


            for(int i = 0 ; i < rstArray.length(); i++){

                jsonObject = rstArray.getJSONObject(i);
                FirstRec firstRec = new FirstRec();

                ///test artist array
                JSONArray artists = jsonObject.getJSONArray("rst_artist");
                ArrayList<String> artList = new ArrayList<>();

                for(int j = 0; j < artists.length(); j++){

                    JSONObject curJs = artists.getJSONObject(j);
                    String curArt = curJs.getString("name");
                    artList.add(curArt);

                }

                firstRec.setArtList(artList);

                firstRec.setName(jsonObject.getString("rst_name"));
                firstRec.setTime(jsonObject.getString("rst_time"));
                firstRec.setVenue(jsonObject.getString("rst_venue"));
                firstRec.setId(jsonObject.getString("rst_id"));
                firstRec.setCategory(jsonObject.getString("rst_category"));

                //get venue lat + lng
                firstRec.setLat(jsonObject.getDouble("rst_lat"));
                firstRec.setLng(jsonObject.getDouble("rst_lng"));

                String category = jsonObject.getString("rst_category");

                if(category.equals("Sports")){
                    firstRec.setImgUrl("http://csci571.com/hw/hw9/images/android/sport_icon.png");
                }else if(category.equals("Music")){
                    firstRec.setImgUrl("http://csci571.com/hw/hw9/images/android/music_icon.png");
                }else if(category.equals("Film")){
                    firstRec.setImgUrl("http://csci571.com/hw/hw9/images/android/film_icon.png");
                }else if(category.equals("Miscellaneous")){
                    firstRec.setImgUrl("http://csci571.com/hw/hw9/images/android/miscellaneous_icon.png");
                }else{
                    firstRec.setImgUrl("http://csci571.com/hw/hw9/images/android/art_icon.png");
                }

                listFrist.add(firstRec);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            noRst = true;
        }


        SearchRstAdapter myad = new SearchRstAdapter(this, listFrist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myad);

        recyclerView.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_rst);

        initilize();

        // get back button
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        handleRst();

//        startRec();

    }

    // get back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    private void handleRst(){

        //get data from search Fragment
        Intent intent = getIntent();

        searchList = intent.getStringExtra("searchList");

//        if(searchList.length() == 0){
//            NoResults.setVisibility(View.VISIBLE);
//        }else{

            try {
                JSONArray rstArray  = new JSONArray(searchList);
                Log.i("rst Json array", Integer.toString(rstArray.length()));////////////////////

                if(rstArray.length() == 0){
                    noRst = true;
                }


                JSONObject jsonObject = null;


                for(int i = 0 ; i < rstArray.length(); i++){

                    jsonObject = rstArray.getJSONObject(i);
                    FirstRec firstRec = new FirstRec();

                    ///test artist array
                    JSONArray artists = jsonObject.getJSONArray("rst_artist");
                    ArrayList<String> artList = new ArrayList<>();

                    for(int j = 0; j < artists.length(); j++){

                        JSONObject curJs = artists.getJSONObject(j);
                        String curArt = curJs.getString("name");
                        artList.add(curArt);

                    }

                    firstRec.setArtList(artList);

                    firstRec.setName(jsonObject.getString("rst_name"));
                    firstRec.setTime(jsonObject.getString("rst_time"));
                    firstRec.setVenue(jsonObject.getString("rst_venue"));
                    firstRec.setId(jsonObject.getString("rst_id"));
                    firstRec.setCategory(jsonObject.getString("rst_category"));

                    //get venue lat + lng
                    firstRec.setLat(jsonObject.getDouble("rst_lat"));
                    firstRec.setLng(jsonObject.getDouble("rst_lng"));

                    String category = jsonObject.getString("rst_category");

                    if(category.equals("Sports")){
                        firstRec.setImgUrl("http://csci571.com/hw/hw9/images/android/sport_icon.png");
                    }else if(category.equals("Music")){
                        firstRec.setImgUrl("http://csci571.com/hw/hw9/images/android/music_icon.png");
                    }else if(category.equals("Film")){
                        firstRec.setImgUrl("http://csci571.com/hw/hw9/images/android/film_icon.png");
                    }else if(category.equals("Miscellaneous")){
                        firstRec.setImgUrl("http://csci571.com/hw/hw9/images/android/miscellaneous_icon.png");
                    }else{
                        firstRec.setImgUrl("http://csci571.com/hw/hw9/images/android/art_icon.png");
                    }

                    listFrist.add(firstRec);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                noRst = true;

                Log.i("noRst", "true");
            }

            startRec();



            Log.i("from search fragment" , searchList);/////////////////////////////////////////////

        }

//    }

    private  void startRec(){

        SearchRstAdapter myad = new SearchRstAdapter(this, listFrist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myad);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        searchLoad.setVisibility(View.GONE);
                        if(noRst){
                            recyclerView.setVisibility(View.GONE);
                            NoResults.setVisibility(View.VISIBLE);
                        }else{

                            recyclerView.setVisibility(View.VISIBLE);
                            NoResults.setVisibility(View.GONE);
                        }
                    }
                }, 1500);

    }

    private void initilize(){

        recyclerView = findViewById(R.id.recycleId);
        listFrist = new ArrayList<>();

        progressBar = findViewById(R.id.proBar);
        searchLoad = findViewById(R.id.load);

        progressBar.setVisibility(View.VISIBLE);
        searchLoad.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        NoResults = findViewById(R.id.NoResults);
//        NoResults.setVisibility(View.GONE);

    }
}
