package com.example.nikol.schedulemyweek;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by nikol on 7/29/2017.
 */

public class DayView extends AppCompatActivity {

    private Day day;
    private int num;
    private View previousView;
    public static Event toBeEdited;
    private boolean started;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        Intent intent = getIntent();
        num = intent.getIntExtra(Calendar.DAY_MESSAGE, 0);
        day = Calendar.days[num];
        day.load(Calendar.instance);
        for (Event e : day.events) {
            addText(e);
            addEditButton(e);
            addDeleteButton(e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (started) {
            finish();
            startActivity(getIntent());
        } else {
            started = true;
        }
    }

    private void addText(Event e) {
        TextView v = new TextView(this);
        v.setText(e.toString());
        v.setBackgroundColor(Color.rgb(170,170,170));
        v.setId((int) (Math.random() * 100000));
        RelativeLayout lv = (RelativeLayout) findViewById(R.id.day_layout);
        int wrap = RelativeLayout.LayoutParams.WRAP_CONTENT;
        int match = RelativeLayout.LayoutParams.MATCH_PARENT;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(match, wrap);
        lp.setMargins(20, 10, 10, 0);
        if (previousView == null) {
            lp.addRule(RelativeLayout.BELOW, R.id.new_event);
        } else {
            lp.addRule(RelativeLayout.BELOW, previousView.getId());
        }

        lv.addView(v, lp);
        previousView = v;
        //System.out.println(R.id.new_event);
        //System.out.println(previousView.getId());
    }

    private void addEditButton(Event e) {
        Button b = new Button(this);
        b.setText("Edit");
        b.setId((int) (Math.random() * 100000));
        RelativeLayout lv = (RelativeLayout) findViewById(R.id.day_layout);
        int wrap = RelativeLayout.LayoutParams.WRAP_CONTENT;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(wrap, wrap);

        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        if (previousView == null) {
            lp.addRule(RelativeLayout.BELOW, R.id.new_event);
        } else {
            lp.addRule(RelativeLayout.ALIGN_BOTTOM, previousView.getId());
        }

        final Event myEvent = e;
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                edit(myEvent);
            }
        });

        lv.addView(b, lp);
        previousView = b;
        //System.out.println(b.getId());
    }

    private void edit(Event e) {
        toBeEdited = e;
        Intent intent = new Intent(DayView.this, EditEvent.class);
        startActivity(intent);
    }

    private void addDeleteButton(Event e) {
        Button b = new Button(this);
        b.setText("Delete");
        b.setId((int) (Math.random() * 100000));
        RelativeLayout lv = (RelativeLayout) findViewById(R.id.day_layout);
        int wrap = RelativeLayout.LayoutParams.WRAP_CONTENT;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(wrap, wrap);

        lp.addRule(RelativeLayout.LEFT_OF, previousView.getId());
        if (previousView == null) {
            lp.addRule(RelativeLayout.BELOW, R.id.new_event);
        } else {
            lp.addRule(RelativeLayout.ALIGN_BOTTOM, previousView.getId());
        }

        final Event myEvent = e;
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                delete(myEvent);
            }
        });
        lv.addView(b, lp);
        //System.out.println(b.getId());
    }

    private void delete(Event e) {
        final Event myEvent = e;
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        deleteAll(myEvent);
                        day.deleteEvent(Calendar.instance, myEvent);

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void deleteAll(Event e) {
        final Event myEvent = e;
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Calendar.deleteEvents(myEvent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
                onResume();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Would you like to delete all instances of this event?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void finish(View view) { finish(); }


    public void newEvent(View view) {
        Intent intent = new Intent(DayView.this, NewEvent.class);
        intent.putExtra(Calendar.DAY_MESSAGE, num);
        startActivity(intent);
    }
}
