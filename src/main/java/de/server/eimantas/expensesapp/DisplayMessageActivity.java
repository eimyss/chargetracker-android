package de.server.eimantas.expensesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.TimeZone;

import de.server.eimantas.expensesapp.entities.Booking;
import de.server.eimantas.expensesapp.helpers.BookingDatabase;

import static de.server.eimantas.expensesapp.MainActivity.BOOKING_END;
import static de.server.eimantas.expensesapp.MainActivity.BOOKING_START;
import static de.server.eimantas.expensesapp.MainActivity.EXTRA_MESSAGE;
import static de.server.eimantas.expensesapp.MainActivity.PROJECT_ID;

public class DisplayMessageActivity extends AppCompatActivity {

    private static final String TAG = DisplayMessageActivity.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.O)
    static void saveStuff(String message, Context ctx) {
        Log.i(TAG, "Saving expense");
        Booking exp = new Booking();
        exp.setName(message);
        exp.setEndDate(LocalDateTime.now());
        exp.setStartdate(LocalDateTime.now());
        //  dao.insertAll(exp);

        Log.i(TAG, "saved.: " + exp.getName() + " witj id: " + exp.getId());
        long id = BookingDatabase.get(ctx).expenseDao().insert(exp);

        Log.i(TAG, "saved.: " + exp.toString() + " witj id: " + id);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        int projectID = intent.getIntExtra(PROJECT_ID, 0);
        Calendar end = new Calendar.Builder().setInstant(intent.getLongExtra(BOOKING_END, 0L)).build();
        Calendar begin = new Calendar.Builder().setInstant(intent.getLongExtra(BOOKING_START, 0L)).build();
        TimeZone tz = begin.getTimeZone();
        ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
        Booking booking = new Booking(0, message, LocalDateTime.ofInstant(begin.toInstant(), zid), LocalDateTime.ofInstant(end.toInstant(), zid), projectID);

        Log.i(TAG,"Booking created: " + booking.toString());

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView);
        textView.setText(message);
        saveStuff(message, getApplicationContext());

    }
}
