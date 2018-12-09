package de.server.eimantas.expensesapp.helpers.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.Response;

import de.server.eimantas.expensesapp.helpers.GatewayService;

public class GetLocationsTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = GetLocationsTask.class.getSimpleName();
    private final Response.Listener successListener;
    private final Response.ErrorListener errorLister;
    private final Context context;

    private Exception exception;

    public GetLocationsTask(Context ctx, Response.Listener listener, Response.ErrorListener errorListener) {
        this.successListener = listener;
        this.context = ctx;
        this.errorLister = errorListener;
    }

    protected Void doInBackground(Void... voids) {
        try {
            GatewayService gw = new GatewayService(context, successListener, errorLister);
            gw.getLocationList();
        } catch (Exception e) {
            this.exception = e;
            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}