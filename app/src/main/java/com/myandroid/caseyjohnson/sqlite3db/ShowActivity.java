package com.myandroid.caseyjohnson.sqlite3db;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ShowActivity extends BaseActivity {

    TextView txtShowName, txtShowDescription, txtShowPrice, txtShowRating;
    Integer recordID;
    Record record;
    ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        txtShowName = findViewById(R.id.txtShowName);
        txtShowDescription = findViewById(R.id.txtShowDescription);
        txtShowPrice = findViewById(R.id.txtShowPrice);
        txtShowRating = findViewById(R.id.txtShowRating);
        imageView = findViewById(R.id.imageRecord);

        imageView.setImageResource(R.drawable.ic_launcher_background);

        recordID = getIntent().getIntExtra("RecordID", 0);
        String url = "https://recordapi.azurewebsites.net/records/" + recordID.toString();
        StringRequest request = new StringRequest(
                Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Do something with the returned data
                        record = gson.fromJson(response, Record.class);
                        txtShowName.setText(record.getName());
                        txtShowDescription.setText(record.getDescription());
                        txtShowPrice.setText(String.valueOf(record.getPrice()));
                        txtShowRating.setText(String.valueOf(record.getRating()));
                        Picasso.with(getApplicationContext()).load(record.getImage()).into(imageView);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something with the error
                        Log.d("INTERNET", error.toString());
                        toastIt("Internet Failer: " + error.toString());
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
        //toastIt("Record Id is: " + recordID);
    }

    public void deleteOnClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Perform something when they click YES
                        toastIt("Record Deleted");
                        String url = "https://recordapi.azurewebsites.net/records/" + recordID.toString();
                        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
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
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Perform something when they say NO
                dialog.cancel();
                toastIt("You pressed No");
            }
        }).create()
                .show();
    }

    public void editOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), EditActivity.class);
        intent.putExtra("RecordID", recordID);
        startActivity(intent);
    }
}
