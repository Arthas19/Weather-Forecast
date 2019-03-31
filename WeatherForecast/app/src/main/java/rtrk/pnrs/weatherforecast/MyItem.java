package rtrk.pnrs.weatherforecast;

import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class MyItem implements Checkable {

    private String myString;
    private RadioButton myRadioButton;
    private LinearLayout myLinearLayout;

    public MyItem(String myString, RadioButton myRadioButton, LinearLayout myLinearLayout) {
        this.myString = myString;
        this.myRadioButton = myRadioButton;
        this.myLinearLayout = myLinearLayout;
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
