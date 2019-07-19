package com.example.myapplication;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.myapplication.helpClasses.alerts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class notificationAdpter extends RecyclerView.Adapter<notificationAdpter.NewsViewHolder> implements Filterable {


    Context context;
    List<alerts> mData;
    List<alerts> mDataFull;


    public notificationAdpter(Context context, List<alerts> mData) {
        this.context = context;
        this.mData = mData;

        mDataFull = new ArrayList<>(mData);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View layout;
        layout = LayoutInflater.from(context).inflate(R.layout.item_notifications, viewGroup, false);


        return new NewsViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int i) {


        //bind data
        // newsViewHolder.ImgSender.setImageResource(R.drawable.sri_lanka);

        newsViewHolder.textTitle.setText(mData.get(i).getTitle());
        newsViewHolder.textMsg.setText(mData.get(i).getContext());
        newsViewHolder.comment_date.setText(mData.get(i).getCreatedDate() + " " + mData.get(i).getCreatedTime());


        Picasso.get()
                .load(mData.get(i).getProfile_image())
                .resize(250, 250)
                .centerCrop()
                .into(newsViewHolder.ImgSender);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public Filter getFilter() {
        return filteredDataSet;
    }


    private Filter filteredDataSet = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<alerts> fileredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {

                fileredList.addAll(mDataFull);
            } else {

                String filterPattern = constraint.toString().toLowerCase().trim();


                for (alerts item : mDataFull) {

                    if (item.getTitle().toLowerCase().contains(filterPattern) || item.getContext().toLowerCase().contains(filterPattern) || item.getCreatedDate().toLowerCase().contains(filterPattern) || item.getCreatedTime().toLowerCase().contains(filterPattern)) {
                        fileredList.add(item);
                    }


                }


            }

            FilterResults results = new FilterResults();
            results.values = fileredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mData.clear();
            mData.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public class NewsViewHolder extends RecyclerView.ViewHolder {


        TextView textTitle, textMsg, comment_date;
        ImageView ImgSender;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.textTitle);
            textMsg = itemView.findViewById(R.id.textMsg);
            ImgSender = itemView.findViewById(R.id.ImgSender);
            comment_date = itemView.findViewById(R.id.comment_date);
        }


    }


}
