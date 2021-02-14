package com.apps.almadenahpharmacy.Adapters;

import android.app.Activity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.apps.almadenahpharmacy.Models.Employee;
import com.apps.almadenahpharmacy.Models.Shift;
import com.apps.almadenahpharmacy.OnIntentReceived;
import com.apps.almadenahpharmacy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RegisterAdapter extends BaseAdapter {
    ArrayList<Employee> data;
    Activity activity;
    OnIntentReceived listener;

    public RegisterAdapter(ArrayList<Employee> data, Activity activity, OnIntentReceived listener) {
        this.data = data;
        this.activity = activity;
        this.listener = listener;
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
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                String currentDateAndTime = format.format(new Date());
                Date date ;
                String month ;
                String day ;
                int monthInt = 0;
                int dayInt  = 0;
                int dayOfWeek = 0;
                try {
                     date = format.parse(currentDateAndTime);
                     month= (String) DateFormat.format("MM", date);
                    day= (String) DateFormat.format("dd", date);
                    monthInt = Integer.parseInt(month);
                     dayInt = Integer.parseInt(day);
                    final String year = (String) android.text.format.DateFormat.format("yyy", date);
                    final int yearInt = Integer.parseInt(year);

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.DAY_OF_MONTH, dayInt); //Set Day of the Month, 1..31
                    cal.set(Calendar.MONTH, monthInt - 1); //Set month, starts with JANUARY = 0
                    cal.set(Calendar.YEAR, yearInt);

                     dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // بسبب اختلاف التوقيت الصيفي والشتوي
                /*SimpleDateFormat date2 = new SimpleDateFormat("z",Locale.getDefault());
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                        Locale.getDefault());
                Date currentLocalTime = calendar.getTime();
                final String localTimeGMT = date2.format(currentLocalTime); */
                if (dayOfWeek == 6) {
                    Toast.makeText(activity, "قم بتسجيلك حضورك من خيار: دوام يوم الجمعة في القائمة الجانبية", Toast.LENGTH_SHORT).show();

                }
                else {
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    if (data.get(i).getLastShift().equals("")){
                        Toast.makeText(activity, "تهانينا! لقد قمت بأول تسجيل حضور لك في هذا التطبيق", Toast.LENGTH_SHORT).show();
                        //Start new shift
                        Shift shift = new Shift(data.get(i).getId(),monthInt, dayInt,0,0,"","",0);
                        final String shiftKey = database.getReference().child("Shifts").child(data.get(i).getId()+"").push().getKey() ;
                        database.getReference().child("Shifts").child(data.get(i).getId()+"").child(shiftKey).setValue(shift);
                        database.getReference().child("Shifts").child(data.get(i).getId()+"").child(shiftKey+"").child("timeStampComing").setValue(ServerValue.TIMESTAMP);
                           /* .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            database.getReference().child("Shifts").child(data.get(i).getId()+"").child(shiftKey+"").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    long timeStampComing = Long.parseLong(dataSnapshot.child("timeStampComing").getValue().toString());
                                    if (localTimeGMT.equals("GMT+02:00")){
                                        database.getReference().child("Shifts").child(data.get(i).getId()+"").child(shiftKey).child("timeStampComing").setValue(timeStampComing+7.2e+6);
                                    }
                                    else if (localTimeGMT.equals("GMT+03:00")){
                                        database.getReference().child("Shifts").child(data.get(i).getId()+"").child(shiftKey).child("timeStampComing").setValue(timeStampComing+1.08e+7);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                            */
                        database.getReference().child("Employees").child(data.get(i).getId()+"").child("lastShift").setValue(shiftKey);

                        listener.onIntent(data.get(i).getId() ,shiftKey , "imageComing");

                    }
                    else {

                        final int finalMonthInt = monthInt;
                        final int finalDayInt = dayInt;
                        database.getReference().child("Shifts").child(data.get(i).getId()+"").child(data.get(i).getLastShift()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                long timeStampLeave = Long.parseLong(dataSnapshot.child("timeStampLeave").getValue().toString());
                                long timeStampComing = Long.parseLong(dataSnapshot.child("timeStampComing").getValue().toString());
                                if (timeStampLeave==0){
                                    //Default timeStampLeave for last shift
                                    if ( System.currentTimeMillis() >timeStampComing+((data.get(i).getHoursCount()-2)*3.6e+6)){
                                        Toast.makeText(activity,"لقد نسيت تسجيل الانصراف لليوم السابق، قمنا بتسجيل انصراف افتراضي لك ولم يحسب أي دوام اضافي", Toast.LENGTH_SHORT).show();
                                  /*  long timeStampLeaveAfterGMT = 0 ;
                                    if (localTimeGMT.equals("GMT+02:00")){
                                        timeStampLeaveAfterGMT = (long) (timeStampLeave+7.2e+6) ;
                                    }
                                    else if (localTimeGMT.equals("GMT+03:00")){
                                        timeStampLeaveAfterGMT = (long) (timeStampLeave+1.08e+7) ;
                                    }

                                   */
                                        database.getReference().child("Shifts").child(data.get(i).getId()+"").child(data.get(i).getLastShift()).child("timeStampLeave").setValue(timeStampComing+(data.get(i).getHoursCount()*3.6e+6));
                                        database.getReference().child("Shifts").child(data.get(i).getId()+"").child(data.get(i).getLastShift()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                long leftTimeStamp = Long.parseLong(dataSnapshot.child("timeStampLeave").getValue().toString());
                                                long timeStampComing = Long.parseLong(dataSnapshot.child("timeStampComing").getValue().toString());
                                                final long differentBetweenTimes = leftTimeStamp - timeStampComing ;
                                                long differentMinutes =  (differentBetweenTimes/(1000*60));
                                                database.getReference().child("Shifts").child(data.get(i).getId()+"").child(data.get(i).getLastShift()).child("shiftPeriod").setValue(differentMinutes);
                                                database.getReference().child("EmployeesHours").child(data.get(i).getId()+"").child(finalMonthInt +"").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        long oldDoneHours = Long.parseLong(dataSnapshot.child("doneTime").getValue().toString());
                                                        database.getReference().child("EmployeesHours").child(data.get(i).getId()+"").child(finalMonthInt+"").
                                                                child("doneTime").setValue(differentBetweenTimes+oldDoneHours);
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
                                    else {
                                        Toast.makeText(activity, "يجب أن تقوم بتسجيل انصرافك أولاً", Toast.LENGTH_SHORT).show();
                                    }





                                }
                                else if (timeStampLeave>0){
                                    //Start new shift
                                    Shift shift = new Shift(data.get(i).getId(),finalMonthInt, finalDayInt,0,0,"","",0);
                                    final String shiftKey = database.getReference().child("Shifts").child(data.get(i).getId()+"").push().getKey() ;
                                    database.getReference().child("Shifts").child(data.get(i).getId()+"").child(shiftKey).setValue(shift);
                                    database.getReference().child("Shifts").child(data.get(i).getId()+"").child(shiftKey+"").child("timeStampComing").setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            database.getReference().child("Shifts").child(data.get(i).getId()+"").child(shiftKey+"").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    long timeStampComing = Long.parseLong(dataSnapshot.child("timeStampComing").getValue().toString());
                                             /*   if (localTimeGMT.equals("GMT+02:00")){
                                                    database.getReference().child("Shifts").child(data.get(i).getId()+"").child(shiftKey).child("timeStampComing").setValue(timeStampComing+7.2e+6);
                                                }
                                                else if (localTimeGMT.equals("GMT+03:00")){
                                                    database.getReference().child("Shifts").child(data.get(i).getId()+"").child(shiftKey).child("timeStampComing").setValue(timeStampComing+1.08e+7);
                                                }*/
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    });
                                    database.getReference().child("Employees").child(data.get(i).getId()+"").child("lastShift").setValue(shiftKey);

                                    listener.onIntent(data.get(i).getId() ,shiftKey , "imageAttendance");

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                }

            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                if (data.get(i).getLastShift().equals("")){
                    Toast.makeText(activity, "لم تقم بعد بتسجيل حضورك في هذا التطبيق!", Toast.LENGTH_SHORT).show();
                }
                else {
                    database.getReference().child("Employees").child(data.get(i).getId()+"").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String shiftKey = dataSnapshot.child("lastShift").getValue().toString();
                            database.getReference().child("Shifts").child(data.get(i).getId()+"").child(shiftKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final long timeStampLeave =  Long.parseLong(dataSnapshot.child("timeStampLeave").getValue().toString());
                                    if (timeStampLeave>0){
                                        Toast.makeText(activity, "لقد قمت بتسجيل انصرافك لهذا اليوم\n نرجو تسجيل حضور جديد", Toast.LENGTH_LONG).show();
                                    }
                                    else if (timeStampLeave==0){

                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                                        String currentDateAndTime = format.format(new Date());
                                        Date date ;
                                        String month ;
                                        int monthInt = 0;
                                        try {
                                            date = format.parse(currentDateAndTime);
                                            month= (String) DateFormat.format("MM", date);
                                            monthInt = Integer.parseInt(month);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        final int finalMonthInt = monthInt;
                                        database.getReference().child("Shifts").child(data.get(i).getId()+"").child(shiftKey+"").child("timeStampLeave").setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                database.getReference().child("Shifts").child(data.get(i).getId()+"").child(shiftKey+"").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        database.getReference().child("Shifts").child(data.get(i).getId()+"").child(shiftKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                long leftTimeStamp = Long.parseLong(dataSnapshot.child("timeStampLeave").getValue().toString());
                                                                long timeStampComing = Long.parseLong(dataSnapshot.child("timeStampComing").getValue().toString());
                                                                final long differentBetweenTimes = leftTimeStamp - timeStampComing ;
                                                                long differentMinutes =  (differentBetweenTimes/(1000*60));
                                                                database.getReference().child("Shifts").child(data.get(i).getId()+"").child(shiftKey).child("shiftPeriod").setValue(differentMinutes);
                                                                database.getReference().child("EmployeesHours").child(data.get(i).getId()+"").child(finalMonthInt +"").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        long oldDoneHours = Long.parseLong(dataSnapshot.child("doneTime").getValue().toString());
                                                                        database.getReference().child("EmployeesHours").child(data.get(i).getId()+"").child(finalMonthInt+"").
                                                                                child("doneTime").setValue(differentBetweenTimes+oldDoneHours);
                                                                        listener.onIntent(data.get(i).getId() , shiftKey , "imageLeave");
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

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
///
                                                    }
                                                });






                                            }
                                        });






                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            }) ;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }

                }
        });




        return v;
    }
}
