package com.example.streethardy.tugasakhirpjw;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private EditText editTextName;
    private EditText editTextAdd;
    private EditText editTextNim;


    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADD = "address";
    private static final String Tag_NIM = "nim";

    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAdd = (EditText) findViewById(R.id.editTextAddress);
        editTextNim = (EditText) findViewById(R.id.editTextNim);

        list = (ListView) findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String, String>>();
        getData();

    }

    public void insert(View view) {

        String name = editTextName.getText().toString();
        String add = editTextAdd.getText().toString();
        String nim = editTextNim.getText().toString();

        insertToDatabase(name, add, nim);
        personList.clear();
        list.setAdapter(null);

        getData();

    }

    protected void insertToDatabase(String name, String add, String nim) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String paramUsername = params[0];
                String paramAddress = params[1];
                String paramNim = params[2];


                String name = editTextName.getText().toString();
                String add = editTextAdd.getText().toString();
                String nim = editTextNim.getText().toString();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("name", name));
                nameValuePairs.add(new BasicNameValuePair("address", add));
                nameValuePairs.add(new BasicNameValuePair("nim", nim));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://tespjw.esy.es/insert-db.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "success";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                TextView textViewResult = (TextView) findViewById(R.id.textViewResult);
                textViewResult.setText("Inserted");

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(name, add, nim);

    }


    private void getData() {
                class GetDataJSON extends AsyncTask<String, Void, String>{

                    @Override
                    protected String doInBackground(String... params) {
                        DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                        HttpPost httppost = new HttpPost("http://tespjw.esy.es/get-data.php");

                        // Depends on your web service
                        httppost.setHeader("Content-type", "application/json");

                        InputStream inputStream = null;
                        String result = null;
                        try {
                            HttpResponse response = httpclient.execute(httppost);
                            HttpEntity entity = response.getEntity();

                            inputStream = entity.getContent();
                            // json is UTF-8 by default
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                            StringBuilder sb = new StringBuilder();

                            String line = null;
                            while ((line = reader.readLine()) != null)
                            {
                                sb.append(line + "\n");
                            }
                            result = sb.toString();
                        } catch (Exception e) {
                            // Oops
                        }
                        finally {
                            try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                        }
                        return result;
                    }

                    @Override
                    protected void onPostExecute(String result){
                        myJSON=result;
                        showList();
                    }
                }
                GetDataJSON g = new GetDataJSON();
                g.execute();
            }

            private void showList() {
                try {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    peoples = jsonObj.getJSONArray(TAG_RESULTS);

                    for(int i=0;i<peoples.length();i++){
                        JSONObject c = peoples.getJSONObject(i);
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        String address = c.getString(TAG_ADD);
                        String nim = c.getString(Tag_NIM);

                        HashMap<String,String> persons = new HashMap<String,String>();

                        persons.put(TAG_ID,id);
                        persons.put(TAG_NAME,name);
                        persons.put(TAG_ADD,address);
                        persons.put(Tag_NIM,nim);

                        personList.add(persons);
                    }

                    ListAdapter adapter = new SimpleAdapter(
                            MainActivity.this, personList, R.layout.list_item,
                            new String[]{TAG_ID,TAG_NAME,TAG_ADD, Tag_NIM},
                            new int[]{R.id.id, R.id.name, R.id.address, R.id.nim}
                    );

                    list.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                // Handle action bar item clicks here. The action bar will
                // automatically handle clicks on the Home/Up button, so long
                // as you specify a parent activity in AndroidManifest.xml.
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.action_settings) {
                    return true;
                }

                return super.onOptionsItemSelected(item);
            }
        }



