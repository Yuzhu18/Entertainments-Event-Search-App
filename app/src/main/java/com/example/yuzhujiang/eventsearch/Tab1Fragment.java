package com.example.yuzhujiang.eventsearch;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
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

import org.json.JSONException;
import org.json.JSONObject;

public class Tab1Fragment extends Fragment {

    private static final String TAG = "detail1Fragment";

    private String eventId;
    private TextView team;
    private TextView venue;
    private TextView time;
    private TextView category;
    private TextView price;
    private TextView status;
    private TextView buyUrl;
    private TextView seatUrl;

    private TextView teamT;
    private TextView venueT;
    private TextView timeT;
    private TextView categoryT;
    private TextView priceT;
    private TextView statusT;
    private TextView buyUrlT;
    private TextView seatUrlT;

    private String detail_buyUrl;
    private String detail_seatUrl;


    //http request
    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.detail_tab1, container, false);

        //http request
        // request queue
        requestQueue = Volley.newRequestQueue(getActivity());

        //test intent get data
        getItemIntent();

        setUpAllView(view);


        return view;

    }

    //set up all views
    private void setUpAllView(View view){

        team = view.findViewById(R.id.detail_team);
        venue = view.findViewById(R.id.detail_venue);
        time = view.findViewById(R.id.detail_time);
        category = view.findViewById(R.id.detail_category);
        price = view.findViewById(R.id.detail_price);
        status = view.findViewById(R.id.detail_status);
        buyUrl = view.findViewById(R.id.detail_buyUrl);
        seatUrl = view.findViewById(R.id.detail_seatUrl);


        teamT = view.findViewById(R.id.content_team);
        venueT = view.findViewById(R.id.content_venue);
        timeT = view.findViewById(R.id.content_time);
        categoryT = view.findViewById(R.id.content_category);
        priceT = view.findViewById(R.id.content_price);
        statusT = view.findViewById(R.id.content_status);
        buyUrlT = view.findViewById(R.id.content_buyUrl);
        seatUrlT = view.findViewById(R.id.content_seatUrl);
    }



    //test if intent get data
    private void getItemIntent(){

        Log.d(TAG,"get item intent");/////////////////////////////

        if(getActivity().getIntent().hasExtra("eventID")){

            String event_id = getActivity().getIntent().getStringExtra("eventID");
            eventId = event_id;
            Log.i("receive id", event_id);///////////////////////////////

            // http request
            requestDetail();

        }
    }

    private void requestDetail(){

        String eventUrl = "http://yuzhu.us-east-2.elasticbeanstalk.com/event_detail?eventId=" + eventId;

        JsonObjectRequest eventRequest = new JsonObjectRequest(Request.Method.GET, eventUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("detail receive json", response.toString());
                try {

                    String detail_team = response.getString("detail_team");
                    String detail_venue = response.getString("detail_venue");
                    String detail_time = response.getString("detail_time");
                    String detail_category = response.getString("detail_category");
                    String detail_price = response.getString("detail_price");
                    String detail_status = response.getString("detail_status");
                    detail_seatUrl = response.getString("detail_seatUrl");
                    detail_buyUrl = response.getString("detail_buyUrl");

                    if(detail_team.equals("")){
                        team.setVisibility(View.GONE);
                        teamT.setVisibility(View.GONE);
                    }else{
                        team.setText("Artist/Team(s)" );
                        teamT.setText(detail_team);
                    }

                    if(detail_venue.equals("")){
                        venue.setVisibility(View.GONE);
                        venueT.setVisibility(View.GONE);
                    }else{
                        venue.setText("Venue               " );
                        venueT.setText(detail_venue);
                    }

                    if(detail_time.equals("")){
                        time.setVisibility(View.GONE);
                        timeT.setVisibility(View.GONE);
                    }else {
                        time.setText("Time                 " );
                        timeT.setText(detail_time);
                    }

                    if(detail_category.equals("")){
                        category.setVisibility(View.GONE);
                        categoryT.setVisibility(View.GONE);
                    }else {
                        category.setText("Category          " );
                        categoryT.setText(detail_category);
                    }

                    if(detail_price.equals("")){
                        price.setVisibility(View.GONE);
                        priceT.setVisibility(View.GONE);
                    }else {
                        price.setText("Price Range    " );
                        priceT.setText(detail_price);
                    }

                    if(detail_status.equals("")){
                        status.setVisibility(View.GONE);
                        statusT.setVisibility(View.GONE);
                    }else {
                        status.setText("Ticket Status   "  );
                        statusT.setText(detail_status);

                    }

                    if(detail_buyUrl.equals("")){
                        buyUrl.setVisibility(View.GONE);
                        buyUrlT.setVisibility(View.GONE);
                    }else{
                        buyUrl.setText("Buy Tickets At " );

                        String ticketLink = "<a href='" + detail_buyUrl + "' target='_blank'>Ticketmaster</a >";


                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                                    buyUrlT.setText(Html.fromHtml("<a href='" + detail_buyUrl + "' target='_blank'>Ticketmaster</a >", Html.FROM_HTML_MODE_COMPACT));
                                }else{
                                    buyUrlT.setText("<a href='" + detail_buyUrl + "' target='_blank'>Ticketmaster</a >");
                                }

                        buyUrlT.setMovementMethod(LinkMovementMethod.getInstance());

//                        buyUrlT.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(Intent.ACTION_VIEW);
//                                intent.setData(Uri.parse(detail_buyUrl));
//                                startActivity(intent);
//                            }
//                        });

                    }

                    if(detail_seatUrl.equals("")){
                        seatUrl.setVisibility(View.GONE);
                        seatUrlT.setVisibility(View.GONE);
                    }else {
                        seatUrl.setText("Seat Map          " );
                        seatUrlT.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intentS = new Intent(Intent.ACTION_VIEW);
                                intentS.setData(Uri.parse(detail_seatUrl));
                                startActivity(intentS);
                            }
                        });
                    }

                    Log.i("detail_buyUrl", detail_buyUrl );///////////////////////////
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // check error
                Log.i("event error", error.getMessage());///////////////
            }

        });
        requestQueue.add(eventRequest);
    }

}
