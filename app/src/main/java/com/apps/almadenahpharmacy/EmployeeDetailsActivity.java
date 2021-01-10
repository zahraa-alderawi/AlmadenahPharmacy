package com.apps.almadenahpharmacy;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.apps.almadenahpharmacy.Models.Employee;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class EmployeeDetailsActivity extends AppCompatActivity {

    private TextView txtEmpName;
    private TextView txtHoursRequired;
    private TextView txtHoursDone;
    private TextView txtHoursExtra;
    private TextView txtPriceExtra;
    private TextView txtExtraHourPrice;
    private MaterialButton butShowThisMonth;
    private MaterialButton butShowLastMonth;
    ImageView showImage ;
    int requiredHours;
    long doneTime ;
    long extraTime;

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    String currentPhotoPath;
    StorageReference storageReference;
    Uri uriImage ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);
        txtEmpName = findViewById(R.id.txtEmpName);
        txtHoursRequired = findViewById(R.id.txtHoursRequired);
        txtHoursDone = findViewById(R.id.txtHoursDone);
        txtHoursExtra = findViewById(R.id.txtHoursExtra);
        txtPriceExtra = findViewById(R.id.txtPriceExtra);
        txtExtraHourPrice = findViewById(R.id.txtExtraHourPrice);
        butShowThisMonth = findViewById(R.id.butShowThisMonth);
        butShowLastMonth = findViewById(R.id.butShowLastMonth);
        showImage = findViewById(R.id.showImage);
        storageReference = FirebaseStorage.getInstance().getReference();

        final Employee employee = (Employee) getIntent().getSerializableExtra("employee");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String currentDateAndTime = format.format(new Date());

        try {
            Date  date = format.parse(currentDateAndTime);
            final String month= (String) DateFormat.format("MM", date);
            final int monthInt = Integer.parseInt(month);
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference().child("EmployeesHours").child(employee.getId()+"").child(monthInt+"").
                    addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    requiredHours =Integer.parseInt( dataSnapshot.child("requiredHours").getValue().toString());
                    doneTime =Long.parseLong( dataSnapshot.child("doneTime").getValue().toString());
                    extraTime =Long.parseLong( dataSnapshot.child("extraTime").getValue().toString());
                    long differentMinutes =  (doneTime/(1000*60));
                   int hours = (int) (differentMinutes / 60);
                    int minutes = (int) (differentMinutes % 60);
                    long requiredInMillie = (long) (requiredHours*(3.6e+6));
                    if (doneTime> requiredInMillie){
                        long extraMinutesMillie = doneTime-requiredInMillie ;
                        database.getReference().child("EmployeesHours").child(employee.getId()+"").child(monthInt+"").child("extraTime").setValue(extraMinutesMillie);
                        long extraMinutes =  (extraMinutesMillie/(1000*60));
                        int hoursExtra = (int) (extraMinutes / 60);
                        int minutesExtra = (int) (extraMinutes % 60);
                        txtHoursExtra.setText(hoursExtra+" ساعات و"+minutesExtra+" دقائق");
                       double extraPriceForOneMinute = employee.getExtraHourPrice() /60.0 ;
                       float extraPriceThisMonth = (float) (extraMinutes * extraPriceForOneMinute);
                        database.getReference().child("EmployeesHours").child(employee.getId()+"").child(monthInt+"").child("extraPrice").setValue(extraPriceThisMonth);
                        txtPriceExtra.setText(extraPriceThisMonth+" شيكل");


                    }

                    txtHoursRequired.setText(requiredHours+" ساعات");
                    txtHoursDone.setText(hours+" ساعات و"+minutes+" دقائق");
                    txtEmpName.setText(employee.getName());
                    txtExtraHourPrice.setText(employee.getExtraHourPrice()+" شيكل");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }

        butShowThisMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermissions();

            }
        });

    }

    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else {
            dispatchTakePictureIntent();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                showImage.setImageURI(Uri.fromFile(f));
                Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

                uploadImageToFirebase(f.getName(), contentUri);


            }

        }

       /* if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
                showImage.setImageURI(contentUri);

                uploadImageToFirebase(imageFileName, contentUri);


            }

        }*/


    }

    private void uploadImageToFirebase(String name, Uri contentUri) {
        final StorageReference image = storageReference.child("pictures/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(EmployeeDetailsActivity.this, uri+"", Toast.LENGTH_SHORT).show();
                        Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
                    }
                });

                Toast.makeText(EmployeeDetailsActivity.this, "Image Is Uploaded.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EmployeeDetailsActivity.this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.apps.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

}