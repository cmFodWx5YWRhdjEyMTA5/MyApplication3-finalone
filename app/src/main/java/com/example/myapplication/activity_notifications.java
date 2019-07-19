package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import com.example.myapplication.helpClasses.alerts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class activity_notifications extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private static int cleanningAlertsStatus = 0;

    RecyclerView recyclerViewNotification;
    notificationAdpter notificationAdpter;
    ArrayList<alerts> listItems;
    ArrayList<alerts> listItemsTemp;

    alerts Alerts;
    Toolbar toolbar;
    User user;

    String webUserImg = "";
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;


    private TinyDB tinyDB;
    private String Uid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        tinyDB = new TinyDB(this);
        Uid = tinyDB.getString("UID");

        user = new User();

        toolbar = findViewById(R.id.toolbarNotification);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("NOTIFICATIONS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerViewNotification = findViewById(R.id.recyclerViewNotification);
        listItems = new ArrayList<>();
        listItemsTemp = new ArrayList<>();
        Alerts = new alerts();

        progressDialog = new ProgressDialog(this);


        loadMyMessages();


    }


    public void loadMyMessages() {

        progressDialog.setMessage("Please wait..Your Messages are Loading.");
        progressDialog.setCancelable(false);
        progressDialog.show();


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("mobile/alert/" + Uid);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listItems.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Alerts = ds.getValue(alerts.class);

                    if (Alerts.getActiveStatus() == 0) {
                        listItems.add(Alerts);


                        if (cleanningAlertsStatus == 0) {
                            listItemsTemp.add(Alerts);
                        }


                    }


                }
                notificationAdpter = new notificationAdpter(activity_notifications.this, listItems);
                recyclerViewNotification.setAdapter(notificationAdpter);
                recyclerViewNotification.setLayoutManager(new LinearLayoutManager(activity_notifications.this));
                progressDialog.dismiss();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        getMenuInflater().inflate(R.menu.common_items, menu);
//        MenuItem mSearch = menu.findItem(R.id.action_search);
//        MenuItem clearAll = menu.findItem(R.id.clelarAll);
//        SearchView mSearchView = (SearchView) mSearch.getActionView();
//        mSearchView.setQueryHint("Search");


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.common_items, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem clelarAll = menu.findItem(R.id.clelarAll);
        SearchView searchView = (SearchView) searchItem.getActionView();

        Button clear = (Button) clelarAll.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                notificationAdpter.getFilter().filter(newText);
                return false;

            }
        });


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.clelarAll) {


            if (listItemsTemp.size() == 0) {
                Toast.makeText(activity_notifications.this, "Their no Alerts to clear", Toast.LENGTH_SHORT).show();
            } else {


                new AlertDialog.Builder(activity_notifications.this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure to Clean Previous Alerts?")
                        .setIcon(R.drawable.sherlock_logo)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                cleanningAlertsStatus = 1;
                                progressDialog.setMessage("Please wait..");
                                progressDialog.show();

                                databaseReference = FirebaseDatabase.getInstance().getReference("mobile/alert/" + Uid);

                                for (int i = 0; i < listItemsTemp.size(); i++) {

                                    try {
                                        String firebaseTokenSelectedAlert = listItemsTemp.get(i).getToken();

                                        databaseReference.child(firebaseTokenSelectedAlert).child("activeStatus").setValue(1);


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }

                                progressDialog.dismiss();
                                cleanningAlertsStatus = 0;
                                listItemsTemp.clear();
                                Toast.makeText(activity_notifications.this, "Successfully cleared", Toast.LENGTH_SHORT).show();


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

    private String getWebUserImg() {

        FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference1 = firebaseDatabase1.getInstance().getReference("WebData").child("Users").child(Alerts.getSender_uid());

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds1 : dataSnapshot.getChildren()) {
                    webUserImg = (ds1.child("profile_pic_url").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return webUserImg;

    }


}
