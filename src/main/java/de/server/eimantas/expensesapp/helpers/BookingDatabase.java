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

import de.server.eimantas.expensesapp.dao.BookingDao;
import de.server.eimantas.expensesapp.entities.Booking;

@Database(entities = {Booking.class}, version = 1)
@TypeConverters({DataConverter.class})
public abstract class BookingDatabase extends RoomDatabase {
    public abstract BookingDao expenseDao();

    private static final String DB_NAME = "expenses.db";
    private static volatile BookingDatabase INSTANCE = null;

   public synchronized static BookingDatabase get(Context ctxt) {
        if (INSTANCE == null) {
            INSTANCE = create(ctxt, false);
        }

        return (INSTANCE);
    }

    static BookingDatabase create(Context ctxt, boolean memoryOnly) {
        RoomDatabase.Builder<BookingDatabase> b;

        if (memoryOnly) {
            b = Room.inMemoryDatabaseBuilder(ctxt.getApplicationContext(),
                    BookingDatabase.class).fallbackToDestructiveMigration().allowMainThreadQueries();
        } else {
            b = Room.databaseBuilder(ctxt.getApplicationContext(), BookingDatabase.class,
                    DB_NAME).fallbackToDestructiveMigration().allowMainThreadQueries();
        }

        return (b.build());
    }


    public static AsyncTask<Void, Void, Booking> saveExense (Activity activity, Booking exp, Context ctx) {
      return new AgentAsyncTask(activity, exp, ctx).execute();
    }


    private static class AgentAsyncTask extends AsyncTask<Void, Void, Booking> {

        private Context context;
        //Prevent leak
        private WeakReference<Activity> weakActivity;
        private Booking booking;

        public AgentAsyncTask(Activity activity, Booking booking, Context ctx) {
            weakActivity = new WeakReference<>(activity);
            this.context = ctx;
            this.booking = booking;
        }

        @Override
        protected Booking doInBackground(Void... params) {
            BookingDao dao = INSTANCE.expenseDao();
            dao.insertAll(booking);
            return booking;
        }

        @Override
        protected void onPostExecute(Booking booking) {
            Activity activity = weakActivity.get();
            if(activity == null) {
                return;
            }

            if (booking == null) {
                //2: If it already exists then prompt user
                Toast.makeText(activity, "Booking was null", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(activity, "Expenese was saved " + booking.getId(), Toast.LENGTH_LONG).show();
                activity.onBackPressed();
            }
        }
    }
}
