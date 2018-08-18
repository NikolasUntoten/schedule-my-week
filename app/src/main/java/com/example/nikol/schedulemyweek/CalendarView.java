package com.example.nikol.schedulemyweek;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by nikol on 9/11/2017.
 */

public class CalendarView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        ImageView view = new ImageView(this);


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = 2300;//metrics.widthPixels;
        int height = 3000;//metrics.heightPixels;


        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(image);
        Paint paint = new Paint();
        paint.setColor(Color.rgb(50, 50, 50));
        paint.setTextSize(30);

        Paint namePaint = new Paint();
        namePaint.setColor(Color.rgb(0, 0, 0));
        namePaint.setTextSize(40);

        Paint titlePaint = new Paint();
        titlePaint.setColor(Color.rgb(0, 0, 0));
        titlePaint.setTextSize(55);

        //DRAWING AREA********************
        //c.drawRect(0, 0, 100, 100, paint);
        int buffer = 200;
        c.drawLine(0, 0, 0, height, paint);
        c.drawLine(buffer, 0, buffer, height, paint);


        int w = width - buffer;
        for (int i = 0; i < 7; i++) {
            c.drawLine(buffer + i*w/7, 0, buffer + i*w/7, height, paint);
        }

        int h = height - buffer;
        for (int i = 0; i < 24; i++) {
            c.drawLine(0, buffer + i*h/24, width, buffer + i*h/24, paint);
        }

        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for (int i = 0; i < 7; i++) {
            int offset = getOffset(days[i], w/7, titlePaint);
            c.drawText(days[i], buffer + i*w/7 + offset, buffer/2, titlePaint);
        }

        int offset = getOffset("12:00 PM", buffer, namePaint);
        c.drawText("12:00 " + "AM", offset, buffer + h/35, namePaint);
        c.drawText("12:00 " + "PM", offset, h/2 + buffer + h/35, namePaint);
        for (int i = 1; i < 12; i++) {
            offset = getOffset(i + ":00 PM", buffer, namePaint);
            c.drawText(i + ":00 " + "AM", offset, buffer + i*h/24 + h/35, namePaint);
            c.drawText(i + ":00 " + "PM", offset, h/2 + buffer + i*h/24 + h/35, namePaint);
        }

        drawEvents(c, buffer, w, h, namePaint, paint);

        //********************************
        view.setImageBitmap(image);

        view.setAdjustViewBounds(true);
        view.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(view);

        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(layout);
        HorizontalScrollView horizScrollView = new HorizontalScrollView(this);
        horizScrollView.addView(scrollView);
        setContentView(horizScrollView);
    }

    private void drawEvents(Canvas c, int buffer, int w, int h, Paint namePaint, Paint descPaint) {
        for (int i = 0; i < 7; i++) {
            Day d = Calendar.days[i];
            for (Event e : d.events) {
                drawEvent(c, e, buffer, w, h, i, namePaint, descPaint);
            }
        }
    }

    private void drawEvent(Canvas c, Event e, int buffer, int w, int h, int day, Paint namePaint, Paint descPaint) {
        Paint backPaint = new Paint();
        backPaint.setColor(Color.rgb(100, 200, 100));
        int x1 = buffer + day*w/7;
        int x2 = x1 + w/7;
        int y1 = buffer + e.startHour*h/24 + e.startMinute*h/24/60;
        int y2 = buffer + e.endHour*h/24 + e.endMinute*h/24/60;
        c.drawRect(x1, y1, x2, y2, backPaint);

        int offset = getOffset(e.name, w/7, namePaint);
        c.drawText(e.name, buffer + day*w/7 + offset, y1 + namePaint.getTextSize(), namePaint);

        String description = e.startTime() + " - " + e.endTime();
        offset = getOffset(description, w/7, descPaint);
        c.drawText(description, buffer + day*w/7 + offset, y2 - 10, descPaint);
    }

    private int getOffset(String str, int max, Paint paint) {
        int textSize = (int) paint.measureText(str);
        int offset = (max - textSize) / 2;
        return offset;
    }
}
