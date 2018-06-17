package com.myandroid.caseyjohnson.sqlite3db;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    static Record[] records;
    Gson gson;
    static String username, password;

    //----------------------------- ON CREATE --------------------------------------
    public void onCreate(@Nullable Bundle savedInstanceState) { //@Nullable PersistableBundle persistentState
        super.onCreate(savedInstanceState); // persistentState ;
        setContentView(R.layout.activity_base);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        gson = gsonBuilder.create();

        // Volley Library
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());

        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
    }

    //------------------------------- OPTIONS MENU -------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate Menu
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menuViewAll:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuShow:
                intent = new Intent(this, ShowActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuEdit:
                intent = new Intent(this, EditActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuAdd:
                intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuLogin:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //----------------------------- TOAST IT METHOD -----------------------------------
    public void toastIt(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
