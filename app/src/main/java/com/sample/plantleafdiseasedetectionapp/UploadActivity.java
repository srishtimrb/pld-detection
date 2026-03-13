package com.sample.plantleafdiseasedetectionapp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {

    Button btn_upload;
    EditText input_image;

    String imageURL = "https://t4.ftcdn.net/jpg/00/37/42/89/360_F_37428956_qHvAy9EIzMqAh15rYR31ZQevyzFGbBuH.jpg"; // Replace Image URL
    String API_KEY = "LaZ6W4vU6FPHjPG7nwxo";
    String DATASET_NAME = "leaf"; // Set Dataset Name (Found in Dataset URL)

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_screen);

        btn_upload = findViewById(R.id.btn_upload);
        input_image = findViewById(R.id.input_image);

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String imageURL = input_image.getText().toString().trim();
                if(imageURL.isEmpty()){
                    Toast.makeText(UploadActivity.this, "Please enter image url", Toast.LENGTH_SHORT).show();
                    return;
                }

                new HttpGetRequest().execute();
            }
        });
    }

    @SuppressLint("NewApi")
    private void uploadImage()
    {

        // Upload URL
        String uploadURL = "https://api.roboflow.com/dataset/" + DATASET_NAME + "/upload";

        Log.e("uploadURL: ",uploadURL);

        // Create a StringRequest
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response
                        Toast.makeText(UploadActivity.this, "Successfully uploaded: " + response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(UploadActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

        ){
            @Override
            protected Map<String, String> getParams() {
                // Prepare the parameters for the POST request
                Map<String, String> params = new HashMap<>();
                params.put("api_key",API_KEY);
                params.put("image",URLEncoder.encode(imageURL, StandardCharsets.UTF_8));
                params.put("name","sample.jpg");
                params.put("split","train");
                Log.e("params: ",""+params);
                return params;
            }
        };

        // Add the request to the RequestQueue
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @SuppressLint("NewApi")
    public class HttpGetRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            // Upload URL
            String uploadURL = "https://api.roboflow.com/dataset/" + DATASET_NAME + "/upload" + "?api_key=" + API_KEY
                    + "&name=YOUR_IMAGE.jpg" + "&split=train" + "&image="
                    + URLEncoder.encode(imageURL, StandardCharsets.UTF_8);

            // Http Request
            HttpURLConnection connection = null;
            try {
                // Configure connection to URL
                URL url = new URL(uploadURL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                connection.setRequestProperty("Content-Length", Integer.toString(uploadURL.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");
                connection.setUseCaches(false);
                connection.setDoOutput(true);

                // Send request
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(uploadURL);
                wr.close();

                // Get Response
                InputStream stream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            // Handle the result (UI updates should be done here)
            Log.e("Response: ",""+result);
        }
    }

}
