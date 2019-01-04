package com.example.yuzhujiang.eventsearch;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tab2Fragment extends Fragment {

    private static final String TAG = "detail fragment 2";
    private String event_category;
    private String[] art_list;


    private TextView art1_title;
    private TextView art1_name;
    private TextView art1_pop;
    private TextView art1_follower;
    private TextView art1_spotify;

    private TextView name1;
    private TextView pop1;
    private TextView follower1;
    private TextView spotify1;

    private TextView name2;
    private TextView pop2;
    private TextView follower2;
    private TextView spotify2;


    private TextView art2_title;
    private TextView art2_name;
    private TextView art2_pop;
    private TextView art2_follower;
    private TextView art2_spotify;

    private ImageView art1_img1;
    private ImageView art1_img2;
    private ImageView art1_img3;
    private ImageView art1_img4;
    private ImageView art1_img5;
    private ImageView art1_img6;
    private ImageView art1_img7;
    private ImageView art1_img8;


    private ImageView art2_img1;
    private ImageView art2_img2;
    private ImageView art2_img3;
    private ImageView art2_img4;
    private ImageView art2_img5;
    private ImageView art2_img6;
    private ImageView art2_img7;
    private ImageView art2_img8;

    private String image1 = "";

    private RequestQueue requestQueue;

    private View myView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.detail_tab2, container, false);

        art1_title = myView.findViewById(R.id.art_title);
        art1_name =  myView.findViewById(R.id.a_name);
        art1_follower = myView.findViewById(R.id.a_follower);
        art1_pop = myView.findViewById(R.id.a_popu);
        art1_spotify = myView.findViewById(R.id.a_spotify);

        name1 = myView.findViewById(R.id.art_name);
        follower1 = myView.findViewById(R.id.art_follower);
        pop1 = myView.findViewById(R.id.art_popu);
        spotify1 = myView.findViewById(R.id.art_spotify);



        art1_img1 = myView.findViewById(R.id.image1);
        art1_img2 = myView.findViewById(R.id.image2);
        art1_img3 = myView.findViewById(R.id.image3);
        art1_img4 = myView.findViewById(R.id.image4);
        art1_img5 = myView.findViewById(R.id.image5);
        art1_img6 = myView.findViewById(R.id.image6);
        art1_img7 = myView.findViewById(R.id.image7);
        art1_img8 = myView.findViewById(R.id.image8);


        art2_title = myView.findViewById(R.id.art_title2);
        art2_name =  myView.findViewById(R.id.a_name2);
        art2_follower = myView.findViewById(R.id.a_follower2);
        art2_pop = myView.findViewById(R.id.a_popu2);
        art2_spotify = myView.findViewById(R.id.a_spotify2);


        name2 = myView.findViewById(R.id.art_name2);
        follower2 = myView.findViewById(R.id.art_follower2);
        pop2 = myView.findViewById(R.id.art_popu2);
        spotify2 = myView.findViewById(R.id.art_spotify2);

        art2_img1 = myView.findViewById(R.id.image21);
        art2_img2 = myView.findViewById(R.id.image22);
        art2_img3 = myView.findViewById(R.id.image23);
        art2_img4 = myView.findViewById(R.id.image24);
        art2_img5 = myView.findViewById(R.id.image25);
        art2_img6 = myView.findViewById(R.id.image26);
        art2_img7 = myView.findViewById(R.id.image27);
        art2_img8 = myView.findViewById(R.id.image28);


        //get artList
        if(getActivity().getIntent().hasExtra("artList")){
            art_list = getActivity().getIntent().getStringArrayExtra("artList");

            //test if receive
            for(String s : art_list){
                Log.i("tab2 receive artist", s);///////////////////////////////
            }
        }

        if(getActivity().getIntent().hasExtra("eventCategory")){
            event_category = getActivity().getIntent().getStringExtra("eventCategory");

            Log.i("tab2 category", event_category);//////////////////////////////////////////
        }

        // request queue
        requestQueue = Volley.newRequestQueue(getActivity());


        if(event_category.equals("Music")){

            if(art_list.length == 1){
                art1_title.setText(art_list[0]);
                requestMusic1(art_list[0]);

                name2.setVisibility(View.GONE);
                follower2.setVisibility(View.GONE);
                pop2.setVisibility(View.GONE);
                spotify2.setVisibility(View.GONE);

                art2_title.setVisibility(View.GONE);
                art2_name.setVisibility(View.GONE);
                art2_follower.setVisibility(View.GONE);
                art2_pop.setVisibility(View.GONE);
                art2_spotify.setVisibility(View.GONE);

                art2_img1.setVisibility(View.GONE);
                art2_img2.setVisibility(View.GONE);
                art2_img3.setVisibility(View.GONE);
                art2_img4.setVisibility(View.GONE);
                art2_img5.setVisibility(View.GONE);
                art2_img6.setVisibility(View.GONE);
                art2_img7.setVisibility(View.GONE);
                art2_img8.setVisibility(View.GONE);

            }else{
                art1_title.setText(art_list[0]);
                art2_title.setText(art_list[1]);

                requestMusic1(art_list[0]);
                requestMusic2(art_list[1]);
            }

        }else{
            // is not music

            name1.setVisibility(View.GONE);
            follower1.setVisibility(View.GONE);
            pop1.setVisibility(View.GONE);
            spotify1.setVisibility(View.GONE);

            art1_name.setVisibility(View.GONE);
            art1_follower.setVisibility(View.GONE);
            art1_pop.setVisibility(View.GONE);
            art1_spotify.setVisibility(View.GONE);


            name2.setVisibility(View.GONE);
            follower2.setVisibility(View.GONE);
            pop2.setVisibility(View.GONE);
            spotify2.setVisibility(View.GONE);


            art2_name.setVisibility(View.GONE);
            art2_follower.setVisibility(View.GONE);
            art2_pop.setVisibility(View.GONE);
            art2_spotify.setVisibility(View.GONE);



            if(art_list.length == 1){

                art1_title.setText(art_list[0]);
                requestCustomer1(art_list[0]);

                art2_title.setVisibility(View.GONE);

                art2_img1.setVisibility(View.GONE);
                art2_img2.setVisibility(View.GONE);
                art2_img3.setVisibility(View.GONE);
                art2_img4.setVisibility(View.GONE);
                art2_img5.setVisibility(View.GONE);
                art2_img6.setVisibility(View.GONE);
                art2_img7.setVisibility(View.GONE);
                art2_img8.setVisibility(View.GONE);
            }else{
                art1_title.setText(art_list[0]);
                art2_title.setText(art_list[1]);
                requestCustomer1(art_list[0]);
                requestCustomer2(art_list[1]);
            }
        }


        return  myView;
    }



    private void requestMusic1(String art){

        String musicUrl = "http://yuzhu.us-east-2.elasticbeanstalk.com/artist_rst?artist=" + art;

        JsonObjectRequest musicRequest = new JsonObjectRequest(Request.Method.GET, musicUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("music table json", response.toString());
                try {


                    art1_name.setText(response.getString("art_name"));
                    art1_follower.setText(response.getString("art_follower"));
                    art1_pop.setText(response.getString("art_pop"));
                    String sUrl = response.getString("art_spotify");

                    //set spotify url
                    String spUrl = "<a href='" + sUrl + "' target='_blank'>Spotify</a >";

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        art1_spotify.setText(Html.fromHtml(spUrl, Html.FROM_HTML_MODE_COMPACT));
                    }else{
                        art1_spotify.setText(spUrl);
                    }
                    art1_spotify.setMovementMethod(LinkMovementMethod.getInstance());

                    //test
                    int art_follower = response.getInt("art_follower");
                    Log.i("artist get follower", Integer.toString(art_follower));//////////////////////////////////

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // check error
                Log.i("music table error", error.getMessage());//////////////////////////
            }
        });

        requestQueue.add(musicRequest);

        // also need to request customer api
        requestCustomer1(art);
    }


    private void requestMusic2(String art){

        String musicUrl = "http://yuzhu.us-east-2.elasticbeanstalk.com/artist_rst?artist=" + art;

        JsonObjectRequest musicRequest2 = new JsonObjectRequest(Request.Method.GET, musicUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("music table json", response.toString());
                try {


                    art2_name.setText(response.getString("art_name"));
                    art2_follower.setText(response.getString("art_follower"));
                    art2_pop.setText(response.getString("art_pop"));
                    String sUrl2 = response.getString("art_spotify");

                    //set spotify url
                    String spUrl = "<a href='" + sUrl2 + "' target='_blank'>Spotify</a >";

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        art2_spotify.setText(Html.fromHtml(spUrl, Html.FROM_HTML_MODE_COMPACT));
                    }else{
                        art2_spotify.setText(spUrl);
                    }
                    art2_spotify.setMovementMethod(LinkMovementMethod.getInstance());


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // check error
                Log.i("music table error", error.getMessage());//////////////////////////
            }
        });

        requestQueue.add(musicRequest2);

        // also need to request customer api
        requestCustomer2(art);
    }


    // request customer api
    private void requestCustomer1(String art){

        String customUrl = "http://yuzhu.us-east-2.elasticbeanstalk.com/img_rst?search=" + art;
        JsonObjectRequest customRequest = new JsonObjectRequest(Request.Method.GET, customUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("custom json", response.toString());
                try {

                    Glide.with(myView).load(response.getString("img1")).into(art1_img1);
                    Glide.with(myView).load(response.getString("img2")).into(art1_img2);
                    Glide.with(myView).load(response.getString("img3")).into(art1_img3);
                    Glide.with(myView).load(response.getString("img4")).into(art1_img4);
                    Glide.with(myView).load(response.getString("img5")).into(art1_img5);
                    Glide.with(myView).load(response.getString("img6")).into(art1_img6);
                    Glide.with(myView).load(response.getString("img7")).into(art1_img7);
                    Glide.with(myView).load(response.getString("img8")).into(art1_img8);

                    //test
                    image1 = response.getString("img1");//////////////////////
                    Log.i("server get image1", image1);////////////////////////


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // check error
                Log.i("custom error", error.getMessage());//////////////////////////
            }
        });
        requestQueue.add(customRequest);
    }


    // request customer api
    private void requestCustomer2(String art){

        String customUrl = "http://yuzhu.us-east-2.elasticbeanstalk.com/img_rst?search=" + art;
        JsonObjectRequest customRequest2 = new JsonObjectRequest(Request.Method.GET, customUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("custom json", response.toString());
                try {

                    Glide.with(myView).load(response.getString("img1")).into(art2_img1);
                    Glide.with(myView).load(response.getString("img2")).into(art2_img2);
                    Glide.with(myView).load(response.getString("img3")).into(art2_img3);
                    Glide.with(myView).load(response.getString("img4")).into(art2_img4);
                    Glide.with(myView).load(response.getString("img5")).into(art2_img5);
                    Glide.with(myView).load(response.getString("img6")).into(art2_img6);
                    Glide.with(myView).load(response.getString("img7")).into(art2_img7);
                    Glide.with(myView).load(response.getString("img8")).into(art2_img8);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // check error
                Log.i("custom error", error.getMessage());//////////////////////////
            }
        });
        requestQueue.add(customRequest2);
    }

}
