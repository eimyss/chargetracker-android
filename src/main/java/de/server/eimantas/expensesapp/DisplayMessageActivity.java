package de.server.eimantas.expensesapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import de.server.eimantas.expensesapp.entities.Booking;
import de.server.eimantas.expensesapp.helpers.BookingDatabase;

import static de.server.eimantas.expensesapp.MainActivity.BOOKING_END;
import static de.server.eimantas.expensesapp.MainActivity.BOOKING_START;
import static de.server.eimantas.expensesapp.MainActivity.EXTRA_MESSAGE;
import static de.server.eimantas.expensesapp.MainActivity.PROJECT_ID;

public class DisplayMessageActivity extends AppCompatActivity {

    private static final String TAG = DisplayMessageActivity.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.O)
    void saveStuff(Booking booking) {
        Log.i(TAG, "Saving Booking");
        long id = BookingDatabase.get(getApplicationContext()).expenseDao().insert(booking);
        Log.i(TAG, "saved.: " + booking.toString() + " witj id: " + id);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        int projectID = intent.getIntExtra(PROJECT_ID, -1);
        LocalDateTime end =
                Instant.ofEpochMilli(intent.getLongExtra(BOOKING_END, 0L)).atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime start =
                Instant.ofEpochMilli(intent.getLongExtra(BOOKING_START, 0L)).atZone(ZoneId.systemDefault()).toLocalDateTime();

        Booking booking = new Booking(0, message, start, end, projectID);

        Log.i(TAG, "Booking created: " + booking.toString());

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView);
        textView.setText(message);
        saveStuff(booking);

    }
}
