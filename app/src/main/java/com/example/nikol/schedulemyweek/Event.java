package com.example.nikol.schedulemyweek;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

/**
 * Created by nikol on 7/29/2017.
 */

public class Event {

    public static final String NAME_TAG = "get_name";
    public static final String START_HOUR_TAG = "get_start_hour";
    public static final String START_MINUTE_TAG = "get_start_minute";
    public static final String END_HOUR_TAG = "get_end_hour";
    public static final String END_MINUTE_TAG = "get_end_minute";
    public static final String NOTES_TAG = "get_notes";
    public static final String NOTIFICATIONS_TAG = "get_notifications";


    public String name;
    public int startHour;
    public int startMinute;
    public int endHour;
    public int endMinute;
    public String notes;
    public boolean notifications;

    public void setStartTime(int hours, int minutes) {
        startHour = hours;
        startMinute = minutes;
    }

    public void setEndTime(int hours, int minutes) {
        endHour = hours;
        endMinute = minutes;
    }

    public String show() {
        int x = 0;
        //String s = name + " starts in " + x + " minutes.";
        String s = name + " starts at ";
        s += convert(startHour) + ":";
        s += (startMinute < 10 ? "0" : "") + startMinute;
        s += (startHour < 12 ? " am" : " pm");
        return s;
    }

    public String startTime() {
        return convert(startHour) + ":" + (startMinute < 10 ? "0" : "") + startMinute + (startHour < 12 ? " am" : " pm");
    }

    public String endTime() {
        return convert(endHour) + ":" + (endMinute < 10 ? "0" : "") + endMinute + (endHour < 12 ? " am" : " pm");
    }

    public String toString() {
        String s = "";
        s += name + "\n";
        s += notes + "\n";
        s += "Start Time:\t";
        s += convert(startHour) + ":" ;
        s += (startMinute<10 ? "0" : "") + startMinute;
        s += (startHour<12 ? " am" : " pm") + "\n";

        s += "End Time:  \t";
        s += convert(endHour) + ":";
        s += (endMinute<10 ? "0" : "") + endMinute;
        s += (endHour<12 ? " am" : " pm");
        return s;
    }

    private int convert(int num) {
        num %= 12;
        if (num == 0) num = 12;
        return num;
    }


    public void setAlarm(Context context, int day) {

        Intent intent = new Intent(context, NotifyEvent.class);
        PendingIntent alarm = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long time = weekStart() + (day*24*60*60*1000) + (startHour*60*60*1000) + (startMinute*60*1000);
        if (time - System.currentTimeMillis() < 0) time += 7*24*60*60*1000;
        Toast.makeText(context, "t-" + (System.currentTimeMillis() - time), Toast.LENGTH_SHORT).show();
        //System.out.println("Success" + time);
        mgr.set(AlarmManager.RTC_WAKEUP, time, alarm);
        Calendar.currentAlarm = alarm;
    }

    public long getTimeMillis() {
        long time = (startHour * 60 * 60 * 1000) + (startMinute * 60 * 1000);
        return time;
    }

    private long weekStart() {
        SystemClock.elapsedRealtime();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.clear(java.util.Calendar.MINUTE);
        cal.clear(java.util.Calendar.SECOND);
        cal.clear(java.util.Calendar.MILLISECOND);

        cal.set(java.util.Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        return cal.getTimeInMillis();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Event) {
            Event e = (Event) other;
            boolean nameB = name.equals(e.name);
            boolean notesB = notes.equals(e.notes);
            boolean startHourB = startHour == e.startHour;
            boolean startMinuteB = startMinute == e.startMinute;
            boolean endHourB = endHour == e.endHour;
            boolean endMinuteB = endMinute == e.endMinute;
            boolean notificationsB = notifications == e.notifications;

            return nameB && notesB && startHourB && startMinuteB &&
                    endHourB && endMinuteB && notificationsB;

        } else {
            return false;
        }
    }
}
