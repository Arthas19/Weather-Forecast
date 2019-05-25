package rtrk.pnrs.weatherforecast.MyLittleHelpers;

public class Conversion {

    static {
        System.loadLibrary("conversionLibrary");
    }

    public native double conversion(double temperature, int x);
}
