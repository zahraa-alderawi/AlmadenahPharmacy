package com.apps.almadenahpharmacy.ui.employeeRecords;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.apps.almadenahpharmacy.Adapters.EmployeeRecordersAdapter;
import com.apps.almadenahpharmacy.Models.Employee;
import com.apps.almadenahpharmacy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmployeeRecordsFragment extends Fragment {

    private EmployeeRecordsViewModel employeeRecordsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        employeeRecordsViewModel =
                ViewModelProviders.of(this).get(EmployeeRecordsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_employee_records, container, false);
        GridView gridEmpRecorders = root.findViewById(R.id.gridEmpRecorders);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<Employee> data = new ArrayList<>();
        final EmployeeRecordersAdapter adapter = new EmployeeRecordersAdapter(data, getActivity());
        gridEmpRecorders.setAdapter(adapter);

        database.getReference().child("Employees").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Employee employee = new Employee();
                    employee.setId(Integer.parseInt(snapshot.child("id").getValue().toString()));
                    employee.setName(snapshot.child("name").getValue().toString());
                    employee.setGender(snapshot.child("gender").getValue().toString());
                    employee.setDaysCount(Integer.parseInt(snapshot.child("daysCount").getValue().toString()));
                    employee.setHoursCount(Integer.parseInt(snapshot.child("hoursCount").getValue().toString()));
                    employee.setDaysNames((ArrayList<String>) snapshot.child("daysNames").getValue());
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
}