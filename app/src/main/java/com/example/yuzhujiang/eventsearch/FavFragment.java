package com.example.yuzhujiang.eventsearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavFragment extends Fragment {
    private static final String TAG = "FavFragment";

    private List<FavItem> listFav;
    private RecyclerView recyclerView;
    View myView;
    private RequestQueue requestQueue;
    private TextView noFav;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fav_fragment, container,false);

        recyclerView = myView.findViewById(R.id.recycleFav);
        listFav = new ArrayList<>();

        noFav = myView.findViewById(R.id.NoFav);
//        noFav.setVisibility(View.VISIBLE);

        // request queue
        requestQueue = Volley.newRequestQueue(this.getContext());


        handleFav();


        return myView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }


    private void handleFav(){

        // load sharedPreference
        SharedPreferences sharedPreferences = myView.getContext().getSharedPreferences("favItems", Context.MODE_PRIVATE);

//        sharedPreferences.edit().clear().commit();///////////////

        Map<String,?> keys = sharedPreferences.getAll();

        if(keys.size() == 0){
            noFav.setVisibility(View.VISIBLE);
        }else{
            noFav.setVisibility(View.GONE);
            requestFavItem(keys);
        }


    }



    private void requestFavItem(Map<String,?> keys){

        for(Map.Entry<String, ?> entry: keys.entrySet()){


            String favUrl = "http://yuzhu.us-east-2.elasticbeanstalk.com/event_detail?eventId=" + entry.getValue();

            JsonObjectRequest favRequest = new JsonObjectRequest(Request.Method.GET, favUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("fav json", response.toString());/////////////////////////////

                    try {

                        FavItem favItem = new FavItem();
                        favItem.setName(response.getString("detail_name"));
                        favItem.setCategory(response.getString("detail_type"));
                        favItem.setId(response.getString("detail_id"));
                        favItem.setVenue(response.getString("detail_venue"));
                        favItem.setLat(response.getString("detail_lat"));
                        favItem.setLng(response.getString("detail_lng"));
                        favItem.setTime(response.getString("detail_time"));

                        JSONArray artists = response.getJSONArray("detail_artist");
                        ArrayList<String> arrayList = new ArrayList<>();

                        for(int j = 0; j < artists.length(); j++){
                            JSONObject curJs = artists.getJSONObject(j);
                            String curArt = curJs.getString("name");
                            arrayList.add(curArt);
                        }

                        favItem.setArtList(arrayList);

                        String category = response.getString("detail_type");

                        if(category.equals("Sports")){
                            favItem.setImgUrl("http://csci571.com/hw/hw9/images/android/sport_icon.png");
                        }else if(category.equals("Music")){
                            favItem.setImgUrl("http://csci571.com/hw/hw9/images/android/music_icon.png");
                        }else if(category.equals("Film")){
                            favItem.setImgUrl("http://csci571.com/hw/hw9/images/android/film_icon.png");
                        }else if(category.equals("Miscellaneous")){
                            favItem.setImgUrl("http://csci571.com/hw/hw9/images/android/miscellaneous_icon.png");
                        }else{
                            favItem.setImgUrl("http://csci571.com/hw/hw9/images/android/art_icon.png");
                        }

                        listFav.add(favItem);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    FavViewAdapter myad = new FavViewAdapter(myView.getContext(), listFav);
                    recyclerView.setLayoutManager(new LinearLayoutManager(myView.getContext()));
                    recyclerView.setAdapter(myad);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // check error
                    Log.i("fav error", error.getMessage());
                }
            });
            requestQueue.add(favRequest);

        }


    }
}
