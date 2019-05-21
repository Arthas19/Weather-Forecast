package rtrk.pnrs.weatherforecast.MyLittleHelpers;

import android.os.Binder;
import android.os.Handler;
import android.os.Looper;


public class MyBinder extends Binder {
    private static final String TAG = "My Binder";

    private int mValue = 0;
    private CallbackCaller mCaller = new CallbackCaller();


    private class CallbackCaller implements Runnable {
        private static final long PERIOD = 1000L;

        private Handler mHandler = null;
        private boolean mRun = true;

        public void start() {
            mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(this, PERIOD);
        }

        public void stop() {
            mRun = false;
            mHandler.removeCallbacks(this);
        }

        @Override
        public void run() {
            if (!mRun) {
                return;
            }
        }
    }
}
