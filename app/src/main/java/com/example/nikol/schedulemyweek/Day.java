package com.example.nikol.schedulemyweek;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by nikol on 7/29/2017.
 */

public class Day {
    public String fileName;
    public String numEventsName;
    private int number;
    public List<Event> events;

    public Day(int day) {
        events = new ArrayList<Event>();
        fileName = "Schedule_Data_for_day_" + day;
        numEventsName = "Event_Num_for_day_" + day;
        number = day;
        load(Calendar.instance);
    }

    private void sortEvents() {
        events.sort(new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return (int) (e1.getTimeMillis() - e2.getTimeMillis());
            }
        });
    }

    public boolean containsEvent(Event e) {
        for (Event ev : events) {
            if (e.equals(ev)) {
                return true;
            }
            //System.out.println(ev.hashCode() + "==" + e.hashCode());
        }
        return false;
    }

    public void save(Context context) {
        SharedPreferences mainPrefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor mainEdit = mainPrefs.edit();
        mainEdit.putInt(numEventsName, events.size());
        mainEdit.commit();
        for (int i = 0; i < events.size(); i++) {
            saveEvent(events.get(i), mainPrefs, fileName + i);
        }
        //System.out.println("Events: " + mainPrefs.getInt(numEventsName, -1));
    }

    public void saveEvent(Event e, SharedPreferences prefs, String fileName) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(fileName + Event.NAME_TAG, e.name);
        editor.putInt(fileName + Event.START_HOUR_TAG, e.startHour);
        editor.putInt(fileName + Event.START_MINUTE_TAG, e.startMinute);
        editor.putInt(fileName + Event.END_HOUR_TAG, e.endHour);
        editor.putInt(fileName + Event.END_MINUTE_TAG, e.endMinute);
        editor.putString(fileName + Event.NOTES_TAG, e.notes);
        editor.putBoolean(fileName + Event.NOTIFICATIONS_TAG, e.notifications);
        System.out.println(e.show() + " has been saved.");
        editor.commit();
    }

    public void load(Context context) {
        SharedPreferences mainPrefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        //System.out.println("Loading events for day " + number);
        events = new ArrayList<Event>();

        for (int i = 0; i < mainPrefs.getInt(numEventsName, 0); i++) {
            Event e = loadEvent(mainPrefs, fileName + i);
            //System.out.println(e + " has been loaded.");
            events.add(e);
        }
    }

    public Event loadEvent(SharedPreferences prefs, String fileName) {

        Event e = new Event();
        e.name = prefs.getString(fileName + Event.NAME_TAG, "null");
        e.startHour = prefs.getInt(fileName + Event.START_HOUR_TAG, 0);
        e.startMinute = prefs.getInt(fileName + Event.START_MINUTE_TAG, 0);
        e.endHour = prefs.getInt(fileName + Event.END_HOUR_TAG, 0);
        e.endMinute = prefs.getInt(fileName + Event.END_MINUTE_TAG, 0);
        e.notes = prefs.getString(fileName + Event.NOTES_TAG, "null");
        e.notifications = prefs.getBoolean(fileName + Event.NOTIFICATIONS_TAG, false);
        return e;
    }

    public void addEvent(Context context, Event e) {
        events.add(e);
        sortEvents();
        save(context);
    }

    public void deleteEvent(Context context, Event e) {
        events.remove(e);

        SharedPreferences mainPrefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor mainEdit = mainPrefs.edit();
        mainEdit.putInt(numEventsName, events.size());
        mainEdit.commit();
        for (int i = 0; i < events.size(); i++) {
            SharedPreferences ePrefs = context.getSharedPreferences(fileName + i, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = ePrefs.edit();
            editor.clear();
            editor.commit();
        }
    }
}
