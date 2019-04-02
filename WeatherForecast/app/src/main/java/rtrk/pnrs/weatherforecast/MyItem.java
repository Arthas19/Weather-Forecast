package rtrk.pnrs.weatherforecast;

import android.widget.RadioButton;

public class MyItem {

    private String string;
    private RadioButton radioButton;

    public MyItem(String string, RadioButton radioButton) {
        this.string = string;
        this.radioButton = radioButton;
    }

    public String getText() {
        return string;
    }
}
