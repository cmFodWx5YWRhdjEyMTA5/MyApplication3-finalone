package com.example.myapplication.helpClasses;


import android.app.Dialog;
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
import android.widget.Toast;

import com.example.myapplication.R;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;


import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;


public class myFeedbackAdpter extends RecyclerView.Adapter<myFeedbackAdpter.NewsViewHolder> implements Filterable {


    Context context;
    List<myInquiry> mData;
    List<myInquiry> mDataFull;
    Dialog dialog;

    public myFeedbackAdpter(Context context, List<myInquiry> mData) {

        this.context = null;
        this.mData = null;

        this.context = context;
        this.mData = mData;
        mDataFull = new ArrayList<>(mData);

        dialog = new Dialog(context);
    }

    @NonNull
    @Override
    public myFeedbackAdpter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View layout;
        layout = LayoutInflater.from(context).inflate(R.layout.item_feedback, viewGroup, false);

        final myFeedbackAdpter.NewsViewHolder NewsViewHolder = new NewsViewHolder(layout);

        dialog.setContentView(R.layout.custom_popup_feedback);


        NewsViewHolder.btnAddAdditionalFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TextView feedback = (TextView) dialog.findViewById(R.id.editTextFeedback);
                feedback.setError(null);

                Button btnCloseAdditionalFeedback = (Button) dialog.findViewById(R.id.btnCloseAdditionalFeedback);
                Button btnSaveAdditionalFeedback = (Button) dialog.findViewById(R.id.btnSaveAdditionalFeedback);

                dialog.show();


                btnCloseAdditionalFeedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        feedback.setError(null);

                    }
                });


                btnSaveAdditionalFeedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String id = mData.get(NewsViewHolder.getAdapterPosition()).getId();


                        if (String.valueOf(feedback.getText()).isEmpty()) {

                            feedback.setError("Feedback cannot be blank");

                        } else {
                            feedback.setError(null);
                            UpdateSelectedRecord(feedback.getText().toString(), id);


                        }
                    }
                });


            }
        });


        // return new myFeedbackAdpter.NewsViewHolder(layout);
        return NewsViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull myFeedbackAdpter.NewsViewHolder newsViewHolder, int i) {

        String fullDesc;
        fullDesc = "Priority : " + mData.get(i).getPriorityType() + "\nDisaster : " + mData.get(i).getDisasterType() + "\nLocation : " + mData.get(i).getLocation() + "\nDescription : " + mData.get(i).getDesc() + "";

        String feedBack = mData.get(i).getAdditionalFeedback();

        if (!feedBack.equals("none")) {
            fullDesc = fullDesc + "\n" + "My feedback : " + mData.get(i).getAdditionalFeedback();
            newsViewHolder.btnAddAdditionalFeedback.setEnabled(false);
        } else {
            newsViewHolder.btnAddAdditionalFeedback.setEnabled(true);
        }


        newsViewHolder.textMsg.setText(fullDesc.replaceAll("\\\\n", "\n"));
        newsViewHolder.comment_date.setText((mData.get(i).getUpdatedDate() + " "+ mData.get(i).getUpdatedTime()));


        // Picasso.get().load(mData.get(i).getImg1()).into(newsViewHolder.ImgSender1);

        Picasso.get()
                .load(mData.get(i).getImg1())
                .resize(250, 250)
                .centerCrop()
                .into(newsViewHolder.ImgSender1);

        if (mData.get(i).getImg2() != "none") {
            //Picasso.get().load(mData.get(i).getImg2()).into(newsViewHolder.ImgSender2);

            Picasso.get()
                    .load(mData.get(i).getImg2())
                    .resize(250, 250)
                    .centerCrop()
                    .into(newsViewHolder.ImgSender2);
        }


        if (mData.get(i).getImg3() != "none") {


            Picasso.get()
                    .load(mData.get(i).getImg3())
                    .resize(250, 250)
                    .centerCrop()
                    .into(newsViewHolder.ImgSender3);


        }

        if (mData.get(i).getImg4() != "none") {

            Picasso.get()
                    .load(mData.get(i).getImg4())
                    .resize(250, 250)
                    .centerCrop()
                    .into(newsViewHolder.ImgSender4);

        }


        //newsViewHolder.onClick(i);


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

            List<myInquiry> fileredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {

                fileredList.addAll(mDataFull);
            } else {

                String filterPattern = constraint.toString().toLowerCase().trim();


                for (myInquiry item : mDataFull) {

                    if (item.getPriorityType().toLowerCase().contains(filterPattern) || item.getDisasterType().toLowerCase().contains(filterPattern) || item.getUpdatedDate().toLowerCase().contains(filterPattern) || item.getDesc().toLowerCase().contains(filterPattern) || item.getUpdatedTime().toLowerCase().contains(filterPattern) ) {
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
        ImageView ImgSender1, ImgSender2, ImgSender3, ImgSender4;
        Button btnAddAdditionalFeedback;


        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            textMsg = itemView.findViewById(R.id.texPiority);
            comment_date = itemView.findViewById(R.id.texDate);
            ImgSender1 = itemView.findViewById(R.id.imageViewInquiry1);
            ImgSender2 = itemView.findViewById(R.id.imageViewInquiry2);
            ImgSender3 = itemView.findViewById(R.id.imageViewInquiry3);
            ImgSender4 = itemView.findViewById(R.id.imageViewInquiry4);
            btnAddAdditionalFeedback = itemView.findViewById(R.id.btnAddAdditionalFeedback);


        }


//        public void onClick(final int i)
//        {
////            btn.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    Toast.makeText(context, i+" is clicked", Toast.LENGTH_SHORT).show();
////                }
////            });
//
//        }
    }


    private void UpdateSelectedRecord(String feedback, String userId) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("mobile/userInquiry");

        try {
            databaseReference.child(userId).child("additionalFeedback").setValue(feedback);
            //databaseReference.onDisconnect();
            Toast.makeText(context, "Successfully added", Toast.LENGTH_SHORT).show();
            dialog.dismiss();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
