package com.example.yuzhujiang.eventsearch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class Tab3Fragment extends Fragment  implements OnMapReadyCallback{
    private  static  final  String TAG = "detail fragment 3";

    private String event_venue;
    View mView;

    //http request
    private RequestQueue requestQueue;
    private TextView address;
    private TextView city;
    private TextView phone;
    private TextView hour;
    private TextView general;
    private TextView child;

    private TextView addressV;
    private TextView cityV;
    private TextView phoneV;
    private TextView hourV;
    private TextView generalV;
    private TextView childV;

    //google map
    GoogleMap mGoogleMap;
    MapView mapView;

    //lat + lng
    Double venue_lat;
    Double venue_lng;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.detail_tab3, container, false);

        address = mView.findViewById(R.id.venue_address);
        city = mView.findViewById(R.id.venue_city);
        phone = mView.findViewById(R.id.venue_phone);
        hour = mView.findViewById(R.id.venue_hour);
        general = mView.findViewById(R.id.venue_general);
        child = mView.findViewById(R.id.venue_child);

        addressV = mView.findViewById(R.id.v_address);
        cityV = mView.findViewById(R.id.v_city);
        phoneV = mView.findViewById(R.id.v_phone);
        hourV = mView.findViewById(R.id.v_hour);
        generalV = mView.findViewById(R.id.v_general);
        childV = mView.findViewById(R.id.v_child);

        // request queue
        requestQueue = Volley.newRequestQueue(getActivity());

        //get lat + lng
        if(getActivity().getIntent().hasExtra("venueLat")){
            String lat = getActivity().getIntent().getStringExtra("venueLat");
            venue_lat = Double.parseDouble(lat);
        }

        if(getActivity().getIntent().hasExtra("venueLng")){
            String lng = getActivity().getIntent().getStringExtra("venueLng");
            venue_lng = Double.parseDouble(lng);
        }

        //eventVenue
        if(getActivity().getIntent().hasExtra("eventVenue")){
            event_venue = getActivity().getIntent().getStringExtra("eventVenue");
            requestVenue();
        }



        return  mView;
    }


    //google map
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) mView.findViewById(R.id.map);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    private void requestVenue(){
        String eventVenueUrl = "http://yuzhu.us-east-2.elasticbeanstalk.com/venue_rst?venue=" + event_venue;

        JsonObjectRequest eventVenueRequest = new JsonObjectRequest(Request.Method.GET, eventVenueUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("event venue tab json", response.toString());////////////////////////////
                try {



                    String mapKey = "AIzaSyCcJrXAPNUALteCt3ORwXwGGDjCU8m1ma0";

                    String venue_address = response.getString("venue_address");
                    String venue_city = response.getString("venue_city");
                    String venue_phone = response.getString("venue_phone");
                    String venue_hour = response.getString("venue_hour");
                    String venue_general = response.getString("venue_general");
                    String venue_child = response.getString("venue_child");

                    if(venue_address.equals("")){
                        address.setVisibility(View.GONE);
                        addressV.setVisibility(View.GONE);
                    }else{
                        address.setText("Address           ");
                        addressV.setText(venue_address);
                    }

                    if(venue_city.equals("")){
                        city.setVisibility(View.GONE);
                        cityV.setVisibility(View.GONE);
                    }else{
                        city.setText("City                   ");
                        cityV.setText(venue_city);
                    }

                    if(venue_phone.equals("")){
                        phone.setVisibility(View.GONE);
                        phoneV.setVisibility(View.GONE);
                    }else {
                        phone.setText("Phone Number");
                        phoneV.setText(venue_phone);
                    }

                    if(venue_hour.equals("")){
                        hour.setVisibility(View.GONE);
                        hourV.setVisibility(View.GONE);
                    }else {
                        hour.setText("Open Hours     ");
                        hourV.setText(venue_hour);
                    }

                    if(venue_general.equals("")){
                        general.setVisibility(View.GONE);
                        generalV.setVisibility(View.GONE);
                    }else {
                        general.setText("General Rule   ");
                        generalV.setText(venue_general);
                    }

                    if(venue_child.equals("")){
                        child.setVisibility(View.GONE);
                        childV.setVisibility(View.GONE);
                    }else {
                        child.setText("Child Rules    ");
                        childV.setText(venue_child);
                    }

                    Log.i("venue lat", Double.toString(venue_lat));////////////////////////////////////////////////
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // check error
                Log.i("venue error", error.getMessage());
            }
        });
        requestQueue.add(eventVenueRequest);
    }


    // google map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.addMarker(new MarkerOptions().position(new LatLng(venue_lat, venue_lng)));

        CameraPosition pos = CameraPosition.builder().target(new LatLng(venue_lat, venue_lng)).zoom(15).bearing(0).build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
    }
}
