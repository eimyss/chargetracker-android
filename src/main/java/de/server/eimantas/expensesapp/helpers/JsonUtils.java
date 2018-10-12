package de.server.eimantas.expensesapp.helpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class JsonUtils {

    public static String getValueFromToken(String name, String token) throws IOException, JSONException {

        JSONObject parser = new JSONObject(token);
        return parser.getString(name);

    }

}
