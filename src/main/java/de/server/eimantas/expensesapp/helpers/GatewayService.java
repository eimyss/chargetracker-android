package de.server.eimantas.expensesapp.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import java.io.IOException;

import de.server.eimantas.expensesapp.R;
import de.server.eimantas.expensesapp.entities.Booking;

public class GatewayService {

    private static final String TAG = "GatewayService";

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_TYPE = "Bearer";

    public static String uploadBooking(Booking[] booking, Context applicationContext) throws IOException, JSONException {

        KeyCloackHelper.login(applicationContext);

        SharedPreferences sharedPref = applicationContext.getSharedPreferences(applicationContext.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        HttpClient client = new DefaultHttpClient();
        String url = "http://" + sharedPref.getString(applicationContext.getString(R.string.pref_gateway_server), "") +
                ":" + sharedPref.getString(applicationContext.getString(R.string.pref_gateway_server_port), "") +
                "/booking/add";
        Log.i(TAG, "test connection to URL: " + url);

        HttpPost request = new HttpPost(url);
        request.setHeader(AUTHORIZATION_HEADER, BEARER_TOKEN_TYPE + " " +
                sharedPref.getString(applicationContext.getString(R.string.access_token), ""));

        Gson gson = new Gson();
        String json = gson.toJson(booking);

        request.setEntity(new StringEntity(json));
        HttpResponse resp = client.execute(request);

        String response = IOUtils.toString(resp.getEntity().getContent());
        Log.d(TAG, "Got response " + response);

        return response;

    }


}
