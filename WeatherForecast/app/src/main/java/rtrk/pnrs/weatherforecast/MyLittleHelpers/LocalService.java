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
    private static final String SERVICE_KEY = "service";
    private static final int NOTIFICATION = 19;

    private static String CITY;

    private final LocalBinder binder = new LocalBinder();


    public RunBoiRun running = new RunBoiRun();

    public NotificationManager mNM;

    @Override
    public IBinder onBind(Intent intent) {
        CITY = intent.getStringExtra("service");
        Log.d(TAG, "[ON_BIND] I just got " + CITY);

        return binder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals("KILL SHOOT")) {
            stopForeground(true);
            running.stop();
            stopSelf();

        } else {

            CITY = intent.getStringExtra(SERVICE_KEY);
            Log.d(TAG, "[ON_START_COMMAND] I just got " + CITY);

            mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            startForeground(NOTIFICATION, makeNewNotification("0"));
            running.start();
        }

        return START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mNM.cancel(NOTIFICATION);
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

    public class LocalBinder extends Binder {

        public LocalService getService() {
            return LocalService.this;
        }
    }

    public class RunBoiRun implements Runnable {

        private static final long PERIOD = 10000L;
        private Handler handler = null;
        private boolean run = false;

        private DBWeatherHelper dbWeatherHelper;


        void start() {
            handler = new Handler(Looper.getMainLooper());
            dbWeatherHelper = new DBWeatherHelper(LocalService.this);

            handler.postDelayed(this, PERIOD);

            run = true;
        }

        void stop() {
            run = false;
            handler.removeCallbacks(this);
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
