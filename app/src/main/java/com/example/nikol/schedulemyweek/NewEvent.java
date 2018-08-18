package com.example.nikol.schedulemyweek;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

/**
 * Created by nikol on 7/30/2017.
 */

public class NewEvent extends AppCompatActivity{

    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        Intent intent = getIntent();
        num = intent.getIntExtra(Calendar.DAY_MESSAGE, 0);
        preCheck();
    }

    private void preCheck() {
        int id = -1;
        switch (num) {
            case 0: id = R.id.sun_bool;
                break;
            case 1: id = R.id.mon_bool;
                break;
            case 2: id = R.id.tues_bool;
                break;
            case 3: id = R.id.wed_bool;
                break;
            case 4: id = R.id.thurs_bool;
                break;
            case 5: id = R.id.fri_bool;
                break;
            case 6: id = R.id.sat_bool;
                break;
        }
        AppCompatCheckBox dayBox = (AppCompatCheckBox) findViewById(id);
        dayBox.setChecked(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void done(View view) {
        EditText nameInput = (EditText) findViewById(R.id.edit_text_name);
        EditText notesInput = (EditText) findViewById(R.id.edit_text_notes);
        TimePicker startInput = (TimePicker) findViewById(R.id.start_time);
        TimePicker endInput = (TimePicker) findViewById(R.id.end_time);
        AppCompatCheckBox notificationsInput = (AppCompatCheckBox) findViewById(R.id.notification_bool);

        Event e = new Event();
        e.name = nameInput.getText().toString();
        e.notes = notesInput.getText().toString();
        e.setStartTime(startInput.getHour(), startInput.getMinute());
        e.setEndTime(endInput.getHour(), endInput.getMinute());
        e.notifications = notificationsInput.isChecked();

        Calendar.addEvents(e, getDayArr());
        finish();
    }

    private boolean[] getDayArr() {
        boolean[] days = new boolean[7];
        days[0] = ((AppCompatCheckBox) findViewById(R.id.sun_bool)).isChecked();
        days[1] = ((AppCompatCheckBox) findViewById(R.id.mon_bool)).isChecked();
        days[2] = ((AppCompatCheckBox) findViewById(R.id.tues_bool)).isChecked();
        days[3] = ((AppCompatCheckBox) findViewById(R.id.wed_bool)).isChecked();
        days[4] = ((AppCompatCheckBox) findViewById(R.id.thurs_bool)).isChecked();
        days[5] = ((AppCompatCheckBox) findViewById(R.id.fri_bool)).isChecked();
        days[6] = ((AppCompatCheckBox) findViewById(R.id.sat_bool)).isChecked();
        return days;
    }
}
