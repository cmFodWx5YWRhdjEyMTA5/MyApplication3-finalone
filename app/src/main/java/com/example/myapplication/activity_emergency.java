package com.example.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.myapplication.helpClasses.userInquiry;
import com.example.myapplication.helpClasses.inquiryImages;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;


public class activity_emergency extends AppCompatActivity { //implements Validator.ValidationListener   not using now


//    @NotEmpty
//    private EditText textLocation;

//    @NotEmpty
//    private EditText editTextCmt;


    EditText textLocation;
    EditText editTextCmt;

    EditText spinnerPiorityText;

    User user;
    private Date date;
    private ImageButton btnGetMyLocation;
    private Button addImgs, submit, clearBtn;
    private Uri ImgUri = null;
    private Uri VideoUri = null;


    private static int PICK_IMAGE = 1, PICK_VIDEO = 2;
    public LinearLayout gallery;
    public LayoutInflater inflater;
    public Spinner spinnerPriority;
    public Spinner spinnerTypes;
    Location getMyLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    private Geocoder geocoder;
    private List<Address> addresses;
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();
    private ArrayList<String> userUriList = new ArrayList<String>();

    private ArrayList<String> userImgList = new ArrayList<String>();

    private ProgressDialog progressDialog;
    private ProgressDialog progressDialogIN;


    private String latitude;
    private String longitude;

    private Validator validator;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    public String id;
    private VideoView selectVideo = null;
    private Button btnAddVideo;
    private MediaController controlSelectVideo;
    private static Integer videoWidth = 0;
    private TinyDB tinyDB;
    private String Uid = "";
    private InputStream inputStream = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        tinyDB = new TinyDB(this);
        Uid = tinyDB.getString("UID");

        user = new User();

        textLocation = findViewById(R.id.textLocation);
        editTextCmt = findViewById(R.id.editTextCmt);
        spinnerTypes = findViewById(R.id.SpinnerType);
        spinnerPriority = findViewById(R.id.SpinnerPiority);

        addImgs = findViewById(R.id.addImgs);
        btnGetMyLocation = findViewById(R.id.btnGetMyLocation);
        submit = findViewById(R.id.loginBtn);
        selectVideo = findViewById(R.id.selectedVideo);
        btnAddVideo = findViewById(R.id.addVideo);
        clearBtn = findViewById(R.id.clearBtn);

        progressDialog = new ProgressDialog(this);


        selectVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer m) {

                try {

                    m.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer m, int width, int height) {

                            controlSelectVideo = new MediaController(activity_emergency.this);
                            selectVideo.setMediaController(controlSelectVideo);
                            controlSelectVideo.setAnchorView(selectVideo);
                            selectVideo.requestFocus();
                            selectVideo.animate();
                        }
                    });

                    if (m.isPlaying()) {
                        m.stop();
                        m.release();
                        m = new MediaPlayer();
                    }


                    //m.setVolume(0f, 0f);
                    m.setLooping(false);
                    m.start();

                    if (m.getDuration() > 1000) {
                        m.seekTo(1000);
                    } else if (m.getDuration() <= 1000) {
                        m.seekTo(m.getDuration());
                    }
                    m.pause();


                } catch (Exception ex) {


                }


            }
        });


        gallery = findViewById(R.id.gallery);
        inflater = LayoutInflater.from(this);

        addItemsOnSpinner2();
        loadValuesToSpinners();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        geocoder = new Geocoder(this, Locale.getDefault());


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // validator.validate();
                    SubmitMyInquiry();

                } catch (Exception ex) {

                }
            }
        });


        addImgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImages();
            }
        });


        btnAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVideo();
            }
        });


        btnGetMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getMyCurrentLocation();

            }
        });


        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
            }
        });


        getSupportActionBar().setTitle("EMERGENCY");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        validator = new Validator(this);
//        validator.setValidationListener(this);


    }


    //    @Override
