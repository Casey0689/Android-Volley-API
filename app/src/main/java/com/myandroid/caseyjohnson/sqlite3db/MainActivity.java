package com.myandroid.caseyjohnson.sqlite3db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends BaseActivity {

    private ListView listViewRecords;
    ArrayAdapter<Record> adapter;
    Gson gson;

    //------------------------------ On Create ------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        gson = gsonBuilder.create();

        internetOnClick(null);

        listViewRecords = findViewById(R.id.listViewRecords);
        listViewRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //toastIt("You clicked position " + position);
                Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
                intent.putExtra("RecordID", records[position].getId());
                startActivity(intent);
            }
        });
    }

    public void internetOnClick(View v) {
        String url = "https://recordapi.azurewebsites.net/records";
        StringRequest request = new StringRequest(
                Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Do something with the returned data
                        records = gson.fromJson(response, Record[].class);
                        adapter = new ArrayAdapter<Record>(getApplicationContext(), R.layout.activity_listview, records);
                        listViewRecords.setAdapter(adapter);
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
    }

    public void addNewOnClick(View v) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }
}
