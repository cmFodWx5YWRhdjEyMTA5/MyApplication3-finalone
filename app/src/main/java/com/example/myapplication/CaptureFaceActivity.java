package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.faceScan.FaceScanActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.UUID;

public class CaptureFaceActivity extends AppCompatActivity {

    ImageView userImage;
    Button btnCapture;
    Button btnContinue;
    private Uri filePath;
    private FirebaseAuth auth;
    private TinyDB tinyDB;
    private StorageReference storageReferance;


    String IMAGE_BASE_64;
    String PATH;
    Bitmap profile_pic;
    File file;
    int pick = 0;
    Boolean isImageSelected;
    String IMG_PATH = "";
    String PicID = null;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_face);


        auth = FirebaseAuth.getInstance();
        storageReferance = FirebaseStorage.getInstance().getReference();

        tinyDB = new TinyDB(this);


        progressDialog = new ProgressDialog(this);


        userImage = findViewById(R.id.userImage);
        btnCapture = findViewById(R.id.btnCapture);
        btnContinue = findViewById(R.id.btnNext);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CaptureFaceActivity.this, FaceScanActivity.class));
//                Selectimage();

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                uploadImage();

//                if (pick == 1) {
//                    Intent i = new Intent(CaptureFaceActivity.this, RegisterActivity_4.class);
//                    startActivity(i);
//                    finish();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Could Not Upload the Image, Try Again..", Toast.LENGTH_SHORT).show();
//                }

            }
        });

        if (PicID == null) {
            PicID = UUID.randomUUID().toString();
            tinyDB.putString("PIcID", PicID);
        }


    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CaptureFaceActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Your SignUp Process Will Be Wiped. Are Your Sure to Start Over?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {

                } catch (NullPointerException e) {
                    auth.getCurrentUser().delete();
                    Intent i = new Intent(CaptureFaceActivity.this, RegisterActivity_1.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        builder.show();

    }

    private void Selectimage() {
        try {
            new ImagePicker.Builder(CaptureFaceActivity.this)
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

            progressDialog.setMessage("Collecting your data, Please Wait..");
            progressDialog.setCancelable(false);
            progressDialog.setIcon(R.drawable.sherlock_logo);
            progressDialog.show();


            //StorageReference storageRef = storageReferance.child("MobileUser").child("UserImage").child(PicID).child("Images/Scetch.jpg");

            StorageReference storageRef = storageReferance.child("MobileUser/UserImage");
            String path = storageRef.getPath();
            //IMG_PATH = path;

            final StorageReference imageName = storageRef.child("Image" + filePath.getLastPathSegment());

            IMG_PATH = "Image" + filePath.getLastPathSegment();
            tinyDB.putString("IMG_PATH_CURRENT", IMG_PATH);

            imageName.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = String.valueOf(uri);
                            tinyDB.putString("ImgPath", url);

                            if (url != "" && tinyDB.getString("ImgPath") != null) {
                                pick = 1;
                                progressDialog.dismiss();
                                nevigateToNext();
                            } else {
                                pick = 0;
                            }
                            //Log.e("IMGURL", url);
                        }
                    });
                    //Toast.makeText(getApplicationContext(),"Uploaded",Toast.LENGTH_SHORT).show();
                }

            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    // int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    //progressDialogIN.setProgress(currentProgress);

                }
            });


        } catch (Exception e) {
            //Log.e("Error", ">>" + e);
            //Toast.makeText(getApplicationContext(), "Could't Upload the Image", Toast.LENGTH_SHORT).show();

        }


    }


    private void nevigateToNext() {

        Intent i = new Intent(CaptureFaceActivity.this, RegisterActivity_4.class);
        startActivity(i);
        finish();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = (List<String>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_PATH);

            userImage.setImageBitmap(getBitmapFromPath(mPaths.get(0)));

            if (userImage != null) {
                btnContinue.setEnabled(true);
            }

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

}
