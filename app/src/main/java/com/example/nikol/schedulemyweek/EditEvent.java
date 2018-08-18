package com.example.nikol.schedulemyweek;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

/**
 * Created by nikol on 8/4/2017.
 */

public class EditEvent extends AppCompatActivity {

    private Event event;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        event = DayView.toBeEdited;
        load();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void load() {
        EditText nameInput = (EditText) findViewById(R.id.edit_text_name);
        EditText notesInput = (EditText) findViewById(R.id.edit_text_notes);
        TimePicker startInput = (TimePicker) findViewById(R.id.start_time);
        TimePicker endInput = (TimePicker) findViewById(R.id.end_time);
        AppCompatCheckBox notificationsInput = (AppCompatCheckBox) findViewById(R.id.notification_bool);

        nameInput.setText(event.name);
        notesInput.setText(event.notes);
        startInput.setHour(event.startHour);
        startInput.setMinute(event.startMinute);
        endInput.setHour(event.endHour);
        endInput.setMinute(event.endMinute);
        notificationsInput.setChecked(event.notifications);

        Day[] days = Calendar.days;
        ((AppCompatCheckBox) findViewById(R.id.sun_bool)).setChecked(days[0].containsEvent(event));
        ((AppCompatCheckBox) findViewById(R.id.mon_bool)).setChecked(days[1].containsEvent(event));
        ((AppCompatCheckBox) findViewById(R.id.tues_bool)).setChecked(days[2].containsEvent(event));
        ((AppCompatCheckBox) findViewById(R.id.wed_bool)).setChecked(days[3].containsEvent(event));
        ((AppCompatCheckBox) findViewById(R.id.thurs_bool)).setChecked(days[4].containsEvent(event));
        ((AppCompatCheckBox) findViewById(R.id.fri_bool)).setChecked(days[5].containsEvent(event));
        ((AppCompatCheckBox) findViewById(R.id.sat_bool)).setChecked(days[6].containsEvent(event));

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

        for (int i = 0; i < Calendar.days.length; i++) {
            Day d = Calendar.days[i];
            if (d.events.contains(event)) {
                d.events.remove(event);
            }
        }
        Calendar.deleteEvents(event);
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