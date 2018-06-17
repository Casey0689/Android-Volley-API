package com.myandroid.caseyjohnson.sqlite3db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditActivity extends BaseActivity {

    Record record;
    EditText edtEditName, edtEditDescription, edtEditPrice, edtEditRating;
    Integer recordID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        edtEditName = findViewById(R.id.edtEditName);
        edtEditDescription = findViewById(R.id.edtEditDescription);
        edtEditPrice = findViewById(R.id.edtEditPrice);
        edtEditRating = findViewById(R.id.edtEditRating);

        recordID = getIntent().getIntExtra("RecordID", 0);
        String url = "https://recordapi.azurewebsites.net/records/" + recordID.toString();
        StringRequest request = new StringRequest(
                Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Do something with the returned data
                        record = gson.fromJson(response, Record.class);
                        edtEditName.setText(record.getName());
                        edtEditDescription.setText(record.getDescription());
                        edtEditPrice.setText(String.valueOf(record.getPrice()));
                        edtEditRating.setText(String.valueOf(record.getRating()));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something with the error
                        Log.d("INTERNET", error.toString());
                        toastIt("Internet Failer: " + error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String credentials = username + ":" + password;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };
        requestQueue.add(request);
        //toastIt("Record Id is: " + recordID);
    }

    public void onEditClick(View v) {
        String url = "https://recordapi.azurewebsites.net/records/" + recordID.toString();
        final String recordName = edtEditName.getText().toString();
        final String recordDescription = edtEditDescription.getText().toString();
        final String recordPrice = edtEditPrice.getText().toString();
        final String recordRating = edtEditRating.getText().toString();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", recordName);
        params.put("description", recordDescription);
        params.put("price", recordPrice);
        params.put("rating", recordRating);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        toastIt(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        toastIt(error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String credentials = username + ":" + password;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };
        requestQueue.add(request);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
