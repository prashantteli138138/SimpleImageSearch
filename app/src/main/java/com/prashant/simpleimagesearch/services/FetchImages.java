package com.prashant.simpleimagesearch.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.prashant.simpleimagesearch.Interfaces.VolleyCallback;

import java.util.HashMap;
import java.util.Map;

public class FetchImages {
    boolean success;
    Context context;
    String query;
    int pageNo;

    public FetchImages(Context context, String query, int pageNo) {
        this.context = context;
        this.query = query;
        this.pageNo = pageNo;
    }

    private ProgressDialog progress;

    public void sendRequest(final VolleyCallback callback) {
        if (query.isEmpty())
            query = "vanilla";

        String JSON_URL = "https://api.imgur.com/3/gallery/search/" + pageNo + "?q=" + query + "&q_size_px=small";
        download();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccessResponse(response);
                        success = true;
                        progress.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                        VolleyLog.d("Nova", "Error: " + error.getMessage());
                        progress.dismiss();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Client-ID 137cda6b5008a7c");
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ClientId", "UserId");
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void download() {
        progress = new ProgressDialog(context);
        progress.setMessage("Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        final int totalProgressTime = 100;
        final Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;

                while (jumpTime < totalProgressTime) {
                    try {
                        sleep(200);
                        jumpTime += 5;
//                        progress.setProgress(jumpTime);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }
}