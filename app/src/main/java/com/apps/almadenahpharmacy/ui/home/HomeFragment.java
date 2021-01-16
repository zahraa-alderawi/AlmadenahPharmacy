package com.apps.almadenahpharmacy.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.apps.almadenahpharmacy.Adapters.HomeRegisterAdapter;
import com.apps.almadenahpharmacy.EmployeeDetailsActivity;
import com.apps.almadenahpharmacy.Models.Employee;
import com.apps.almadenahpharmacy.OnIntentReceived;
import com.apps.almadenahpharmacy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;


public class HomeFragment extends Fragment implements  OnIntentReceived {

    private HomeViewModel homeViewModel;
    Uri uri;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    String currentPhotoPath;
    StorageReference storageReference;
    Uri uriImage ;
    int empId ;
    int shiftMonth ;
    int shiftDay ;
    String shiftImageType;
    ProgressBar progressBar ;
    LinearLayout linearHome ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 1);
        checkPermission(Manifest.permission.CAMERA, 2);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
       /* Button btn = root.findViewById(R.id.btnTakePhoto);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, 1);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
            }
        });*/

        ListView gridEmpRegister = root.findViewById(R.id.gridEmpRegister);
         linearHome = root.findViewById(R.id.linearHome);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        final ArrayList<Employee> data = new ArrayList<>();
        final HomeRegisterAdapter adapter = new HomeRegisterAdapter(data, getActivity(), HomeFragment.this);
        gridEmpRegister.setAdapter(adapter);
        database.getReference().child("Employees").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Employee employee = new Employee();
                    employee.setId(Integer.parseInt(snapshot.child("id").getValue().toString()));
                    employee.setName(snapshot.child("name").getValue().toString());
                    employee.setGender(snapshot.child("gender").getValue().toString());
                    employee.setDaysCount(Integer.parseInt(snapshot.child("daysCount").getValue().toString()));
                    employee.setHoursCount(Integer.parseInt(snapshot.child("hoursCount").getValue().toString()));
                    employee.setDaysNames((ArrayList<String>) snapshot.child("daysNames").getValue());
                    data.add(employee);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });


        return root;

    }

    public void checkPermission(String permission, int requestCode) {
                                //Manifest.permission.CAMERA, 2
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                getActivity(),
                permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat
                    .requestPermissions(
                            getActivity(),
                            new String[]{permission},
                            requestCode);
        }
    }


    @Override
    public void onIntent(int id  , int month ,int day , String imageType ) {
        empId = id;
        shiftMonth = month ;
        shiftDay = day ;
        shiftImageType = imageType ;
        linearHome.setVisibility(View.VISIBLE);
        askCameraPermissions();

    }

    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
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
                Toast.makeText(getActivity(), "يجب إطاء التطبيق صلاحية الوصول للكاميرا", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.apps.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);

            }
        }
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                //showImage.setImageURI(Uri.fromFile(f));
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                getActivity().sendBroadcast(mediaScanIntent);

                uploadImageToFirebase(f.getName(), contentUri);


            }

        }


    }

    private void uploadImageToFirebase(String name, Uri contentUri) {
        final StorageReference image = storageReference.child("pictures/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        database.getReference().child("Shifts").child(empId+"").child(shiftMonth+"").
                                child(shiftDay+"").child(shiftImageType).setValue(uri+"");
                        linearHome.setVisibility(View.GONE);


                    }
                });

                Toast.makeText(getActivity(), "Image Is Uploaded.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage()+"", Toast.LENGTH_SHORT).show();
            }
        });

    }





}