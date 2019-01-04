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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;

public class FavViewAdapter extends RecyclerView.Adapter<FavViewAdapter.MyViewHolder>{

    private Context myContext;
    private List<FavItem> myFavData;
    private View myView;
    private RequestOptions opt;


    public FavViewAdapter(Context myContext, List<FavItem> myFavData) {

        this.myContext = myContext;
        this.myFavData = myFavData;

        // request option for Glide
        opt = new RequestOptions().centerCrop().placeholder(R.drawable.rst_img).error(R.drawable.rst_img);


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(myContext);
        myView = inflater.inflate(R.layout.fav_item_row ,viewGroup, false);

        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {


        // onclick item
        final FavItem myItem = myFavData.get(i);

        // set Text
        setText( myViewHolder,  myItem);


        // click the favorite button
        myViewHolder.fa_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // load sharedPreference
                SharedPreferences sharedPreferences = myContext.getSharedPreferences("favItems", Context.MODE_PRIVATE);


                if(sharedPreferences.contains(myItem.getId())){
                    //already in the favorite

                    //toast
                    Toast.makeText(myContext,   myItem.getName() + "was removed from favorites", Toast.LENGTH_LONG).show();
                    myViewHolder.fa_fav.setBackgroundResource(R.drawable.heart_outline_black);
                    sharedPreferences.edit().remove(myItem.getId()).commit();

                    //remove item
                    myFavData.remove(myItem);
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i,myFavData.size());


                }else{
                    // not in the favorite

                    // toast
                    Toast.makeText(myContext, myItem.getName() + "was added to favorites", Toast.LENGTH_LONG).show();

                    myViewHolder.fa_fav.setBackgroundResource(R.drawable.heart_fill_red);
                    sharedPreferences.edit().putString(myItem.getId(), myItem.getId()).apply();

                    Log.i("after added size", Integer.toString(sharedPreferences.getAll().size()));///////////////////////
                }

            }
        });


        jumpToNextActivity(myViewHolder, myItem);

    }

    private void setText(MyViewHolder myViewHolder,FavItem myItem){

        myViewHolder.fa_name.setText(myItem.getName());
        myViewHolder.fa_venue.setText(myItem.getVenue());
        myViewHolder.fa_time.setText(myItem.getTime());

        // load image from internet
        Glide.with(myContext).load(myItem.getImgUrl()).apply(opt).into(myViewHolder.fa_img);

        myViewHolder.fa_fav.setBackgroundResource(R.drawable.heart_fill_red);

    }




    private void jumpToNextActivity(MyViewHolder myViewHolder, FavItem myItem){

        // click to jump to four tab activity
        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // jump to next tab activity
                Intent intent = new Intent(myContext, DetailFour.class);
                intent.putExtra("detailName", myItem.getName());
                intent.putExtra("eventID", myItem.getId());
                intent.putExtra("eventVenue", myItem.getVenue());
                intent.putExtra("venueLat",myItem.getLat());
                intent.putExtra("venueLng", myItem.getLng());
                intent.putExtra("eventCategory", myItem.getCategory());
                intent.putExtra("isFav", "isFav");

                ArrayList<String> arts = myItem.getArtList();
                String[] art_send = new String[arts.size()];

                for(int i = 0; i < arts.size(); i++){
                    art_send[i] = arts.get(i);
                }
                intent.putExtra("artList",art_send);

                myContext.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return myFavData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView fa_name;
        TextView fa_venue;
        TextView fa_time;
        ImageView fa_img;
        ImageView fa_fav;
        // onclick item
        LinearLayout linearLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            fa_name = itemView.findViewById(R.id.fav_name);
            fa_time = itemView.findViewById(R.id.fav_time);
            fa_venue = itemView.findViewById(R.id.fav_venue);
            fa_img = itemView.findViewById(R.id.fav_img);
            fa_fav = itemView.findViewById(R.id.fav_fav);

            //onclick item
            linearLayout = (LinearLayout)itemView.findViewById(R.id.favLinear);
        }
    }


}
