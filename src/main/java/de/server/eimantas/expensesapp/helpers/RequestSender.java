package de.server.eimantas.expensesapp.helpers;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class RequestSender {

    private final Context context;

    public RequestSender(Context ctx) {
        this.context = ctx;
        // Instantiate the cache
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

    }


    RequestQueue mRequestQueue;


    public void sendRequest(String url, final Map<String, String> params, Response.Listener listener, Response.ErrorListener errorListener) {

        final String[] message = {""};

        // Start the queue
        mRequestQueue.start();
        try {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return params;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {
                        Log.i("VOLLEY", "NETWORK RESPONSE: " + new String(
                                response.data,
                                HttpHeaderParser.parseCharset(response.headers)));
                        return Response.success(new String(
                                response.data,
                                HttpHeaderParser.parseCharset(response.headers)), HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return Response.success(e.getMessage(), HttpHeaderParser.parseCacheHeaders(response));
                    }
                }
            };

            mRequestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void sendRequest(String url, final Map<String, String> headers, final String body, Response.Listener listener, Response.ErrorListener errorListener) {

        final String[] message = {""};

        // Start the queue
        mRequestQueue.start();
        try {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=UTF-8";
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }


                @Override
                public byte[] getBody() throws AuthFailureError {
                    return body.getBytes();
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {
                        Log.i("VOLLEY", "NETWORK RESPONSE: " + new String(
                                response.data,
                                HttpHeaderParser.parseCharset(response.headers)));
                        return Response.success(new String(
                                response.data,
                                HttpHeaderParser.parseCharset(response.headers)), HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return Response.success(e.getMessage(), HttpHeaderParser.parseCacheHeaders(response));
                    }
                }
            };

            mRequestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
