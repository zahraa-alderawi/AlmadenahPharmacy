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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HomeFridaysRegisterAdapter extends BaseAdapter {
    ArrayList<Employee> data;
    Activity activity;
    OnIntentReceived listener;



    public HomeFridaysRegisterAdapter(ArrayList<Employee> data, Activity activity , OnIntentReceived listener) {
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
                FirebaseDatabase database = FirebaseDatabase.getInstance();


              //  long timeStamp = System.currentTimeMillis();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.US);
                String currentDateAndTime = format.format(new Date());
                try {
                    Date date = format.parse(currentDateAndTime);
                    final String year = (String) android.text.format.DateFormat.format("yyy", date);
                    final int yearInt = Integer.parseInt(year);
                    final String month = (String) DateFormat.format("MM", date);
                    final int monthInt = Integer.parseInt(month);
                    final String day = (String) DateFormat.format("dd", date);
                    final int dayInt = Integer.parseInt(day);

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.DAY_OF_MONTH, dayInt); //Set Day of the Month, 1..31
                    cal.set(Calendar.MONTH, monthInt - 1); //Set month, starts with JANUARY = 0
                    cal.set(Calendar.YEAR, yearInt);

                    int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                    if (dayOfWeek == 6) {

                    Shift shift = new Shift(data.get(i).getId(),data.get(i).getName(), monthInt, dayInt, 0, 0, "", "", 0,0);
                    final String shiftKey = database.getReference().child("FridaysShifts").child(data.get(i).getId() + "").push().getKey();
                    database.getReference().child("FridaysShifts").child(data.get(i).getId() + "").child(shiftKey).setValue(shift);
                    database.getReference().child("FridaysShifts").child(data.get(i).getId() + "").child(shiftKey + "").child("timeStampComing").setValue(ServerValue.TIMESTAMP);
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
                    database.getReference().child("Employees").child(data.get(i).getId() + "").child("lastShiftFriday").setValue(shiftKey);

                    listener.onIntent(data.get(i).getId(), shiftKey, "imageComing");

                    // String time= (String) DateFormat.format("HH:mm", date);

                }
                    else {
                        Toast.makeText(activity,   "اليوم ليس يوم الجمعة", Toast.LENGTH_SHORT).show();
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }



            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                if (data.get(i).getLastShiftFriday().equals("")){
                    Toast.makeText(activity, "لم تقم بعد بتسجيل حضورك يوم الجمعة في هذا التطبيق!", Toast.LENGTH_SHORT).show();
                }
              //  final long timeStamp = System.currentTimeMillis();
              /*  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS'Z'");
                    database.getReference().child("Employees").child(data.get(i).getId()+"").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String shiftKey = dataSnapshot.child("lastShiftFriday").getValue().toString();
                            database.getReference().child("FridaysShifts").child(data.get(i).getId()+"").child(shiftKey).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                        final String month ;
                                        int monthInt = 0;
                                        try {
                                            date = format.parse(currentDateAndTime);
                                            month= (String) DateFormat.format("MM", date);
                                            monthInt = Integer.parseInt(month);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        final int finalMonthInt = monthInt;
                                        database.getReference().child("FridaysShifts").child(data.get(i).getId()+"").child(shiftKey+"").child("timeStampLeave").setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                database.getReference().child("FridaysShifts").child(data.get(i).getId()+"").child(shiftKey+"").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        long leftTimeStamp = Long.parseLong(dataSnapshot.child("timeStampLeave").getValue().toString());
                                                        long timeStampComing = Long.parseLong(dataSnapshot.child("timeStampComing").getValue().toString());
                                                        final long differentBetweenTimes = leftTimeStamp - timeStampComing ;
                                                        final long differentMinutes =  (differentBetweenTimes/(1000*60));
                                                        database.getReference().child("FridaysShifts").child(data.get(i).getId()+"").child(shiftKey).child("shiftPeriod").setValue(differentMinutes);
                                                        database.getReference().child("EmployeesHours").child(data.get(i).getId()+"").child(finalMonthInt +"").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                database.getReference().child("FridaysShifts").child(data.get(i).getId()+"").child(shiftKey).child("shiftPeriod").setValue(differentMinutes);
                                                                double shiftPrice = (12.5/60) * differentMinutes ;
                                                                database.getReference().child("FridaysShifts").child(data.get(i).getId()+"").child(shiftKey).child("shiftPrice").setValue(shiftPrice);

                                                                listener.onIntent(data.get(i).getId() , shiftKey , "imageLeave");
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
                    }); */
                else {
                    database.getReference().child("Employees").child(data.get(i).getId()+"").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String shiftKey = dataSnapshot.child("lastShiftFriday").getValue().toString();
                            database.getReference().child("FridaysShifts").child(data.get(i).getId()+"").child(shiftKey).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                        database.getReference().child("FridaysShifts").child(data.get(i).getId()+"").child(shiftKey+"").child("timeStampLeave").setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                database.getReference().child("FridaysShifts").child(data.get(i).getId()+"").child(shiftKey+"").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        database.getReference().child("FridaysShifts").child(data.get(i).getId()+"").child(shiftKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                long leftTimeStamp = Long.parseLong(dataSnapshot.child("timeStampLeave").getValue().toString());
                                                                long timeStampComing = Long.parseLong(dataSnapshot.child("timeStampComing").getValue().toString());
                                                                final long differentBetweenTimes = leftTimeStamp - timeStampComing ;
                                                                final long differentMinutes =  (differentBetweenTimes/(1000*60));
                                                                database.getReference().child("FridaysShifts").child(data.get(i).getId()+"").child(shiftKey).child("shiftPeriod").setValue(differentMinutes);
                                                                database.getReference().child("EmployeesHours").child(data.get(i).getId()+"").child(finalMonthInt +"").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        database.getReference().child("FridaysShifts").child(data.get(i).getId()+"").child(shiftKey).child("shiftPeriod").setValue(differentMinutes);
                                                                        float shiftPrice = (float) ((12.5/60) * differentMinutes);
                                                                        database.getReference().child("FridaysShifts").child(data.get(i).getId()+"").child(shiftKey).child("shiftPrice").setValue(shiftPrice);


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
