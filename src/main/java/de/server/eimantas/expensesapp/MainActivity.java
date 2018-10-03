package de.server.eimantas.expensesapp;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import org.jboss.aerogear.android.core.Callback;

import java.time.Duration;
import java.util.Calendar;

import de.server.eimantas.expensesapp.helpers.KeyCloackHelper;

public class MainActivity extends AppCompatActivity {

    EditText txtTime, logoutTime;
    boolean started = false;
    Button btnTimePicker, btnLogout;
    private static final String TAG = "MyActivity";
    private int mLogoutMinute, mLogoutHour, mHour, mMinute, mLoginMinute, mLoginHour;
    public static final String EXTRA_MESSAGE = "de.server.eimantas.expensesapp.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtTime = (EditText) findViewById(R.id.txtTime);
        logoutTime = (EditText) findViewById(R.id.logout_time);
        btnTimePicker = (Button) findViewById(R.id.btnTimePicker);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setEnabled(false);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }



    public void listData(View view) {
        Intent intent = new Intent(this, Listdata.class);
        startActivity(intent);
    }

    public void startAuth() {
        Log.v(TAG, "starting auth");
        if (!KeyCloackHelper.isConnected()) {

            KeyCloackHelper.connect(MainActivity.this, new Callback() {
                        @Override
                        public void onSuccess(Object o) {
                            //   sendPhoto(UploadService.PROVIDERS.KEYCLOAK);
                            txtTime.setText("okay");
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                            txtTime.setText(" not okay " + e.getMessage());
                        }
                    }
            );

        } else {
            // sendPhoto(UploadService.PROVIDERS.KEYCLOAK);
            txtTime.setText("else");
        }
    }

    public void selectTimePicker(View view) {

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

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

        final Calendar c = Calendar.getInstance();
        mLogoutHour = c.get(Calendar.HOUR_OF_DAY);
        mLogoutMinute = c.get(Calendar.MINUTE);


        final Calendar cOrig = Calendar.getInstance();
        cOrig.set(Calendar.HOUR_OF_DAY, mLoginHour);
        cOrig.set(Calendar.MINUTE, mLoginMinute);

        long diff = c.getTimeInMillis() - cOrig.getTimeInMillis();


        long minutes = Duration.between(cOrig.toInstant(), c.toInstant()).toMinutes();

        logoutTime.setText("Hours " + minutes / 60 + " Minutes " + (minutes % 60));

    }

}
