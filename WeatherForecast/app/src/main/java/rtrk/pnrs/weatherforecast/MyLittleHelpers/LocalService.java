package rtrk.pnrs.weatherforecast.MyLittleHelpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import rtrk.pnrs.weatherforecast.DetailsActivity;
import rtrk.pnrs.weatherforecast.R;

import static rtrk.pnrs.weatherforecast.MyLittleHelpers.Notification.CHANNEL_ID;


public class LocalService extends Service {

    private static final String TAG = "FROM_CHICAGO_YOURS";
    private static final String SERVICE_KEY = "service_key";
    private static final int NOTIFICATION = 19;

    private static String CITY;

    private final LocalBinder binder = new LocalBinder();


    public RunBoiRun running = new RunBoiRun();

    public NotificationManager mNM;

    @Override
    public IBinder onBind(Intent intent) {
        CITY = intent.getStringExtra(SERVICE_KEY);
        Log.d(TAG, "[BLOOD BOUND] " + CITY);

        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        startForeground(NOTIFICATION, makeNewNotification("0"));

        return binder;
    }


    @Override
    public boolean onUnbind(Intent intent) {

        mNM.cancel(NOTIFICATION);
        running.stop();
        stopSelf();

        return super.onUnbind(intent);
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "NEMA VISE CETNICKOGA TICA, NEMA VISE SLAVKA JANKOVICA");

        super.onDestroy();
    }

    private Notification makeNewNotification(String temp) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,
                new Intent(this, DetailsActivity.class),
                0);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_cloudy_2)
                .setContentTitle(CITY)
                .setContentText(temp + "°")
                .setColor(getColor(R.color.colorPrimary))
                .setContentIntent(pendingIntent)
                .build();
    }

    // Binder Class
    public class LocalBinder extends Binder {

        public LocalService getService() {
            return LocalService.this;
        }
    }

    // Runnable class
    public class RunBoiRun implements Runnable {

        private static final long PERIOD = 10000L;

        private boolean run = false;
        private Handler handler = null;
        private DBWeatherHelper dbWeatherHelper;


        public void start() {
            handler = new Handler(Looper.getMainLooper());
            dbWeatherHelper = new DBWeatherHelper(LocalService.this);

            handler.postDelayed(this, PERIOD);

            run = true;
        }

        void stop() {
            handler.removeCallbacks(this);
            run = false;
        }

        @Override
        public void run() {
            if (run) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Forecast forecast;
                        forecast = new Forecast(CITY);

                        if (dbWeatherHelper.insert(forecast))
                            Log.d("[SERVICE DB]", "SUCCESSFUL");

                        mNM.notify(NOTIFICATION, makeNewNotification(String.valueOf(forecast.getTemperature())));
                    }
                }).start();
            }

            handler.postDelayed(this, PERIOD);
        }
    }
}
