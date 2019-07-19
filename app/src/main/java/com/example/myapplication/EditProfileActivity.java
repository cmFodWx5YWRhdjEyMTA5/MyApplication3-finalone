package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {

    private Button btnUpdateUser;
    private EditText editFirstName, editLastName, editNIC, editMail, editAddress;
    private ProgressDialog progressDialog;
    StorageReference storageReferance;
    private FirebaseAuth auth;
    private TinyDB tinyDB;
    User user;
    ImageView btnCamera, userImg;
    int flag = 0;
    String Uid = "";
    private Uri filePath = null;
    String IMG_PATH = "";
    int pick;
    String PicID = "";
    String token = "";
    String img_url = "";


    String IMAGE_BASE_64;
    String PATH;
    Bitmap profile_pic;
    File file;
    Boolean isImageSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().setTitle("EDIT PROFILE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        auth = FirebaseAuth.getInstance();
        storageReferance = FirebaseStorage.getInstance().getReference();

        user = new User();
        tinyDB = new TinyDB(this);

        Uid = tinyDB.getString("UID");
        PicID = tinyDB.getString("PIcID");
        token = tinyDB.getString("Token");

        String IMG_PATH_CURR = tinyDB.getString("IMG_PATH_CURRENT");


        btnCamera = findViewById(R.id.btnCamera);
        userImg = findViewById(R.id.userImage);


        progressDialog = new ProgressDialog(this);

        progressDialog = new ProgressDialog(this);
        btnUpdateUser = (Button) findViewById(R.id.btnUpdateUser);

        editFirstName = (EditText) findViewById(R.id.editFirstName);
        editFirstName.setGravity(Gravity.CENTER);

        editLastName = (EditText) findViewById(R.id.editLastName);
        editLastName.setGravity(Gravity.CENTER);

        editNIC = (EditText) findViewById(R.id.editNIC);
        editNIC.setGravity(Gravity.CENTER);

        editMail = (EditText) findViewById(R.id.editEmail);
        editMail.setGravity(Gravity.CENTER);

        editAddress = (EditText) findViewById(R.id.editAddress);
        editAddress.setGravity(Gravity.CENTER);

        //storageReferance = storageReferance.child("MobileUser").child("UserImage").child(PicID).child("Images/Scetch.jpg");
        //img_url = storageReferance.child("MobileUser").child("UserImage").child(PicID).child("Images/Scetch.jpg").toString();

        storageReferance = storageReferance.child("MobileUser").child("UserImage").child(IMG_PATH_CURR);


        final long ONE_MEGABYTE = 1024 * 1024;

        storageReferance.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                userImg.setMinimumHeight(dm.heightPixels);
                userImg.setMinimumWidth(dm.widthPixels);
                userImg.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "Could Not Upload the Image", Toast.LENGTH_SHORT).show();

            }
        });


        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("mobile").child("user").child(Uid);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                Log.e("DS", dataSnapshot.toString());
                try {
                    editFirstName.setText(user.FirstName);
                    editLastName.setText(user.LastName);
                    editNIC.setText(user.NIC);
                    editMail.setText(user.Mail);
                    editAddress.setText(user.Address);

                } catch (NullPointerException e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();


            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Selectimage();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = (List<String>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_PATH);

            userImg.setImageBitmap(getBitmapFromPath(mPaths.get(0)));

            IMAGE_BASE_64 = encodeToBase64(getBitmapFromPath(mPaths.get(0)), Bitmap.CompressFormat.JPEG, 10);
            profile_pic = getBitmapFromPath(mPaths.get(0));
            PATH = mPaths.get(0);
            isImageSelected = true;
            file = new File(mPaths.get(0));
            filePath = Uri.fromFile(file);

            Log.e("uri >>", filePath + "");

        }


    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    private Bitmap getBitmapFromPath(String path) {

        File imgFile = new File(path);
        return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    private void Selectimage() {
        try {
            new ImagePicker.Builder(EditProfileActivity.this)
                    .mode(ImagePicker.Mode.CAMERA)
                    .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                    .directory(ImagePicker.Directory.DEFAULT)
                    .extension(ImagePicker.Extension.PNG)
                    .scale(600, 600)
                    .allowMultipleImages(false)
                    .enableDebuggingMode(true)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void uploadImage() {
        try {
            progressDialog.setMessage("Updating, Please Wait..");
            progressDialog.setCancelable(false);
            progressDialog.setIcon(R.drawable.sherlock_logo);
            progressDialog.show();

            //StorageReference storageRef = storageReferance.child("MobileUser").child("UserImage").child(PicID).child("Images/Scetch.jpg");
            //String path = storageReferance.getPath();
            //IMG_PATH = path;

            if (filePath != null) {

                StorageReference storageRef = storageReferance.child("MobileUser/UserImage");
                final StorageReference imageName = storageRef.child("Image" + filePath.getLastPathSegment());


                imageName.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String url = String.valueOf(uri);
                                tinyDB.putString("ImgPath", url);

                                if (url != "" && tinyDB.getString("ImgPath") != null) {
                                    UpdateUser();
                                }
                            }
                        });
                        //pick = 0;
                        //Toast.makeText(getApplicationContext(),"Uploaded",Toast.LENGTH_SHORT).show();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

//                    Toast.makeText(getApplicationContext(),"Could Not load the Image",Toast.LENGTH_SHORT).show();
                    }
                });


            } else {

                UpdateUser();

            }


        } catch (Exception e) {
            Log.e("Error", ">>" + e);
            //Toast.makeText(getApplicationContext(), "Could't Upload the Image", Toast.LENGTH_SHORT).show();
        }

    }

    private void UpdateUser() {

        final String FirstName = editFirstName.getText().toString().trim();
        final String LastName = editLastName.getText().toString().trim();
        final String NIC = editNIC.getText().toString().trim();
        final String Mail = editMail.getText().toString().trim();
        final String Address = editAddress.getText().toString().trim();
        final String Type = "Client";

        if (TextUtils.isEmpty(FirstName)) {
            progressDialog.dismiss();
            editFirstName.setError("Enter Your First Name");
//            Toast.makeText(getApplicationContext(),"Enter Your First Name",Toast.LENGTH_SHORT).show();
            flag = 1;
            return;
        }
        if (TextUtils.isEmpty(LastName)) {
            progressDialog.dismiss();
            editLastName.setError("Enter Your Last Name");
//            Toast.makeText(getApplicationContext(),"Enter Your Last Name",Toast.LENGTH_SHORT).show();
            flag = 1;
            return;
        }

        if (TextUtils.isEmpty(NIC)) {
            progressDialog.dismiss();
            editNIC.setError("Enter Your NIC");
//            Toast.makeText(getApplicationContext(),"Enter Your NIC",Toast.LENGTH_SHORT).show();
            return;
        }
        if (NIC.length() < 10 || NIC.length() > 12) {
            progressDialog.dismiss();
            editNIC.setError("Enter A Valid NIC");
//            Toast.makeText(getApplicationContext(),"Enter A Valid NIC",Toast.LENGTH_SHORT).show();
            flag = 1;
            return;
        }

        if (TextUtils.isEmpty(Mail)) {
            progressDialog.dismiss();
            editMail.setError("Enter Your Email Address");
//            Toast.makeText(getApplicationContext(),"Enter Your Email",Toast.LENGTH_SHORT).show();
            flag = 1;
            return;
        }

        if (TextUtils.isEmpty(Address)) {
            progressDialog.dismiss();
            editAddress.setError("Enter Your Home Address");
//            Toast.makeText(getApplicationContext(),"Enter Your Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if (Mail.length() < 12) {
            progressDialog.dismiss();
            editMail.setError("Enter A Valid Email Address");
//            Toast.makeText(getApplicationContext(),"Enter A Valid Email Address",Toast.LENGTH_SHORT).show();
            flag = 1;
            return;

        }


//        Toast.makeText(getApplicationContext(),"User Updated",Toast.LENGTH_SHORT).show();

        if (flag == 0) {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("mobile").child("user").child(Uid);


            User user = new User(FirstName, LastName, NIC, Mail, Address, Type, Uid, "Active", token, tinyDB.getString("ImgPath"));

            Log.e("TAG", user.FirstName);
            myRef.setValue(user);

            progressDialog.dismiss();

            Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();

        } else {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Error Updating the Profile", Toast.LENGTH_SHORT).show();
        }

    }


}
