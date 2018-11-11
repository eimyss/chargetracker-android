package de.server.eimantas.expensesapp.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import de.server.eimantas.expensesapp.R;

public class KeyCloackHelper {

    private static final String TAG = "KeycloackHelper";

    public static void login(final Context appContext, Response.Listener listener, Response.ErrorListener errorListener) throws IOException, JSONException {

        final SharedPreferences sharedPref = appContext.getSharedPreferences(appContext.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        //ttpClient client = new DefaultHttpClient();
        String url = "https://" + sharedPref.getString(appContext.getString(R.string.pref_auth_server), "") +
                ":" + sharedPref.getString(appContext.getString(R.string.pref_auth_server_port), "") +
                "/auth/realms/expenses-dev/protocol/openid-connect/token";
        Log.i(TAG, "test connection to URL: " + url);

        HashMap<String, String> params = new HashMap<>();

        params.put("client_id", "expenses-app-dev");
        params.put("username", sharedPref.getString(appContext.getString(R.string.pref_user), ""));
        params.put("password", sharedPref.getString(appContext.getString(R.string.pref_pass), ""));
        params.put("grant_type", "password");


        RequestSender sender = new RequestSender(appContext);

        sender.sendRequest(url, params, listener,errorListener);

    }




}