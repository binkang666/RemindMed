package com.example.cecs491project.ui.reminder;

import static com.example.cecs491project.ui.reminder.Day.Friday;
import static com.example.cecs491project.ui.reminder.Day.Monday;
import static com.example.cecs491project.ui.reminder.Day.Saturday;
import static com.example.cecs491project.ui.reminder.Day.Sunday;
import static com.example.cecs491project.ui.reminder.Day.Thursday;
import static com.example.cecs491project.ui.reminder.Day.Tuesday;
import static com.example.cecs491project.ui.reminder.Day.Wednesday;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.cecs491project.R;
import com.example.cecs491project.ui.medication.Medications;
import com.example.cecs491project.ui.notifications.Notification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

//TODO: user will probably need to select the medicine from the medication list.
//TODO: by selecting the everyday check box, all the box will be checked automatically.
public class addReminderActivity extends AppCompatActivity implements OnItemSelectedListener{
    EditText et_reminderNote;
    EditText reminderName;
    //ProgressDialog progressDialog;
    private Spinner medications;
    private ArrayList<Medications> medArrayList;
    private ArrayList<Day> daysInput;
    private ImageView ASClose ;
    private TextView SAVE ;
    private View Card_View ;
    private Button clockTime;
    private Button btnStartDate , btnEndDate ;
    public ArrayList<String> medNames;
    private ArrayList<CheckBox> daysCheckBoxes;
    public String startDate, endDate;
    int hour, minutes;
    String displayTime;
    ArrayAdapter<String> adapter;
    private String selectedItem;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(0);
        initializePage();
        EventChangeListener();
        medNames.add("Select Medication");
        showMedications();

