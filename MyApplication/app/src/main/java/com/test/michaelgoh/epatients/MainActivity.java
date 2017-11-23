package com.test.michaelgoh.epatients;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    private AssetManager assetManager;

    private ArrayList<HashMap<String, String>> patientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        patientList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        assetManager = this.getResources().getAssets();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("name", patientList.get(i).get("name"));
                intent.putExtra("id", patientList.get(i).get("id"));
                startActivity(intent);
            }
        });

        new PatientList().execute();
    }


    private class PatientList extends AsyncTask<Void,Void,Void> {
        private static final String URL = "http://159.203.62.239:3000/patients.json";
        private static final String FILENAME = "patients.json";

        @Override
        protected Void doInBackground(Void... arg0) {
            getJSONString();

            if(patientList.isEmpty()){
                getListFromDevice();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, patientList,
                    R.layout.list_item, new String[]{"name", "id"},
                    new int[]{R.id.name, R.id.id});

            lv.setAdapter(adapter);
        }

        private void getJSONString() {
            HttpURLConnection con;
            InputStream in;
            try {
                URL url = new URL(URL);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(3000);    // if the connection takes longer than 3000ms, cut it
                in = new BufferedInputStream(con.getInputStream());

                JsonReader jsonReader = new JsonReader(new InputStreamReader(in, "UTF-8"));
                readJSONArray(jsonReader);
            }catch (Exception e) {
                Log.e(TAG, "Error: " + e.getMessage());
            }
        }

        private void getListFromDevice(){
            try {
                InputStream input = assetManager.open(FILENAME);
                JsonReader jsonReader = new JsonReader(new InputStreamReader(input, "UTF-8"));
                readJSONArray(jsonReader);
            }catch(Exception e){
                Log.e(TAG, "Error: " + e.getMessage());
            }
        }

        private void readJSONArray(JsonReader jsonReader){
            try {
                try {
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        patientList.add(readJSON(jsonReader));
                    }
                    jsonReader.endArray();
                } finally {
                    jsonReader.close();
                }
            }catch(IOException e){
                Log.e(TAG, "IOException: " + e.getMessage());
            }
        }

        private HashMap<String, String> readJSON(JsonReader jsonReader){
            HashMap<String, String> patient = new HashMap<>();

            try {
                jsonReader.beginObject();

                while(jsonReader.hasNext()) {
                    jsonReader.nextName();
                    String name = jsonReader.nextString();

                    jsonReader.nextName();
                    String id = jsonReader.nextString();

                    patient.put("name", name);
                    patient.put("id", id);
                }

                jsonReader.endObject();
            }catch(IOException e){
                Log.e(TAG, "IOException: " + e.getMessage());
            }

            return patient;
        }
    }
}