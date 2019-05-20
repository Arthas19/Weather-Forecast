package rtrk.pnrs.weatherforecast.MyLittleHelpers;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import rtrk.pnrs.weatherforecast.R;

import static rtrk.pnrs.weatherforecast.MyLittleHelpers.Notification.CHANNEL_ID;

public class MyService extends Service {
    public static final String LOG_TAG = "MyService";
    public static final long PERIOD = 30000L;

    private RunnableService mRunnable;

    @Override
    public void onCreate() {
        super.onCreate();

        mRunnable = new RunnableService();
        mRunnable.start();

        Notification notification = new NotificationCompat.Builder(MyService.this, CHANNEL_ID)
                .setContentTitle("Novi Sad")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("TEMP: " + "°"))
                .setSmallIcon(R.drawable.ic_cloudy_2)
                .build();

        MyService.this.startForeground(1, notification);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mRunnable.stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class RunnableService implements Runnable {
        private Handler mHandler;
        private boolean mRun = false;

        Forecast forecast;


        RunnableService() {
            mHandler = new Handler(getMainLooper());
        }

        void start() {
            mRun = true;
            mHandler.postDelayed(this, PERIOD);
        }

        void stop() {
            mRun = false;
            mHandler.removeCallbacks(this);
        }

        @Override
        public void run() {
            if (!mRun)
                return;


            mHandler.postDelayed(this, PERIOD);

            Log.d(LOG_TAG, "Hello from Runnable");
        }
    }
}
