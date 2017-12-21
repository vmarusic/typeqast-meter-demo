package com.typeqast.meter.utils;

import com.typeqast.meter.validation.EvaluableRule;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class ValidationUtils {

    public static Optional<EvaluableRule> validate(EvaluableRule[] rules, Collection dtos) {
        AtomicReference<EvaluableRule> ruleFailedToSatisfy = new AtomicReference<>();
        Stream.of(rules)
                .allMatch(r -> {
                    boolean result = r.eval(dtos);
                    if (!result) ruleFailedToSatisfy.set(r);
                    return result;
                });
        return Optional.ofNullable(ruleFailedToSatisfy.get());
    }
}
