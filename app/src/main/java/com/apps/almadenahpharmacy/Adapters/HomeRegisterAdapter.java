package com.apps.almadenahpharmacy.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.apps.almadenahpharmacy.Models.Employee;
import com.apps.almadenahpharmacy.Models.Shift;
import com.apps.almadenahpharmacy.OnIntentReceived;
import com.apps.almadenahpharmacy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeRegisterAdapter extends BaseAdapter {
    ArrayList<Employee> data;
    Activity activity;
    OnIntentReceived listener;
    Uri uri;

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
        Button btnCome =  v.findViewById(R.id.btnCome);
        Button btnLeft =  v.findViewById(R.id.btnLeft);
        rowNameEmp.setText(data.get(i).getName());

        btnCome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               long timeStamp = System.currentTimeMillis();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateAndTime = format.format(new Date());
                try {
                    Date date = format.parse(currentDateAndTime);
                    String day= (String) DateFormat.format("MMM-dd", date);
                    String time= (String) DateFormat.format("HH:mm", date);
                    Shift shift = new Shift(time,timeStamp,"image1",null,0,null,null,0);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    database.getReference().child("Shifts").child(data.get(i).getId()+"").child(day+"").setValue(shift);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


               // Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //listener.onIntent(takePictureIntent,1);
            }
        });
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final long timeStamp = System.currentTimeMillis();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateAndTime = format.format(new Date());
                try {
                    Date date = format.parse(currentDateAndTime);
                    final String day= (String) DateFormat.format("MMM-dd", date);
                    final String time= (String) DateFormat.format("HH:mm", date);

                    database.getReference().child("Shifts").child(data.get(i).getId()+"")
                            .child(day+"").child("timeStampAttendance").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           long timeStampAttendance = Long.parseLong(dataSnapshot.getValue().toString());
                           long differentBetweenTimes = timeStamp - timeStampAttendance ;
                           long differentMinutes =  (differentBetweenTimes/(1000*60)); //timestamp to minutes
                        //   long differentMinutes =  (differentBetweenTimes/(1000*60)); timestamp to hours
                            int hours = (int) (differentMinutes / 60);
                            int minutes = (int) (differentMinutes % 60);

                            database.getReference().child("Shifts").child(data.get(i).getId()+"").child(day+"").child("timeLeave").setValue(time);
                            database.getReference().child("Shifts").child(data.get(i).getId()+"").child(day+"").child("imageLeave").setValue("image2");
                            database.getReference().child("Shifts").child(data.get(i).getId()+"").child(day+"").child("timeStampLeave").setValue(timeStamp+"");
                            database.getReference().child("Shifts").child(data.get(i).getId()+"").child(day+"").child("spendingTimeInWork").setValue(hours+":"+minutes);
                            database.getReference().child("Shifts").child(data.get(i).getId()+"").child(day+"").child("minutesCountInWork").setValue(differentMinutes);

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
