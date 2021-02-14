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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.apps.almadenahpharmacy.Adapters.HomeRegisterAdapter;
import com.apps.almadenahpharmacy.Models.Employee;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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

      int saturday;
      int sunday;
      int monday;
     int tuesday;
     int wednesday;
     int thursday;
      int allHours ;
      LinearLayout linearDetails ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);
        linearDetails = findViewById(R.id.linearDetails);
        final Employee employee = (Employee) getIntent().getSerializableExtra("employee");
        allHours = 0;
        txtEmpName = findViewById(R.id.txtEmpName);
        txtHoursRequired = findViewById(R.id.txtHoursRequired);
        txtHoursDone = findViewById(R.id.txtHoursDone);
        txtHoursExtra = findViewById(R.id.txtHoursExtra);
        txtPriceExtra = findViewById(R.id.txtPriceExtra);
        txtExtraHourPrice = findViewById(R.id.txtExtraHourPrice);
        butShowThisMonth = findViewById(R.id.butShowThisMonth);
        butShowLastMonth = findViewById(R.id.butShowLastMonth);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        countOfHoursTheEmployeesMustWorkEveryMonth(employee.getId(), employee.getDaysNames(), employee.getHoursCount());



        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String currentDateAndTime = format.format(new Date());

        try {
            Date  date = format.parse(currentDateAndTime);
            final String month= (String) DateFormat.format("MM", date);
             monthInt = Integer.parseInt(month);
            database.getReference().child("EmployeesHours").child(employee.getId()+"").child(monthInt+"").child("requiredHours").setValue(allHours);
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
                                        linearDetails.setVisibility(View.GONE);


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


    public  void countOfHoursTheEmployeesMustWorkEveryMonth(int id, ArrayList<String> daysNames, int hoursCount) {
        java.text.DateFormat dateFormat = new SimpleDateFormat("EEE, dd/MM/yyyy");
        String currentDateAndTime = dateFormat.format(new Date());

        try {
            Date date = dateFormat.parse(currentDateAndTime);
            final String year = (String) android.text.format.DateFormat.format("yyy", date);
            final int yearInt = Integer.parseInt(year);
            final String month = (String) android.text.format.DateFormat.format("MM", date);
            final int monthInt = Integer.parseInt(month);
            // final String day= (String) android.text.format.DateFormat.format("EEEE", date);

            Calendar calendar = new GregorianCalendar(yearInt, monthInt - 1, 1);
            int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            for (int i = 1; i <= daysInMonth; i++) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, i); //Set Day of the Month, 1..31
                cal.set(Calendar.MONTH, monthInt - 1); //Set month, starts with JANUARY = 0
                cal.set(Calendar.YEAR, yearInt);

                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek == 7) {
                    saturday++;
                } else if (dayOfWeek == 1) {
                    sunday++;
                } else if (dayOfWeek == 2) {
                    monday++;
                } else if (dayOfWeek == 3) {
                    tuesday++;
                } else if (dayOfWeek == 4) {
                    wednesday++;
                } else if (dayOfWeek == 5) {
                    thursday++;
                }

            }
            /*HashMap<String,Integer> countOfDays = new HashMap<>();
            countOfDays.put("Saturdays",saturday);
            countOfDays.put("Sundays",sunday);
            countOfDays.put("Mondays",monday);
            countOfDays.put("Tuesdays",tuesday);
            countOfDays.put("Wednesday",wednesday);
            countOfDays.put("Thursday",thursday);

            FirebaseDatabase database = FirebaseDatabase.getInstance();

            database.getReference().child(year).child(month).setValue(countOfDays); */

        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < daysNames.size(); i++) {
            if (daysNames.get(i).equals("السبت")) {
                allHours = allHours + (saturday * hoursCount);
            } else if (daysNames.get(i).equals("الأحد")) {
                allHours = allHours + (sunday * hoursCount);
            } else if (daysNames.get(i).equals("الاثنين")) {
                allHours = allHours + (monday * hoursCount);
            } else if (daysNames.get(i).equals("الثلاثاء")) {
                allHours = allHours + (tuesday * hoursCount);
            } else if (daysNames.get(i).equals("الأربعاء")) {
                allHours = allHours + (wednesday * hoursCount);
            } else if (daysNames.get(i).equals("الخميس")) {
                allHours = allHours + (thursday * hoursCount);
            }
        }




    }





}