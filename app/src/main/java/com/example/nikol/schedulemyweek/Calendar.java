package com.example.nikol.schedulemyweek;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

//TODO: event saving/loading, event notifications
public class Calendar extends AppCompatActivity {
    public static final String DAY_MESSAGE = "DAY_TO_SHOW";
    public static final long NOTIFICATION_DELAY = -10000;

    public static Day[] days;
    public static Calendar instance;
    public static PendingIntent currentAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        instance = this;
        days = loadDays();

    }

    public void showCalendar(View v) {
        Intent intent = new Intent(Calendar.this, CalendarView.class);
        startActivity(intent);
    }

    public void showSunday(View v) {      showDay(0); }
    public void showMonday(View v) {      showDay(1); }
    public void showTuesday(View v) {     showDay(2); }
    public void showWednesday(View v) {   showDay(3); }
    public void showThursday(View v) {    showDay(4); }
    public void showFriday(View v) {      showDay(5); }
    public void showSaturday(View v) {    showDay(6); }

    public void showDay(int day) {
        Intent intent = new Intent(Calendar.this, DayView.class);
        intent.putExtra(DAY_MESSAGE, day);
        startActivity(intent);
    }

    public static void addEvents(Event e, boolean[] eventDays) {
        for (int i = 0; i < eventDays.length; i++) {
            if (eventDays[i]) {
                days[i].addEvent(instance, e);
            }
            if (currentAlarm == null) {
                e.setAlarm(instance, i); //TODO This is wrong.
            }
        }
    }

    public void cancelAlarm(Context context) {
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (currentAlarm != null) {
            mgr.cancel(currentAlarm);
            currentAlarm = null;
        }
    }

    private void testAlarm() {
        Context context = Calendar.this;
        Intent intent = new Intent(context, NotifyEvent.class);
        PendingIntent alarm = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long time = System.currentTimeMillis() + 10000;//weekStart() + (day*24*60*60*1000) + (startHour*60*60*1000) + (startMinute*60*1000);
        //Toast.makeText(context, "t-" + (System.currentTimeMillis() - time), Toast.LENGTH_SHORT).show();
        // mgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, time,
        //        0, alarm);
        mgr.set(AlarmManager.RTC_WAKEUP, time, alarm);
    }

    public static void deleteEvents(Event e) {
        for (int i = 0; i < days.length; i++) {
            if (days[i].containsEvent(e)) {
                days[i].deleteEvent(instance, e);
            }
            //TODO if event deleted is current alarm, cancel alarm
            //TODO delete saved events
        }
    }

    private Day[] loadDays() {
        Day[] arr = new Day[7];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new Day(i);
        }
        return arr;
    }
}
