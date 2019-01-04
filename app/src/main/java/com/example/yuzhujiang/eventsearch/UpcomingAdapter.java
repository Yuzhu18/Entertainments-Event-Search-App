package com.example.yuzhujiang.eventsearch;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UpcomingAdapter  extends RecyclerView.Adapter<UpcomingAdapter.MyViewHolder>{

    private Context mContext;
    private List<UpcomingEvent> UpcomingList;


    public UpcomingAdapter(Context mContext, List<UpcomingEvent> UpcomingList) {

        this.mContext = mContext;
        this.UpcomingList = UpcomingList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.upcoming_item_row, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {


        myViewHolder.up_artist.setText(UpcomingList.get(i).getArtist());
        myViewHolder.up_time.setText(UpcomingList.get(i).getTime());
        myViewHolder.up_type.setText(UpcomingList.get(i).getType());

        setUrl(myViewHolder, i);

    }

    private void setUrl(MyViewHolder myViewHolder, int i){

        //set url
        String getUrl = UpcomingList.get(i).getUri();
        String getDis = UpcomingList.get(i).getDisplay();
        String fUrl = "<a href='" + getUrl + "' target='_blank'>"+ getDis +"</a >";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            myViewHolder.up_display.setText(Html.fromHtml("<a href='" + getUrl + "' target='_blank'>"+ getDis +"</a >", Html.FROM_HTML_MODE_COMPACT));
        }else{
            myViewHolder.up_display.setText("<a href='" + getUrl + "' target='_blank'>"+ getDis +"</a >");
        }

        myViewHolder.up_display.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    public int getItemCount() {
        return UpcomingList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView up_display;
        TextView up_artist;
        TextView up_time;
        TextView up_type;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            up_display = itemView.findViewById(R.id.upcoming_display);
            up_artist = itemView.findViewById(R.id.upcoming_artist);
            up_time = itemView.findViewById(R.id.upcoming_time);
            up_type = itemView.findViewById(R.id.upcoming_type);
        }
    }
}
