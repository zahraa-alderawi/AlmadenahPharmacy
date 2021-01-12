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
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class HomeRegisterAdapter extends BaseAdapter {
    ArrayList<Employee> data;
    Activity activity;
    OnIntentReceived listener;
    Uri uri;
    int saturday;
    int sunday;
    int monday;
    int tuesday;
    int wednesday;
    int thursday;
    int allHours ;





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
        TextView rowNameEmp = v.findViewById(R.id.employeeNameRegister);
        ImageView imgCome =  v.findViewById(R.id.btnCome);
        ImageView imgLeft =  v.findViewById(R.id.btnLeft);
        ImageView imageEmpHome =  v.findViewById(R.id.imageEmpHome);
        rowNameEmp.setText(data.get(i).getName());
        if (data.get(i).getGender().equals("أنثى")){
            imageEmpHome.setImageResource(R.drawable.pharmacist_female);
        }
        else if (data.get(i).getGender().equals("ذكر")){
            imageEmpHome.setImageResource(R.drawable.pharmacist_male);
        }
        imgCome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allHours = 0;
                final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                long timeStamp = System.currentTimeMillis();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS'Z'");
                format.setTimeZone(TimeZone.getTimeZone("GMT"));
                String currentDateAndTime = format.format(new Date());
                try {
                    Date date = format.parse(currentDateAndTime);
                    final String month= (String) DateFormat.format("MM", date);
                    final int monthInt = Integer.parseInt(month);
                    final String day= (String) DateFormat.format("dd", date);
                    final int dayInt = Integer.parseInt(day);
                    String time= (String) DateFormat.format("HH:mm", date);
                    final Shift shift = new Shift(time,timeStamp,"image1",null,0,"image2",null,0,monthInt,dayInt);
                    rootRef.child("Shifts").child(data.get(i).getId()+"").child(monthInt+"").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild(day+"")) {
                                Toast.makeText(activity, "لقد قمت بتسجيل حضورك اليوم", Toast.LENGTH_SHORT).show();

                            }
                            else {
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                database.getReference().child("Shifts").child(data.get(i).getId()+"").child(monthInt+"").child(dayInt+"").setValue(shift);
                                listener.onIntent(data.get(i).getId() , monthInt,dayInt , "imageAttendance");

                                rootRef.child("EmployeesHours").child(data.get(i).getId()+"").child(monthInt+"").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (snapshot.hasChild("doneTime")) {
                                        }
                                        else {
                                            database.getReference().child("EmployeesHours").child(data.get(i).getId()+"").child(monthInt+"").child("doneTime").setValue(0);
                                            database.getReference().child("EmployeesHours").child(data.get(i).getId()+"").child(monthInt+"").child("requiredHours").setValue(allHours);
                                            database.getReference().child("EmployeesHours").child(data.get(i).getId()+"").child(monthInt+"").child("extraTime").setValue(0);

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                countOfHoursTheEmployeesMustWorkEveryMonth(data.get(i).getId(), data.get(i).getDaysNames(), data.get(i).getHoursCount());
                               }
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

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final long timeStamp = System.currentTimeMillis();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS'Z'");
                format.setTimeZone(TimeZone.getTimeZone("GMT"));
                String currentDateAndTime = format.format(new Date());
                try {
                    Date date = format.parse(currentDateAndTime);
                    final String month= (String) DateFormat.format("MM", date);
                    final int monthInt = Integer.parseInt(month);
                    final String day= (String) DateFormat.format("dd", date);
                    final int dayInt = Integer.parseInt(day);
                    final String time= (String) DateFormat.format("HH:mm", date);
                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    rootRef.child("Shifts").child(data.get(i).getId()+"").child(monthInt+"").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild(day+"")) {
                            if (snapshot.child(day+"").hasChild("spendingTimeInWork")) {
                                Toast.makeText(activity, "لقد قمت بتسجيل الانصراف لهذا اليوم", Toast.LENGTH_SHORT).show();

                            }
                            else {
                                database.getReference().child("Shifts").child(data.get(i).getId()+"").child(monthInt+"")
                                        .child(day+"").child("timeStampAttendance").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        long timeStampAttendance = Long.parseLong(dataSnapshot.getValue().toString());
                                        final long differentBetweenTimes = timeStamp - timeStampAttendance ;
                                        long differentMinutes =  (differentBetweenTimes/(1000*60)); //timestamp to minutes
                                        //   long differentMinutes =  (differentBetweenTimes/(1000*60)); timestamp to hours
                                        int hours = (int) (differentMinutes / 60);
                                        int minutes = (int) (differentMinutes % 60);

                                        database.getReference().child("Shifts").child(data.get(i).getId()+"").child(monthInt+"").child(dayInt+"").child("timeLeave").setValue(time);
                                        database.getReference().child("Shifts").child(data.get(i).getId()+"").child(monthInt+"").child(dayInt+"").child("imageLeave").setValue("image2");
                                        database.getReference().child("Shifts").child(data.get(i).getId()+"").child(monthInt+"").child(dayInt+"").child("timeStampLeave").setValue(timeStamp);
                                        database.getReference().child("Shifts").child(data.get(i).getId()+"").child(monthInt+"").child(dayInt+"").child("spendingTimeInWork").setValue(hours+":"+minutes);
                                        database.getReference().child("Shifts").child(data.get(i).getId()+"").child(monthInt+"").child(dayInt+"").child("minutesCountInWork").setValue(differentMinutes);

                                        database.getReference().child("EmployeesHours").child(data.get(i).getId()+"").child(monthInt+"").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                long oldDoneHours = Long.parseLong(dataSnapshot.child("doneTime").getValue().toString());
                                                database.getReference().child("EmployeesHours").child(data.get(i).getId()+"").child(monthInt+"").
                                                        child("doneTime").setValue(differentBetweenTimes+oldDoneHours);

                                                listener.onIntent(data.get(i).getId() , monthInt,dayInt , "imageLeave");


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
    public void countOfHoursTheEmployeesMustWorkEveryMonth(int id, ArrayList<String> daysNames, int hoursCount) {
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
