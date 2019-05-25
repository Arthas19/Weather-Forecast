package rtrk.pnrs.weatherforecast.MyLittleHelpers;

public class Conversion {

    static {
        System.loadLibrary("MyLib");
    }

    public native double conversion(double temperature, int x);
}
