package rtrk.pnrs.weatherforecast;

import android.widget.Checkable;
import android.widget.RadioButton;

public class MyItem implements Checkable {

    private String myString;
    private RadioButton myRadioButton;

    public MyItem(String string, RadioButton radioButton) {
        this.myString = string;
        this.myRadioButton = radioButton;
    }

    public String getText() {
        return myString;
    }

    @Override
    public boolean isChecked() {
        return myRadioButton.isChecked();
    }

    @Override
    public void setChecked(boolean checked) {
        myRadioButton.setChecked(checked);
    }

    @Override
    public void toggle() {
        myRadioButton.toggle();
    }
}