//    public void onValidationSucceeded()
    public void SubmitMyInquiry() {

        if (spinnerTypes.getSelectedItemId() == 0 || spinnerPriority.getSelectedItemId() == 0) {

            Toast.makeText(activity_emergency.this, "Please fill all details.", Toast.LENGTH_SHORT).show();

        } else if (textLocation.getText().length() == 0 || textLocation.getText().toString() == "") {
            textLocation.setError("Please Add Location.");
        } else if (editTextCmt.getText().length() == 0 || editTextCmt.getText().toString() == "") {
            editTextCmt.setError("Please Add Detail.");
        } else if (gallery.getChildCount() == 0) {

            Toast.makeText(activity_emergency.this, "Please Select at least one image.", Toast.LENGTH_SHORT).show();

        } else {


            spinnerTypes.getSelectedItem();
            spinnerPriority.getSelectedItem();

//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            progressDialogIN = new ProgressDialog(this);
            progressDialogIN.setTitle("Uploading");
            progressDialogIN.setMessage("Your Inquiry Data is Uploading..");
            progressDialogIN.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialogIN.setProgress(0);
            progressDialogIN.setCancelable(false);
            progressDialogIN.setIcon(R.drawable.sherlock_logo);
            progressDialogIN.show();


            int status = getMyData();
            if (status == 1) {


                StorageReference imageFolder = storage.getInstance().getReference().child("userInquiryImages");


                for (int i = 0; i < ImageList.size(); i++) {

                    Uri currentImg = ImageList.get(i);


                    final StorageReference imageName = imageFolder.child("Image" + currentImg.getLastPathSegment());
                    imageName.putFile(currentImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String url = String.valueOf(uri);
                                    postInquiry(url);
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialogIN.setProgress(currentProgress);

                        }
                    });
                }

            }


        }


    }


