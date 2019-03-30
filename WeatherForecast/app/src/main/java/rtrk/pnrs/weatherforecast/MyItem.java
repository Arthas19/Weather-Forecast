package rtrk.pnrs.weatherforecast;

import android.widget.Button;

public class MyItem {

    private String text;
    private Button button;

    public MyItem(String text, Button button) {
        this.text = text;
        this.button = button;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }
}
