package com.dsp.fir.domain.global.validator;

import androidx.annotation.StringRes;

import lombok.Getter;

@Getter
public class ValidatorException extends Exception {
    @StringRes
    private int validatorMessage;

    public ValidatorException(int validatorMessage) {
        this.validatorMessage = validatorMessage;
    }
}
