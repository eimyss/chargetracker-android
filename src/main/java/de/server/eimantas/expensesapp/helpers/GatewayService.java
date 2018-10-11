package de.server.eimantas.expensesapp.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;

import de.server.eimantas.expensesapp.R;
import de.server.eimantas.expensesapp.entities.Booking;

public class GatewayService {

    private static final String TAG = "GatewayService";

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_TYPE = "Bearer";

    public static String uploadBooking(Booking[] booking, Context applicationContext) throws IOException, JSONException {

        KeyCloackHelper.login(applicationContext, null,null);

        SharedPreferences sharedPref = applicationContext.getSharedPreferences(applicationContext.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        //   HttpClient client = new DefaultHttpClient();
        String url = "http://" + sharedPref.getString(applicationContext.getString(R.string.pref_gateway_server), "") +
                ":" + sharedPref.getString(applicationContext.getString(R.string.pref_gateway_server_port), "") +
                "/booking/add";
        Log.i(TAG, "test connection to URL: " + url);


        Gson gson = new Gson();
        String json = gson.toJson(booking);


        return "";

    }


}
