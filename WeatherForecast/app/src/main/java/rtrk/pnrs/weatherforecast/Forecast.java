package rtrk.pnrs.weatherforecast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import rtrk.pnrs.weatherforecast.MyLittleHelpers.HttpHelper;

class Forecast {
    private double temperature, humidity, pressure;

    private String sunrise, sunset;

    private double windSpeed;
    private String windDirection;

    public Forecast(String url) {

        HttpHelper httpHelper = new HttpHelper();

        try {
            JSONObject json = httpHelper.getJSONObjectFromURL(url);

            JSONObject currently = json.getJSONObject("currently");
            JSONObject daily = json.getJSONObject("daily");
            JSONArray data = daily.getJSONArray("data");

            this.temperature = currently.getDouble("temperature");
            this.humidity = currently.getDouble("humidity");
            this.pressure = currently.getDouble("pressure");
            this.windSpeed = currently.getDouble("windSpeed");
            this.windDirection = "North Remembers";

            this.sunrise = convertUnixTime(data.getJSONObject(0).getString("sunriseTime"));
            this.sunset = convertUnixTime(data.getJSONObject(0).getString("sunsetTime"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static String convertUnixTime(String time) {
        long ut = Long.parseLong(time);
        Date date = new Date(ut * 1000L);

        return DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault()).format(date);
    }

    double getTemperature() {
        return temperature;
    }

    double getHumidity() {
        return humidity;
    }

    double getPressure() {
        return pressure;
    }

    String getSunrise() {
        return sunrise;
    }

    String getSunset() {
        return sunset;
    }

    double getWindSpeed() {
        return windSpeed;
    }

    String getWindDirection() {
        return windDirection;
    }
}
