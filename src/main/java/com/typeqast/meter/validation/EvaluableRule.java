package com.typeqast.meter.validation;

public interface EvaluableRule<T> {

    boolean eval(T t);
}
