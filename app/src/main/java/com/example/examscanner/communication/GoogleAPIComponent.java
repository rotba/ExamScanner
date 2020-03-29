package com.example.examscanner.communication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.examscanner.MainActivity;

import java.util.HashMap;
import java.util.Map;

public class GoogleAPIComponent {

    // This is the part where data is transferred from device to sheet by using HTTP Rest API calls
    private void addItemToSheet(Context context, String username, String password, String role) {

        final ProgressDialog loading = ProgressDialog.show(context,"Adding User","Please wait");


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://docs.google.com/spreadsheets/d/1N-rlfTiWNYGsY6-1uuPn-pGLFdnVOnkE7Op1Ycge9kU/edit#gid=0",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loading.dismiss();
                        //Toast.makeText(AddItem.this,response,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("volley", "failed to access sheet");
                    }
                }
        ) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                //here we pass params
                params.put("action","addUser");
                params.put("Date",java.time.LocalDate.now().toString());
                params.put("Username",username);
                params.put("Password",password);
                params.put("Role",role);

                return params;
            }
        };

        int socketTimeOut = 50000;

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(context);

        queue.add(stringRequest);


    }
}
