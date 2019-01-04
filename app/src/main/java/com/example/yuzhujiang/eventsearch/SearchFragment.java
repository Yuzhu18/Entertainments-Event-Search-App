package com.example.yuzhujiang.eventsearch;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;


public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";

    // input areas
    private EditText inputKeyword;
    private EditText inputDistance;
    private EditText inputLocation;

    // warn texts
    private TextView warnKeyword;
    private TextView warnLocation;
    private View view;


    // radio
//    private RadioGroup radioGroup;
    private RadioButton curRadio;
    private RadioButton othRadio;
    // spinner
    private Spinner spinnerC;
    private ArrayAdapter<CharSequence> adapterC;
    private Spinner spinnerM;
    private ArrayAdapter<CharSequence> adapterM;

    // search button + clear button
    private Button searchButton;
    private Button clearButton;

    // search data
    private String keyword_value, category_value, miles_value;
    private String othLoc_value = "";
    private String distance_value;
    private double curLat;
    private double curLng;
    private String input_lat;
    private String input_lng;
    private String param ;


    //http request
    private RequestQueue requestQueue;

    //location permission
    LocationManager locationManager;
    LocationListener locationListener;


    //autocomplete
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;



    private void makeApiCall(String text) {
        ApiCall.make(this.getContext(), text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONObject items = responseObject.getJSONObject("_embedded");
                    JSONArray array = items.getJSONArray("attractions");

//                    JSONArray array = responseObject.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        stringList.add(row.getString("name"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            if(ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.search_fragment, container, false);

        //spinner categoty
        spinnerC = view.findViewById(R.id.input_category);
        adapterC = ArrayAdapter.createFromResource(getContext(), R.array.categ, android.R.layout.simple_spinner_item);
        adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerC.setAdapter(adapterC);

        //spinner miles
        spinnerM = view.findViewById(R.id.input_mile);
        adapterM = ArrayAdapter.createFromResource(getContext(), R.array.mile, android.R.layout.simple_spinner_item);
        adapterM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerM.setAdapter(adapterM);

        // cur + oth radios
        curRadio = view.findViewById(R.id.current_id);
        curRadio.setChecked(true);
        othRadio = view.findViewById(R.id.other_id);


        // two warn messsage
        warnKeyword = (TextView) view.findViewById(R.id.warn_keyword);
        warnKeyword.setVisibility(View.GONE);

        warnLocation = (TextView) view.findViewById(R.id.warn_inputLoc);
        warnLocation.setVisibility(View.GONE);
        //initalize hide two warn message


        //three input areas
        inputKeyword = view.findViewById(R.id.auto_complete_edit_text);
        inputDistance = view.findViewById(R.id.input_distance);
        inputLocation = view.findViewById(R.id.input_loc);

        // initalize inputLocation disabled
        inputLocation.setEnabled(false);

        // request queue
        requestQueue = Volley.newRequestQueue(getActivity());

        //autocomplete
        final AppCompatAutoCompleteTextView autoCompleteTextView =
                view.findViewById(R.id.auto_complete_edit_text);


        //Setting up the adapter for AutoSuggest
        autoSuggestAdapter = new AutoSuggestAdapter(this.getContext(),
                android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(autoSuggestAdapter);


        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        makeApiCall(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });


        curRadio.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // if currentRadio checked hide warn location
                warnLocation.setVisibility(View.GONE);
                // disabled input location area
                inputLocation.setEnabled(false);
                inputLocation.setText("");
                Log.i("radioMsg", "current location");/////////////////////////////////
            }
        });

        othRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputLocation.setEnabled(true);

            }
        });


        // get location permission
        locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                curLat = location.getLatitude();
                curLng = location.getLongitude();

                Log.i("current lat: ", Double.toString(curLat));///////////////////////////////////
                Log.i("current lng: ", Double.toString(curLng));//////////////////////////////////

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };


        // if does not have permission
        if(ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // ask permission
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }


        // search + clear button
        searchButton = view.findViewById(R.id.searchBtn);
        clearButton = view.findViewById(R.id.clearBtn);


        //when search button was clicked
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                requestInputLoc();

                boolean valid = true;

                // check keyword
                keyword_value = inputKeyword.getText().toString();

                if(keyword_value.matches("")){
                    valid = false;
                    warnKeyword.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(),"Please fix all fields with errors", Toast.LENGTH_LONG).show();
                }

                // other location
                if(othRadio.isChecked()){
                    othLoc_value = inputLocation.getText().toString();
                    if(othLoc_value.matches("")){
                        valid = false;
                        warnLocation.setVisibility(View.VISIBLE);

                        Toast.makeText(getActivity(),"Please fix all fields with errors", Toast.LENGTH_LONG).show();
                    }
                }

                // get distance value
                if(inputDistance.getText().length() == 0){
                    distance_value = "10";
                }else{
                    distance_value = inputDistance.getText().toString();
                }

                //get category value
                category_value = spinnerC.getSelectedItem().toString();


                //get miles value
                miles_value = "miles";



                // request for JSON
                if(valid){

                    Log.i("search_keyword", keyword_value);///////////////////////////////////////////////////
                    Log.i("search_category", category_value);///////////////////////////////////////////////////
                    Log.i("search_distance", distance_value);///////////////////////////////////////////////////
                    Log.i("search_miles", miles_value);///////////////////////////////////////////////////

                    if(othRadio.isChecked()){


                        requestInputLoc();


                    }else{

                        requestSearch();
                    }


                }
            }
        });




        //when clear button is clicked
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warnKeyword.setVisibility(View.GONE);
                warnLocation.setVisibility(View.GONE);

                othRadio.setChecked(false);
                inputLocation.setEnabled(false);
                curRadio.setChecked(true);

                inputKeyword.setText("");
                inputLocation.setText("");
                inputDistance.setText("");

                spinnerC.setSelection(0);
                spinnerM.setSelection(0);
            }
        });


        return view;
    }


    private void requestSearch(){

        param = "";

        if(othRadio.isChecked()){


            param = "keyword=" + keyword_value + '&' +
                    "category=" + category_value + '&' +
                    "distance=" + distance_value + '&' +
                    "locLat=" + input_lat + '&' +
                    "locLng=" + input_lng + '&' +
                    "unit=" + miles_value;


        }else{

            param = "keyword=" + keyword_value + '&' +
                    "category=" + category_value + '&' +
                    "distance=" + distance_value + '&' +
                    "locLat=" + Double.toString(curLat) + '&' +
                    "locLng=" + Double.toString(curLng) + '&' +
                    "unit=" + miles_value;
        }


        Log.i("param is", param);////////////////////////////////////////


        // ticket master search

        String ticketUrl = "http://yuzhu.us-east-2.elasticbeanstalk.com/search_rst?" + param;

        JsonArrayRequest ticketRequest = new JsonArrayRequest(Request.Method.GET, ticketUrl, null, new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) {

                // jump to next search results page, and send table data to that
                sendSearchRst(response);

                Log.i("ticketRequest json", response.toString());/////////////////////////////////////////
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // check error
                Log.i("ticket error", error.getMessage());
            }

        });
        requestQueue.add(ticketRequest);

    }



    private void requestInputLoc(){

        //  input location
        othLoc_value = inputLocation.getText().toString();

        String inputLocUrl = "http://yuzhu.us-east-2.elasticbeanstalk.com/input_loc?inputLoc=" + othLoc_value;

        Log.i("search_otherLoc", othLoc_value);///////////////////////////////////////////////////

        JsonObjectRequest othLocRequest = new JsonObjectRequest(Request.Method.GET, inputLocUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("input location json", response.toString());
                try {

                    input_lat = response.getString("lat");
                    input_lng = response.getString("lng");

                    requestSearch();

                    Log.i("lat come from server", input_lat);//////////////////////////
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // check error
                Log.i("location error", error.getMessage());
            }

        });

        requestQueue.add(othLocRequest);


    }

    private void sendSearchRst(JSONArray res){

        Intent intent = new Intent(getActivity(), searchRst.class);
        intent.putExtra("searchList", res.toString());
        startActivity(intent);

    }
}