        ASClose.setOnClickListener(v -> addReminderActivity.super.onBackPressed());
    }



    private void initializePage() {
        // stored reminder attributes
        // This is to create the drop down menu
        //progressDialog = new ProgressDialog(getBaseContext());
        et_reminderNote = findViewById(R.id.note);
        reminderName = findViewById(R.id.reminder_name_textInput);
        medications = findViewById(R.id.medication_spinner);
        startDate ="";
        endDate ="";
        selectedItem = "";

        daysInput = new ArrayList<>();
        medArrayList = new ArrayList<>();
        medNames = new ArrayList<>();
        daysCheckBoxes = new ArrayList<>();
        daysCheckBoxes.add(findViewById(R.id.dv_sunday));
        daysCheckBoxes.add(findViewById(R.id.dv_monday));
        daysCheckBoxes.add(findViewById(R.id.dv_tuesday));
        daysCheckBoxes.add(findViewById(R.id.dv_wednesday));
        daysCheckBoxes.add(findViewById(R.id.dv_thursday));
        daysCheckBoxes.add(findViewById(R.id.dv_friday));
        daysCheckBoxes.add(findViewById(R.id.dv_saturday));


        clockTime = findViewById(R.id.clockTime);
        ASClose = findViewById(R.id.ASClose);
        SAVE = findViewById(R.id.SAVE);
        Card_View = findViewById(R.id.Card_View);
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);

        SAVE.setZ(20);
        Card_View.setZ(2);
        ASClose.setZ(20);


    }


    private void addRemToDatabase(ArrayList<Day> days){
        SAVE.setOnClickListener(view -> {
            if(reminderName.getText().toString().trim() == ""){
                Toast.makeText(getApplicationContext(),"Please input a reminder name", Toast.LENGTH_SHORT).show();
            }
            else if(selectedItem == "Select Medication"){
                Toast.makeText(getApplicationContext(),"Please select a medication", Toast.LENGTH_SHORT).show();
            }
            else if(daysInput.size()==0){
                Toast.makeText(getApplicationContext(),"Please select days", Toast.LENGTH_SHORT).show();
            }
            else if(startDate == "" || endDate == ""){
                Toast.makeText(getApplicationContext(),"Please select a start and end date", Toast.LENGTH_SHORT).show();
            }
            else {
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Medications med1 = new Medications();
                String time = "";
                String note = "";
                String remName = "";
                try{
                    remName = reminderName.getText().toString().trim();
                }catch(Exception e) {
                    e.printStackTrace();
                }
                try {
                    note = et_reminderNote.getText().toString().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    med1 = medArrayList.get(medNames.indexOf(selectedItem)-1);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    time = displayTime;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DocumentReference userTablet = null;
                DocumentReference noti = null;
                if(FirebaseAuth.getInstance().getCurrentUser().isAnonymous()){
                    userTablet = db.collection("Anonymous User Database")
                            .document(userID).collection("Reminder").document(remName);
                    noti = db.collection("Anonymous User Database")
                            .document(userID).collection("Notification").document(remName);
                }
                else{
                    userTablet = db.collection("User Database")
                            .document(userID).collection("Reminder").document(remName);
                    noti = db.collection("User Database")
                            .document(userID).collection("Notification").document(remName);
                }
                try {
                    Reminder rem = new Reminder(remName, selectedItem, daysInput, time, startDate, endDate, note);
                    Notification notification = new Notification(remName, selectedItem, time, 0);
                    noti.set(notification);
                    userTablet.set(rem).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                makeSnackBarMessage("Reminder added");
                                //Toast toast2 = Toast.makeText(getApplicationContext(),
                                        //"Reminder" + daysInput.toString() + startDate + endDate
                                        //, Toast.LENGTH_SHORT);
                                //toast2.show();
                            } else {
                                makeSnackBarMessage("Failed");
                            }
                        }
                    });
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }); // end of save click listener

    }
    private void makeSnackBarMessage(String message){
        Snackbar.make(Card_View, message, Snackbar.LENGTH_SHORT).show();
    }

    private void EventChangeListener(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference collectionReference;
        if(FirebaseAuth.getInstance().getCurrentUser().isAnonymous()){
            collectionReference = db.collection("Anonymous User Database")
                    .document(uid).collection("Medication");
        }
        else{
            collectionReference = db.collection("User Database")
                    .document(uid).collection("Medication");
        }
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast toast = Toast.makeText(getApplicationContext(), "error occured grabbing from database", Toast.LENGTH_SHORT);
                    toast.show();
                }
                try {
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            Medications med = dc.getDocument().toObject(Medications.class);
                            medArrayList.add(med);
                            medNames.add(med.getMedicationName());
                            Log.d("MEDICATION", dc.getDocument().toObject(Medications.class).toString());
                            Log.d("MEDNAME:", dc.getDocument().toObject(Medications.class).getMedicationName());
                        }
                    }
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });
    }

    // TODO: Make the selection show up on the spinner.
    public void showMedications(){

        //medications.setOnItemSelectedListener(this);
        //medNames.removeAll(Collections.singleton(null));
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, medNames);
        adapter.notifyDataSetChanged();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medications.setAdapter(adapter);
        medications.setOnItemSelectedListener(this);

        //Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_SHORT).show();


    }
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        //Toast.makeText(getApplicationContext(), "Selected User: "+ arg0.getItemAtPosition(position).toString() ,Toast.LENGTH_SHORT).show();
        String text = arg0.getItemAtPosition(position).toString();
        selectedItem = medications.getSelectedItem().toString();
        if(text!="Select Medication")
        Toast.makeText(arg0.getContext(), "Selected medication: "+text,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO - Custom Code
        //((TextView) arg0.getChildAt(0)).setText("Select Medication");
    }
    public void onClickEveryDay(View view){
        int id = view.getId();
        TransitionManager.beginDelayedTransition((ConstraintLayout)findViewById(R.id.Card_View));
        CheckBox checkBox = (CheckBox) view;

        if(checkBox.isChecked()) {
            daysInput.addAll(Arrays.asList(Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday));
            for(CheckBox c: daysCheckBoxes){
                c.setChecked(false);
                c.setEnabled(false);
            }
            addRemToDatabase(daysInput);
        } else{
            daysInput.clear();
            for(CheckBox c: daysCheckBoxes){
                c.setEnabled(true);
            }
        }
    }
    // This is when day checkboxes are clicked and unclicked.
    public void onClickDays(View view){
        int id = view.getId();
        TransitionManager.beginDelayedTransition((ConstraintLayout)findViewById(R.id.Card_View));
        CheckBox checkBox = (CheckBox) view;


        if(checkBox.isChecked()) { // if they are checked
                if (id == R.id.dv_sunday && !daysInput.contains(Sunday)) {
                    //Log.d("DAYS:", "Clicked on Sunday");
                    daysInput.add(Sunday);
                    addRemToDatabase(daysInput);
                } else if (id == R.id.dv_monday && !daysInput.contains(Monday)) {
                    //Log.d("DAYS:", "Clicked on Monday");
                    daysInput.add(Monday);
                    addRemToDatabase(daysInput);
                } else if (id == R.id.dv_tuesday && !daysInput.contains(Tuesday)) {
                    //Log.d("DAYS:", "Clicked on Tuesday");
                    daysInput.add(Tuesday);
                    addRemToDatabase(daysInput);
                } else if (id == R.id.dv_wednesday && !daysInput.contains(Wednesday)) {
                    //Log.d("DAYS:", "Clicked on Wednesday");
                    daysInput.add(Wednesday);
                    addRemToDatabase(daysInput);
                } else if (id == R.id.dv_thursday && !daysInput.contains(Thursday)) {
                    daysInput.add(Thursday);
                    addRemToDatabase(daysInput);
                } else if (id == R.id.dv_friday && !daysInput.contains(Friday)) {
                    daysInput.add(Friday);
                    addRemToDatabase(daysInput);
                } else if (id == R.id.dv_saturday && !daysInput.contains(Saturday)) {
                    daysInput.add(Saturday);
                    addRemToDatabase(daysInput);
                }

        }
        else{ //unchecked
            if (id == R.id.dv_sunday && daysInput.contains(Sunday)) {
                //Log.d("DAYS:", "Clicked on Sunday");
                daysInput.remove(Sunday);
                addRemToDatabase(daysInput);
            } else if (id == R.id.dv_monday && daysInput.contains(Monday)) {
                //Log.d("DAYS:", "Clicked on Monday");
                daysInput.remove(Monday);
                addRemToDatabase(daysInput);
            } else if (id == R.id.dv_tuesday && daysInput.contains(Tuesday)) {
                //Log.d("DAYS:", "Clicked on Tuesday");
                daysInput.remove(Tuesday);
                addRemToDatabase(daysInput);
            } else if (id == R.id.dv_wednesday && daysInput.contains(Wednesday)) {
                //Log.d("DAYS:", "Clicked on Wednesday");
                daysInput.remove(Wednesday);
                addRemToDatabase(daysInput);
            } else if (id == R.id.dv_thursday && daysInput.contains(Thursday)) {
                daysInput.remove(Thursday);
                addRemToDatabase(daysInput);
            } else if (id == R.id.dv_friday && daysInput.contains(Friday)) {
                daysInput.remove(Friday);
                addRemToDatabase(daysInput);
            } else if (id == R.id.dv_saturday && daysInput.contains(Saturday)) {
                daysInput.remove(Saturday);
                addRemToDatabase(daysInput);
            }
        }
    }

    public void showDatePicker(View view){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        btnStartDate.setOnClickListener(view1 -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    addReminderActivity.this, (datePicker, year1, month1, day1) -> {
                        month1 = month1 +1;
                        String date2 = day1 + "/" + month1 + "/" + year1;

                        btnStartDate.setText(date2);
                        startDate = date2;
                    }, year, month, day);

            datePickerDialog.show();


        });

        btnEndDate.setOnClickListener(view12 -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    addReminderActivity.this, (datePicker, year12, month12, day12) -> {
                        month12 = month12 +1;
                        String date2 = day12 + "/" + month12 + "/" + year12;
                        btnEndDate.setText(date2);
                        endDate = date2;
                    }, year, month, day);
            datePickerDialog.show();

        });

    }

    public void onClickBeforeAfter( View view ){
        view.getResources().getColor(R.color.colorAccent);
    }

    public void showTimePicker( View view ){
        clockTime.setOnClickListener(view1 -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    addReminderActivity.this,
                    (timePicker, hr, min) -> {
                        hour = hr;
                        minutes = min;
                        String am_pm;
                        if (hour > 12)
                        {
                            hour = hour - 12;
                            am_pm = "PM";
                        } else {
                            am_pm = "AM";
                        }
                        if (minutes < 10){
                            String minutes_str = "0"+ minutes;
                            String time = hour + ":" +minutes_str +" " +am_pm;
                            clockTime.setText(time);
                            displayTime = time;
                        }
                        else{
                            String time = hour + ":" +minutes +" " +am_pm;
                            clockTime.setText(time);
                            displayTime = time;
                        }

                    }, 12, 0, false
            );
            timePickerDialog.updateTime(hour, minutes);
            timePickerDialog.show();
        });
    }

    //Hide input keypad after focus changed
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }




}
