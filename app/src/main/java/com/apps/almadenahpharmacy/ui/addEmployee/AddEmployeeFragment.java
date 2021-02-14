package com.apps.almadenahpharmacy.ui.addEmployee;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.apps.almadenahpharmacy.MainActivity;
import com.apps.almadenahpharmacy.Models.Employee;
import com.apps.almadenahpharmacy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddEmployeeFragment extends Fragment {

    private AddEmployeeViewModel galleryViewModel;
    FirebaseDatabase database;
    private TextInputEditText editNameAdd;
    private TextInputEditText editHoursCountAdd;
    private TextInputEditText editExtraHourPriceAdd;
    private AutoCompleteTextView spinnerComingHour;
    private AutoCompleteTextView spinnerLeftHour;
    private CheckBox checkSaturday;
    private CheckBox checkSunday;
    private CheckBox checkMonday;
    private CheckBox checkTuesday;
    private CheckBox checkWednesday;
    private CheckBox checkThursday;
    private RadioGroup radioGroupAdd;
    private RadioButton genderRadioButton;
    private MaterialButton addNewBtn;
    int lastKey ;
    int saturday;
    int sunday;
    int monday;
    int tuesday;
    int wednesday;
    int thursday;
    int allHours ;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(AddEmployeeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_add_employee, container, false);
        lastKey=0;
        allHours = 0;
        editNameAdd = root.findViewById(R.id.editNameAdd);
        editHoursCountAdd = root.findViewById(R.id.editHoursCountAdd);
        spinnerComingHour = root.findViewById(R.id.spinnerComingHour);
        spinnerLeftHour = root.findViewById(R.id.spinnerLeftHour);
        editExtraHourPriceAdd = root.findViewById(R.id.editExtraHourPriceAdd);
        checkSaturday = root.findViewById(R.id.checkSaturday);
        checkSunday = root.findViewById(R.id.checkSunday);
        checkMonday = root.findViewById(R.id.checkMonday);
        checkTuesday = root.findViewById(R.id.checkTuesday);
        checkWednesday = root.findViewById(R.id.checkWednesday);
        checkThursday = root.findViewById(R.id.checkThursday);
        radioGroupAdd = (RadioGroup) root.findViewById(R.id.radioGroupAdd);

        addNewBtn = root.findViewById(R.id.addNewBtn);
        database = FirebaseDatabase.getInstance();
        showHoursInSpinner();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final Query lastQuery = reference.child("Employees").orderByKey().limitToLast(1);
        lastQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                     lastKey = Integer.parseInt(  snap.getKey()+"");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<String> days = new ArrayList<>();
                int selectedId = radioGroupAdd.getCheckedRadioButtonId();
                genderRadioButton =  root.findViewById(selectedId);

                if (checkSaturday.isChecked()) {
                    days.add("السبت");
                }
                if (checkSunday.isChecked()) {
                    days.add("الأحد");
                }
                if (checkMonday.isChecked()) {
                    days.add("الاثنين");
                }
                if (checkTuesday.isChecked()) {
                    days.add("الثلاثاء");
                }
                if (checkWednesday.isChecked()) {
                    days.add("الأربعاء");
                }
                if (checkThursday.isChecked()) {
                    days.add("الخميس");
                }

                if (days.size() > 0 ) {
                    Employee employee = new Employee(lastKey + 1,
                            editNameAdd.getText().toString(),
                            Integer.parseInt(editHoursCountAdd.getText().toString()),
                            genderRadioButton.getText().toString(),
                            days,
                            Integer.parseInt(editExtraHourPriceAdd.getText().toString()),
                            spinnerComingHour.getText().toString(),spinnerLeftHour.getText().toString(),"","");

                    database.getReference().child("Employees").child((lastKey + 1) + "").setValue(employee).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), "تم إضافة الموظف بنجاح", Toast.LENGTH_SHORT).show();
                            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                            countOfHoursTheEmployeesMustWorkEveryMonth(lastKey, days, Integer.parseInt(editHoursCountAdd.getText().toString()));


                        }
                    });
                } else
                    Toast.makeText(getActivity(), "يجب تحديد الأيام التي يداوم فيها الموظف", Toast.LENGTH_SHORT).show();


            }
        });
        return root;

    }
    public  void countOfHoursTheEmployeesMustWorkEveryMonth( int id ,ArrayList<String> daysNames, int hoursCount) {
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
            database.getReference().child("EmployeesHours").child(id+"").child(monthInt+"").child("doneTime").setValue(0);
            database.getReference().child("EmployeesHours").child(id+"").child(monthInt+"").child("requiredHours").setValue(allHours);
            database.getReference().child("EmployeesHours").child(id+"").child(monthInt+"").child("extraTime").setValue(0);


        } catch (ParseException e) {
            e.printStackTrace();
        }






    }
    public  void showHoursInSpinner(){
        ArrayList<String> hours = new ArrayList<>();
        for (int i = 1 ; i <= 24 ; i++){
            hours.add(i+":00");
        }

        ArrayAdapter adapterHours = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, hours);
        adapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerComingHour.setAdapter(adapterHours);
        spinnerLeftHour.setAdapter(adapterHours);


    }



}