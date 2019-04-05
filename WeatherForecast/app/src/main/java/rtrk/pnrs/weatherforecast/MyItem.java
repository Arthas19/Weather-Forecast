package rtrk.pnrs.weatherforecast;

import android.widget.Button;
import android.widget.RadioButton;

public class MyItem {

    private String string;
    private Button button;
    private RadioButton radioButton;

    public MyItem(String string, Button button, RadioButton radioButton) {
        this.string = string;
        this.button = button;
        this.radioButton = radioButton;
    }

    public String getText() {
        return string;
    }
}
