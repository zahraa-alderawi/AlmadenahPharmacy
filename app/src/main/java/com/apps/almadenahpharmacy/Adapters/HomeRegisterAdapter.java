package com.apps.almadenahpharmacy.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.apps.almadenahpharmacy.EmployeeDetailsActivity;
import com.apps.almadenahpharmacy.Models.Employee;
import com.apps.almadenahpharmacy.Models.Shift;
import com.apps.almadenahpharmacy.OnIntentReceived;
import com.apps.almadenahpharmacy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class HomeRegisterAdapter extends BaseAdapter {
    ArrayList<Employee> data;
    Activity activity;
    OnIntentReceived listener;
    int lastShiftForEmp;


    public HomeRegisterAdapter(ArrayList<Employee> data, Activity activity , OnIntentReceived listener) {
        this.data = data;
        this.activity = activity;
       this.listener=listener;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final View v = LayoutInflater.from(activity).inflate(R.layout.raw_home_register, null, false);
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        TextView rowNameEmp = v.findViewById(R.id.employeeNameRegister);
        com.google.android.material.button.MaterialButton btnCome =  v.findViewById(R.id.btnCome);
        com.google.android.material.button.MaterialButton btnLeft =  v.findViewById(R.id.btnLeft);
        ImageView imageEmpHome =  v.findViewById(R.id.imageEmpHome);
        rowNameEmp.setText(data.get(i).getName());
        if (data.get(i).getGender().equals("أنثى")){
            imageEmpHome.setImageResource(R.drawable.pharmacist_female);
        }
        else if (data.get(i).getGender().equals("ذكر")){
            imageEmpHome.setImageResource(R.drawable.pharmacist_male);
        }
        btnCome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  long timeStamp = System.currentTimeMillis();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.US);
                String currentDateAndTime = format.format(new Date());
                try {
                    Date date = format.parse(currentDateAndTime);
                    final String month= (String) DateFormat.format("MM", date);
                    final int monthInt = Integer.parseInt(month);
                    final String day= (String) DateFormat.format("dd", date);
                    final int dayInt = Integer.parseInt(day);
                    final Query lastQuery = reference.child("Shifts").child(data.get(i).getId()+"").orderByKey().limitToLast(1);
                    lastQuery.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int lastShiftForEmp ;
                            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                lastShiftForEmp= Integer.parseInt(  snap.getKey()+"");

                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                   // String time= (String) DateFormat.format("HH:mm", date);

                    final Shift shift = new Shift(data.get(i).getId(),monthInt,dayInt,0,0,null,null,0);
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    database.getReference().child("Shifts").child(data.get(i).getId()+"").child(lastShiftForEmp +1 +"").setValue(shift);
                    database.getReference().child("Shifts").child(data.get(i).getId() + "").child(lastShiftForEmp + 1 + "").child("timeStampComing").setValue(ServerValue.TIMESTAMP );
                   // listener.onIntent(data.get(i).getId() , monthInt,dayInt , "imageAttendance");

                } catch (ParseException e) {
                    e.printStackTrace();
                }



            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
              //  final long timeStamp = System.currentTimeMillis();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS'Z'");
                String currentDateAndTime = format.format(new Date());
                try {
                    Date date = format.parse(currentDateAndTime);
                    final String month= (String) DateFormat.format("MM", date);
                    final int monthInt = Integer.parseInt(month);
                    final String day= (String) DateFormat.format("dd", date);
                    final int dayInt = Integer.parseInt(day);
                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    rootRef.child("Shifts").child(data.get(i).getId()+"").child(monthInt+"").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild(day+"")) {
                                if (snapshot.child(day+"").hasChild("minutesCountInWork")) {
                                Toast.makeText(activity, "لقد قمت بتسجيل الانصراف لهذا اليوم", Toast.LENGTH_SHORT).show();

                                     }
                                else {
                              /*  database.getReference().child("Shifts").child(data.get(i).getId()+"").child(monthInt+"")
                                        .child(day+"").child("timeStampAttendance").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        long timeStampAttendance = Long.parseLong(dataSnapshot.getValue().toString());
                                        final long differentBetweenTimes = timeStamp - timeStampAttendance ;
                                        long differentMinutes =  (differentBetweenTimes/(1000*60)); //timestamp to minutes
                                        //   long differentMinutes =  (differentBetweenTimes/(1000*60)); timestamp to hours
                                        int hours = (int) (differentMinutes / 60);
                                        int minutes = (int) (differentMinutes % 60); */

                                        database.getReference().child("Shifts").child(data.get(i).getId()+"").child(monthInt+"").child(dayInt+"").child("imageLeave").setValue("image2");
                                        database.getReference().child("Shifts").child(data.get(i).getId()+"").child(monthInt+"").child(dayInt+"").child("timeStampLeave").setValue(ServerValue.TIMESTAMP);

                                        database.getReference().child("Shifts").child(data.get(i).getId()+"").child(monthInt+"").child(dayInt+"").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                long leftTimeStamp = Long.parseLong(dataSnapshot.child("timeStampLeave").getValue().toString());
                                                long timeStampComing = Long.parseLong(dataSnapshot.child("timeStampComing").getValue().toString());
                                                final long differentBetweenTimes = leftTimeStamp - timeStampComing ;
                                                long differentMinutes =  (differentBetweenTimes/(1000*60));
                                                database.getReference().child("Shifts").child(data.get(i).getId()+"").child(monthInt+"").child(dayInt+"").child("minutesCountInWork").setValue(differentMinutes);



                                        database.getReference().child("EmployeesHours").child(data.get(i).getId()+"").child(monthInt+"").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                long oldDoneHours = Long.parseLong(dataSnapshot.child("doneTime").getValue().toString());
                                                database.getReference().child("EmployeesHours").child(data.get(i).getId()+"").child(monthInt+"").
                                                        child("doneTime").setValue(differentBetweenTimes+oldDoneHours);

                                               // listener.onIntent(data.get(i).getId() , monthInt,dayInt , "imageLeave");


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });



                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }




                            }
                            else Toast.makeText(activity, "لم تقم بعد بتسجيل الحضور لهذا اليوم", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });

        return v;

    }



}
