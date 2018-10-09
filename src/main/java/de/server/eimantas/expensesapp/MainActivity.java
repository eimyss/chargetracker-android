package de.server.eimantas.expensesapp;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import de.server.eimantas.expensesapp.entities.Project;

public class MainActivity extends AppCompatActivity {

    EditText txtTime, logoutTime;
    boolean started = false;
    TextView sharedServerText;
    Button btnTimePicker, btnLogout;
    private static final String TAG = "MyActivity";
    Spinner spinner;
    private int mLogoutMinute, mLogoutHour, mHour, mMinute, mLoginMinute, mLoginHour;
    public static final String EXTRA_MESSAGE = "de.server.eimantas.expensesapp.MESSAGE";
    public static final String PROJECT_ID = "de.server.eimantas.expensesapp.PROJECT_ID";
    public static final String BOOKING_START = "de.server.eimantas.expensesapp.BOOKING_START";
    public static final String BOOKING_END = "de.server.eimantas.expensesapp.BOOKING_END";
    private Button btnSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtTime = (EditText) findViewById(R.id.txtTime);
        spinner = (Spinner) findViewById(R.id.spinner);
        logoutTime = (EditText) findViewById(R.id.logout_time);
        btnTimePicker = (Button) findViewById(R.id.btnTimePicker);
        btnSend = (Button) findViewById(R.id.button_send);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        List<Project> list = new ArrayList<Project>();
        Project p = new Project(1, "No Project");
        list.add(p);
        ArrayAdapter<Project> dataAdapter = new ArrayAdapter<Project>(this,
                android.R.layout.simple_spinner_item, list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                // Replace text with my own
                view.setText(getItem(position).getName());
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                TextView label = (TextView) super.getDropDownView(position, convertView, parent);
                label.setTextColor(Color.RED);
                label.setText(getItem(position).getName());

                return label;
            }
        };

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        btnLogout.setEnabled(false);
        btnSend.setEnabled(false);
        deleteDB();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(PROJECT_ID, ((Project) spinner.getSelectedItem()).getId());
        intent.putExtra(BOOKING_END, LocalDateTime.now().toInstant(OffsetDateTime.now().getOffset()).toEpochMilli());
        LocalDateTime loginTime = LocalDateTime.now().with(LocalTime.of(mLoginHour, mLoginMinute));
        Log.i(TAG, "value for minute: " + mLoginMinute + " and hour: " + mLoginHour);
        Log.i(TAG, "Setting begin date " + loginTime.toString());
        intent.putExtra(BOOKING_START, loginTime.toInstant(OffsetDateTime.now().getOffset()).toEpochMilli());
        startActivity(intent);

    }

    public void listData(View view) {
        Intent intent = new Intent(this, Listdata.class);
        startActivity(intent);
    }


    public void settingsActivity(View view) {
        Intent intent = new Intent(this, SettingsActvity.class);
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
