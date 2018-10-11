package de.server.eimantas.expensesapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.server.eimantas.expensesapp.helpers.KeyCloackHelper;

public class SettingsActvity extends AppCompatActivity {

    private EditText servertext;
    private EditText portText;
    private EditText usernametext;
    private EditText passwordTxt;
    private static final String TAG = "SettingsActivity";
    private Button testConnectionBtn;
    private EditText gateway_port_txt;
    private EditText gateway_server_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_actvity);

        servertext = (EditText) findViewById(R.id.servertext);
        portText = (EditText) findViewById(R.id.portText);
        usernametext = (EditText) findViewById(R.id.usernametext);
        testConnectionBtn = (Button) findViewById(R.id.testConnectionBtn);
        passwordTxt = (EditText) findViewById(R.id.passwordTxt);
        gateway_port_txt = (EditText) findViewById(R.id.gateway_port_txt);
        gateway_server_txt = (EditText) findViewById(R.id.gateway_server_txt);
        readValuesFromPref();

    }

    private void readValuesFromPref() {

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        servertext.setText(sharedPref.getString(getString(R.string.pref_auth_server), ""));
        portText.setText(sharedPref.getString(getString(R.string.pref_auth_server_port), ""));
        gateway_server_txt.setText(sharedPref.getString(getString(R.string.pref_gateway_server), ""));
        gateway_port_txt.setText(sharedPref.getString(getString(R.string.pref_gateway_server_port), ""));
        usernametext.setText(sharedPref.getString(getString(R.string.pref_user), ""));
        passwordTxt.setText(sharedPref.getString(getString(R.string.pref_pass), ""));
    }


    public void savePreferences(View view) {

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_auth_server), String.valueOf(servertext.getText()));
        editor.putString(getString(R.string.pref_auth_server_port), String.valueOf(portText.getText()));
        editor.putString(getString(R.string.pref_gateway_server), String.valueOf(gateway_server_txt.getText()));
        editor.putString(getString(R.string.pref_gateway_server_port), String.valueOf(gateway_port_txt.getText()));
        editor.putString(getString(R.string.pref_user), String.valueOf(usernametext.getText()));
        editor.putString(getString(R.string.pref_pass), String.valueOf(passwordTxt.getText()));
        editor.commit();
        Toast.makeText(getApplicationContext(), "Settings saved", Toast.LENGTH_SHORT).show();


    }

    void setSuccess(String message) {

        // TODO parse json
        Log.i(TAG, "entity received: " + message);
        Toast.makeText(getApplicationContext(), "success: " + message, Toast.LENGTH_SHORT).show();
        testConnectionBtn.setText("Test Connection");

    }

    void setError(String message) {

        // TODO parse json
        Log.i(TAG, "error entity received: " + message);
        Toast.makeText(getApplicationContext(), "error " + message, Toast.LENGTH_SHORT).show();
        testConnectionBtn.setText("Test Connection");
    }

    public void testSettings(View view) {

        Log.i(TAG, "starting request");
        Toast.makeText(getApplicationContext(), "testing", Toast.LENGTH_SHORT).show();
        testConnectionBtn.setText("Testing....");
        new TestConnectionTask().execute();

    }

    class TestConnectionTask extends AsyncTask<Void, Void, Void> {

        private Exception exception;

        protected Void doInBackground(Void... voids) {
            try {

                Response.Listener listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("KEYCLOACK-HANDLER", response);
                        Log.d(TAG, "Got response " + response);
                        try {
                            final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
                            String accessToken = getValueFromToken("access_token", response);
                            String refreshToken = getValueFromToken("refresh_token", response);
                            Log.i(TAG, "saving values for further use");
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getApplicationContext().getString(R.string.access_token), accessToken);
                            editor.putString(getApplicationContext().getString(R.string.refresh_token), refreshToken);
                            editor.commit();
                            setSuccess(getValueFromToken("session_state", response));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", error.toString());
                        setError(error.toString());
                    }
                };

                KeyCloackHelper.login(getApplicationContext(), listener, errorListener);
            } catch (Exception e) {
                this.exception = e;
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        protected void onPostExecute(String response) {
            //  infotext.setText(response);
            if (exception == null) {
                setSuccess(response);
            } else {
                setError(exception.getMessage());
            }

        }
    }

    public static String getValueFromToken(String name, String token) throws IOException, JSONException {

        JSONObject parser = new JSONObject(token);
        return parser.getString(name);

    }
}
