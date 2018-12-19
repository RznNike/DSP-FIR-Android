package com.dsp.fir.global;

import android.text.Editable;
import android.text.TextWatcher;

public class StateTextWatcher implements TextWatcher {
    private String lastValue = "";

    public StateTextWatcher() {
    }

    public StateTextWatcher(String lastValue) {
        this.lastValue = lastValue;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String currentValue = editable.toString();
        String oldValue = lastValue;
        if (!currentValue.equals(lastValue)) {
            lastValue = currentValue;
            onValueChanged(oldValue, currentValue);
        }
    }

    public void onValueChanged(String oldValue, String newValue) {

    }
}
