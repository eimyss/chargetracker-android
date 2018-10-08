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

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class SettingsActvity extends AppCompatActivity {

    private EditText servertext;
    private EditText portText;
    private EditText usernametext;
    private EditText passwordTxt;
    private static final String TAG = "SettingsActivity";
    private Button testConnectionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_actvity);

        servertext = (EditText) findViewById(R.id.servertext);
        portText = (EditText) findViewById(R.id.portText);
        usernametext = (EditText) findViewById(R.id.usernametext);
        testConnectionBtn = (Button) findViewById(R.id.testConnectionBtn);
        passwordTxt = (EditText) findViewById(R.id.passwordTxt);
        readValuesFromPref();

    }

    private void readValuesFromPref() {

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        servertext.setText(sharedPref.getString(getString(R.string.pref_server), ""));
        portText.setText(sharedPref.getString(getString(R.string.pref_server_port), ""));
        usernametext.setText(sharedPref.getString(getString(R.string.pref_user), ""));
        passwordTxt.setText(sharedPref.getString(getString(R.string.pref_pass), ""));
    }


    public void savePreferences(View view) {

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_server), String.valueOf(servertext.getText()));
        editor.putString(getString(R.string.pref_server_port), String.valueOf(portText.getText()));
        editor.putString(getString(R.string.pref_user), String.valueOf(usernametext.getText()));
        editor.putString(getString(R.string.pref_pass), String.valueOf(passwordTxt.getText()));
        editor.commit();
        Toast.makeText(getApplicationContext(), "Settings saved", Toast.LENGTH_SHORT).show();


    }

    void setSuccess(String message) {

        // TODO parse json
        Log.i(TAG, "entity received: " + message);
        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
        testConnectionBtn.setText("Test Connection");

    }

    void setError(String message) {

        // TODO parse json
        Log.i(TAG, "error entity received: " + message);
        Toast.makeText(getApplicationContext(), "error " + message , Toast.LENGTH_SHORT).show();
        testConnectionBtn.setText("Test Connection");
    }

    public void testSettings(View view) {

        Log.i(TAG, "starting request");
        Toast.makeText(getApplicationContext(), "testing", Toast.LENGTH_SHORT).show();
        testConnectionBtn.setText("Testing....");
        new TestConnectionTask().execute("test");

    }

    class TestConnectionTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
                HttpClient client = new DefaultHttpClient();
                String url = "http://" + sharedPref.getString(getString(R.string.pref_server), "") +
                        ":" + sharedPref.getString(getString(R.string.pref_server_port), "") +
                        "/auth/realms/expenses/protocol/openid-connect/token";
                Log.i(TAG,"test connection to URL: " + url);

                HttpPost request = new HttpPost(url);

                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair("client_id", "expenses-app"));
                pairs.add(new BasicNameValuePair("username", sharedPref.getString(getString(R.string.pref_user), "")));
                pairs.add(new BasicNameValuePair("password", sharedPref.getString(getString(R.string.pref_pass), "")));
                pairs.add(new BasicNameValuePair("grant_type", "password"));
                request.setEntity(new UrlEncodedFormEntity(pairs));
                HttpResponse resp = client.execute(request);

                String response = IOUtils.toString(resp.getEntity().getContent());

                Log.i(TAG, "entity content:" + response);
                //  infotext.setText(response);
                return response;
            } catch (Exception e) {
                this.exception = e;
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                return e.getMessage();
            }
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
}
