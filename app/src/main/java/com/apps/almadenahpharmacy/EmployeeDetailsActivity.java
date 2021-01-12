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
    int monthInt ;
    int requiredHours;
    long doneTime ;
    long extraTime;


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

        final Employee employee = (Employee) getIntent().getSerializableExtra("employee");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String currentDateAndTime = format.format(new Date());

        try {
            Date  date = format.parse(currentDateAndTime);
            final String month= (String) DateFormat.format("MM", date);
             monthInt = Integer.parseInt(month);
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
                Intent intent = new Intent(EmployeeDetailsActivity.this,EmployeeShiftsActivity.class);
                intent.putExtra("id" , employee.getId());
                intent.putExtra("month" , monthInt);
                startActivity(intent);

            }
        });

        butShowLastMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmployeeDetailsActivity.this,EmployeeShiftsActivity.class);
                intent.putExtra("id" , employee.getId());
                intent.putExtra("month" , monthInt-1);
                startActivity(intent);
            }
        });


    }







}