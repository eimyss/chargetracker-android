package de.server.eimantas.expensesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import de.server.eimantas.expensesapp.dao.BookingDao;
import de.server.eimantas.expensesapp.entities.Booking;
import de.server.eimantas.expensesapp.helpers.BookingDatabase;

public class Listdata extends AppCompatActivity {


    private TableLayout table;
    private static final String TAG = Listdata.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listdata);

        table = (TableLayout) findViewById(R.id.table);
        pupulateItems();

    }

    private void pupulateItems() {

        Log.i(TAG, "populating stuff");
        BookingDao dao = BookingDatabase.get(getApplicationContext()).expenseDao();

        List<Booking> exp = dao.getAll();

        Log.i(TAG, "got " + exp.size() + " expenses");

        for (Booking e : exp) {

            TextView view = new TextView(getApplicationContext());
            view.setText("id: " + e.getId());


            TextView view2 = new TextView(getApplicationContext());
            view2.setText("name: " + e.getName());

            //TextView view3 = new TextView(getApplicationContext());
           // view.setText(e.toString());

            TableRow row = new TableRow(getApplicationContext());
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(layoutParams);


            row.addView(view);
            row.addView(view2);
            //row.addView(view3);

            table.addView(row);
        }

    }
}
