package com.example.yuzhujiang.eventsearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchRstAdapter extends RecyclerView.Adapter<SearchRstAdapter.MyViewHolder> {

    private Context myContext;
    private List<FirstRec> myData;
    private RequestOptions option;

    private View view;

    public SearchRstAdapter(Context myContext, List<FirstRec> myData) {

        this.myContext = myContext;
        this.myData = myData;

        // request option for Glide
        option = new RequestOptions().centerCrop().placeholder(R.drawable.rst_img).error(R.drawable.rst_img);
    }

    @NonNull
    @Override
    public SearchRstAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(myContext);
        view = inflater.inflate(R.layout.rst_item_row, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRstAdapter.MyViewHolder mHolder, int i) {

        // onclick item
        final FirstRec myItem = myData.get(i);

        //favorite
        String favId = myItem.getId();

        mHolder.rs_name.setText(myItem.getName());
        mHolder.rs_venue.setText(myItem.getVenue());
        mHolder.rs_time.setText(myItem.getTime());

        // if it is already in the fav
        SharedPreferences sharedPreferences = myContext.getSharedPreferences("favItems", Context.MODE_PRIVATE);

        if(sharedPreferences.contains(myItem.getId())){
            mHolder.rs_fav.setBackgroundResource(R.drawable.heart_fill_red);
        }else{
            mHolder.rs_fav.setBackgroundResource(R.drawable.heart_outline_black);
        }

        // load image from internet
        Glide.with(myContext).load(myItem.getImgUrl()).apply(option).into(mHolder.rs_img);


        mHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // jump to next tab activity
                Intent intent = new Intent(myContext, DetailFour.class);
                intent.putExtra("detailName", myItem.getName());
                intent.putExtra("eventID", myItem.getId());
                intent.putExtra("eventVenue", myItem.getVenue());
                intent.putExtra("venueLat",myItem.getLat().toString());
                intent.putExtra("venueLng", myItem.getLng().toString());
                intent.putExtra("eventCategory", myItem.getCategory());

                ArrayList<String> arts = myItem.getArtList();
                String[] art_send = new String[arts.size()];
                for(int i = 0; i < arts.size(); i++){
                    art_send[i] = arts.get(i);
                }
                intent.putExtra("artList",art_send);

                myContext.startActivity(intent);

            }
        });


        // click the favorite button
        mHolder.rs_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // load sharedPreference
                SharedPreferences sharedPreferences = myContext.getSharedPreferences("favItems", Context.MODE_PRIVATE);

                if(sharedPreferences.contains(favId)){
                    //already in the favorite
                    //toast
                    Toast.makeText(myContext,   myItem.getName() + "was removed from favorites", Toast.LENGTH_LONG).show();
                    mHolder.rs_fav.setBackgroundResource(R.drawable.heart_outline_black);
                    sharedPreferences.edit().remove(favId).commit();
                }else{
                    // not in the favorite
                    //toast
                    Toast.makeText(myContext,   myItem.getName() + "was added to favorites", Toast.LENGTH_LONG).show();
                    mHolder.rs_fav.setBackgroundResource(R.drawable.heart_fill_red);
                    sharedPreferences.edit().putString(favId, favId).apply();

                    Log.i("after added size", Integer.toString(sharedPreferences.getAll().size()));///////////////

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return myData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView rs_name;
        TextView rs_venue;
        TextView rs_time;
        ImageView rs_img;
        Button rs_fav;

        // onclick item
        LinearLayout linearLayout;


        public MyViewHolder(View itemView){
            super(itemView);

            rs_name = itemView.findViewById(R.id.rst_name);
            rs_time = itemView.findViewById(R.id.rst_time);
            rs_venue = itemView.findViewById(R.id.rst_venue);
            rs_img = itemView.findViewById(R.id.rst_img);

            rs_fav = itemView.findViewById(R.id.rightFav);

            //onclick item
            linearLayout = (LinearLayout)itemView.findViewById(R.id.row_item);

        }
    }
}
