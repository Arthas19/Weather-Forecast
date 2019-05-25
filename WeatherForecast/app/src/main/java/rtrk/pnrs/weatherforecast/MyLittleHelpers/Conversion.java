package rtrk.pnrs.weatherforecast.MyLittleHelpers;

public class Conversion {

    static {
        System.loadLibrary("MyLIB");
    }

    public native double conversion(double temperature, int x);
}
