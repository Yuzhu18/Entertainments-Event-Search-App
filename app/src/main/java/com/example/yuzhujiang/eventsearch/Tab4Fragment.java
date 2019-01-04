package com.example.yuzhujiang.eventsearch;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tab4Fragment extends Fragment {
    private static final String TAG = "detail fragment 4";

    private RequestQueue requestQueue;
    private String venue_name;
    private String venue_id;

    private List<UpcomingEvent> upcomingList;
    private List<UpcomingEvent> sortList;
    private RecyclerView recyclerView;
    private TextView NoRecord;

    private TextView upcomingUri;
    View mView;

    // spinner
    private Spinner spinnerItem;
    private ArrayAdapter<CharSequence> adapterItem;
    private Spinner spinnerOrder;
    private ArrayAdapter<CharSequence> adapterOrder;

    private int itemPos = 0;
    private boolean ascending = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         mView = inflater.inflate(R.layout.detail_tab4, container, false);


        init();


        handleSpinnerItem();

        // request queue
        requestQueue = Volley.newRequestQueue(getActivity());

        // get venue name
        if(getActivity().getIntent().hasExtra("eventVenue")){
            venue_name = getActivity().getIntent().getStringExtra("eventVenue");

            //test
            Log.i("tab4 coming venue name", venue_name);//////////////////////////
        }

        //request for event id
        requestUpcomingId();


        return mView;
    }



    private void init(){

        //recycler view
        recyclerView = mView.findViewById(R.id.tab4_recyclerview);
        upcomingList = new ArrayList<>();
        sortList = new ArrayList<>();

        NoRecord = mView.findViewById(R.id.NoRecord);

        //spinner item
        spinnerItem = (Spinner) mView.findViewById(R.id.sortItem);
        adapterItem = ArrayAdapter.createFromResource(getContext(), R.array.sortItem, android.R.layout.simple_spinner_item);
        adapterItem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItem.setAdapter(adapterItem);

        //spinner order
        spinnerOrder = (Spinner)  mView.findViewById(R.id.sortOrder);
        adapterOrder = ArrayAdapter.createFromResource(getContext(), R.array.sortOrder, android.R.layout.simple_spinner_item);
        adapterOrder.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrder.setAdapter(adapterOrder);

    }

    private void handleSpinnerItem(){

        spinnerOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    ascending = true;

                }else{
                    ascending = false;
                }

                determineSort(ascending, itemPos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        spinnerItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){
                    spinnerOrder.setSelection(0);
                    itemPos = 0;
                }

                if(position == 1){
                    itemPos = 1;
                }

                if(position == 2){
                    itemPos = 2;
                }

                if(position == 3){
                    itemPos = 3;
                }

                determineSort(ascending, itemPos);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void determineSort(boolean asd, int pos){
        if(asd){
            // default sort
            if(pos == 0){
                setUpRec(upcomingList);
            }

            if(pos == 1){
                sortByEventA();
                setUpRec(sortList);
            }

            if(pos == 2){
                sortByTimeA();
                setUpRec(sortList);
            }

            if(pos == 3){
                sortByArtA();
                setUpRec(sortList);
            }

            if(pos == 4){
                sortByTypeA();
                setUpRec(sortList);
            }
        }else{

            if(pos == 1){
                sortByEventD();
                setUpRec(sortList);
            }

            if(pos == 2){
                sortByTimeD();
                setUpRec(sortList);
            }

            if(pos == 3){
                sortByArtD();
                setUpRec(sortList);
            }

            if(pos == 4){
                sortByTypeD();
                setUpRec(sortList);
            }
        }
    }


    private void sortByTimeA(){
        Collections.sort(sortList, (item1, item2) ->item1.getTime().compareTo(item2.getTime()));
    }

    private void sortByTimeD(){
        Collections.sort(sortList, (item1, item2) ->item2.getTime().compareTo(item1.getTime()));
    }

    private void sortByEventA(){
        Collections.sort(sortList, (item1, item2) ->item1.getDisplay().compareTo(item2.getDisplay()));
    }

    private void sortByEventD(){
        Collections.sort(sortList, (item1, item2) ->item2.getDisplay().compareTo(item1.getDisplay()));
    }

    private void sortByArtA(){
        Collections.sort(sortList, (item1, item2) ->item1.getArtist().compareTo(item2.getArtist()));
    }

    private void sortByArtD(){
        Collections.sort(sortList, (item1, item2) ->item2.getArtist().compareTo(item1.getArtist()));
    }

    private void sortByTypeA(){
        Collections.sort(sortList, (item1, item2) ->item1.getType().compareTo(item2.getType()));
    }

    private void sortByTypeD(){
        Collections.sort(sortList, (item1, item2) ->item2.getType().compareTo(item1.getType()));
    }

    //get upcoming event id
    private void requestUpcomingId(){

        String upcomingIdUrl = "http://yuzhu.us-east-2.elasticbeanstalk.com/upcoming_id?venueName=" + venue_name;

                JsonObjectRequest upcomingId = new JsonObjectRequest(Request.Method.GET, upcomingIdUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("tab4 upcoming id json", response.toString());////////////////////////////
                        try {
                            venue_id = response.getString("venue_id");

                            Log.i("upcoming id", venue_id);//////////////////////////////////////

                            //request upcoming events array
                            requestUpcomingArray();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // check error
                        Log.i("upcoming id error", error.getMessage());
                    }
                });

                requestQueue.add(upcomingId);
    }


    // get upcoming events details
    private void requestUpcomingArray(){

        String upcomingUrl = "http://yuzhu.us-east-2.elasticbeanstalk.com/upcoming_event?upcomingId=" + venue_id;
        JsonArrayRequest upcomingEvents = new JsonArrayRequest(Request.Method.GET, upcomingUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.i("upcoming events json", Integer.toString(response.length()));//////////////////////

                JSONObject curEvent = null;

                if(response.length() == 0){
                    spinnerItem.setVisibility(View.GONE);
                    spinnerOrder.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    NoRecord.setVisibility(View.VISIBLE);
                    Log.i("no record", "no");//////////////////////
                }

                for (int j = 0; j < response.length(); j++) {
                    try {
                        curEvent = response.getJSONObject(j);
                        UpcomingEvent upEvent = new UpcomingEvent();

                        upEvent.setDisplay(curEvent.getString("upc_display"));
                        upEvent.setArtist(curEvent.getString("upc_artist"));
                        upEvent.setTime(curEvent.getString("upc_time"));
                        upEvent.setType(curEvent.getString("upc_type"));
                        upEvent.setUri(curEvent.getString("upc_uri"));

                        upcomingList.add(upEvent);
                        sortList.add(upEvent);

                        final String upcoming_uri = curEvent.getString("upc_uri");


                        //test
                        String upcoming_display = curEvent.getString("upc_display");////////////////////////////////
//                        Log.i("upcoming display", upcoming_display);////////////////////////////////

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }

                setUpRec(upcomingList);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // check error
                Log.i("upcoming events error", error.getMessage());
                spinnerItem.setVisibility(View.GONE);
                spinnerOrder.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                NoRecord.setVisibility(View.VISIBLE);
                Log.i("no record", "no");//////////////////////

            }
        });
        requestQueue.add(upcomingEvents);
    }


    private void setUpRec(List<UpcomingEvent> upList){

        UpcomingAdapter myadapter = new UpcomingAdapter(getContext(),upList);/////////////@@@@@
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));///////////@@@@@
        recyclerView.setAdapter(myadapter);

    }
}
