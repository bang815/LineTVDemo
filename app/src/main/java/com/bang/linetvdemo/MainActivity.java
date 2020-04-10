package com.bang.linetvdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.bang.linetvdemo.app.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SearchView searchView;
    private ListAdapter adapter;
    private List<Drama> list= new ArrayList<Drama>();
    private ListView lv;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        requestSTORAGEPermission();
        lv = (ListView) findViewById(R.id.listView);
        searchView = (SearchView) findViewById(R.id.searchView);
        lv.setTextFilterEnabled(true);
        setSearch_function();
        preferences = getSharedPreferences("dramaInfo",MODE_PRIVATE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJSON().execute(AppConfig.URL_GETDRAMAS);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this,DramaDetailsActivity.class);
                i.putExtra("Drama_Thumb",adapter.getItem(position).getThumb());
                i.putExtra("Drama_Name",adapter.getItem(position).getName());
                i.putExtra("Drama_Rating",adapter.getItem(position).getRating());
                i.putExtra("Drama_Created_at",adapter.getItem(position).getCreated_at());
                i.putExtra("Drama_Total_views",adapter.getItem(position).getTotal_views());
                startActivity(i);
                finish();

            }


        });

    }

    private void setSearch_function() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                preferences.edit().putString("searchState",newText).commit();
                adapter.resetData();
                adapter.getFilter().filter(newText);
                return true;
            }
        });

    }

    public Button.OnClickListener btnListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //case R.id.search_btn:
                //    arrayList.clear();
                //    runOnUiThread(new Runnable() {
                //        @Override
                //        public void run() {
                //            new ReadJSON().execute(AppConfig.URL_GETDRAMAS); // listview資訊
                //        }
                //    });
                //    break;
            }
        }
    };

    public void onBackPressed() {
        super.onBackPressed();

    }

    class ReadJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return readURL(params[0]);
        }

        @Override
        protected void onPostExecute(String content) {
            handlejson(content);
        }
    }


    private String readURL(String theUrl) {
        StringBuffer content = new StringBuffer();

        try {
            URL url = new URL(theUrl);

            InputStream is = url.openStream();
            try{
                BufferedReader rd = new BufferedReader(new InputStreamReader(is,"utf-8"));

                int cp;
                while ((cp = rd.read()) != -1) {
                    content.append((char) cp);
                }
            }finally {
                is.close();
            }

        } catch (Exception e) {
            disconnect();
            e.printStackTrace();
        }
        return content.toString();


    }

    private void disconnect(){
        String strJson = preferences.getString("jsondata","");

        if (strJson != null) {
            handlejson(strJson);
        }
    }

    private void handlejson(String strJson){
        try {
        JSONObject jObj = new JSONObject(strJson);
        String data = jObj.getString("data");
        JSONArray array = new JSONArray(data);
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            list.add(new Drama(
                    jsonObject.getString("drama_id"),
                    jsonObject.getString("name"),
                    jsonObject.getString("total_views")+"次",
                    jsonObject.getString("created_at"),
                    jsonObject.getString("thumb"),
                    jsonObject.getString("rating")+"分"
            ));

        }
        if(isConnected()){
            preferences.edit().putString("jsondata",jObj.toString()).commit();
        }else{

        }


    } catch (JSONException e) {
            e.printStackTrace();
    }
        adapter = new ListAdapter(list, getApplicationContext());
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                lv.setAdapter(adapter);
                searchView.setQueryHint(getString(R.string.search_hint));
                if(!preferences.getString("searchState", "").equals("")) {
                    String str = preferences.getString("searchState", "");
                    searchView.onActionViewExpanded();
                    searchView.setQuery(str,false);
                }
            }
        });
    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if ( networkInfo != null && networkInfo.isAvailable()){
            return true;
        }else{
            return false;
        }
    }

    private void requestSTORAGEPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String permissionEXTERNAL = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
            int hasPermissionEXTERNAL = checkSelfPermission(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (hasPermissionEXTERNAL != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{permissionEXTERNAL}, 1);
            }
        }
    }
}
