package model;

import bindings.DataObjectBase;

public class ModelBean extends DataObjectBase {

    private String spinnerValue = "00:30";

    public ModelBean() {
    }

    public String getSpinnerValue() {
        return spinnerValue;
    }

    public void setSpinnerValue(String spinnerValue) {
        String oldValue = spinnerValue;
        this.spinnerValue = spinnerValue;
        onPropertyChanged(this, "spinner", oldValue, spinnerValue);
    }
}
