package com.dsp.fir.domain.global.validator;

public interface Validator<T> {
    void validate(T data) throws ValidatorException;
}
