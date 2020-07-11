package model;

import java.io.Serializable;

public class ModelBean implements Serializable {

    private String spinnerValue = "00:30";

    public ModelBean() {
    }

    public String getSpinnerValue() {
        return spinnerValue;
    }

    public void setSpinnerValue(String spinnerValue) {
        this.spinnerValue = spinnerValue;
    }
}
