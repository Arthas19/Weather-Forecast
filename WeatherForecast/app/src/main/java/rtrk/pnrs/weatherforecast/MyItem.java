package rtrk.pnrs.weatherforecast;

import android.widget.Checkable;
import android.widget.RadioButton;

public class MyItem implements Checkable {

    private String string;
    private RadioButton radioButton;

    public MyItem(String string, RadioButton radioButton) {
        this.string = string;
        this.radioButton = radioButton;
    }

    public String getText() {
        return string;
    }

    @Override
    public boolean isChecked() {
        return radioButton.isChecked();
    }

    @Override
    public void setChecked(boolean checked) {
        radioButton.setChecked(checked);
    }

    @Override
    public void toggle() {
        radioButton.toggle();
    }
}
