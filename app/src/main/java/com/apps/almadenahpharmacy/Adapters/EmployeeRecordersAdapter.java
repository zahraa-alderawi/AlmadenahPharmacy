package com.apps.almadenahpharmacy.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.apps.almadenahpharmacy.EditEmployeeActivity;
import com.apps.almadenahpharmacy.Models.Employee;
import com.apps.almadenahpharmacy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmployeeRecordersAdapter extends BaseAdapter {
    ArrayList<Employee> data;
    Activity activity;
//try to make new commit

    public EmployeeRecordersAdapter(ArrayList<Employee> data, Activity activity) {
        this.data = data;
        this.activity = activity;
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
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        final View v = LayoutInflater.from(activity).inflate(R.layout.raw_record, null, false);
        TextView rowNameEmp = v.findViewById(R.id.rowNameEmp);
        Button butGoToEdit =  v.findViewById(R.id.butGoToEdit);
        Button butDelete =  v.findViewById(R.id.butDelete);
        rowNameEmp.setText(data.get(i).getName());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        butGoToEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, EditEmployeeActivity.class);
                intent.putExtra("Employee",data.get(i));
                activity.startActivity(intent);
            }
        });
        butDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                final Query applesQuery = ref.child("Employees").orderByChild("id").equalTo(data.get(i).getId());
                final View dialogView = LayoutInflater.from(activity).inflate(R.layout.my_dialog, viewGroup, false);
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                Button buttonDelDialog = dialogView.findViewById(R.id.buttonDelDialog);
                Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
                buttonDelDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                });
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();

                    }
                });

            }
        });



        return v;
    }
}