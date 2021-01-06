package com.apps.almadenahpharmacy.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.apps.almadenahpharmacy.Adapters.HomeRegisterAdapter;
import com.apps.almadenahpharmacy.Models.Employee;
import com.apps.almadenahpharmacy.OnIntentReceived;
import com.apps.almadenahpharmacy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    int saturday ;
    int sunday ;
    int monday ;
    int tuesday ;
    int wednesday ;
    int thursday ;
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
        countOfHoursTheEmployeesMustWorkEveryMonth();

        ListView list = root.findViewById(R.id.listEmpRegister);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<Employee> data = new ArrayList<>();
        final HomeRegisterAdapter adapter = new HomeRegisterAdapter(data, getActivity(), HomeFragment.this);
        list.setAdapter(adapter);
        database.getReference().child("Employees").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Employee employee = new Employee();
                    employee.setName(snapshot.child("name").getValue().toString());
                    employee.setId(Integer.parseInt(snapshot.child("id").getValue().toString()));
                    data.add(employee);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });


        return root;

    }

    public void checkPermission(String permission, int requestCode) {
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
    public void onIntent(Intent i, int resultCode) {
    // here i wil write the code to take picture/save and pick


        startActivityForResult(i,2);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Toast.makeText(getActivity(), data.getData()+"11", Toast.LENGTH_SHORT).show();
        }

    }
    public void takePhoto() {
        String imageFileName =  "155"; //make a better file name
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(imageFileName,
                    ".jpg",
                    storageDir
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        uri = Uri.fromFile(image);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(takePhotoIntent,1);

    }

    public void countOfHoursTheEmployeesMustWorkEveryMonth() {
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd/MM/yyyy");
        String currentDateAndTime = dateFormat.format(new Date());

        try {
            Date date  = dateFormat.parse(currentDateAndTime);
            final String year= (String) android.text.format.DateFormat.format("yyy", date);
            final int yearInt = Integer.parseInt(year);
            final String month= (String) android.text.format.DateFormat.format("MM", date);
            final int monthInt= Integer.parseInt(month);
           // final String day= (String) android.text.format.DateFormat.format("EEEE", date);

            Calendar calendar = new GregorianCalendar(yearInt, monthInt-1, 1);
            int daysInMonth= calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            for (int i=1 ;i<=daysInMonth ;i++){
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, i); //Set Day of the Month, 1..31
                cal.set(Calendar.MONTH,monthInt-1); //Set month, starts with JANUARY = 0
                cal.set(Calendar.YEAR,yearInt);

                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek==7) {
                    saturday++;
                }
                else if (dayOfWeek==1) {
                    sunday++;
                }
                else if (dayOfWeek==2) {
                    monday++;
                }
                else if (dayOfWeek==3) {
                    tuesday++;
                }
                else if (dayOfWeek==4) {
                    wednesday++;
                }
                else if (dayOfWeek==5) {
                    thursday++;
                }

            }
            HashMap<String,Integer> countOfDays = new HashMap<>();
            countOfDays.put("Saturdays",saturday);
            countOfDays.put("Sundays",sunday);
            countOfDays.put("Mondays",monday);
            countOfDays.put("Tuesdays",tuesday);
            countOfDays.put("Wednesday",wednesday);
            countOfDays.put("Thursday",thursday);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference().child(year).child(month).setValue(countOfDays);


        } catch (ParseException e) {
            e.printStackTrace();
        }



    }



}