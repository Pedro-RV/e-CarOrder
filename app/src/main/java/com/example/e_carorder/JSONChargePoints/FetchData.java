package com.example.e_carorder.JSONChargePoints;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.e_carorder.helpers.AddressInfoHelperClass;
import com.example.e_carorder.helpers.ChargePointHelperClass;
import com.example.e_carorder.helpers.ConnectorHelperClass;
import com.example.e_carorder.helpers.QueueItemHelperClass;
import com.example.e_carorder.helpers.ReservationUserHelperClass;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;

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
import java.util.ArrayList;

public class FetchData extends AsyncTask<String, String, String> {
    private String data = "";
    private String dataParsed = "";
    private String singleParsed = "";
    private JSONArray jsonArray;

    private DatabaseReference databaseReference;
    private ArrayList<String> titles;
    private String title, address, stateOrProvince, town, postCode;
    private double latitude, longitude;
    private AddressInfoHelperClass addressInfoHelperClass;
    private ArrayList<ConnectorHelperClass> connectors = new ArrayList<>();
    private ConnectorHelperClass connectorHelperClass;
    private String statusType;
    private String latitudeString, longitudeString;
    private String id;
    private ChargePointHelperClass chargePointHelperClass;

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL("https://api.openchargemap.io/v3/poi/?output=json&countrycode=ES&key=hola&maxresults=200");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";

            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }

            jsonArray = new JSONArray(data);

            databaseReference = FirebaseDatabase.getInstance().getReference("ChargePoints");

            titles = new ArrayList<>();

            for(int i = 0; i < jsonArray.length(); i++){
                try {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                    JSONObject jsonObjectAddressInfo = jsonObject.getJSONObject("AddressInfo");

                    latitude = (double) jsonObjectAddressInfo.get("Latitude");

                    longitude = (double) jsonObjectAddressInfo.get("Longitude");

                    title = jsonObjectAddressInfo.get("Title").toString();

                    address = jsonObjectAddressInfo.get("AddressLine1").toString();

                    stateOrProvince = jsonObjectAddressInfo.get("StateOrProvince").toString();

                    town = jsonObjectAddressInfo.get("Town").toString();
                    dataParsed = dataParsed + address + "\n" + title + "\n" + stateOrProvince + "\n" + town + "\n" + "20000" + "\n" + latitude + "\n" + longitude + "\n";
                    postCode = jsonObjectAddressInfo.get("PostCode").toString();


                    /*addressInfoHelperClass = new AddressInfoHelperClass(title, address, stateOrProvince, town, postCode, latitude, longitude);

                    connectorHelperClass = new ConnectorHelperClass("", "Schuko", "3.4", false,
                            -1, -1, new ArrayList<ReservationUserHelperClass>(), new ArrayList<QueueItemHelperClass>());

                    connectors.add(connectorHelperClass);

                    statusType = "Available";

                    latitudeString = Double.toString(addressInfoHelperClass.getLatitude());
                    longitudeString = Double.toString(addressInfoHelperClass.getLongitude());

                    latitudeString = latitudeString.replace(".", "");
                    latitudeString = latitudeString.replace("-", "");
                    longitudeString = longitudeString.replace(".", "");
                    longitudeString = longitudeString.replace("-", "");

                    if(latitudeString.length() < 5){
                        latitudeString = latitudeString + "0000";
                    }

                    if(longitudeString.length() < 5){
                        longitudeString = longitudeString + "0000";
                    }

                    id = latitudeString.substring(0,5) + longitudeString.substring(0,5);

                    chargePointHelperClass = new ChargePointHelperClass(id, statusType, addressInfoHelperClass, connectors);

                    boolean alreadyAdded = false;

                    for(int j = 0; j < titles.size() && !alreadyAdded; j++){
                        if(title.equals(titles.get(i))){
                            alreadyAdded = true;
                        }
                    }

                    titles.add(title);

                    if(!alreadyAdded){
                        //databaseReference.child(id).setValue(chargePointHelperClass);
                    }
                    databaseReference.child(id).setValue(chargePointHelperClass);*/

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

        return dataParsed;
    }

    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);

        //JSONChargePointsActivity.data.setText(dataParsed);

    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
