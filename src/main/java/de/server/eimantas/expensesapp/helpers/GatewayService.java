package de.server.eimantas.expensesapp.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import de.server.eimantas.expensesapp.R;
import de.server.eimantas.expensesapp.entities.Booking;
import de.server.eimantas.expensesapp.entities.Location;
import de.server.eimantas.expensesapp.helpers.serializers.LocalDateTimeAdapter;

public class GatewayService {

    private static final String TAG = "GatewayService";

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_TYPE = "Bearer";
    private final Context ctx;
    private final Response.ErrorListener errorListener;
    private final Response.Listener listener;


    public GatewayService(final Context applicationContext, final Response.Listener listener, final Response.ErrorListener errorListener) {
        this.ctx = applicationContext;
        this.listener = listener;
        this.errorListener = errorListener;
    }


    public String uploadBooking(final Booking[] booking) throws IOException, JSONException {

        Response.Listener loginListener = new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                // hier login war okay
                Log.i(TAG, "Gateway response is: " + response);
                try {
                    String token = JsonUtils.getValueFromToken("access_token", response);

                    Log.i(TAG, "Gateway Token is: " + token);
                    Gson gson = new GsonBuilder()
                            .setPrettyPrinting()
                            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                            .create();
                   String body = gson.toJson(booking);

                    Log.i(TAG, "Request body: " + body);
                    uploadDto(token, body);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener loginErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                // just delegate it...
                errorListener.onErrorResponse(error);
            }
        };

        KeyCloackHelper.login(ctx, loginListener, loginErrorListener);


        return "";

    }

    private void uploadDto(String token, String body) {

        SharedPreferences sharedPref = ctx.getSharedPreferences(ctx.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        //   HttpClient client = new DefaultHttpClient();
        String url = "https://" + sharedPref.getString(ctx.getString(R.string.pref_gateway_server), "") +
                ":" + sharedPref.getString(ctx.getString(R.string.pref_gateway_server_port), "") +
                "/bookings/save";
        Log.i(TAG, "test connection to URL: " + url);


        HashMap<String, String> headers = new HashMap<>();
        headers.put(AUTHORIZATION_HEADER, BEARER_TOKEN_TYPE + " " + token);

        RequestSender sender = new RequestSender(ctx);
        sender.sendRequest(url, headers, body, listener, errorListener);

    }

    public void getLocationList() throws IOException, JSONException {
        Response.Listener loginListener = new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                // hier login war okay
                Log.i(TAG, "Gateway response is: " + response);
                try {
                    String token = JsonUtils.getValueFromToken("access_token", response);
                    Log.i(TAG, "Gateway Token is: " + token);
                    getLocationsFromServer(token);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener loginErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                errorListener.onErrorResponse(error);
            }
        };

        KeyCloackHelper.login(ctx, loginListener, loginErrorListener);
    }

    private void getLocationsFromServer(String token) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(ctx.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        String url = sharedPref.getString(ctx.getString(R.string.pref_gateway_server), "") +
                ":" + sharedPref.getString(ctx.getString(R.string.pref_gateway_server_port), "") +
                "/projects/get/address/all";
        Log.i(TAG, "test connection to URL: " + url);
        HashMap<String, String> headers = new HashMap<>();
        Log.i(TAG, "test connection token: " + token);
        headers.put(AUTHORIZATION_HEADER, BEARER_TOKEN_TYPE + " " + token);
        headers.put("Content-Type", "application/json");
        RequestSender sender = new RequestSender(ctx);
        sender.sendGetRequest(url, headers, listener, errorListener);
    }


}
