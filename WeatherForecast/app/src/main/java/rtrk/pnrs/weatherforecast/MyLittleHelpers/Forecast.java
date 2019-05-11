package rtrk.pnrs.weatherforecast.MyLittleHelpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Forecast {

    public static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    public static final String SECRET_KEY = "&APPID=8827e28e1ab35a7ff0a4a64651bea798";
    public static final String EXTRAS = "&units=metric";

    private String city, date;

    private double temperature, humidity, pressure;

    private String sunrise, sunset;

    private double windSpeed;
    private String windDirection;

    /* Friend of http and brother JSON, but we good  */
    public Forecast(String url) {

        HttpHelper httpHelper = new HttpHelper();

        try {
            JSONObject json = httpHelper.getJSONObjectFromURL(url);

            JSONObject main = json.getJSONObject("main");
            JSONObject wind = json.getJSONObject("wind");
            JSONObject sys = json.getJSONObject("sys");

            this.temperature = main.getDouble("temp");
            this.humidity = main.getDouble("humidity");
            this.pressure = main.getDouble("pressure");

            this.windSpeed = wind.getDouble("speed");

            this.city = json.getString("name");
            this.date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

            try {
                this.windDirection = convertDegreesToDirection(wind.getDouble("deg"));
            } catch (JSONException e) {
                e.printStackTrace();
                this.windDirection = "404";
            }

            this.sunrise = convertUnixTime(sys.getString("sunrise"));
            this.sunset = convertUnixTime(sys.getString("sunset"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* Deep in dat data */
    public Forecast(String city, String date, double temperature, double humidity, double pressure, String sunrise, String sunset, double windSpeed, String windDirection) {
        this.city = city;
        this.date = date;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
    }

    private static String convertUnixTime(String time) {
        long ut = Long.parseLong(time);
        Date date = new Date(ut * 1000L);

        return DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault()).format(date);
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public String getCity() {
        return city;
    }

    public String getDate() {
        return date;
    }

    private static String convertDegreesToDirection(double deg) {

        if ((deg >= 348.75 && deg <= 360.00) || (deg >= 0 && deg < 11.25)) {
            return "North";
        } else if (deg >= 11.25 && deg < 33.75) {
            return "North-North-East";
        } else if (deg >= 33.75 && deg < 56.25) {
            return "North-East";
        } else if (deg >= 56.25 && deg < 78.75) {
            return "East-North-East";
        } else if (deg >= 78.75 && deg < 101.25) {
            return "East";
        } else if (deg >= 101.25 && deg < 123.75) {
            return "East-South-East";
        } else if (deg >= 123.75 && deg < 146.25) {
            return "South-East";
        } else if (deg >= 146.25 && deg < 168.75) {
            return "South-South-East";
        } else if (deg >= 168.75 && deg < 191.25) {
            return "South";
        } else if (deg >= 191.25 && deg < 213.75) {
            return "South-South-West";
        } else if (deg >= 213.75 && deg < 236.25) {
            return "South-West";
        } else if (deg >= 236.25 && deg < 258.75) {
            return "West-South-West";
        } else if (deg >= 258.75 && deg < 281.25) {
            return "West";
        } else if (deg >= 281.25 && deg < 303.75) {
            return "West-North-West";
        } else if (deg >= 303.75 && deg < 326.25) {
            return "North-West";
        } else if (deg >= 326.25 && deg < 348.75) {
            return "North-West-North";
        } else {
            return "";
        }
    }
}