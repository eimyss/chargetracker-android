package de.server.eimantas.expensesapp.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.server.eimantas.expensesapp.R;

public class KeyCloackHelper {

    private static final String TAG = "KeycloackHelper";

    public static String login(Context appContext) throws IOException, JSONException {

        SharedPreferences sharedPref = appContext.getSharedPreferences(appContext.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        HttpClient client = new DefaultHttpClient();
        String url = "http://" + sharedPref.getString(appContext.getString(R.string.pref_server), "") +
                ":" + sharedPref.getString(appContext.getString(R.string.pref_server_port), "") +
                "/auth/realms/expenses/protocol/openid-connect/token";
        Log.i(TAG, "test connection to URL: " + url);

        HttpPost request = new HttpPost(url);

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("client_id", "expenses-app"));
        pairs.add(new BasicNameValuePair("username", sharedPref.getString(appContext.getString(R.string.pref_user), "")));
        pairs.add(new BasicNameValuePair("password", sharedPref.getString(appContext.getString(R.string.pref_pass), "")));
        pairs.add(new BasicNameValuePair("grant_type", "password"));
        request.setEntity(new UrlEncodedFormEntity(pairs));
        HttpResponse resp = client.execute(request);

        String response = IOUtils.toString(resp.getEntity().getContent());
        Log.d(TAG, "Got response " + response);

        String accessToken = getValueFromToken("access_token", response);
        String refreshToken = getValueFromToken("refresh_token", response);

        Log.i(TAG, "saving values for further use");


        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(appContext.getString(R.string.access_token), accessToken);
        editor.putString(appContext.getString(R.string.refresh_token), refreshToken);
        editor.commit();

        // should I return true / false?
        return getValueFromToken("session_state", response);
    }


    public static String getValueFromToken(String name, String token) throws IOException, JSONException {

        JSONObject parser = new JSONObject(token);
        return parser.getString(name);

    }

}