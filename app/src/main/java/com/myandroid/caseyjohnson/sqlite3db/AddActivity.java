package com.myandroid.caseyjohnson.sqlite3db;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddActivity extends BaseActivity {

    EditText edtAddName, edtAddDescription, edtAddPrice, edtAddRating;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        edtAddName = findViewById(R.id.edtAddName);
        edtAddDescription = findViewById(R.id.edtAddDescription);
        edtAddPrice = findViewById(R.id.edtAddPrice);
        edtAddRating = findViewById(R.id.edtAddRating);
    }

    public void addOnClick(View v) {
        String url = "https://recordapi.azurewebsites.net/records";
        final String recordName = edtAddName.getText().toString();
        final String recordDescription = edtAddDescription.getText().toString();
        final String recordPrice = edtAddPrice.getText().toString();
        final String recordRating = edtAddRating.getText().toString();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", recordName);
        params.put("description", recordDescription);
        params.put("price", recordPrice);
        params.put("rating", recordRating);
        JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(params),
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
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
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
        //toastIt("Record Added");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
