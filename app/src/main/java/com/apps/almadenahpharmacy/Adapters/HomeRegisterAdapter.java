package com.apps.almadenahpharmacy.Adapters;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.almadenahpharmacy.EditEmployeeActivity;
import com.apps.almadenahpharmacy.Models.Employee;
import com.apps.almadenahpharmacy.OnIntentReceived;
import com.apps.almadenahpharmacy.R;

import java.util.ArrayList;

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

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                listener.onIntent(takePictureIntent,1);
            }
        });
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return v;

    }


}
