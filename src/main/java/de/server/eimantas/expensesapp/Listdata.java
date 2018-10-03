package de.server.eimantas.expensesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import de.server.eimantas.expensesapp.dao.ExpenseDao;
import de.server.eimantas.expensesapp.entities.Expense;
import de.server.eimantas.expensesapp.helpers.ExpenseDatabase;

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
        ExpenseDao dao = ExpenseDatabase.get(getApplicationContext()).expenseDao();

        List<Expense> exp = dao.getAll();

        Log.i(TAG, "got " + exp.size() + " expenses");

        for (Expense e : exp) {

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
