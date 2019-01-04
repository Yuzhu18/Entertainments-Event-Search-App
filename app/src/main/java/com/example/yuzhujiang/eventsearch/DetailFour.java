package com.example.yuzhujiang.eventsearch;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class DetailFour extends AppCompatActivity {


    private static final String TAG = "DetailActivity";
    private DetailPageAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Toolbar DetailToolbar;
    private TabLayout tabLayout;

    private String detailName;

    private ImageView favBtn;
    private ImageView twitterBtn;

    private String venueName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_four);


        mSectionsPagerAdapter = new DetailPageAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.detailViewPager);
        DetailPageAdapter adapter = new DetailPageAdapter(getSupportFragmentManager());

        adapter.addFragment(new Tab1Fragment(), "EVENT");
        adapter.addFragment(new Tab2Fragment(), "ARTISTS");
        adapter.addFragment(new Tab3Fragment(), "VENUE");
        adapter.addFragment(new Tab4Fragment(), "UPCOMING");

        mViewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.detail_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //set tab icon
        setTabIcon();

        //set bar title and back button
        setBarTitle();


        //get detail event id
        if(this.getIntent().hasExtra("eventID")){
           String favId = this.getIntent().getStringExtra("eventID");

           if(this.getIntent().hasExtra("isFav")){
               //toolbar fav button
               favBtn = (ImageView) findViewById(R.id.tabFav);
               favBtn.setBackgroundResource(R.drawable.heart_fill_red);
           }



            //toolbar fav button
            favBtn = (ImageView) findViewById(R.id.tabFav);

            SharedPreferences sharedPreferences = getSharedPreferences("favItems", Context.MODE_PRIVATE);

            if(sharedPreferences.contains(favId)){
                favBtn.setBackgroundResource(R.drawable.heart_fill_red);
            }else{
                favBtn.setBackgroundResource(R.drawable.heart_fill_white);
            }



            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //sharedpreference
                    SharedPreferences sharedPreferences = getSharedPreferences("favItems", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    if(sharedPreferences.contains(favId)){
                        //add to fav toast
                        Toast.makeText(v.getContext(), detailName + "was removed from favorites", Toast.LENGTH_LONG).show();
                        favBtn.setBackgroundResource(R.drawable.heart_fill_white);
                        editor.remove(favId).apply();
                    }else{
                        //add to fav toast
                        Toast.makeText(v.getContext(), detailName + "was added to favorites", Toast.LENGTH_LONG).show();
                        favBtn.setBackgroundResource(R.drawable.heart_fill_red);
                        editor.putString(favId, favId).apply();
                    }

                }
            });
        }

        if(this.getIntent().hasExtra("eventVenue")){
           venueName = this.getIntent().getStringExtra("eventVenue");
        }

        //twitter button
        twitterBtn = (ImageView) findViewById(R.id.barTwitter);
        twitterBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Uri twitter = Uri.parse("https://twitter.com/intent/tweet?text=Check out "+ detailName +" located at " + venueName + ". Website: " + "%23CSCI571EventSearch");
                Intent goTwitter = new Intent(Intent.ACTION_VIEW, twitter);
                startActivity(goTwitter);

            }
        });

    }

    private void setTabIcon(){
        tabLayout.getTabAt(0).setIcon(R.drawable.info_outline);
        tabLayout.getTabAt(1).setIcon(R.drawable.artist);
        tabLayout.getTabAt(2).setIcon(R.drawable.venue);
        tabLayout.getTabAt(3).setIcon(R.drawable.upcoming);
    }


    private void setBarTitle(){
        //get back button
        DetailToolbar = (Toolbar) findViewById(R.id.Detail_toolbar);
        setSupportActionBar(DetailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(this.getIntent().hasExtra("detailName")){
            detailName = this.getIntent().getStringExtra("detailName");
            getSupportActionBar().setTitle(detailName);
        }
    }

}
