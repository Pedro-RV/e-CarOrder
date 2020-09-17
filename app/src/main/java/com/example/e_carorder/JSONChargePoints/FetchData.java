package com.example.e_carorder.JSONChargePoints;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchData extends AsyncTask<Void, Void, Void> {
    private String data = "";
    private String dataParsed = "";
    private String singleParsed = "";
    private JSONArray jsonArray;

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://api.openchargemap.io/v3/poi/?output=json&countrycode=ES&key=hola&maxresults=5");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";

            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }

            jsonArray = new JSONArray(data);

            for(int i = 0; i < jsonArray.length(); i++){
                try {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                    //AddressInfo addressInfo = jsonObject.get("AddressInfo").getClass();

                    JSONObject jsonObject2 = jsonObject.getJSONObject("AddressInfo");

                    singleParsed = "Address: " + jsonObject2.get("AddressLine1");

                    //singleParsed = "Address: " + jsonObject.get("UUID");

                    dataParsed = dataParsed + singleParsed + "\n";

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        JSONChargePointsActivity.data.setText(this.dataParsed);

    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
