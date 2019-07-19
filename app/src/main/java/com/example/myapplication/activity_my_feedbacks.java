package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.helpClasses.userInquiry;
import com.example.myapplication.helpClasses.myInquiry;
import com.example.myapplication.helpClasses.myFeedbackAdpter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class activity_my_feedbacks extends AppCompatActivity {

    private Toolbar toolbarFeedback;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private ArrayList<userInquiry> list;
    private userInquiry UserInquiry;

    private ArrayList<myInquiry> ListmyInquiry;
    private ArrayList<myInquiry> ListmyInquiryTemp;

    private static int inquiryCleanningStatus = 0;

    private myInquiry MyInquiry;

    private RecyclerView RecyclerViewFeedback;
    private myFeedbackAdpter MyFeedbackAdpter;
    private ProgressDialog progressDialog;

    private TinyDB tinyDB;
    private String Uid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_feedbacks);

        tinyDB = new TinyDB(this);
        Uid = tinyDB.getString("UID");

//        getSupportActionBar().setTitle("Feedback");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarFeedback = findViewById(R.id.toolbarFeedback);
        setSupportActionBar(toolbarFeedback);

        getSupportActionBar().setTitle("FEEDBACK");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        RecyclerViewFeedback = findViewById(R.id.recyclerViewFeedback);

        list = new ArrayList<>();
        UserInquiry = new userInquiry();

        ListmyInquiry = new ArrayList<>();
        ListmyInquiryTemp = new ArrayList<>();
        MyInquiry = new myInquiry();


        getMyFeedbck();


    }


    private void getMyFeedbck() {

        progressDialog.setMessage("Please Wait..Your Inquiries Are Loading.");
        progressDialog.setCancelable(false);
        progressDialog.show();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("mobile/userInquiry");
        Query query = databaseReference.orderByChild("user/UID").equalTo(Uid);
        //.orderByChild("user/activeStatus").equalTo(0);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                ListmyInquiry.clear();
                //RecyclerViewFeedback.removeAllViews();
                //RecyclerViewFeedback.removeAllViewsInLayout();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    MyInquiry = ds.getValue(myInquiry.class);
                    MyInquiry.setImg1(ds.child("images/img1").getValue().toString());
                    MyInquiry.setImg2(ds.child("images/img2").getValue().toString());
                    MyInquiry.setImg3(ds.child("images/img3").getValue().toString());
                    MyInquiry.setImg4(ds.child("images/img4").getValue().toString());


                    if (MyInquiry.getActiveStatus() == 0) {


                        ListmyInquiry.add(MyInquiry);

                        if (inquiryCleanningStatus == 0) {
                            ListmyInquiryTemp.add(MyInquiry);
                        }


                    }


                }

                MyFeedbackAdpter = new myFeedbackAdpter(activity_my_feedbacks.this, ListmyInquiry);
                RecyclerViewFeedback.setAdapter(MyFeedbackAdpter);
                RecyclerViewFeedback.setLayoutManager(new LinearLayoutManager(activity_my_feedbacks.this));

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.common_items, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem clelarAll = menu.findItem(R.id.clelarAll);

        SearchView searchView = (SearchView) searchItem.getActionView();


        Button clear = (Button) clelarAll.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {


                MyFeedbackAdpter.getFilter().filter(s);
                return false;
            }
        });

        return true;


//        getMenuInflater().inflate(R.menu.common_items, menu);
//        MenuItem mSearch = menu.findItem(R.id.action_search);//
//        SearchView mSearchView = (SearchView) mSearch.getActionView();
//        mSearchView.setQueryHint("Search");
//        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                //mAdapter.getFilter().filter(newText);
//                myFeedbackAdpter.getFilter().filter(newText);
//                return true;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.clelarAll) {


            if (ListmyInquiryTemp.size() == 0) {
                Toast.makeText(activity_my_feedbacks.this, "Their No Inquiry To Clear", Toast.LENGTH_SHORT).show();
            } else {


                new AlertDialog.Builder(activity_my_feedbacks.this)
                        .setCancelable(true)
                        .setTitle("Confirmation")
                        .setIcon(R.drawable.sherlock_logo)
                        .setMessage("Are You Sure To Clean Previous Inquiries?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                inquiryCleanningStatus = 1;
                                progressDialog.setMessage("Please wait..");
                                progressDialog.show();

                                android.text.format.DateFormat df = new android.text.format.DateFormat();
                                String currentDate = df.format("MMM dd yyyy", new java.util.Date()).toString();
                                String currentTime = df.format("hh:mm a", new java.util.Date()).toString();

                                databaseReference = FirebaseDatabase.getInstance().getReference("mobile/userInquiry");

                                for (int i = 0; i < ListmyInquiryTemp.size(); i++) {

                                    try {
                                        String inquiryid = ListmyInquiryTemp.get(i).getId();

                                        databaseReference.child(inquiryid).child("activeStatus").setValue(1);
                                        databaseReference.child(inquiryid).child("updatedDate").setValue(currentDate);
                                        databaseReference.child(inquiryid).child("updatedTime").setValue(currentTime);


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }


                                progressDialog.dismiss();
                                inquiryCleanningStatus = 0;
                                Toast.makeText(activity_my_feedbacks.this, "Successfully cleared", Toast.LENGTH_SHORT).show();

                            }


                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
            }

        }


        return super.onOptionsItemSelected(item);
    }
}
