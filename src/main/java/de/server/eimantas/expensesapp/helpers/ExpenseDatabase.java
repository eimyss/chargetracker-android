package de.server.eimantas.expensesapp.helpers;

import android.app.Activity;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import de.server.eimantas.expensesapp.dao.ExpenseDao;
import de.server.eimantas.expensesapp.entities.Expense;

@Database(entities = {Expense.class}, version = 1)
@TypeConverters({DataConverter.class})
public abstract class ExpenseDatabase extends RoomDatabase {
    public abstract ExpenseDao expenseDao();

    private static final String DB_NAME = "expenses.db";
    private static volatile ExpenseDatabase INSTANCE = null;

   public synchronized static ExpenseDatabase get(Context ctxt) {
        if (INSTANCE == null) {
            INSTANCE = create(ctxt, false);
        }

        return (INSTANCE);
    }

    static ExpenseDatabase create(Context ctxt, boolean memoryOnly) {
        RoomDatabase.Builder<ExpenseDatabase> b;

        if (memoryOnly) {
            b = Room.inMemoryDatabaseBuilder(ctxt.getApplicationContext(),
                    ExpenseDatabase.class);
        } else {
            b = Room.databaseBuilder(ctxt.getApplicationContext(), ExpenseDatabase.class,
                    DB_NAME).fallbackToDestructiveMigration().allowMainThreadQueries();
        }

        return (b.build());
    }


    public static AsyncTask<Void, Void, Expense> saveExense (Activity activity, Expense exp, Context ctx) {
      return new AgentAsyncTask(activity, exp, ctx).execute();
    }


    private static class AgentAsyncTask extends AsyncTask<Void, Void, Expense> {

        private Context context;
        //Prevent leak
        private WeakReference<Activity> weakActivity;
        private Expense expense;

        public AgentAsyncTask(Activity activity, Expense expense, Context ctx) {
            weakActivity = new WeakReference<>(activity);
            this.context = ctx;
            this.expense = expense;
        }

        @Override
        protected Expense doInBackground(Void... params) {
            ExpenseDao dao = INSTANCE.expenseDao();
            dao.insertAll(expense);
            return expense;
        }

        @Override
        protected void onPostExecute(Expense expense) {
            Activity activity = weakActivity.get();
            if(activity == null) {
                return;
            }

            if (expense == null) {
                //2: If it already exists then prompt user
                Toast.makeText(activity, "Expense was null", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(activity, "Expenese was saved " + expense.getId(), Toast.LENGTH_LONG).show();
                activity.onBackPressed();
            }
        }
    }
}
