package com.example.cecs491project.ui.reminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cecs491project.R;

import java.util.Calendar;
import java.util.Objects;


//TODO: user will probably need to select the medicine from the medication list.
//TODO: by selecting the everyday check box, all the box will be checked automatically.
public class addReminderActivity extends AppCompatActivity  {

    private ImageView ASClose ;
    private TextView SAVE ;
    private View Card_View ;
    private Button clockTime;
    private Button btnStartDate , btnEndDate ;
    int hour, minutes;

    private Button tablet, capsule, drops, injections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(0);
        initializePage();


        ASClose.setOnClickListener(v -> addReminderActivity.super.onBackPressed());
    }


    private void initializePage() {
        clockTime = findViewById(R.id.clockTime);
        ASClose = findViewById(R.id.ASClose);
        SAVE = findViewById(R.id.SAVE);
        Card_View = findViewById(R.id.Card_View);
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);

        tablet = findViewById(R.id.ctg_Tablet);
        capsule = findViewById(R.id.ctg_Capsule);
        drops = findViewById(R.id.ctg_Drops);
        injections = findViewById(R.id.ctg_Injection);

        SAVE.setZ(20);
        Card_View.setZ(2);
        ASClose.setZ(20);


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
                    }, year, month, day);
            datePickerDialog.show();

        });

        btnEndDate.setOnClickListener(view12 -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    addReminderActivity.this, (datePicker, year12, month12, day12) -> {
                        month12 = month12 +1;
                        String date2 = day12 + "/" + month12 + "/" + year12;
                        btnEndDate.setText(date2);
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
                        }
                        else{
                            String time = hour + ":" +minutes +" " +am_pm;
                            clockTime.setText(time);
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
