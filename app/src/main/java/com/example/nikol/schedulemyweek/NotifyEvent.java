package com.example.nikol.schedulemyweek;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import java.util.List;

/**
 * Created by nikol on 8/1/2017.
 */

public class NotifyEvent extends BroadcastReceiver {

    public static final int ID = 10931948;

    @Override
    public void onReceive(Context context, Intent intent) {
        notifyEvent(context, intent);
        setNextAlarm(context);
    }

    public static void test(Context context, String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        //Intent view = new Intent(context, DayView.class);
       // PendingIntent pending = PendingIntent.getBroadcast(context, 0, view, 0);;
        builder.setContentTitle("Upcoming Event!");
        builder.setContentText(text);
       // builder.setContentIntent(pending);
        builder.setSmallIcon(R.drawable.notification);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID, builder.build());
    }

    private void notifyEvent(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentTitle("Upcoming Event!");
        Event e = getEvent();
        builder.setContentText(getEvent().show());
        builder.setContentIntent(pending);
        builder.setSmallIcon(R.drawable.notification);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID, builder.build());
    }

    private Event getEvent() {
        //find current day
        //get day's events
        //find which event starts within the next 10 minutes.
        //return said event.
        long start = weekStart();
        long now = System.currentTimeMillis();
        int day = (int) ((now-start) / (24*60*60*1000));
        List<Event> events = Calendar.days[day].events;
        for (Event e : events) {
            long eventTime = start + (day * 24 * 60 * 60 * 1000) + (e.startHour * 60 * 60 * 1000) + (e.startMinute * 60 * 1000);
            long time = eventTime - now;
            if (time < 10 * 60 * 1000 && time > -10 * 60 * 1000) {
                return e;
            }
        }
        return null;
    }

    private long weekStart() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.clear(java.util.Calendar.MINUTE);
        cal.clear(java.util.Calendar.SECOND);
        cal.clear(java.util.Calendar.MILLISECOND);

        cal.set(java.util.Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        return cal.getTimeInMillis();
    }

    private void setNextAlarm(Context context) {
        Event e = getEvent();
        Event next = getNextEvent(e);

        long start = weekStart();
        long now = System.currentTimeMillis();
        int day = (int) ((now-start) / (24*60*60*1000));

        if (next == null) {
            e.setAlarm(context, day + 1);
        } else {
            next.setAlarm(context, day);
        }
    }

    private Event getNextEvent(Event event) {
        //find current day
        //get day's events
        //find which event starts within the next 10 minutes.
        //return said event.
        long start = weekStart();
        long now = System.currentTimeMillis();
        int day = (int) ((now-start) / (24*60*60*1000));
        List<Event> events = Calendar.days[day].events;
        long soonest = Integer.MAX_VALUE;
        Event soonestEvent = null;
        for (Event e : events) {
            long eventTime = start + (day * 24 * 60 * 60 * 1000) + (e.startHour * 60 * 60 * 1000) + (e.startMinute * 60 * 1000);
            long time = eventTime - now;
            if (time > 0 && time < soonest && e != event) {
                soonestEvent = e;
                soonest = time;
            }
        }
        return soonestEvent;
    }
}
