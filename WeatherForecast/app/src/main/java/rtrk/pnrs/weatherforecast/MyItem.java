package rtrk.pnrs.weatherforecast;

import android.widget.Button;
import android.widget.RadioButton;

class MyItem {

    private String string;
    private Button button;
    private RadioButton radioButton;

    MyItem(String string, Button button, RadioButton radioButton) {
        this.string = string;
        this.button = button;
        this.radioButton = radioButton;
    }

    String getText() {
        return string;
    }
}