//    @Override
//    public void onValidationFailed(List<ValidationError> errors) {
//        for (ValidationError error : errors) {
//            View view = error.getView();
//            String message = error.getCollatedErrorMessage(this);
//            // Display error messages
//            if (view instanceof EditText) {
//                ((EditText) view).setError(message);
//            } else {
//                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
//            }
//        }
//    }


    public void chooseImages() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGE);
    }


    private void chooseVideo() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO);


    }


    public void loadValuesToSpinners() {


        List<String> List = new ArrayList<String>();
        List.add(0, "Select priority Type");
        List.add(1, "High");
        List.add(2, "Mid");
        List.add(3, "Low");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, List);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(dataAdapter);

        spinnerPriority.setFocusable(true);
        spinnerPriority.setFocusableInTouchMode(true);
        //  spinnerPriority.RequestFocus (FocusSearchDirection.Up);

        // ((TextView)spinnerPriority.SelectedView).Error = "Your Error Text";

    }


    public void addItemsOnSpinner2() {

        List<String> list = new ArrayList<String>();
        list.add(0, "Select Disaster Type");
        list.add(1, "Fire");
        list.add(1, "Floods");
        list.add(1, "Wildfires");
        list.add(1, "Earthquakes");
        list.add(1, "Drought");
        list.add(2, "Other");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypes.setAdapter(dataAdapter);
    }


    public void getMyCurrentLocation() {


        if (ContextCompat.checkSelfPermission(activity_emergency.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission has already been granted


            progressDialog.setTitle("Please Wait..");
            progressDialog.setMessage("Getting Your Location.");
            progressDialog.setIcon(R.drawable.sherlock_logo);
            progressDialog.show();


            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        try {
                            latitude = String.valueOf(location.getLatitude());
                            longitude = String.valueOf(location.getLongitude());


                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            String address = addresses.get(0).getAddressLine(0);
                            String area = addresses.get(0).getLocality();
                            String city = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            textLocation.setText(address + "," + area + "," + city + "," + country);

                            location.reset();

                            progressDialog.dismiss();

                        } catch (Exception ex) {


                        }
                    }
                }
            });


        } else {
            requestStoragePermission();
        }


    }


    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity_emergency.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {

            new AlertDialog.Builder(activity_emergency.this)
                    .setTitle("Required permissions")
                    .setMessage("Please allow permissions")
                    .setIcon(R.drawable.sherlock_logo)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ActivityCompat.requestPermissions(activity_emergency.this,
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create().show();

        } else {


            ActivityCompat.requestPermissions(activity_emergency.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

        }

        progressDialog.dismiss();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                if (ContextCompat.checkSelfPermission(activity_emergency.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {


                    fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                try {
                                    latitude = String.valueOf(location.getLatitude());
                                    longitude = String.valueOf(location.getLongitude());


                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    String address = addresses.get(0).getAddressLine(0);
                                    String area = addresses.get(0).getLocality();
                                    String city = addresses.get(0).getAdminArea();
                                    String country = addresses.get(0).getCountryName();
                                    String postalCode = addresses.get(0).getPostalCode();
                                    textLocation.setText(address + "," + area + "," + city + "," + country);

                                    location.reset();

                                } catch (Exception ex) {


                                }
                            }
                        }
                    });


                }


            } else {


            }

        }


    }

    public void postInquiry(String url) {

        userUriList.add(url);


        if (ImageList.size() == userUriList.size()) {
            for (int ii = 0; ii < userUriList.size(); ii++) {
                userImgList.add(userUriList.get(ii));
            }

            if (userUriList.size() != 4) {
                int remain = 4 - userUriList.size();
                for (int i = 0; i < remain; i++) {
                    userImgList.add("none");
                }
            }


            if (VideoUri != null) {
                progressDialogIN.setProgress(0);
                progressDialogIN.setMessage("Your Inquiry Selected Video is Uploading..Time Is Depend On Your Video Size");

                StorageReference videoFolder = storage.getInstance().getReference().child("userInquiryVideo");

                final StorageReference videoName = videoFolder.child("Video" + VideoUri.getLastPathSegment());
                videoName.putFile(VideoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        videoName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String videourl = String.valueOf(uri);

                                postMyInquiryWithVideo(videourl);

                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialogIN.setProgress(currentProgress);

                    }
                });


            } else {


                postMyInquiryWithVideo("none");


            }


        }


    }


    private void postMyInquiryWithVideo(String videoUrl) {

        final String myVideo = videoUrl;


        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String currentDate = df.format("MMM dd yyyy", new java.util.Date()).toString();
        String currentTime = df.format("hh:mm a", new java.util.Date()).toString();


        databaseReference = FirebaseDatabase.getInstance().getReference("mobile/userInquiry");
        id = databaseReference.push().getKey();

        userInquiry userInquiry = new userInquiry(id, spinnerTypes.getSelectedItem().toString(), spinnerPriority.getSelectedItem().toString(), textLocation.getText().toString(), editTextCmt.getText().toString(), currentDate, user.UID, currentDate, user.UID, 0, "0", "Operator", "A", latitude, longitude, "none", currentTime, currentTime, 0);
        databaseReference.child(id).setValue(userInquiry).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                databaseReference = FirebaseDatabase.getInstance().getReference("mobile/userInquiry/" + id);
                inquiryImages inquiryImages = new inquiryImages(userImgList.get(0), userImgList.get(1), userImgList.get(2), userImgList.get(3));
                databaseReference.child("images").setValue(inquiryImages).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        databaseReference.child("user").setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                databaseReference.child("video/video_Url").setValue(myVideo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        progressDialogIN.dismiss();
                                        Toast.makeText(activity_emergency.this, "Uploaded Successfully", Toast.LENGTH_LONG).show();
//                                       databaseReference.onDisconnect();
                                        //  getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        clearFields();
                                    }
                                });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressDialogIN.dismiss();
                        Toast.makeText(activity_emergency.this, "Error ocured", Toast.LENGTH_LONG).show();
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressDialogIN.dismiss();
                Toast.makeText(activity_emergency.this, "Error ocured", Toast.LENGTH_LONG).show();

            }
        });


    }


    private void clearFields() {


        spinnerPriority.setSelection(0);
        spinnerTypes.setSelection(0);
        textLocation.setText("");
        editTextCmt.setText("");
        gallery.removeAllViews();

        if (selectVideo.getVisibility() == View.VISIBLE) {
            selectVideo.stopPlayback();
            selectVideo.clearAnimation();
            selectVideo.suspend(); // clears media player
            selectVideo.setVideoURI(null);
            selectVideo.getLayoutParams().height = 0;
            selectVideo.getLayoutParams().width = 0;
            selectVideo.requestLayout();

            //selectVideo.setVisibility(View.GONE);
            //selectVideo.setVisibility(View.VISIBLE);

        }

    }


    private int getMyData() {

        databaseReference = FirebaseDatabase.getInstance().getReference("mobile").child("user").child(Uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return 1;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {

            try {


                gallery.removeAllViews();


                if (data.getClipData() != null) {

                    if (data.getClipData().getItemCount() > 5) {


                    } else {


                        // ImgUri = data.getData();
                        // ClipData mClipdata = data.getClipData();

                        try {

                            for (int i = 0; i < data.getClipData().getItemCount(); i++) {

                                //imageView.setImageResource(R.mipmap.ic_launcher);

                                // imageView.setImageURI(clipdata.getItemAt(i).getUri());
                                //ImgUri = data.getClipData().getItemAt(i).getUri();
                                // ImgUri = data.getData();
                                //  Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),ImgUri);
                                // imageView.setImageBitmap(bitmap);

                                View view = inflater.inflate(R.layout.images, gallery, false);
                                ImageView imageView = view.findViewById(R.id.imageView);


                                ImgUri = data.getClipData().getItemAt(i).getUri();
                                ImageList.add(ImgUri);

//                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), ImgUri);
//                                imageView.setImageBitmap(bitmap);

//                                Bitmap img = BitmapFactory.decodeStream(inputStream);
//                                imageView.setImageBitmap(img);

                                // imageView.setImageResource(R.mipmap.ic_launcher);


                                // imageView.setImageBitmap(DecodeSampleBitmapFromResource(getResources(), file, 500, 500));


                                inputStream = getContentResolver().openInputStream(ImgUri);
                                Bitmap bmp = BitmapFactory.decodeStream(inputStream);

                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bmp.compress(Bitmap.CompressFormat.JPEG, 5, stream);
                                imageView.setImageBitmap(bmp);
                                inputStream.close();


                                gallery.addView(view);

                            }

                        } catch (Exception ex) {

                            Toast.makeText(activity_emergency.this, "Error ocured" + ex.toString(), Toast.LENGTH_LONG).show();


                        }
                    }
                } else if (data.getData() != null) {

                    View view = inflater.inflate(R.layout.images, gallery, false);
                    ImageView imageView = view.findViewById(R.id.imageView);


                    ImgUri = data.getData();
                    ImageList.add(ImgUri);

                    //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), ImgUri);
                    //imageView.setImageBitmap(bitmap);
//                    imageView.setImageURI(ImgUri);


                    inputStream = getContentResolver().openInputStream(ImgUri);
                    Bitmap bmp = BitmapFactory.decodeStream(inputStream);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 2, stream);
                    imageView.setImageBitmap(bmp);
                    inputStream.close();

                    gallery.addView(view);

                }

            } catch (Exception ex) {

                ex.printStackTrace();

            }

        }


        if (requestCode == PICK_VIDEO && resultCode == RESULT_OK && data != null) {

            try {


                VideoUri = data.getData();
                selectVideo.setVideoURI(VideoUri);

                if (selectVideo.getLayoutParams().width == 0) {
                    selectVideo.getLayoutParams().width = 620;
                    selectVideo.getLayoutParams().height = selectVideo.getLayoutParams().width;
                } else {
                    videoWidth = selectVideo.getLayoutParams().width;
                    selectVideo.getLayoutParams().height = selectVideo.getLayoutParams().width;
                }


                selectVideo.requestLayout();

            } catch (Exception ex) {


            }
        }


    }


    private static int calculateImgSize(BitmapFactory.Options options, int reqWidth, int reqHeiht) {


        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;


        if (height > reqHeiht || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;


            while ((halfHeight / inSampleSize) >= reqHeiht && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize = 2;

            }


        }
        return inSampleSize;

    }


    private static Bitmap DecodeSampleBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {


        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);


        options.inSampleSize = calculateImgSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


}

