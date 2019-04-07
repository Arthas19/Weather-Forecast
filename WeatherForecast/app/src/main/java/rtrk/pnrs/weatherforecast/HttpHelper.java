package rtrk.pnrs.weatherforecast;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class HttpHelper {

    private static final int SUCCESS = 200;

    JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {

        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        /* Header fields */
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Accept", "application/json");

        urlConnection.setReadTimeout(3000 /* milliseconds */);
        urlConnection.setConnectTimeout(10000 /* milliseconds */);

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;

        while ( ( line = br.readLine() ) != null ) {
            sb.append(line);
            sb.append("\n");
        }

        br.close();

        String jsonString = sb.toString();
        Log.d("HTTP GET", "JSON obj - " + jsonString);

        int responseCode = urlConnection.getResponseCode();
        urlConnection.disconnect();

        return responseCode == SUCCESS ? new JSONObject(jsonString) : null;
    }
}
