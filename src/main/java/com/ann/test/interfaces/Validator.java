package com.ann.test.interfaces;

public interface Validator<T> {
    boolean isValid(T value);
}