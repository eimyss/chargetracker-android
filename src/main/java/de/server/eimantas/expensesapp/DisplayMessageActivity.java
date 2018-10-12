package de.server.eimantas.expensesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import de.server.eimantas.expensesapp.entities.Booking;
import de.server.eimantas.expensesapp.helpers.BookingDatabase;
import de.server.eimantas.expensesapp.helpers.GatewayService;

import static de.server.eimantas.expensesapp.MainActivity.BOOKING_END;
import static de.server.eimantas.expensesapp.MainActivity.BOOKING_START;
import static de.server.eimantas.expensesapp.MainActivity.EXTRA_MESSAGE;
import static de.server.eimantas.expensesapp.MainActivity.PROJECT_ID;

public class DisplayMessageActivity extends AppCompatActivity {

    private static final String TAG = DisplayMessageActivity.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.O)
    Booking saveStuff(Booking booking) {
        Log.i(TAG, "Saving Booking");
        long id = BookingDatabase.get(getApplicationContext()).expenseDao().insert(booking);
        Log.i(TAG, "saved.: " + booking.toString() + " witj id: " + id);
        return BookingDatabase.get(getApplicationContext()).expenseDao().findById(id);
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
        Booking saved = saveStuff(booking);
        new SaveBookingTask().execute(saved);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void setSuccess(String booking) {
        Log.i(TAG, "entity received: " + booking);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(),formatter);
            }
        }).create();
        Booking received = gson.fromJson(booking,Booking.class);
        new UpdateBookingTask().execute(received);
    }

    void setError(String message) {

        // TODO parse json
        Log.i(TAG, "error entity received: " + message);
        Toast.makeText(getApplicationContext(), "error " + message, Toast.LENGTH_SHORT).show();
    }


    class SaveBookingTask extends AsyncTask<Booking, Void, Void> {

        private Exception exception;

        protected Void doInBackground(Booking... bookings) {
            try {
                Response.Listener listener = new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Got response " + response);
                        setSuccess(response);
                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", error.toString());
                        setError(error.toString());
                    }
                };

                GatewayService gw = new GatewayService(getApplicationContext(), listener, errorListener);
                gw.uploadBooking(bookings);


            } catch (Exception e) {
                this.exception = e;
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

    }

    class UpdateBookingTask extends AsyncTask<Booking, Void, Integer> {

        protected Integer doInBackground(Booking... bookings) {
            Log.i(TAG, "Updating bookings to set server backend id");
            Integer rows = BookingDatabase.get(getApplicationContext()).expenseDao().updateBookings(bookings);
            return rows;
        }

        protected void onPostExecute(Integer numOfRows) {
            Log.i(TAG, "Done updateing stuff " + numOfRows);
            Toast.makeText(getApplicationContext(), "Entity Saved in Backends Anzahl : " + numOfRows, Toast.LENGTH_LONG).show();
        }

    }

}
