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

import de.server.eimantas.expensesapp.entities.Expense;
import de.server.eimantas.expensesapp.helpers.ExpenseDatabase;

public class DisplayMessageActivity extends AppCompatActivity {

    private static final String TAG = DisplayMessageActivity.class.getSimpleName();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);


        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView);
        textView.setText(message);
        saveStuff(message, getApplicationContext());

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    static void saveStuff(String message, Context ctx) {
        Log.i(TAG, "Saving expense");
        Expense exp = new Expense();
        exp.setName(message);
        exp.setEndDate(LocalDateTime.now());
        exp.setStartdate(LocalDateTime.now());
        //  dao.insertAll(exp);

        Log.i(TAG, "saved.: " + exp.getName() + " witj id: " + exp.getId());
        long id = ExpenseDatabase.get(ctx).expenseDao().insert(exp);

        Log.i(TAG, "saved.: " + exp.toString() + " witj id: " + id);
    }
}
