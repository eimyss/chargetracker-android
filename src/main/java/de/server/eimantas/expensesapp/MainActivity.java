package de.server.eimantas.expensesapp;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import de.server.eimantas.expensesapp.entities.Location;
import de.server.eimantas.expensesapp.entities.Project;
import de.server.eimantas.expensesapp.helpers.adapters.DropDownLocationAdapter;
import de.server.eimantas.expensesapp.helpers.adapters.DropDownProjectsAdapter;
import de.server.eimantas.expensesapp.helpers.tasks.GetLocationsTask;

public class MainActivity extends AppCompatActivity {

    EditText txtTime, logoutTime;
    boolean started = false;
    TextView sharedServerText, elapsedTextView;
    Button btnTimePicker, btnLogout, loginButton;
    private static final String TAG = "MyActivity";
    Spinner projectSpinner,worklocationSpinner;
    private int mLogoutMinute, mLogoutHour, mHour, mMinute, mLoginMinute, mLoginHour;
    public static final String EXTRA_MESSAGE = "de.server.eimantas.expensesapp.MESSAGE";
    public static final String PROJECT_ID = "de.server.eimantas.expensesapp.PROJECT_ID";
    public static final String BOOKING_START = "de.server.eimantas.expensesapp.BOOKING_START";
    public static final String BOOKING_END = "de.server.eimantas.expensesapp.BOOKING_END";
    private Button btnSend;
    private List<Location> locationList;
    private Timer loginTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        elapsedTextView = (TextView) findViewById(R.id.time_elapse);
        txtTime = (EditText) findViewById(R.id.txtTime);
        projectSpinner = (Spinner) findViewById(R.id.project_dropdown);
        worklocationSpinner = (Spinner) findViewById(R.id.workplace_dropdown);
        logoutTime = (EditText) findViewById(R.id.logout_time);
        btnTimePicker = (Button) findViewById(R.id.btnTimePicker);
        btnSend = (Button) findViewById(R.id.button_send);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        loginButton = (Button) findViewById(R.id.start_login_button);

        List<Project> projectArrayList = new ArrayList<Project>();
        Project project = new Project(1, "No Project");
        projectArrayList.add(project);

       locationList = new ArrayList<Location>();
        Location location = new Location();
        location.setId(2);
        location.setName("No Location");
        locationList.add(location);

        ArrayAdapter<Project> dataAdapter = new DropDownProjectsAdapter(this, projectArrayList);
        projectSpinner.setAdapter(dataAdapter);

        ArrayAdapter<Location> locationArrayAdapter = new DropDownLocationAdapter(this,locationList);
        worklocationSpinner.setAdapter(locationArrayAdapter);


        btnLogout.setEnabled(false);
        btnSend.setEnabled(false);
        deleteDB();


    }

    private void setError(String message) {
        Toast.makeText(getApplicationContext(), "error " + message, Toast.LENGTH_SHORT).show();
    }

    private void updateLocations(String response) {
        Log.i(TAG, "Got Response " + response);
        try {

            Type listType = new TypeToken<List<Location>>() {}.getType();

            Gson g = new Gson();
            List<Location> locations = g.fromJson(response, listType);
            Log.i(TAG,"Parsed: " + locations.toString());
            locationList.addAll(locations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void fetchDate (View view) {
        Response.Listener listener = new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Got response " + response);
                updateLocations(response);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                setError(error.toString());
            }
        };

        new GetLocationsTask(getApplicationContext(),listener,errorListener).execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(PROJECT_ID, ((Project) projectSpinner.getSelectedItem()).getId());
        intent.putExtra(BOOKING_END, LocalDateTime.now().toInstant(OffsetDateTime.now().getOffset()).toEpochMilli());
        LocalDateTime loginTime = LocalDateTime.now().with(LocalTime.of(mLoginHour, mLoginMinute,1,1));
        Log.i(TAG, "value for minute: " + mLoginMinute + " and hour: " + mLoginHour);
        Log.i(TAG, "Setting begin date " + loginTime.toString());
        intent.putExtra(BOOKING_START, loginTime.toInstant(OffsetDateTime.now().getOffset()).toEpochMilli());
        startActivity(intent);

    }

    public void listData(View view) {
        Intent intent = new Intent(this, Listdata.class);
        startActivity(intent);
    }



    public void startLogin(View view) {

        // if its started - stop
        if (loginTimer != null) {
            loginTimer.cancel();
            loginTimer = null;
            loginButton.setText("Start Login");
        } else {
            loginButton.setText("End Login");
            elapsedTextView.setText("Starting");
            final long startTime =  System.currentTimeMillis();
            loginTimer = new Timer();
            loginTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Long spentTime = System.currentTimeMillis() - startTime;

                            int seconds = (int) (spentTime / 1000) % 60 ;
                            int minutes = (int) ((spentTime / (1000*60)) % 60);
                            int hours   = (int) ((spentTime / (1000*60*60)) % 24);
                            elapsedTextView.setText("Hours: " + hours + " minutes: " + minutes + " seconds: " + seconds);
                        }
                    });

                }
            },0, 1000);
        }

    }

    public void settingsActivity(View view) {
        Intent intent = new Intent(this, SettingsActvity.class);
        startActivity(intent);
    }


    public void manuallyEnterActivity(View view) {
        Intent intent = new Intent(this, ManuallyAddBookingActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void selectTimePicker(View view) {

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, 0,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        txtTime.setText(hourOfDay + ":" + minute);
                        started = true;
                        btnLogout.setEnabled(true);
                        mLoginMinute = minute;
                        mLoginHour = hourOfDay;
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setLogouTime(View view) {

        LocalDateTime loginTime = LocalDateTime.now().with(LocalTime.of(mLoginHour, mLoginMinute));
        long minutes = Duration.between(loginTime, LocalDateTime.now()).toMinutes();

        logoutTime.setText("Hours " + minutes / 60 + " Minutes " + (minutes % 60));
        btnTimePicker.setEnabled(true);
        btnLogout.setEnabled(false);
        btnSend.setEnabled(true);

    }


    private void deleteDB() {
        getApplicationContext().deleteDatabase("expenses.db");
    }

}
