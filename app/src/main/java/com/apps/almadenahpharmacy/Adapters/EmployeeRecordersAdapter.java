package com.apps.almadenahpharmacy.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.apps.almadenahpharmacy.EmployeeDetailsActivity;
import com.apps.almadenahpharmacy.Models.Employee;
import com.apps.almadenahpharmacy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
        ImageView imageEmpRec = v.findViewById(R.id.imageEmpRec);
        TextView rowNameEmp = v.findViewById(R.id.rowNameEmp);
        ImageView butGoToEdit =  v.findViewById(R.id.butGoToEdit);
        ImageView butDelete =  v.findViewById(R.id.butDelete);
        rowNameEmp.setText(data.get(i).getName());
        if (data.get(i).getGender().equals("أنثى")){
            imageEmpRec.setImageResource(R.drawable.pharmacist_female);
        }
        else if (data.get(i).getGender().equals("ذكر")){
            imageEmpRec.setImageResource(R.drawable.pharmacist_male);
        }
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, EmployeeDetailsActivity.class);
                intent.putExtra("employee" , data.get(i));
                activity.startActivity(intent);
            }
        });
        butGoToEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View dialogView = LayoutInflater.from(activity).inflate(R.layout.row_edit_dialog, viewGroup, false);
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();

                final EditText editNameEditDialog = dialogView.findViewById(R.id.editNameEditDialog);
                final EditText editHoursCountEditDialog = dialogView.findViewById(R.id.editHoursCountEditDialog);
                final EditText editDaysCountEditDialog = dialogView.findViewById(R.id.editDaysCountEditDialog);
                final EditText editExtraHourPriceDialog = dialogView.findViewById(R.id.editExtraHourPriceDialog);

                final CheckBox checkSaturdayEditDialog = dialogView.findViewById(R.id.checkSaturdayEditDialog);
                final CheckBox checkSundayEditDialog = dialogView.findViewById(R.id.checkSundayEditDialog);
                final CheckBox checkMondayEditDialog = dialogView.findViewById(R.id.checkMondayEditDialog);
                final CheckBox checkTuesdayEditDialog = dialogView.findViewById(R.id.checkTuesdayEditDialog);
                final CheckBox checkWednesdayEditDialog = dialogView.findViewById(R.id.checkWednesdayEditDialog);
                final CheckBox checkThursdayEditDialog = dialogView.findViewById(R.id.checkThursdayEditDialog);

                final RadioGroup radioGroupEditDialog = (RadioGroup) dialogView.findViewById(R.id.radioGroupEditDialog);

                final ArrayList<String> days = data.get(i).getDaysNames();

                final int id = data.get(i).getId();

                editNameEditDialog.setText(data.get(i).getName());
                editHoursCountEditDialog.setText(data.get(i).getHoursCount() + "");
                editDaysCountEditDialog.setText(data.get(i).getDaysCount() + "");
                editExtraHourPriceDialog.setText(data.get(i).getExtraHourPrice() + "");

                if (data.get(i).getGender().equals("ذكر")) {
                    radioGroupEditDialog.check(R.id.radioButMaleEdit);
                } else if (data.get(i).getGender().equals("أنثى")) {
                    radioGroupEditDialog.check(R.id.radioButFemaleEdit);
                }

                for (int i = 0; i < days.size(); i++) {
                    if (days.get(i).equals("السبت")) {
                        checkSaturdayEditDialog.setChecked(true);
                    } else if (days.get(i).equals("الأحد")) {
                        checkSundayEditDialog.setChecked(true);
                    } else if (days.get(i).equals("الاثنين")) {
                        checkMondayEditDialog.setChecked(true);
                    } else if (days.get(i).equals("الثلاثاء")) {
                        checkTuesdayEditDialog.setChecked(true);
                    } else if (days.get(i).equals("الأربعاء")) {
                        checkWednesdayEditDialog.setChecked(true);
                    } else if (days.get(i).equals("الخميس")) {
                        checkThursdayEditDialog.setChecked(true);
                    }
                }

                Button buttonEditDialog = dialogView.findViewById(R.id.buttonEditDialog);
                buttonEditDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final ArrayList<String> days = new ArrayList<>();

                        if (checkSaturdayEditDialog.isChecked()) {
                            days.add("السبت");
                        }
                        if (checkSundayEditDialog.isChecked()) {
                            days.add("الأحد");
                        }
                        if (checkMondayEditDialog.isChecked()) {
                            days.add("الاثنين");
                        }
                        if (checkTuesdayEditDialog.isChecked()) {
                            days.add("الثلاثاء");
                        }
                        if (checkWednesdayEditDialog.isChecked()) {
                            days.add("الأربعاء");
                        }
                        if (checkThursdayEditDialog.isChecked()) {
                            days.add("الخميس");
                        }

                        if (days.size() > 0) {
                            int selectedId = radioGroupEditDialog.getCheckedRadioButtonId();
                            RadioButton genderRadioButton = (RadioButton) dialogView.findViewById(selectedId);
                            Employee employee = new Employee(id, editNameEditDialog.getText().toString(), Integer.parseInt(editHoursCountEditDialog.getText().toString()),
                                    Integer.parseInt(editDaysCountEditDialog.getText().toString()),days, genderRadioButton.getText().toString(),
                                    Integer.parseInt(editExtraHourPriceDialog.getText().toString()));
                            database.getReference().child("Employees").child(id + "").setValue(employee).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(activity, "تم تعديل الموظف بنجاح", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }
                            });
                        } else
                            Toast.makeText(activity, "يجب تحديد الأيام التي يداوم فيها الموظف", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        butDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                final Query applesQuery = ref.child("Employees").orderByChild("id").equalTo(data.get(i).getId());
                final View dialogView = LayoutInflater.from(activity).inflate(R.layout.row_delete_dialog, viewGroup, false);
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